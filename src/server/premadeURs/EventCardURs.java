package server.premadeURs;

import api.socketcomm.Client;
import pandemic.CityName;
import pandemic.InfectionCard;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;
import server.ServerCommands;
import shared.CardTargetType;
import shared.PlayerCardSimple;
import shared.request.PostCondition;
import shared.request.UpdateRequest;

import java.util.Arrays;
import java.util.List;

/**
 * The UpdateRequest encoding of each event card
 */
public class EventCardURs {

    /**
     * The basic encoding structure of sending an event card to the server
     */
    private static void sendEventCardUR(Client client, EventCardName name, Object... arguments) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.EVENT_CARD,
                                    name,
                                    Arrays.asList(arguments)
                        )
                )
        );
    }

    /**
     * move any 1 pawn to a city; get permission before moving another's pawn
     */
    public static void sendAirliftUR(Client client, String cardOwnerName, String playerToMoveName, CityName cityDestinationName) {
        sendEventCardUR(client,
                EventCardName.AirLift, cardOwnerName, playerToMoveName, cityDestinationName);
    }

    /**
     * Removes cardToRemove in the infection discard pile from the game
     */
    public static void sendResilientPopulationUR(Client client, String cardOwnerName, InfectionCard cardToMove) {
        sendEventCardUR(client,
                EventCardName.ResilientPopulation, cardOwnerName, cardToMove);
    }

    /**
     * Builds a research station at the specified city. Player has selected a targetCity to add one research station to,
     * targetCity does not already have a research station on it, and there are unused research stations,
     */
    public static void sendGovernmentGrantUR(Client client, String cardOwnerName, CityName targetCityName) {
        sendEventCardUR(client,
                EventCardName.GovernmentGrant, cardOwnerName, targetCityName);
    }

    /**
     * draw, look at, and rearrange the top 6 cards of the infection deck. put them back on top
     * cardOwnerName has viewed top 6 cards of Infection Deck. rearrangedCards is the order that the Player has chosen for these cards.
     */
    public static void sendForecastUR(Client client, String cardOwnerName, List<InfectionCard> rearrangedCards) {
        sendEventCardUR(client,
                EventCardName.Forecast, cardOwnerName, rearrangedCards);
    }

    /**
     * skip the next infect cities step
     *
     */
    public static void sendOneQuietNight(Client client, String cardOwnerName) {
        sendEventCardUR(client,
                EventCardName.OneQuietNight, cardOwnerName);
    }

    /**
     * this turn, remove 1 disease cube from each city the player drives/ferries to
     * Pre: the currentPlayer has selected to play this card, endTurn has not yet begun
     */
    public static void sendMobileHospitalUR(Client client) {
        sendEventCardUR(client,
                EventCardName.MobileHospital);
    }

    /**
     * take 2 extra actions this turn
     * Pre: the currentPlayer has selected to play this card, endTurn has not yet begun
     */
    public static void sendBorrowedTimeUR(Client client) {
        sendEventCardUR(client,
                EventCardName.BorrowedTime);
    }

    /**
     * the infection rate is 1 until the current player' next turn begins.
     * put this card in front of this player, discard it when his nect turn begins
     */
    public static void sendCommericalTravelBanUR(Client client, String cardOwnerName) {
        sendEventCardUR(client,
                EventCardName.CommercialTravelBan, cardOwnerName);
    }


    /**
     * play immediately after a discover cure action to remove 1-5 cubes of the cured disease
     * these disease cubes must come from CONNECTED cities
     */
    public static void sendRapidVaccineDeploymentUR() {
        //TODO russell
    }

    /**
     * this turn, the player may spend actions to move 1 other pawn (with permission) as if it were his own
     */
    public static void sendSpecialOrdersUR() {
        //TODO russell
    }


    /**
     * select a player, this player may draw any 1 city card from the player discard pile into his hard
     * (discarding if over his hand limit)
     */
    public static void sendReExaminedResearchUR() {
        //TODO russell
    }

    /**
     * remove 2 disease cubes from the board
     */
    public static void sendRemoteTreatment() {
        //TODO russell
    }

    /**
     * select a player. this player swaps his Role card with any of the unused roles
     */
    public static void sendNewAssignmentUR() {
        //TODO russell
    }
}
