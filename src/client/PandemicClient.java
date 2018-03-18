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

    public PandemicClient(String hostName, int port) throws IOException {
        super(hostName, port);
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
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
                GUICommandLinker.handleReceiveUpdatedGS(gui, newGS);
                break;
        }
    }
}
