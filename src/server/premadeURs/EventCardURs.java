package server.premadeURs;

import api.socketcomm.Client;
import server.ServerCommands;
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
    public static void sendResilientPopulationUR(Client client, PlayerCardSimple cardToRemove) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
            new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.MOVE_CARD, cardToRemove,
                                                        CardTargetType.DISCARD_PILE.name(),         //source
                                                        CardTargetType.REMOVE_FROM_GAME.name()      //destination
                )
            )
        );
    }

    /**
     * Builds a research station at the specified city. If there are too many cities
     * @param cityToBuildStation specified city
     */
    public static void sendGovernmentGrantUR(Client client, String cityToBuildStation, String cityNameToRemove_Optional) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
            new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.BUILD_RESEARCH_STATION, cityToBuildStation, cityNameToRemove_Optional
                )
            )
        );
    }

    //move any 1 pawn to a city; get permission before moving another's pawn
    public static void sendAirliftUR() {
        //TODO russell
    }

    //draw, look at, and rearrange the top 6 cards of the infection deck. put them back on top
    public static void sendForecastUR() {
        //TODO russell
    }

    //skip the next infect cities step
    public static void sendOneQuietNight() {
        //TODO russell
    }

    //this turn, remove 1 disease cube from each city the player drives/ferries to
    public static void sendMobileHospitalUR() {
        //TODO russell
    }

    //play immediately after a discover cure action to remove 1-5 cubes of the cured disease
    //these disease cubes must come from CONNECTED cities
    public static void sendRapidVaccineDeploymentUR() {
        //TODO russell
    }

    //this turn, the player may spend actions to move 1 other pawn (with permission) as if it were his own
    public static void sendSpecialOrdersUR() {
        //TODO russell
    }


    //select a player, this player may draw any 1 city card from the player discard pile into his hard
    //(discarding if over his hand limit)
    public static void sendReExaminedResearchUR() {
        //TODO russell
    }

    //remove 2 disease cubes from the board
    public static void sendRemoteTreatment() {
        //TODO russell
    }

    //the infection rate is 1 until the current player' next turn begins.
    //put this card in front of this player, discard it when his nect turn begins
    public static void sendCommericalTravelBanUR() {
        //TODO russell
    }

    //take 2 extra actions this turn
    public static void sendBorrowedTimeUR() {
        //TODO russell
    }

    //select a player. this player swaps his Role card with any of the unused roles
    public static void sendNewAssignmentUR() {
        //TODO russell
    }

}
