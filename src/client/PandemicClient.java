package client;

import api.socketcomm.Client;
import api.socketcomm.SocketBundle;
import pandemic.views.GUI;
import shared.GameState;
import shared.MessageType;
import shared.Utils;

import java.io.IOException;
import java.util.List;

public class PandemicClient extends Client {

    private GUI gui;
    private String clientName;
    private int numOfPlayersConnectedToServer;

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

            case RECEIVE_MESSAGE:
                final MessageType messageType = (MessageType)message.get(1);
                final String messageText = (String)message.get(2);
                GUICommandLinker.handleReceiveMessage(gui, messageType, messageText);
                break;

            case RECEIVE_UPDATED_GAMESTATE:
                final GameState newGS = (GameState)message.get(1);
                System.out.println("RECEIVED GAME STATE client: " + clientName + " gui: " + gui);
                GUICommandLinker.handleReceiveUpdatedGS(gui, newGS);
                break;
            case RECEIVE_GAME_WON:
                final MessageType messageTypeGameWon = (MessageType)message.get(1);
                final String messageTextGameWon = (String)message.get(2);
                GUICommandLinker.handleReceiveMessage(gui, messageTypeGameWon, messageTextGameWon);
                break;
            case RECEIVE_GAME_LOST:
                final MessageType messageTypeGameLost = (MessageType)message.get(1);
                final String messageTextGameLost = (String)message.get(2);
                GUICommandLinker.handleReceiveMessage(gui, messageTypeGameLost, messageTextGameLost);
                break;
            case RECEIVE_DISCARD_CARD:
                final MessageType messageTypeDiscardCard = (MessageType)message.get(1);
                final String messageTextDiscardCard = (String)message.get(2);
                GUICommandLinker.handleReceiveMessage(gui, messageTypeDiscardCard, messageTextDiscardCard);
                break;
            case RECEIVE_NUM_OF_PLAYERS:
                numOfPlayersConnectedToServer = (int) message.get(1);
                break;


        }
    }

    public int getNumOfPlayersConnectedToServer() {
        return numOfPlayersConnectedToServer;
    }
}
