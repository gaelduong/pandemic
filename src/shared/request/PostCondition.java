package shared.request;

import java.util.Arrays;
import java.util.List;

/**
 * A single action request
 */
public class PostCondition {

    private final ACTION action;
    private final List arguments;

    /**
     * The core effects that all update requests can be broken down into.
     */
    public enum ACTION {
        /**
         * CardSource source, CardSource destination
         */
        MOVE_CARD, //from hand to discard pile, from deck to hand, from  player to player

        /**
         * String playerUserName, String cityName,
         */
        MOVE_PLAYER_POS, //drive/ferry, direct flight, shuttle flight


        TREAT_DISEASE,  //type of disease


        DISCOVER_CURE,  //type of disease
    }

    public PostCondition(ACTION action, Object... arguments) {
        this.action = action;
        this.arguments = Arrays.asList(arguments);
    }

    public List getArguments() {
        return arguments;
    }

    public ACTION getActionType() {
        return action;
    }
}
