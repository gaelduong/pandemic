package shared.request;

import api.socketcomm.Server;
import pandemic.*;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;
import pandemic.eventcards.impl.*;
import server.ServerRequests;
import shared.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an action that the client would like to perform.
 * If the action is accepted by the server, the server will update its internal GameState accordingly.
 * Then it will propagate the new GameState to all clients.
 */
public class UpdateRequest implements Serializable {

    private static final long serialVersionUID = 6316510531595159342L;

    private final List<PostCondition> postConditions;
    private PreCondition preCondition = null;
    private PostCondition currentlyProcessedAction;
    private boolean epidemicOccured = false;
    private String epidemicString;

    /**
     * A sequence of actions that will be performed in order
     * @param actions nonempty list
     */
    public UpdateRequest(PostCondition... actions) {
        this.postConditions = Arrays.asList(actions);
    }

    /**
     * A sequence of actions that will be performed in order if and only if its pre condition is satisfied.
     *
     * @param preCondition condition which must be satisfied before execution
     * @param actions nonempty list
     */
    public UpdateRequest(PreCondition preCondition, PostCondition... actions) {
        this(actions);
        this.preCondition = preCondition;
    }

    public boolean isRequestValid() {
        return preCondition == null || preCondition.isSatisifed();
    }

    /**
     * Executes the update request on the game.
     */
    public String executeRequest(Server server, Game game, String playerUsername) {
        postConditions.forEach(action -> {
            currentlyProcessedAction = action;

            String epidemic = executeAction(game, playerUsername, action);
            if(!epidemic.equals(""))
                setEpidemicOccured(epidemic);

            if (currentlyProcessedAction.isLogValid())
                ServerRequests.sendGameLog(server, currentlyProcessedAction);
        });

        if(epidemicOccured)
            return epidemicString;
        return "";
    }



    private String executeAction(Game game, String playerUsername, PostCondition action) {
        String status = "";
        currentlyProcessedAction.setLog_playerUserName(playerUsername);
        final List arguments = action.getArguments();
        switch (action.getActionType()) {
            case MOVE_CARD:
                executeMoveCard(game, playerUsername, arguments);
                break;

            case MOVE_PLAYER_POS:
                executeMovePlayerPos(game, playerUsername, arguments);
                break;

            case DISCOVER_CURE:
                executeDiscoverCure(game, playerUsername, arguments);
                break;

            case TREAT_DISEASE:
                executeTreatDisease(game, playerUsername, arguments);
                break;

            case BUILD_RESEARCH_STATION:
                executeBuildResearchStation(game, playerUsername, arguments);
                break;

            case END_TURN:
                status = executeEndTurn(game);
                break;

            case EVENT_CARD:
                executeEventCard(game, playerUsername, arguments);
                break;

            case BIOT_TURN:
                executeBioTTurn(game, playerUsername, arguments);
                //TODO add currentlyProcessedAction.setLog_actionResult() for game log
                break;

            case INFECT_NEXT_CITY:
                executeInfectNextCity(game, playerUsername);
                //TODO add currentlyProcessedAction.setLog_actionResult() for game log
                break;

        }
        return status;
    }

    private void executeInfectNextCity(Game game, String playerUsername) {
        final GameManager gameManager = game.getGameManager();
        gameManager.infectNextCity();
    }

