package client;

import pandemic.views.GUI;
import server.ServerCommands;
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
        gui.drawAcceptDeclineMessageBox(consentPrompt);
    }

    /**
     * This method displays a message on the front-ends GUI. You can use the type parameter to
     * tell the severity of the message (use this for painting differences if you want).
     */
    public static void handleReceiveMessage(GUI gui, MessageType type, String message) {
    	gui.drawReceiveMessage(message,type);
        System.out.println("receive message!!");
        switch (type) {
            case GAME_WON:
                //might want to do something special here?
                break;
            case GAME_LOST:
                //might want to do something special here?
                break;
            case INFORMATION:
                break;
            case DISCARD_CARD:
                gui.enableDiscardCardButton();
                break;
        }
    }

    /**
     * This method receives the new updated GS from the server
     */
    public static void handleReceiveUpdatedGS(GUI gui, GameState gs) {
        if (gui == null) {
            System.err.println("gui is null");
            return;
        }

        System.out.println("GUI : " + gui);
        System.out.println("GameState: " + gs);
    	gui.setGameState(gs);
    	gui.draw();
    }

    /**
     * This method is called when the client receives a new chat message from a player
     */
    public static void handleReceiveGameLogMsgMessage(GUI gui, String playerName, String message) {
        //TODO gael / gavin implement in front end pretty display
        System.out.println("NEW GAME LOG!");
        System.out.println(playerName + ": " + message);
    }
}
