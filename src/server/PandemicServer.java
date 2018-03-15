package server;

import api.socketcomm.Server;
import api.socketcomm.SocketBundle;
import shared.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PandemicServer extends Server {
    private Map<SocketBundle, String> clientMap;    //<socket, playerUserName>

    public PandemicServer(int port) throws IOException {
        super(port);

    }

    @Override
    protected void handleReceivedMessage(SocketBundle client, List<Object> message) {
        if (message == null) return;

        final String commandString = (String)message.get(0);
        final ServerCommands command = Utils.getEnum(ServerCommands.class, commandString);
        if (command == null) return;

        switch (command) {
            case ANSWER_CONSENT_PROMPT:
                //TODO
                break;

            case SEND_UPDATE_REQUEST:
                //TODO
                break;

            case INITIATE_CONSENT_REQUIRING_MOVE:
                //TODO
                break;
        }
    }

    @Override
    protected void onClientConnected(SocketBundle client) {
        clientMap.put(client, null);
    }
}
