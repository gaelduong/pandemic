package client;

/**
 * Each of the below is a command that the Pandemic client can receive from the server.
 * Above each command are the arguments received.
 *
 * @author Russell Katz
 */
public enum ClientCommands {

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
     *
     * <b>Parameters:</b>
     * MessageType type, String message
     */
    RECEIVE_MESSAGE,

    /**
     * This command receives a message to prompt the user with explaining the request
     * from another player that requires his consent.
     *
     * <b>Parameters:</b>
     * String consentPrompt
     */
    RECEIVE_CONSENT_REQUEST,

    /**
     * This command receives a message to inform the player that the game has been won.
     *
     *
     * <b>Parameters:</b>
     * String gameWon
     */
    RECEIVE_GAME_WON,

    /**
     * This command receives a message to inform the player that the game has been lost.
     *
     *
     * <b>Parameters:</b>
     * String gameLost
     */
    RECEIVE_GAME_LOST


}
