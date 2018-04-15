package client;

import api.socketcomm.Client;
import api.socketcomm.SocketBundle;
import pandemic.views.GUI;
import pandemic.views.LobbyState;
import pandemic.views.MenuLayout;
import server.ServerCommands;
import shared.GameState;
import shared.MessageType;
import shared.Utils;

import java.io.IOException;
import java.util.List;

public class PandemicClient extends Client {

    private GUI gui;
    private String clientName;

    public PandemicClient(String hostName, String clientName, int port) throws IOException {
        super(hostName, port);
        this.clientName = clientName;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public GUI getGui() {
        return gui;
    }

    @Override
    public void handleReceivedMessage(SocketBundle server, List<Object> message) {
        if (message == null) return;

        final String commandString = (String) message.get(0);
        final ClientCommands command = Utils.getEnum(ClientCommands.class, commandString);
        if (command == null) return;
        switch (command) {
            case RECEIVE_CONSENT_REQUEST:
                final String consentPrompt = (String)message.get(1);
                GUICommandLinker.handleReceiveConsentRequest(gui, consentPrompt);
                break;

            case RECEIVE_GAME_MESSAGE:
                final MessageType messageType = (MessageType)message.get(1);
                final String messageText = (String)message.get(2);
                GUICommandLinker.handleReceiveMessage(gui, messageType, messageText);
                break;

            case RECEIVE_UPDATED_GAMESTATE:
                final GameState newGS = (GameState)message.get(1);
                System.out.println("RECEIVED GAME STATE client: " + clientName + " gui: " + gui);
                GUICommandLinker.handleReceiveUpdatedGS(gui, newGS);
                break;

            case LOBBY_STATE_UPDATE:
                System.out.println("client got updated lobby state");
                // hack
                if (MenuLayout.getInstance().getPandemicServer() != null) {
                    MenuLayout.getInstance().lobbyChatServ.update((LobbyState)message.get(1));
                } else {
                    MenuLayout.getInstance().lobbyChatClient.update((LobbyState)message.get(1));
                }
                break;
            case SERVER_WANTS_PINGBACK:
                // just immediately respond
                sendMessageToServer(ServerCommands.CLIENT_PING_RESPOND.name(), clientName);
                break;

            case RECEIVE_GAME_LOG_MESSAGE:
                final String gameLogName = (String)message.get(1);
                final String gameLogAction = (String)message.get(2);
                GUICommandLinker.handleReceiveGameLogMsgMessage(gui, gameLogName, gameLogAction);
                break;
        }
    }
}
