package client;

import pandemic.views.GUI;
import shared.GameState;
import shared.MessageType;

/**
 * Links the commands the client will receive to the front end GUI.
 * Gael will fill this out with his corresponding GUI commands!
 */
public class GUICommandLinker {

    /**
     * This method receives a message consentPrompt.
     * The front end gui should draw consentPrompt on the screen with two buttons: Accept and Decline.
     * Once the user selects a button, send the {@link server.ServerCommands#ANSWER_CONSENT_PROMPT}
     * command to the server with the argument 'true' if Accept and 'false' if Decline.
     */
    public static void handleReceiveConsentRequest(GUI gui, String consentPrompt) {
      //TODO gael
    }

    /**
     * This method displays a message on the front-ends GUI. You can use the type parameter to
     * tell the severity of the message (use this for painting differences if you want).
     */
    public static void handleReceiveMessage(GUI gui, MessageType type, String message) {
        //TODO gael
    }

    /**
     * This method receives the new updated GS from the server
     */
    public static void handleReceiveUpdatedGS(GUI gui, GameState gs) {
        //TODO gael
    }
}
