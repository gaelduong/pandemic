package server;

import api.socketcomm.Server;
import client.ClientCommands;
import shared.request.PostCondition;

public class ServerRequests {

    /**
     * Sends a game log from the server to all clients
     *
     * Logs are displayed as:
     * playerName: action
     */
    public static void sendGameLog(Server server, String playerName, String action) {
        server.sendMessageToClients(ClientCommands.RECEIVE_GAME_LOG_MESSAGE.name(), playerName, action);
    }

    /**
     * Sends a game log from the server to all clients
     *
     * Logs are displayed as:
     * playerName: action
     */
    public static void sendGameLog(Server server, PostCondition processedAction) {
        sendGameLog(server, processedAction.getLog_playerUserName(), processedAction.getLog_actionResult());
    }
}
