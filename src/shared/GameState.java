package shared;

import javafx.util.Pair;
import pandemic.*;

import java.util.List;
import java.util.Map;

/**
 * Condensed version of the server's core game state.
 * Includes everything the client needs to render the state on the front-end
 */
public class GameState {

    private final Map<RoleType, List<PlayerCard>> cardMap;
    private final Map<RoleType, CityName> positionMap;
    private final Map<CityName, List<Pair<DiseaseType,Integer>>> diseaseCubesMap;
    private final InfectionDiscardPile infectionDiscardPile;
    private final PlayerDiscardPile playerDiscardPile;

    /**
     * The server will create this object! The client just receives it.
     */
    public GameState(Map<RoleType, List<PlayerCard>> cardMap, Map<RoleType, CityName> positionMap, Map<CityName,
            List<Pair<DiseaseType, Integer>>> diseaseCubesMap, InfectionDiscardPile infectionDiscardPile, PlayerDiscardPile playerDiscardPile) {
        this.cardMap = cardMap;
        this.positionMap = positionMap;
        this.diseaseCubesMap = diseaseCubesMap;
        this.infectionDiscardPile = infectionDiscardPile;
        this.playerDiscardPile = playerDiscardPile;
    }

    /**
     * Maps players to their hand
     */
    public Map<RoleType, List<PlayerCard>> getCardMap() {
        return cardMap;
    }

    /**
     * Maps players to their position on the board
     */
    public Map<RoleType, CityName> getPositionMap() {
        return positionMap;
    }

    /**
     * Maps cities to their list of DiseaseCubeTuples. {@link javafx.util.Pair} is used for the tuple object.
     */
    public Map<CityName, List<Pair<DiseaseType, Integer>>> getDiseaseCubesMap() {
        return diseaseCubesMap;
    }

    public InfectionDiscardPile getInfectionDiscardPile() {
        return infectionDiscardPile;
    }

    public PlayerDiscardPile getPlayerDiscardPile() {
        return playerDiscardPile;
    }
}