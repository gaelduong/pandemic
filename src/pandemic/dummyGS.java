package pandemic;

import java.util.List;
import java.util.Map;

import javafx.util.Pair;



/**
 * Condensed version of the server's core game state.
 * Includes everything the client needs to render the state on the front-end
 */
public class dummyGS {

    private final Map<RoleType, String> userMap;
    private final Map<RoleType, List<String>> cardMap; //pawn-hand
    private final Map<RoleType, CityName> positionMap; //pawn-position (city)
    private final Map<CityName, List<Pair<DiseaseType,Integer>>> diseaseCubesMap; //City - <disease,numCubes>
    private final List<CityName> infectionDiscardPile;//infection discard pile
    private final List<CityName> playerDiscardPile; //playerDiscard pile
    

    /**
     * The server will create this object! The client just receives it.
     */
    public dummyGS(Map<RoleType, String> userMap, Map<RoleType, List<String>> cardMap, Map<RoleType, CityName> positionMap, Map<CityName, List<Pair<DiseaseType, Integer>>> diseaseCubesMap, List<CityName> infectionDiscardPile, List<CityName> playerDiscardPile) {
        this.userMap = userMap;
        this.cardMap = cardMap;
        this.positionMap = positionMap;
        this.diseaseCubesMap = diseaseCubesMap;
        this.infectionDiscardPile = infectionDiscardPile;
        this.playerDiscardPile = playerDiscardPile;
       
    }

    /**
     * Maps players to their hand
     */
    public Map<RoleType, List<String>> getCardMap() {
        return cardMap;
    }

    /**
     * Maps players to their position on the board
     */
    public Map<RoleType, CityName> getPositionMap() {
        return positionMap;
    }

   
    public Map<CityName, List<Pair<DiseaseType, Integer>>> getDiseaseCubesMap() {

        return diseaseCubesMap;
    }

    /**
     * Maps players to their login username
     */
    public Map<RoleType, String> getUserMap() {
        return userMap;
    }

    public List<CityName> getInfectionDiscardPile() {
        return infectionDiscardPile;
    }

    public List<CityName> getPlayerDiscardPile() {
        return playerDiscardPile;
    }
    
   
}
