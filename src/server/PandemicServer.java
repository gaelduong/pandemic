package server;

import api.socketcomm.Server;
import api.socketcomm.SocketBundle;
import client.ClientCommands;
import pandemic.Game;
import shared.GameState;
import shared.Utils;
import shared.request.UpdateRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PandemicServer extends Server {
    private final Map<SocketBundle, String> clientMap;    //<socket, playerUserName>
    private final Game game;

    public PandemicServer(Game game, int port) throws IOException {
        super(port);
        this.clientMap = new HashMap<>();
        this.game = game;
    }

    @Override
    protected void handleReceivedMessage(SocketBundle client, List<Object> message) {
        if (message == null) return;

        final String commandString = (String)message.get(0);
        final ServerCommands command = Utils.getEnum(ServerCommands.class, commandString);
        if (command == null) return;

        switch (command) {
            case ANSWER_CONSENT_PROMPT:
                //TODO russell will fill this out

                break;

            case SEND_UPDATE_REQUEST:
                final String playerUsername = clientMap.get(client);
                if (playerUsername == null) {
                    System.out.printf("No player registered from %s! ERROR!\n", client.getSocket().getRemoteSocketAddress().toString());
                    return;
                }

                final UpdateRequest updateRequest = (UpdateRequest)message.get(1);
                executeRequestAndPropagate(playerUsername, game, updateRequest);
                break;

            case INITIATE_CONSENT_REQUIRING_MOVE:
                //TODO russell will fill this out

                break;

            case REGISTER_USERNAME:
                final String playerUserName = (String)message.get(1);
                clientMap.put(client, playerUserName);
                System.out.printf("Registered player %s, from %s!\n", playerUserName, client.getSocket().getRemoteSocketAddress().toString());
                break;
        }
    }

    private void executeRequestAndPropagate(String playerUsername, Game g, UpdateRequest updateRequest) {
        if (updateRequest.isRequestValid()) {
            updateRequest.executeRequest(game, playerUsername);
            sendMessageToClients(ClientCommands.RECEIVE_UPDATED_GAMESTATE.name(), g.generateCondensedGameState());
        } else {
            System.out.println("Could not satisfy update request from player " + playerUsername);
        }
    }

    @Override
    protected void onClientConnected(SocketBundle client) {
        clientMap.put(client, null);
    }
}
