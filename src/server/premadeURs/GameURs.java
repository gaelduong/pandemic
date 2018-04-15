package server.premadeURs;

import api.socketcomm.Client;
import pandemic.CardType;
import pandemic.CityCard;
import pandemic.DiseaseType;
import pandemic.RoleType;
import server.ServerCommands;
import shared.BioTActionType;
import shared.CardTargetType;
import shared.PlayerCardSimple;
import shared.TravelType;
import shared.request.PostCondition;
import shared.request.UpdateRequest;

import java.util.List;

/**
 * The UpdateRequest encoding of the game actions
 */
public class GameURs {

    public static UpdateRequest getMovePlayerMPosUR(String playerUserName, String cityName, TravelType travelType) {
        return new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.MOVE_PLAYER_POS,
                        playerUserName, //source
                        cityName,       //destination
                        travelType
                )
        );
    }

    public static UpdateRequest getMovePlayerMPosBioTUR(String playerUserName, String cityName, BioTActionType travelActionType,
                                                        TravelType travelType) {
        return new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.BIOT_TURN,
                        travelActionType, // type of bioT turn action
                        playerUserName, //source
                        cityName,      //destination
                        travelType
                )
        );
    }

    /**
     * Creates a drive/ferry update request, which the player drives to the target city
     */
    public static void sendDriveFerryUR(Client client, String playerUserName, String cityName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                getMovePlayerMPosUR(playerUserName, cityName, TravelType.DRIVE_FERRY));
    }

    public static void sendDriveFerryBioTUR(Client client, String playerUserName, String cityName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                getMovePlayerMPosBioTUR(playerUserName, cityName, BioTActionType.TRAVEL, TravelType.DRIVE_FERRY));
    }

    /**
     * Creates a drive update request, which the player drives to the target city
     */
    public static void sendDirectFlightUR(Client client, String playerUserName, String cityName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                getMovePlayerMPosUR(playerUserName, cityName, TravelType.DIRECT_FLIGHT));
    }

    public static void sendDirectFlightBioTUR(Client client, String playerUserName, String cityName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                getMovePlayerMPosBioTUR(playerUserName, cityName, BioTActionType.TRAVEL, TravelType.DIRECT_FLIGHT));
    }

    /**
     * Creates a shuttle flight request.
     * Assumes the caller of this method makes sure this method call is valid.
     */
    public static void sendShuttleFlightUR(Client client, String playerUserName, String cityName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                getMovePlayerMPosUR(playerUserName, cityName, TravelType.SHUTTLE_FLIGHT));
    }

    /**
     * Creates a charter flight update request. Discards the cardToDiscardCityName and moves the player to cityName.
     * Assumes the caller of this method makes sure this method call is valid.
     */
    public static void sendCharterFlightUR(Client client, String cardToDiscardCityName, String cityName, String playerUserName, RoleType playerRole) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                       /* new PostCondition(
                                PostCondition.ACTION.MOVE_CARD,
                                new PlayerCardSimple(CardType.CityCard, cardToDiscardCityName),
                                playerRole.name(),                         //source
                                CardTargetType.DISCARD_PILE         //destination
                        ),*/
                        new PostCondition(
                                PostCondition.ACTION.MOVE_PLAYER_POS,
                                playerUserName,
                                cityName,
                                TravelType.CHARTER_FLIGHT,
                                cardToDiscardCityName
                        )
                )
        );
    }

    /**
     * This action discovers the cure for a certain DiseaseType and discards the inputted list of cards.
     * <br>
     * The server will check the player curing the disease is at a ResearchStation.
     *
     * <br><br><b>Arguments required:</b><br>
     * List<{@link pandemic.CityCard}> cardsToDiscard
     */
    public static void sendDiscoverCure(Client client, List<CityCard> cardsToDiscard) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.DISCOVER_CURE,
                                cardsToDiscard
                        )
                )
        );
    }

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
    public static void sendBuildResearchStation(Client client, String cityLocationToBuild, String cityNameToRemove_Optional) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.BUILD_RESEARCH_STATION,
                                cityLocationToBuild,
                                cityNameToRemove_Optional
                        )
                )
        );
    }

    /**
     * Creates an update request to treat a disease in the player's current city
     */
    public static void sendTreatDiseaseUR(Client client, DiseaseType diseaseType) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
            new UpdateRequest(
                    new PostCondition(
                            PostCondition.ACTION.TREAT_DISEASE,
                            diseaseType
                    )
            )
        );
    }

    /**
     * Creates an update request that discovers the cure for a certain DiseaseType
     * and discards the inputted list of cards
     */
    public static void sendDiscoverCureUR(Client client, List<CityCard> cityCardsToDiscard) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
            new UpdateRequest(
                new PostCondition(
                        PostCondition.ACTION.DISCOVER_CURE,
                        cityCardsToDiscard
                )
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
        System.out.printf("consentPrompt: %s, receivingPlayerName: %s, cityCardName: %s, givingPlayerRole: %s, receivingPlayerRole: %s\n",
                consentPrompt, receivingPlayerName, cityCardName, givingPlayerRole, receivingPlayerRole);

        client.sendMessageToServer(
                ServerCommands.INITIATE_CONSENT_REQUIRING_MOVE.name(),
                            receivingPlayerName, consentPrompt,
                            new UpdateRequest(
                                    new PostCondition(
                                            PostCondition.ACTION.MOVE_CARD,
                                            new PlayerCardSimple(CardType.CityCard, cityCardName),
                                            givingPlayerRole.name(),
                                            receivingPlayerRole.name()
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
    public static void sendEndTurnRequest(Client client) {
        client.sendMessageToServer(
                ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.END_TURN
                        )
                )
        );
    }

    public static void sendDrawCardBioTUR(Client client, String playerUserName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.BIOT_TURN,
                                BioTActionType.DRAW_CARD, // type of bioT turn action
                                playerUserName
                        )
                )
        );
    }

    public static void sendInfectLocallyBioTUR(Client client, String playerUserName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.BIOT_TURN,
                                BioTActionType.INFECT_LOCALLY, // type of bioT turn action
                                playerUserName
                        )
                )
        );
    }

    public static void sendInfectRemotelyBioTUR(Client client, String playerUserName, String cityName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.BIOT_TURN,
                                BioTActionType.INFECT_REMOTELY, // type of bioT turn action
                                playerUserName,
                                cityName
                        )
                )
        );
    }

    public static void sendSabotageBioTUR(Client client, String playerUserName, String cityName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.BIOT_TURN,
                                BioTActionType.SABOTAGE, // type of bioT turn action
                                playerUserName,
                                cityName
                        )
                )
        );
    }

    public static void sendEscapeBioTUR(Client client, String playerUserName, String cityName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.BIOT_TURN,
                                BioTActionType.ESCAPE, // type of bioT turn action
                                playerUserName,
                                cityName
                        )
                )
        );
    }

    public static void sendCaptureBioTUR(Client client, String playerUserName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.BIOT_TURN,
                                BioTActionType.CAPTURE, // type of bioT turn action
                                playerUserName
                        )
                )
        );
    }

    public static void sendEndTurnBioTUR(Client client, String playerUserName) {
        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
                new UpdateRequest(
                        new PostCondition(
                                PostCondition.ACTION.BIOT_TURN,
                                BioTActionType.END_TURN, // type of bioT turn action
                                playerUserName
                        )
                )
        );
    }

}