package shared.request;

import shared.PlayerCardSimple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * A single action request
 */
public class PostCondition implements Serializable {

    private final ACTION action;
    private final List arguments;

    private String log_playerUserName;  //who did the action
    private String log_actionResult;    //description of what happened

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
         * {@link PlayerCardSimple} card, String source, String destination
         * <br><br>
         * If the source or destination is a player, then it should be the {@link pandemic.RoleType#name()} of that player,
         * Otherwise, use the {@link shared.CardTargetType#name()}.
         */
        MOVE_CARD,

        /**
         * This action moves a players position to a city.
         * <br>
         * The server will make sure the request is valid before execution.
         *
         * <br><br><b>Arguments required:</b><br>
         * String playerUserName, String cityName, {@link shared.TravelType travelType} travelType
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
         * List<{@link pandemic.CityCard}> cardsToDiscard
         */
        DISCOVER_CURE,

        /**
         * This action builds a research station at the player's current position and discards cards if necessary.
         * <br>
         * The server will handle discarding cards if the player's role is not OperationsExpert.
         *
         * The cityNameToRemove_Optional argument is the name of one of 6 existing research stations to remove.
         * You may leave this parameter empty (no parameter) or send null if there are < 6 research stations already.
         *
         * <br><br><b>Arguments required:</b><br>
         * String cityLocationToBuild, String cityNameToRemove_Optional
         */
        BUILD_RESEARCH_STATION,

        /**
         * This action will end the turn for the current player
         */
        END_TURN,

        /**
         * This action will play a specified event card
         * <br><br><b>Arguments required:</b><br>
         * EventCardName name, List arguments
         * <br>
         *     (the arguments are encoded by the parameters specified in {@link server.premadeURs.EventCardURs}
         */
        EVENT_CARD,

        BIOT_TURN,

        INFECT_NEXT_CITY,

        IMPOSE_QUARANTINE_MARKER
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

    public String getLog_playerUserName() {
        return log_playerUserName;
    }

    public void setLog_playerUserName(String log_playerUserName) {
        this.log_playerUserName = log_playerUserName;
    }

    public String getLog_actionResult() {
        return log_actionResult;
    }

    public void setLog_actionResult(String log_actionResult) {
        if (!log_actionResult.endsWith("!") && !log_actionResult.endsWith(".") && !log_actionResult.endsWith("?")) {
            log_actionResult += ".";
        }

        this.log_actionResult = log_actionResult;
    }

    public boolean isLogValid() {
        return log_playerUserName != null && !log_playerUserName.isEmpty()
                && log_actionResult != null && log_actionResult.length() > 1;
    }
}
