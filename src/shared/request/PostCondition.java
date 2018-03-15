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
         * This action moves a card from the source to destination.
         * This action can emulate drawing cards, discarding cards, sharing cards, etc.
         * You may use a null value for the PlayerCardSimple if you do not know the card (such as if you are drawing a card from the deck).
         *
         * <br><br><b>Arguments required:</b><br>
         * {@link shared.PlayerCardSimple} card, String source, String destination
         * <br><br>
         * If the source or destination is a player, then it should be the {@link pandemic.RoleType#name()} of that player,
         * Otherwise, use the {@link shared.CardTargetType#name()}.
         */
        MOVE_CARD, //from hand to discard pile, from deck to hand, from  player to player

        /**
         * This action moves a players position to a city.
         * <br>
         * The server will make sure the request is valid before execution.
         *
         * <br><br><b>Arguments required:</b><br>
         * String playerUserName, String cityName, {@link shared.TravelType travelType}
         */
        MOVE_PLAYER_POS,

        /**
         * This action does the treat disease action in the player's current city.
         * <br>
         * The server will handle removing 1 cube or all depending on if the disease was cured.
         *
         * <br><br><b>Arguments required:</b><br>
         * {@link pandemic.DiseaseType} diseaseType
         */
        TREAT_DISEASE,

        /**
         * This action discovers the cure for a certain DiseaseType and discards the inputted list of cards.
         * <br>
         * The server will check the player curing the disease is at a ResearchStation.
         *
         * <br><br><b>Arguments required:</b><br>
         * {@link pandemic.DiseaseType} diseaseType, List<{@link shared.PlayerCardSimple}> cardsToDiscard
         */
        DISCOVER_CURE,

        /**
         * This action builds a research station at the player's current position and discards cards if necessary.
         * <br>
         * The server will handle discarding cards if the player's role is not OperationsExpert.
         * The server will handle the fact that there can only be at most 6 stations on the board.
         *
         * <br><br><b>Arguments required:</b> NONE
         */
        BUILD_RESEARCH_STATION,
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
