package server.premadeURs;

import api.socketcomm.Client;
import pandemic.CardType;
import pandemic.CityCard;
import pandemic.DiseaseType;
import pandemic.RoleType;
import server.ServerCommands;
import shared.PlayerCardSimple;
import shared.TravelType;
import shared.request.PostCondition;
import shared.request.UpdateRequest;

import java.util.List;

/**
 * The UpdateRequest encoding of the game actions
 */
public class GameURs {

    /**
     * Creates a drive/ferry update request, which the player drives to the target city
     */
    public static UpdateRequest getDriveFerryUR(String playerUserName, String cityName) {
        return new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.MOVE_PLAYER_POS,
                        playerUserName, //source
                        cityName,       //destination
                        TravelType.DRIVE_FERRY
                )
        );
    }

    /**
     * Creates a drive update request, which the player drives to the target city
     */
    public static UpdateRequest getDirectFlightUR(String playerUserName, String cityName) {
        return new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.MOVE_PLAYER_POS,
                        playerUserName, //source
                        cityName,       //destination
                        TravelType.DIRECT_FLIGHT
                )
        );
    }

    /**
     * Creates an update request to treat a disease in the player's current city
     */
    public static UpdateRequest getTreatDiseaseUR(DiseaseType diseaseType) {
        return new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.TREAT_DISEASE,
                        diseaseType
                )
        );
    }

    /**
     * Creates an update request that discovers the cure for a certain DiseaseType
     * and discards the inputted list of cards
     */
    public static UpdateRequest getDiscoverCureUR(List<CityCard> cityCardsToDiscard) {
        return new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.DISCOVER_CURE,
                        cityCardsToDiscard
                )
        );
    }

    /**
     * Sends a message to the server that you would like to initiate a Share Knowledge consent prompt
     * @param client the client object
     * @param consentPrompt the message shown to the receiving player. For example "Player A wants to share knowledge with you".
     * @param receivingPlayerName the name of the player that receives the consent prompt
     * @param cityCardName the city card that is being shared from one player to the other
     * @param receivingPlayerRole the role of the player that RECEIVES the event card from the givingPlayerRole
     * @param givingPlayerRole the role of the player GIVING the event card to the receivingPlayerRole
     */
    public static void sendShareKnowledgeConsentRequest(Client client,
                                                        String consentPrompt, String receivingPlayerName, String cityCardName,
                                                        RoleType givingPlayerRole, RoleType receivingPlayerRole) {
        client.sendMessageToServer(
                ServerCommands.INITIATE_CONSENT_REQUIRING_MOVE.name(),
                            receivingPlayerName, consentPrompt,
                            new UpdateRequest(
                                    new PostCondition(
                                            PostCondition.ACTION.MOVE_CARD,
                                            new PlayerCardSimple(CardType.CityCard, cityCardName),
                                            givingPlayerRole,
                                            receivingPlayerRole
                                    )
                            )
                 );
    }

    /**
     * Sends a message to the server that the current player would like to accept/decline the share knowledge request.
     * @param client the client object
     * @param acceptPrompt true if the player would like to accept; false otherwise
     */
    public static void sendAnswerToShareKnowledge(Client client, boolean acceptPrompt) {
        client.sendMessageToServer(
                ServerCommands.ANSWER_CONSENT_PROMPT.name(),
                acceptPrompt
        );
    }

    /**
     * Sends a message to the server that the current player would like to end his turn.
     * @param client the client object
     */
    public static void sendEndTurnRequest(Client client, boolean acceptPrompt) {
        client.sendMessageToServer(
                ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.END_TURN
                        )
                )
        );
    }
}