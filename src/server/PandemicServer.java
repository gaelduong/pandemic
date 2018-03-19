package server;

import api.socketcomm.Server;
import api.socketcomm.SocketBundle;
import client.ClientCommands;
import pandemic.*;
import pandemic.views.MenuLayout;
import shared.MessageType;
import shared.Utils;
import shared.request.UpdateRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class PandemicServer extends Server {
    private final Map<SocketBundle, String> clientMap;    //<socket, playerUserName>
    private final Map<String, UpdateRequest> consentRequestMap; //<username, UR>

    private final MenuLayout menuLayout;

    private Semaphore updateRequestSemaphore; //makes sure we only execute one UR at a time

    public PandemicServer(MenuLayout menuLayout, Game g, int port) throws IOException {
        super(g, port);
        this.menuLayout = menuLayout;
        this.clientMap = Collections.synchronizedMap(new HashMap<>());
        this.consentRequestMap = Collections.synchronizedMap(new HashMap<>());
        this.updateRequestSemaphore = new Semaphore(1);
    }

    @Override
    protected void handleReceivedMessage(SocketBundle client, List<Object> message) {
        Game game = getGame();
        if (game.getGamePhase() != GamePhase.Completed) {
            if (message == null) return;

            final String commandString = (String)message.get(0);
            final ServerCommands command = Utils.getEnum(ServerCommands.class, commandString);
            if (command == null) return;

            switch (command) {
                case ANSWER_CONSENT_PROMPT:
                    answerConsentPrompt(client, message);
                    break;

                case SEND_UPDATE_REQUEST:
                    sendUpdateRequest(client, message);
                    break;

                case INITIATE_CONSENT_REQUIRING_MOVE:
                    initiateConsentReqMove(message);
                    break;

                case REGISTER_USERNAME:
                    final String playerUserName = (String)message.get(1);
                    clientMap.put(client, playerUserName);
                    System.out.printf("Registered player %s, from %s!\n", playerUserName, client.getSocket().getRemoteSocketAddress().toString());

                    User clientUser = new User(playerUserName, "lol", null);
                    game.getGameManager().joinGame(clientUser);
                    menuLayout.updateCreateLabel(playerUserName);
                    break;
            }
        } else {

            if(game.getGameManager().isGameLost())
                sendMessageToClients(ClientCommands.RECEIVE_GAME_LOST.name(), MessageType.GAME_LOST, "GAME LOST!");
            else
                sendMessageToClients(ClientCommands.RECEIVE_GAME_WON.name(), MessageType.GAME_WON, "GAME WON!");

            //STOP THE SERVER THREAD HERE?
        }
    }

    private void answerConsentPrompt(SocketBundle client, List<Object> message) {
        Game game = getGame();
        final String playerUsername = clientMap.get(client);
        if (playerUsername == null) {
            System.err.printf("No player registered from %s! ERROR!\n", client.getSocket().getRemoteSocketAddress().toString());
            return;
        }

        final boolean acceptedRequest = (Boolean) message.get(1);
        if (consentRequestMap.containsKey(playerUsername) && consentRequestMap.get(playerUsername) != null) {
            if (acceptedRequest) {
                System.out.printf("Player %s accepted the consent request!\n", playerUsername);
                executeRequestAndPropagate(playerUsername, game, consentRequestMap.get(playerUsername));

            }
            consentRequestMap.put(playerUsername, null);
        }
    }

    private void initiateConsentReqMove(List<Object> message) {
        final String targetPlayerUsername = (String) message.get(1);
        final String consentPrompt = (String) message.get(2);
        final UpdateRequest consentUR = (UpdateRequest) message.get(3);

        final SocketBundle targetPlayerSocket;
        synchronized (clientMap) {
            targetPlayerSocket = clientMap.entrySet().stream()
                    .filter(e -> e.getValue().equals(targetPlayerUsername)).map(Map.Entry::getKey).findFirst().orElse(null);
        }

        if (targetPlayerSocket == null) {
            System.err.printf("Unable to find corresponding socket connection for %s\n", targetPlayerUsername);
            return;
        }

        if (consentRequestMap.get(targetPlayerUsername) == null) {  //only one consent per person at a time
            Server.sendMessage(targetPlayerSocket, ClientCommands.RECEIVE_CONSENT_REQUEST.name(), consentPrompt);
            consentRequestMap.put(targetPlayerUsername, consentUR);
        }
    }

    private void sendUpdateRequest(SocketBundle client, List<Object> message) {
        Game game = getGame();
        final String playerUsername = clientMap.get(client);
        if (playerUsername == null) {
            System.err.printf("No player registered from %s! ERROR!\n", client.getSocket().getRemoteSocketAddress().toString());
            System.out.println("client map (in sendUpdateRequest): " + clientMap);
            return;
        }

        if (!playerUsername.equals(game.getCurrentPlayer().getPlayerUserName())) {
            System.err.printf("Rejected UpdateRequest from player %s, not your turn!\n", playerUsername);
            return;
        }

        final UpdateRequest updateRequest = (UpdateRequest)message.get(1);
        executeRequestAndPropagate(playerUsername, game, updateRequest);

    }

    private boolean executeRequestAndPropagate(String playerUsername, Game g, UpdateRequest updateRequest) {
        Game game = getGame();
        boolean ret = false;

        try {
            updateRequestSemaphore.acquire();
        } catch (InterruptedException ignore) {}    //we don't interrupt

        if (updateRequest.isRequestValid()) {

            if(updateRequest.executeRequest(game, playerUsername) == 1)
                sendMessageToClients(ClientCommands.RECEIVE_MESSAGE.name(), MessageType.INFORMATION, "An epidemic has occured.");

            sendMessageToClients(ClientCommands.RECEIVE_UPDATED_GAMESTATE.name(), g.generateCondensedGameState());
            ret = true;

            if(game.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayerDiscardingCards) {
                String clientToDiscard = game.getPlayerDiscardingCards().getPlayerUserName();
                final SocketBundle clientToDiscardSocket;

                synchronized (clientMap) {
                    clientToDiscardSocket = clientMap.entrySet().stream()
                            .filter(e -> e.getValue().equals(clientToDiscard)).map(Map.Entry::getKey).findFirst().orElse(null);
                }

                if (clientToDiscardSocket == null) {
                    System.err.printf("Unable to find corresponding socket connection for %s\n", clientToDiscard);
                    return false;
                }

                sendMessage(clientToDiscardSocket, ClientCommands.RECEIVE_DISCARD_CARD.name(), MessageType.DISCARD_CARD,
                                                        "You have too many cards. Please discard a card.");
            }

        } else {
            System.out.println("Could not satisfy update request from player " + playerUsername);
        }

        updateRequestSemaphore.release();

        return ret;
    }



    @Override
    protected void onClientConnected(SocketBundle client) {
        clientMap.put(client, null);
        System.out.println("client map: " + clientMap);
    }
}
