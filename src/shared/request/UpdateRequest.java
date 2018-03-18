package shared.request;

import pandemic.*;
import shared.PlayerCardSimple;
import shared.CardTargetType;
import shared.TravelType;
import shared.Utils;

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
    public void executeRequest(Game game, String playerUsername) {
        postConditions.forEach(action -> executeAction(game, playerUsername, action));
    }

    private void executeAction(Game game, String playerUsername, PostCondition action) {
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
                executeBuildResearchStation(game, playerUsername);
                break;

            case END_TURN:
                executeEndTurn(game);
                break;
        }
    }

    private void executeMoveCard(Game game, String playerUsername, List arguments) {
        final PlayerCardSimple cardToMove = (PlayerCardSimple)arguments.get(0);
        final String cardSourceString = (String)arguments.get(1);         //read the MOVE_CARD enum for the string encoding
        final String cardDestinationString = (String)arguments.get(2);

        final CardSource cardSource = (CardSource) getCardSourceTarget(cardSourceString, playerUsername, game);
        final CardTarget cardTarget = (CardTarget) getCardSourceTarget(cardDestinationString, playerUsername, game);

        cardTarget.acceptCard(cardSource.getCard(cardToMove));



        //(should we consider being thread-safe?)
    }

    private void executeMovePlayerPos(Game game, String playerUsername, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final String playerUserName = (String)arguments.get(0);
        final String cityName = (String)arguments.get(1);
        final TravelType travelType = (TravelType)arguments.get(2);

        final CityName newPositionCityName;
        final City newPosition;


        if (game.getCurrentPlayer().getPlayerUserName().equals(playerUserName)) {

            try {

                switch (travelType) {
                    case DRIVE_FERRY:
                        newPositionCityName = Utils.getEnum(CityName.class, cityName);
                        newPosition = gameManager.getCityByName(newPositionCityName);
                        gameManager.playDriveFerry(newPosition);
                    case DIRECT_FLIGHT:
                        CityCard card = (CityCard) game.getCurrentPlayer().getCard(new PlayerCardSimple(CardType.CityCard, cityName));
                        gameManager.playDirectFlight(card);
                        game.getCurrentPlayer().discardCard(card);
                        game.getPlayerDiscardPile().acceptCard(card);
                    case SHUTTLE_FLIGHT:
                        //TODO to fill in later

                }
            } catch (NullPointerException e) {
                System.out.println("ERROR - passed city name for move not found.");
                e.printStackTrace();
            }

        }

        //TODO link to backend commands (should we consider being thread-safe?)
    }

    private void executeDiscoverCure(Game game, String playerUsername, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final DiseaseType diseaseType = (DiseaseType)arguments.get(0);
        @SuppressWarnings("unchecked")
        final List<PlayerCardSimple> cardsToDiscard = (List<PlayerCardSimple>)arguments.get(1);

        //TODO link to backend commands (should we consider being thread-safe?)
    }

    private void executeTreatDisease(Game game, String playerUsername, List arguments) {
        final GameManager gameManager = game.getGameManager();
        final DiseaseType diseaseType = (DiseaseType)arguments.get(0);

        //TODO link to backend commands (should we consider being thread-safe?)
    }

    private void executeBuildResearchStation(Game game, String playerUsername) {
        //TODO link to backend commands (should we consider being thread-safe?)
    }

    private void executeEndTurn(Game game) {
        //TODO link to backend commands (should we consider being thread-safe?)
        final GameManager gameManager = game.getGameManager();
    }

    private CardSourceTarget getCardSourceTarget(String sourceTarget, String playerUsername, Game game) {
        final RoleType playerRole = Utils.getEnum(RoleType.class, sourceTarget);
        CardSourceTarget result = null;

        if (playerRole == null) { //not a player
            final CardTargetType deckSource = Utils.getEnum(CardTargetType.class, sourceTarget);
            switch (deckSource) {
                case DECK:
                    result = game.getPlayerDeck();

                case DISCARD_PILE:
                    result = game.getPlayerDiscardPile();
            }
        } else {
            result = game.getGameManager().getActivePlayers().stream()
                                                            .filter(p -> p.getRoleType() == playerRole
                                                                     && p.getPlayerUserName().equals(playerUsername))
                                                            .findAny().orElse(null);
        }
        return result;
    }
}