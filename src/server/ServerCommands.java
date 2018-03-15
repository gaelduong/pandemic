package server;

import shared.request.UpdateRequest;

/**
 * Each of the below is a command that the Pandemic server can receive from the client.
 * Above each command are the arguments received.
 *
 * @author Russell Katz
 */
public enum ServerCommands {

    /**
     * Clients will use this command to send an {@link UpdateRequest} to the server.
     * This requests a GameState update in the interval server core GameState.
     * Requests are granted if its pre-conditions can be satisfied with the current GameState.
     *
     * <b>Parameters:</b>
     * UpdateRequest request
     */
    SEND_UPDATE_REQUEST,

    /**
     * Clients will use this command to initiate a consent-requiring move with a target player.
     * If the target player accepts, the UpdateRequest will be sent to the server.
     *
     * <b>Parameters:</b>
     * String targetPlayerUsername, String consentPrompt, UpdateRequest request
     */
    INITIATE_CONSENT_REQUIRING_MOVE,

    /**
     * Clients will use this command to notify the server of a player's choice when he is
     * prompted from consent.
     *
     * <b>Parameters:</b>
     * boolean accept
     */
    ANSWER_CONSENT_PROMPT,
}
