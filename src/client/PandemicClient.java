package client;

import api.socketcomm.Client;
import api.socketcomm.SocketBundle;
import shared.Utils;

import java.io.IOException;
import java.util.List;

public class PandemicClient extends Client {

    public PandemicClient(String hostName, int port) throws IOException {
        super(hostName, port);
    }

    @Override
    public void handleReceivedMessage(SocketBundle server, List<Object> message) {
        if (message == null) return;

        final String commandString = (String) message.get(0);
        final ClientCommands command = Utils.getEnum(ClientCommands.class, commandString);
        if (command == null) return;
        switch (command) {
            case RECEIVE_CONSENT_REQUEST:

                break;

            case RECEIVE_MESSAGE:

                break;

            case RECEIVE_UPDATED_GAMESTATE:

                break;
        }
    }
}
