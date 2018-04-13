package server.premadeURs;

import shared.CardTargetType;
import shared.PlayerCardSimple;
import shared.request.PostCondition;
import shared.request.UpdateRequest;

/**
 * The UpdateRequest encoding of each event card
 */
public class EventCardURs {

    /**
     * Removes cardToRemove in the infection discard pile from the game
     * @param cardToRemove card in the infection discard file
     */
    public static UpdateRequest getResilientPopulationUR(PlayerCardSimple cardToRemove) {
        return new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.MOVE_CARD, cardToRemove,
                                                        CardTargetType.DISCARD_PILE.name(),         //source
                                                        CardTargetType.REMOVE_FROM_GAME.name()      //destination
                )
        );
    }

    /**
     * Builds a research station at the specified city. If there are too many cities
     * @param cityToBuildStation specified city
     */
    public static UpdateRequest getGovernmentGrantUR(String cityToBuildStation, String cityNameToRemove_Optional) {
        return new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.BUILD_RESEARCH_STATION, cityToBuildStation, cityNameToRemove_Optional
                )
        );
    }

    //move any 1 pawn to a city; get permission before moving another's pawn
    public static UpdateRequest getAirliftUR() {
        return null; //TODO russell
    }

    //draw, look at, and rearrange the top 6 cards of the infection deck. put them back on top
    public static UpdateRequest getForecastUR() {
        return null; //TODO russell
    }

    //skip the next infect cities step
    public static UpdateRequest getOneQuietNight() {
        return null; //TODO russell
    }

    //this turn, remove 1 disease cube from each city the player drives/ferries to
    public static UpdateRequest getMobileHospitalUR() {
        return null; //TODO russell
    }

    //play immediately after a discover cure action to remove 1-5 cubes of the cured disease
    //these disease cubes must come from CONNECTED cities
    public static UpdateRequest getRapidVaccineDeploymentUR() {
        return null; //TODO russell
    }

    //this turn, the player may spend actions to move 1 other pawn (with permission) as if it were his own
    public static UpdateRequest getSpecialOrdersUR() {
        return null; //TODO russell
    }


    //select a player, this player may draw any 1 city card from the player discard pile into his hard
    //(discarding if over his hand limit)
    public static UpdateRequest getReExaminedResearchUR() {
        return null; //TODO russell
    }

    //remove 2 disease cubes from the board
    public static UpdateRequest getRemoteTreatment() {
        return null; //TODO russell
    }

    //the infection rate is 1 until the current player' next turn begins.
    //put this card in front of this player, discard it when his nect turn begins
    public static UpdateRequest getCommericalTravelBanUR() {
        return null; //TODO russell
    }

    //take 2 extra actions this turn
    public static UpdateRequest getBorrowedTimeUR() {
        return null; //TODO russell
    }

    //select a player. this player swaps his Role card with any of the unused roles
    public static UpdateRequest getNewAssignmentUR() {
        return null; //TODO russell
    }

}
