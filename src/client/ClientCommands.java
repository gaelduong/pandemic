package client;

import java.io.Serializable;

/**
 * Each of the below is a command that the Pandemic client can receive from the server.
 * Above each command are the arguments received.
 *
 * @author Russell Katz
 */
public enum ClientCommands implements Serializable{

    /**
     * This command receives the new GameState of Pandemic so that the
     * client can update its front-end accordingly.
     *
     * <b>Parameters:</b>
     * GameState newGS
     */
    RECEIVE_UPDATED_GAMESTATE,

    /**
     * This command receives a message from the server with type {@link shared.MessageType}
     * Supported message types can inform the client of every enum:
     * INFORMATION,
     * GAME_WON,
     * GAME_LOST,
     * DISCARD_CARD
     *
     * <b>Parameters:</b>
     * MessageType type, String message
     */
    RECEIVE_GAME_MESSAGE,

    /**
     * This command receives a message to prompt the user with explaining the request
     * from another player that requires his consent.
     *
     * <b>Parameters:</b>
     * String consentPrompt
     */
    RECEIVE_CONSENT_REQUEST,

    /**
     * This command receives a chat message from another user.
     *
     * <b>Parameters:</b>
     * String playerName, String message
     */
    RECEIVE_CHAT_MESSAGE,

    /**
     * Server wants an update on our status (aka whether we are still connected).
     */
    SERVER_WANTS_PINGBACK,

    /**
     * Server has sent a new lobby state.
     */
    LOBBY_STATE_UPDATE
}
