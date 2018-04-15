package server;

import api.socketcomm.Server;
import api.socketcomm.SocketBundle;
import client.ClientCommands;
import pandemic.*;
import pandemic.views.LobbyState;
import pandemic.views.MenuLayout;
import shared.ConsentRequestBundle;
import shared.MessageType;
import shared.Utils;
import shared.request.UpdateRequest;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class PandemicServer extends Server {
    private final Map<SocketBundle, String> clientMap;    //<socket, playerUserName>
    private final Map<String, ConsentRequestBundle> consentRequestMap; //<username, UR>

    private final MenuLayout menuLayout;

    private Semaphore updateRequestSemaphore; //makes sure we only execute one UR at a time
    private Timer connectionCheckTimer;
    private Map<String, Long> clientLastResponse;

    public PandemicServer(MenuLayout menuLayout, Game g, int port) throws IOException {
        super(g, port);
        this.menuLayout = menuLayout;
        this.clientMap = Collections.synchronizedMap(new HashMap<>());
        this.consentRequestMap = Collections.synchronizedMap(new HashMap<>());
        this.updateRequestSemaphore = new Semaphore(1);
        this.connectionCheckTimer = new Timer();
        this.clientLastResponse = Collections.synchronizedMap(new HashMap<>());

        long timerCheckRate = 1000; // check every second
        long timeout = 5000; // 5 seconds
        this.connectionCheckTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendMessageToClients(ClientCommands.SERVER_WANTS_PINGBACK.name());
                // check if any clients haven't responded in a while
                AtomicReference<SocketBundle> bundleToRemove = new AtomicReference<>(null);
                AtomicReference<String> nameToRemove = new AtomicReference<>(null);

                clientMap.forEach((bundle, name) -> {
                    if (name == null || name.equals("host")) {
                        return;
                    } else if (!clientLastResponse.containsKey(name)) {
                        System.out.printf("Player name not in last response map '%s'\n.", name);
                        return;
                    }

                    if (clientLastResponse.get(name) > timeout) {
                        // a client has timed out
                        bundleToRemove.set(bundle);
                        nameToRemove.set(name);
                        // return or something idk?
                    } else {
                        // add time to last response
                        clientLastResponse.put(name, clientLastResponse.get(name) + timerCheckRate);
                    }
                });

                if (bundleToRemove.get() != null && nameToRemove.get() != null) {
                    userTimedOut(bundleToRemove.get(), nameToRemove.get());
                }
            }
        }, 1000, timerCheckRate);
    }

    @Override
    public void close() {
        this.connectionCheckTimer.purge();
        this.connectionCheckTimer.cancel();
        super.close();
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
                    //TODO russell track to game log
                    answerConsentPrompt(client, message);
                    break;

                case SEND_UPDATE_REQUEST:
                    sendUpdateRequest(client, message);
                    break;

                case INITIATE_CONSENT_REQUIRING_MOVE:
                    //TODO russell track to game log
                    initiateConsentReqMove(client, message);
                    break;

                case REGISTER_USERNAME: {
                    final String playerUserName = (String) message.get(1);
                    System.out.printf("Registered player %s, from %s!\n", playerUserName,
                            client.getSocket().getRemoteSocketAddress().toString());

                    System.out.println("game phase is : " + game.getGamePhase());
                    if (game.getGamePhase() == GamePhase.ReadyToJoin) {
                        clientMap.put(client, playerUserName);
                        clientLastResponse.put(playerUserName, (long) 0);

                        User clientUser = new User(playerUserName, "lol",
                                client.getSocket().getRemoteSocketAddress().toString());
                        game.getGameManager().joinGame(clientUser);
                        sendUpdatedLobbyState(null);
                    } else {
                        System.out.println("This game has already started!");
                    }
                    break;
                }
                case CLIENT_PING_RESPOND: {
                    final String playerUserName = (String) message.get(1);
                    if (playerUserName.equals("host")) {
                        break;
                    } else if (!clientLastResponse.containsKey(playerUserName)) {
                        //System.out.printf("User '%s' isn't in list?\n", playerUserName);
                    } else {
                        // reset response time to zero since the user has responded to our ping
                        //System.out.printf("Got player pickback from '%s'\n", playerUserName);
                        clientLastResponse.put(playerUserName, (long) 0);
                    }
                    break;
                }
                case CLIENT_UPDATE_LOBBBY:
                    sendUpdatedLobbyState((LobbyState) message.get(1));
                    break;
            }
        } else {

            if(game.getGameManager().isGameLost())
                sendMessageToClients(ClientCommands.RECEIVE_GAME_MESSAGE.name(), MessageType.GAME_LOST, "GAME LOST!");
            else
                sendMessageToClients(ClientCommands.RECEIVE_GAME_MESSAGE.name(), MessageType.GAME_WON, "GAME WON!");

            //STOP THE SERVER THREAD HERE?
        }
    }

    private void userTimedOut(SocketBundle client, String playerUserName) {
        Game game = getGame();
        System.out.printf("Player %s isn't responding (from %s)!\n", playerUserName,
                client.getSocket().getRemoteSocketAddress().toString());

        System.out.println("game phase is : " + game.getGamePhase());
        if (game.getGamePhase() == GamePhase.ReadyToJoin) {
            User clientUser = new User(playerUserName, "lol",
                    client.getSocket().getRemoteSocketAddress().toString());

            clientMap.remove(client, playerUserName);
            clientLastResponse.remove(playerUserName);
            game.getGameManager().leaveGameFromLobby(clientUser);
            sendUpdatedLobbyState(null);
        } else {
            System.out.println("This game has already started!");
            // TODO: save and exit
        }
    }

    private void sendUpdatedLobbyState(LobbyState newState)
    {
        System.out.println("send updated lobby state");
        if (newState == null) {
            newState = menuLayout.lobbyChatServ.getState();
        }

        ArrayList<String> players = new ArrayList<>();
        clientMap.forEach((bundle, name) -> {
            players.add(name);
        });
        // ensure player list is correct
        newState.playerList = players;

        sendMessageToClients(ClientCommands.LOBBY_STATE_UPDATE.name(),
                newState);
    }

    private void answerConsentPrompt(SocketBundle client, List<Object> message) {
        Game game = getGame();
        final String playerUsername = clientMap.get(client);
        if (playerUsername == null) {
            System.err.printf("No player registered from %s! ERROR!\n", client.getSocket().getRemoteSocketAddress().toString());
            return;
        }

        final boolean acceptedRequest = (Boolean) message.get(1);
        if (consentRequestMap.containsKey(playerUsername)) {
            if (acceptedRequest) {
                executeRequestAndPropagate(consentRequestMap.get(playerUsername).getSourcePlayer(), game, consentRequestMap.get(playerUsername).getUr());
            }
            consentRequestMap.put(playerUsername, null);
        }
    }

    private void initiateConsentReqMove(SocketBundle client, List<Object> message) {
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

        final String playerUsername = clientMap.get(client);
        if (playerUsername == null) {
            System.err.printf("No player registered from %s! ERROR!\n", client.getSocket().getRemoteSocketAddress().toString());
            System.out.println("client map (in sendUpdateRequest): " + clientMap);
            return;
        }

        if (consentRequestMap.get(targetPlayerUsername) == null) {  //only one consent per person at a time
            Server.sendMessage(targetPlayerSocket, ClientCommands.RECEIVE_CONSENT_REQUEST.name(), consentPrompt);
            consentRequestMap.put(targetPlayerUsername, new ConsentRequestBundle(playerUsername, targetPlayerUsername, consentUR));
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
            String updateString = updateRequest.executeRequest(this, game, playerUsername);
            if(!updateString.equals(""))
                sendMessageToClients(ClientCommands.RECEIVE_GAME_MESSAGE.name(), MessageType.INFORMATION, updateString);

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

                //sendMessage(clientToDiscardSocket, ClientCommands.RECEIVE_GAME_MESSAGE.name(), MessageType.DISCARD_CARD,
                //                                        "You have too many cards. Please discard a card.");
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