    private void executeMoveCard(Game game, String playerUsername, List arguments) {
        final PlayerCardSimple cardToMove = (PlayerCardSimple)arguments.get(0);
        final String cardSourceString = (String)arguments.get(1);         //read the MOVE_CARD enum for the string encoding
        final String cardDestinationString = (String)arguments.get(2);

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.getPlayerUserName().equals(playerUsername)) {
            final CardSource cardSource = (CardSource) getCardSourceTarget(cardSourceString, game, currentPlayer);
            final CardTarget cardTarget = (CardTarget) getCardSourceTarget(cardDestinationString, game, currentPlayer);

            Card cardToMoveObj = cardSource.getCard(cardToMove);

            cardTarget.acceptCard(cardToMoveObj);

            if(cardSource instanceof PlayerDeck)
                ((PlayerDeck) cardSource).drawCard();

            if(cardSource instanceof InfectionDeck)
                ((InfectionDeck) cardSource).drawCard();

            if(cardSource instanceof Player)
                ((Player) cardSource).discardCard(cardToMoveObj);
        }
    }

    private void executeMovePlayerPos(Game game, String playerUsername, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final String playerUserName = (String)arguments.get(0);
        final String cityName = (String)arguments.get(1);
        final TravelType travelType = (TravelType)arguments.get(2);

        final CityName newPositionCityName;
        final City newPosition;


        if (game.getCurrentPlayer().getPlayerUserName().equals(playerUserName)) {
            Player currentPlayer = game.getCurrentPlayer();

            try {

                switch (travelType) {
                    case DRIVE_FERRY:
                        newPositionCityName = Utils.getEnum(CityName.class, cityName);
                        newPosition = gameManager.getCityByName(newPositionCityName);
                        gameManager.playDriveFerry(newPosition);
                        currentlyProcessedAction.setLog_actionResult("has driven to " + cityName);
                        break;
                    case DIRECT_FLIGHT:
                        Card card = game.getCurrentPlayer().getCard(new PlayerCardSimple(CardType.MovingCard, cityName));
                        gameManager.playDirectFlight((MovingCard) card);
                        currentPlayer.discardCard(card);
                        if(currentPlayer.isBioTerrorist()) {
                            game.getInfectionDiscardPile().acceptCard(card);
                        } else {
                            game.getPlayerDiscardPile().acceptCard(card);
                        }
                        currentlyProcessedAction.setLog_actionResult("has taken a direct flight to " + cityName);
                        break;
                    case SHUTTLE_FLIGHT:
                        newPositionCityName = Utils.getEnum(CityName.class, cityName);
                        newPosition = gameManager.getCityByName(newPositionCityName);
                        gameManager.playShuttleFlight(newPosition);
                        currentlyProcessedAction.setLog_actionResult("has taken a shuttle flight to " + cityName);
                        break;

                    case CHARTER_FLIGHT:
                        newPositionCityName = Utils.getEnum(CityName.class, cityName);
                        newPosition = gameManager.getCityByName(newPositionCityName);
                        String cardName = (String) arguments.get(3);
                        Card cardToDiscard = game.getCurrentPlayer().getCard(new PlayerCardSimple(CardType.MovingCard, cardName));

                        gameManager.playCharterFlight(newPosition);
                        currentPlayer.discardCard(cardToDiscard);
                        if(currentPlayer.isBioTerrorist()) {
                            game.getInfectionDiscardPile().acceptCard(cardToDiscard);
                        } else {
                            game.getPlayerDiscardPile().acceptCard(cardToDiscard);
                        }
                        currentlyProcessedAction.setLog_actionResult("has taken a charter flight to " + cityName);
                        break;

                }
            } catch (NullPointerException e) {
                System.out.println("ERROR - passed city name for move not found.");
                e.printStackTrace();
            }

        } else {
            System.err.println("ERROR - passed player user name does not correspond to current player");
        }
    }

    @SuppressWarnings("unchecked")
    private void executeDiscoverCure(Game game, String playerUsername, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final List<CityCard> cardToDiscard = (List<CityCard>)arguments.get(0);

        final List<PlayerCard> cardsToDiscard = (List<PlayerCard>)arguments.get(0);
        final DiseaseType toCure = (DiseaseType)arguments.get(arguments.size());

        gameManager.playDiscoverCure(toCure, cardsToDiscard);
        currentlyProcessedAction.setLog_actionResult("has discovered a cure for type " + cardToDiscard.get(0).getRegion().toString().toLowerCase() + "!");
    }

    private void executeTreatDisease(Game game, String playerUsername, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final DiseaseType diseaseType = (DiseaseType) arguments.get(0);

        if (game.getCurrentPlayer().getPlayerUserName().equals(playerUsername)) {
            City currentPlayerPos = game.getCurrentPlayer().getPawn().getLocation();

                DiseaseFlag toTreat = (DiseaseFlag) currentPlayerPos.getCityUnits().stream().filter(u -> u.getUnitType() == UnitType.DiseaseFlag
                                                                                                    && ((DiseaseFlag) u).getDiseaseType() == diseaseType)
                                                        .findAny().orElse(null);
            if (toTreat == null) {
                System.out.println("DiseaseType : " + diseaseType);
                System.out.println("toTreat : " + toTreat);
                System.out.println("agr[0] :" + (String) arguments.get(0));
            } else {
                gameManager.playTreatDisease(toTreat);
            }

            currentlyProcessedAction.setLog_actionResult("has treated the " + diseaseType + " disease!");
            if (toTreat == null) {
                System.err.println("ERROR - cant find disease flag of that type in current player city");
                return;
            }

        } else {
            System.err.println("ERROR - passed player user name does not correspond to current player");
        }
    }

    private void executeBuildResearchStation(Game game, String playerUsername, List arguments) {
        final String cityLocationToBuild = (String)arguments.get(0);
        final String cityNameToRemove_Optional = arguments.size() == 2 ? (String)arguments.get(1) : null;

        if (cityNameToRemove_Optional != null){
            CityName cityNameToRemoveFrom = Utils.getEnum(CityName.class, cityNameToRemove_Optional);
            City cityToRemoveFrom = game.getCityByName(cityNameToRemoveFrom);
            ResearchStation toRemove = null;
            for (Unit u : cityToRemoveFrom.getCityUnits()){
                if (u.getUnitType().equals(UnitType.ResearchStation)){
                    toRemove = (ResearchStation) u;
                    break;
                }
            }
            if (toRemove != null) {
                game.getGameManager().removeResearchStation(toRemove);
            }
        }

       game.getGameManager().playBuildResearchStation();
        currentlyProcessedAction.setLog_actionResult("has built a research station in " + cityLocationToBuild);
    }

    private String executeEndTurn(Game game) {
        final GameManager gameManager = game.getGameManager();
        final String endTurnStatus = gameManager.endTurn();
        currentlyProcessedAction.setLog_playerUserName("GAME");
        currentlyProcessedAction.setLog_actionResult(endTurnStatus);
        return endTurnStatus;


        //if(game.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayerDiscardingCards)
            // inform current player to discard cards
    }

    private CardSourceTarget getCardSourceTarget(String sourceTarget, Game game, Player currentPlayer) {
        final RoleType playerRole = Utils.getEnum(RoleType.class, sourceTarget);
        CardSourceTarget result = null;



        if (playerRole == null) { //not a player
            final CardTargetType deckSource = Utils.getEnum(CardTargetType.class, sourceTarget);
            switch (deckSource) {   //it won't be null don't worry
                case DECK:
                    result = currentPlayer.isBioTerrorist() ? game.getInfectionDeck() : game.getPlayerDeck();
                    break;
                case DISCARD_PILE:
                    result = currentPlayer.isBioTerrorist() ? game.getInfectionDiscardPile() : game.getPlayerDiscardPile();
                    break;
                case REMOVE_FROM_GAME:
                    result = game.getMyGameCardRemover();
                    break;
            }
        } else {
            result = game.getGameManager().getActivePlayers().stream()
                                                            .filter(p -> p.getRoleType() == playerRole)
                                                            .findAny().orElse(null);
        }
        return result;
    }

    private void executeBioTTurn(Game game, String playerUsername, List arguments) {
        final BioTActionType actionType = (BioTActionType) arguments.get(0);

        if (game.getCurrentPlayer().getPlayerUserName().equals(playerUsername)) {
            Player currentPlayer = game.getCurrentPlayer();

            try {
                switch (actionType) {
                    case DRAW_CARD:
                        if(currentPlayer.isBioTerrorist())
                            playDrawCardBioT(game, currentPlayer);
                        break;

                    case TRAVEL:
                        if(currentPlayer.isBioTerrorist())
                            executeMovePlayerPos(game, playerUsername, arguments.subList(1, arguments.size()));
                        break;

                    case INFECT_LOCALLY:
                        if(currentPlayer.isBioTerrorist())
                            playInfectLocallyBioT(game);
                        break;

                    case INFECT_REMOTELY:
                        if(currentPlayer.isBioTerrorist())
                            playInfectRemotelyBioT(game, currentPlayer, arguments);
                        break;

                    case SABOTAGE:
                        if(currentPlayer.isBioTerrorist())
                            playSabotageBioT(game, currentPlayer, arguments);
                        break;

                    case ESCAPE:
                        if(currentPlayer.isBioTerrorist())
                            playEscapeBioT(game, currentPlayer, arguments);
                        break;
                    case CAPTURE:
                        if(!currentPlayer.isBioTerrorist())
                            playCaptureBioT(game);
                        break;
                    case END_TURN:
                        if(currentPlayer.isBioTerrorist())
                            playEndTurnBioT(game);
                        break;

                }
            } catch (NullPointerException e) {
                System.out.println("ERROR - passed city name for move not found.");
                e.printStackTrace();
            }

        } else {
            System.err.println("ERROR - passed player user name does not correspond to current player");
        }
    }

    private void playDrawCardBioT(Game game, Player bioTPlayer) {
        final GameManager gameManager = game.getGameManager();


        final InfectionCard cardDrawn = game.getInfectionDeck().drawCard();

        if(cardDrawn != null) {

            if (bioTPlayer.getBioTTurnTracker().isType2ActionsCompleted()) {
                return;
            } else {
                bioTPlayer.acceptCard(cardDrawn);
                bioTPlayer.getBioTTurnTracker().incrementType2ActionCounter();
            }
        }

        if(bioTPlayer.getCardsInHandBioT().size() > 7) {
            // TODO:
            //      must alert for discard
        }

    }

    private void playInfectLocallyBioT(Game game) {
        final GameManager gameManager = game.getGameManager();
        gameManager.playInfectLocallyBioT();
    }

    private void playInfectRemotelyBioT(Game game, Player bioTPlayer, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final String cityName = (String)arguments.get(2);

        CityInfectionCard card =
                (CityInfectionCard) game.getCurrentPlayer().getCard(new PlayerCardSimple(CardType.CityInfectionCard, cityName));

        gameManager.playInfectRemotelyBioT(card);
        bioTPlayer.discardCard(card);
        game.getInfectionDiscardPile().acceptCard(card);
    }

    private void playSabotageBioT(Game game, Player bioTPlayer, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final String cityName = (String)arguments.get(2);

        CityInfectionCard card =
                (CityInfectionCard) game.getCurrentPlayer().getCard(new PlayerCardSimple(CardType.CityInfectionCard, cityName));

        gameManager.playSabotageBioT(card);
        bioTPlayer.discardCard(card);
        game.getInfectionDiscardPile().acceptCard(card);

    }

    private void playEscapeBioT(Game game, Player bioTPlayer, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final String cityName = (String) arguments.get(2);

        CityInfectionCard card =
                (CityInfectionCard) game.getCurrentPlayer().getCard(new PlayerCardSimple(CardType.CityInfectionCard, cityName));

        gameManager.playEscapeBioT(card);
        bioTPlayer.discardCard(card);
        game.getInfectionDiscardPile().acceptCard(card);
    }

    private void playCaptureBioT(Game game) {
        final GameManager gameManager = game.getGameManager();
        gameManager.playCaptureBioT();
    }

    private void playEndTurnBioT(Game game) {
        final GameManager gameManager = game.getGameManager();
        gameManager.endTurnBioT();
    }

    private void executeEventCard(Game game, String playerUsername, List arguments) {
        final EventCardName name = (EventCardName) arguments.get(0);
        final List eventCardArgs = (List) arguments.get(1);

        final Player eventCardPlayer = game.getGameManager().getPlayerFromUsername(playerUsername);
        final EventCard eventCard = (EventCard)eventCardPlayer.getCard(new PlayerCardSimple(CardType.EventCard, name.toString()));

        switch (name) {
            case AirLift:
                playAirLiftEC(game, eventCard, eventCardArgs);
                break;
            case Forecast:
                playForecastEC(game, eventCard, eventCardArgs);
                break;
            case CommercialTravelBan:
                playCommercialBanEC(game, eventCard, eventCardArgs);
                break;
            case BorrowedTime:
                playBorrowedTimeEC(game, eventCard);
                break;
            case GovernmentGrant:
                playGovernmentGrantEC(game, eventCard, eventCardArgs);
                break;
            case MobileHospital:
                playMobileHospitalEC(game, eventCard);
                break;
            case OneQuietNight:
                playOneQuietNightEC(game, eventCard, eventCardArgs);
                break;
            case ResilientPopulation:
                playResilientPopulationEC(game, eventCard, eventCardArgs);
                break;
        }

    }

    private void playResilientPopulationEC(Game game, EventCard eventCard, List eventCardArgs) {
        final ResilientPopulationEventCard toPlay = (ResilientPopulationEventCard)eventCard;
        final String cardOwnerName = (String) eventCardArgs.get(0);
        final InfectionCard cardToMove = (InfectionCard) eventCardArgs.get(1);
        toPlay.playEventCard(game.getGameManager().getPlayerFromUsername(cardOwnerName), cardToMove);
    }

    private void playOneQuietNightEC(Game game, EventCard eventCard, List eventCardArgs) {
        final OneQuietNightEventCard toPlay = (OneQuietNightEventCard)eventCard;
        final String cardOwnerName = (String) eventCardArgs.get(0);
        toPlay.playEventCard(game.getGameManager().getPlayerFromUsername(cardOwnerName));
    }

    private void playMobileHospitalEC(Game game, EventCard eventCard) {
        final MobileHospitalEventCard toPlay = (MobileHospitalEventCard)eventCard;
        toPlay.playEventCard();
    }

    private void playGovernmentGrantEC(Game game, EventCard eventCard, List eventCardArgs) {
        final GovernmentGrantEventCard toPlay = (GovernmentGrantEventCard)eventCard;
        final String cardOwnerName = (String) eventCardArgs.get(0);
        final CityName targetCityName = (CityName) eventCardArgs.get(1);
        toPlay.playEventCard(game.getGameManager().getPlayerFromUsername(cardOwnerName),
                game.getGameManager().getCityByName(targetCityName));
    }

    private void playBorrowedTimeEC(Game game, EventCard eventCard) {
        final BorrowedTimeEventCard toPlay = (BorrowedTimeEventCard)eventCard;
        toPlay.playEventCard();
    }

    private void playCommercialBanEC(Game game, EventCard eventCard, List eventCardArgs) {
        final CommercialTravelBanEventCard toPlay = (CommercialTravelBanEventCard)eventCard;
        final String cardOwnerName = (String) eventCardArgs.get(0);
        toPlay.playEventCard(game.getGameManager().getPlayerFromUsername(cardOwnerName));
    }

    @SuppressWarnings("unchecked")
    private void playForecastEC(Game game, EventCard eventCard, List eventCardArgs) {
        final ForecastEventCard toPlay = (ForecastEventCard)eventCard;
        final String cardOwnerName = (String) eventCardArgs.get(0);
        final List<InfectionCard> rearrangedCards = (List<InfectionCard>) eventCardArgs.get(1);
        toPlay.playEventCard(game.getGameManager().getPlayerFromUsername(cardOwnerName), rearrangedCards);
    }

    private void playAirLiftEC(Game game, EventCard eventCard, List eventCardArgs) {
        final AirLiftEventCard toPlay = (AirLiftEventCard)eventCard;
        final String cardOwnerName = (String) eventCardArgs.get(0);
        final String playerToMoveName = (String) eventCardArgs.get(1);
        final CityName cityDestinationName = (CityName) eventCardArgs.get(2);
        toPlay.playEventCard(game.getGameManager().getPlayerFromUsername(cardOwnerName),
                game.getGameManager().getPlayerFromUsername(playerToMoveName).getPawn(),
                game.getGameManager().getCityByName(cityDestinationName));
    }

    private void setEpidemicOccured(String epidemicString) {
        epidemicOccured = true;
        this.epidemicString = epidemicString;
    }

    public List<String> getPrettyLogPrint() {
        //TODO russell
        return null;
    }
}