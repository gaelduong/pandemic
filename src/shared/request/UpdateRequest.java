package shared.request;

import pandemic.*;
import shared.PlayerCardSimple;
import shared.CardTargetType;
import shared.TravelType;
import shared.Utils;

import java.io.Serializable;
import java.util.ArrayList;
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
    private boolean epidemicOccured = false;

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
    public int executeRequest(Game game, String playerUsername) {
        postConditions.forEach(action -> {if(executeAction(game, playerUsername, action) == 1)
                                                setEpidemicOccured();
                                            });
        if(epidemicOccured)
            return 1;
        return 0;
    }

    private int executeAction(Game game, String playerUsername, PostCondition action) {
        int status = 0;
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
        }
        return status;
    }

    private void executeMoveCard(Game game, String playerUsername, List arguments) {
        final PlayerCardSimple cardToMove = (PlayerCardSimple)arguments.get(0);
        final String cardSourceString = (String)arguments.get(1);         //read the MOVE_CARD enum for the string encoding
        final String cardDestinationString = (String)arguments.get(2);

        final CardSource cardSource = (CardSource) getCardSourceTarget(cardSourceString, playerUsername, game);
        final CardTarget cardTarget = (CardTarget) getCardSourceTarget(cardDestinationString, playerUsername, game);

        PlayerCard cardToMoveObj = cardSource.getCard(cardToMove);

        cardTarget.acceptCard(cardToMoveObj);

        if(cardSource instanceof PlayerDeck)
            ((PlayerDeck) cardSource).drawCard();

        if(cardSource instanceof Player)
            ((Player) cardSource).discardCard(cardToMoveObj);
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
                        break;
                    case DIRECT_FLIGHT:
                        CityCard card = (CityCard) game.getCurrentPlayer().getCard(new PlayerCardSimple(CardType.CityCard, cityName));
                        gameManager.playDirectFlight(card);
                        currentPlayer.discardCard(card);
                        game.getPlayerDiscardPile().acceptCard(card);
                        break;
                    case SHUTTLE_FLIGHT:
                        break;
                        //TODO more flight options to fill in later

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

        //TODO omer link to backend commands
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


            if (toTreat == null) {
                System.err.println("ERROR - cant find disease flag of that type in current player city");
                return;
            }

        } else {
            System.err.println("ERROR - passed player user name does not correspond to current player");
        }
    }

    private void executeBuildResearchStation(Game game, String playerUsername, List arguments) {
        final String cityNameToRemove_Optional = arguments.size() == 1 ? (String)arguments.get(0) : null;
        //TODO omer link with back-end commands
    }

    private int executeEndTurn(Game game) {
        final GameManager gameManager = game.getGameManager();
        return gameManager.endTurn();


        //if(game.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayerDiscardingCards)
            // inform current player to discard cards
    }

    private CardSourceTarget getCardSourceTarget(String sourceTarget, String playerUsername, Game game) {
        final RoleType playerRole = Utils.getEnum(RoleType.class, sourceTarget);
        CardSourceTarget result = null;

        if (playerRole == null) { //not a player
            final CardTargetType deckSource = Utils.getEnum(CardTargetType.class, sourceTarget);
            switch (deckSource) {
                case DECK:
                    result = game.getPlayerDeck();
                    break;
                case DISCARD_PILE:
                    result = game.getPlayerDiscardPile();
                    break;
            }
        } else {
            result = game.getGameManager().getActivePlayers().stream()
                                                            .filter(p -> p.getRoleType() == playerRole)
                                                            .findAny().orElse(null);
        }
        return result;
    }

    private void setEpidemicOccured() {
        epidemicOccured = true;
    }
}