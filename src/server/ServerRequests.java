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
    public static void sendGameLog(String playerName, String action) {
        final Server server = PandemicServer.getInstance();
        if (server == null) {
            System.err.println("UNABLE TO SEND GAME LOG, SERVER NULL"); //shouldn't happen
            return;
        }
        server.sendMessageToClients(ClientCommands.RECEIVE_GAME_LOG_MESSAGE.name(), playerName, action);
    }

    /**
     * Sends a game log from the server to all clients
     *
     * Logs are displayed as:
     * playerName: action
     */
    public static void sendGameLog(PostCondition processedAction) {
        sendGameLog(processedAction.getLog_playerUserName(), processedAction.getLog_actionResult());
    }
}
