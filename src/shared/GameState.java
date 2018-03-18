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

    private final Map<RoleType, String> userMap;
    private final Map<RoleType, List<PlayerCard>> cardMap;
    private final Map<RoleType, CityName> positionMap;
    private final Map<CityName, List<Pair<DiseaseType,Integer>>> diseaseCubesMap;
    private final InfectionDiscardPile infectionDiscardPile;
    private final PlayerDiscardPile playerDiscardPile;
    private final int currentInfectionRate;
    private final int currentOutbreakMeter;



    /**
     * The server will create this object! The client just receives it.
     */
    public GameState(Map<RoleType, String> userMap, Map<RoleType, List<PlayerCard>> cardMap, Map<RoleType, CityName> positionMap, Map<CityName,
            List<Pair<DiseaseType, Integer>>> diseaseCubesMap, InfectionDiscardPile infectionDiscardPile, PlayerDiscardPile playerDiscardPile,
            int currentInfectionRate, int currentOutbreakMeter) {
        this.userMap = userMap;
        this.cardMap = cardMap;
        this.positionMap = positionMap;
        this.diseaseCubesMap = diseaseCubesMap;
        this.infectionDiscardPile = infectionDiscardPile;
        this.playerDiscardPile = playerDiscardPile;
        this.currentInfectionRate = currentInfectionRate;
        this.currentOutbreakMeter = currentOutbreakMeter;
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

    /**
     * Maps players to their login username
     */
    public Map<RoleType, String> getUserMap() {
        return userMap;
    }

    public InfectionDiscardPile getInfectionDiscardPile() {
        return infectionDiscardPile;
    }

    public PlayerDiscardPile getPlayerDiscardPile() {
        return playerDiscardPile;
    }

    /**
     * Return the current game infection rate and outbreak meter readings
     */
    public int getCurrentInfectionRate() { return currentInfectionRate; }

    public int getCurrentOutbreakMeter() { return currentOutbreakMeter; }


}