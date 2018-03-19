package shared;

import javafx.util.Pair;
import pandemic.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Condensed version of the server's core game state.
 * Includes everything the client needs to render the state on the front-end
 */
public class GameState implements Serializable {

    private final Map<RoleType, String> userMap;
    private final Map<RoleType, List<PlayerCard>> cardMap;
//    private final Map<RoleType, CityName> positionMap;
    private final Map<RoleType, City> positionMap;
    private final Map<CityName, List<Pair<DiseaseType,Integer>>> diseaseCubesMap;
    private final Map<DiseaseType, Integer> remainingDiseaseCubesMap;
    private final InfectionDiscardPile infectionDiscardPile;
    private final PlayerDiscardPile playerDiscardPile;
    private final int currentInfectionRate;
    private final int currentOutbreakMeter;
    private final int currentPlayerActionsRemaining;
    private final ArrayList<DiseaseType> curedDiseases;
    private final String currentPlayerName;



    /**
     * The server will create this object! The client just receives it.
     */
    public GameState(Map<RoleType, String> userMap, Map<RoleType, List<PlayerCard>> cardMap, Map<RoleType, City> positionMap,
                     Map<CityName, List<Pair<DiseaseType, Integer>>> diseaseCubesMap, Map<DiseaseType, Integer> remainingDiseaseCubesMap,
                     InfectionDiscardPile infectionDiscardPile, PlayerDiscardPile playerDiscardPile, int currentInfectionRate, int currentOutbreakMeter, int actionsRemaining, ArrayList<DiseaseType> diseases,
                     String currentPlayerName) {
        this.userMap = userMap;
        this.cardMap = cardMap;
        this.positionMap = positionMap;
        this.diseaseCubesMap = diseaseCubesMap;
        this.remainingDiseaseCubesMap = remainingDiseaseCubesMap;
        this.infectionDiscardPile = infectionDiscardPile;
        this.playerDiscardPile = playerDiscardPile;
        this.currentInfectionRate = currentInfectionRate;
        this.currentOutbreakMeter = currentOutbreakMeter;
        this.currentPlayerActionsRemaining = actionsRemaining;
        this.curedDiseases = diseases;
        this.currentPlayerName = currentPlayerName;
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
    public Map<RoleType, City> getPositionMap() {
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
     * Map disease types to the number of flags of that type that remain in supply
     */

    public Map<DiseaseType, Integer> getRemainingDiseaseCubesMap() {
        return remainingDiseaseCubesMap;
    }

    /**
     * Return the current game infection rate and outbreak meter readings
     */
    public int getCurrentInfectionRate() { return currentInfectionRate; }

    public int getCurrentOutbreakMeter() { return currentOutbreakMeter; }

    public int getCurrentPlayerActionsRemaining(){
        return currentPlayerActionsRemaining;
    }

    public String getCurrentPlayer(){
        return currentPlayerName;
    }

}