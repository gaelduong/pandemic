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

    private final Map<BioTTurnStats, Boolean> bioTMap;

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

    private final ArrayList<City> researchStationLocations;

    private final ArrayList<City> quarantineMarkerOneLocations;
    private final ArrayList<City> quarantineMarkerTwoLocations;

    private final boolean eventCardsEnabled;
    private final CurrentPlayerTurnStatus turnStatus;
    private final boolean archivistActionUsed;
    private final boolean epidemiologistActionUsed;
    private final boolean fieldOperativeActionUsed;
    private final ArrayList<DiseaseFlag> fieldOperativeSamples;
    private final boolean complexMolecularStructureActive;
    private final boolean governmentInterferenceActive;
    private final boolean isGovernmentInterferenceSatisfied;
    private final int infectionsRemaining;
    private final Map<DiseaseType, Boolean> cureMap;
    private final boolean purpleInPlay;
    private DiseaseType virulentStrain;
    private boolean virulentStrainSet;


    /**
     * The server will create this object! The client just receives it.
     */
    public GameState(Map<RoleType, String> userMap, Map<RoleType, List<PlayerCard>> cardMap, Map<RoleType, City> positionMap,
                     Map<CityName, List<Pair<DiseaseType, Integer>>> diseaseCubesMap, Map<DiseaseType, Integer> remainingDiseaseCubesMap,
                     InfectionDiscardPile infectionDiscardPile, PlayerDiscardPile playerDiscardPile, int currentInfectionRate, int currentOutbreakMeter, int actionsRemaining, ArrayList<DiseaseType> diseases,
                     String currentPlayerName, ArrayList<City> researchStations, boolean eventCardsEnabled, CurrentPlayerTurnStatus status, boolean aActionUsed, boolean eActionUsed, boolean fOActionUsed,
                     ArrayList<DiseaseFlag> fOSamples, boolean cMSActive, boolean gIActive, boolean gISatisfied, int iRemaining, Map<DiseaseType, Boolean> cures,
                     Map<BioTTurnStats, Boolean> bioTMap, ArrayList<City> quarantineMarkerOneLocations, ArrayList<City> quarantineMarkerTwoLocations, boolean pInPlay, DiseaseType vStrain, boolean vStrainSet) {
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
        this.researchStationLocations = researchStations;
        this.eventCardsEnabled = eventCardsEnabled;
        this.turnStatus= status;
        this.archivistActionUsed = aActionUsed;
        this.epidemiologistActionUsed = eActionUsed;
        this.fieldOperativeActionUsed = fOActionUsed;
        this.fieldOperativeSamples = fOSamples;
        this.complexMolecularStructureActive = cMSActive;
        this.governmentInterferenceActive = gIActive;
        this.isGovernmentInterferenceSatisfied = gISatisfied;
        this.infectionsRemaining = iRemaining;

        this.bioTMap = bioTMap;
        this.quarantineMarkerOneLocations = quarantineMarkerOneLocations;
        this.quarantineMarkerTwoLocations = quarantineMarkerTwoLocations;
        this.cureMap = cures;
        this.purpleInPlay = pInPlay;
        this.virulentStrain = vStrain;
        this.virulentStrainSet = vStrainSet;
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

    public ArrayList<City> getResearchStationLocations(){
        return researchStationLocations;
    }

    public boolean getEventCardsEnabled(){
        return eventCardsEnabled;
    }

    public CurrentPlayerTurnStatus getTurnStatus(){
        return turnStatus;
    }

    public boolean getArchivistActionUsed(){
        return archivistActionUsed;
    }

    public boolean getEpidemiologistActionUsed(){
        return epidemiologistActionUsed;
    }

    public boolean getFieldOperativeActionUsed(){
        return fieldOperativeActionUsed;
    }

    public ArrayList<DiseaseFlag> getFieldOperativeSamples(){
        return fieldOperativeSamples;
    }

    public boolean getComplexMolecularStructureActive(){
        return complexMolecularStructureActive;
    }

    public int getInfectionsRemaining(){
        return infectionsRemaining;
    }

    public Map<DiseaseType, Boolean> getCureMap(){
        return cureMap;
    }

    public boolean getPurpleInPlay(){
        return purpleInPlay;
    }

    public DiseaseType getVirulentStrain(){
        return virulentStrain;
    }

    public boolean getVirulentStrainSet(){
        return virulentStrainSet;
    }

    public boolean getGovernmentInterferenceActive(){
        return governmentInterferenceActive;
    }

    public boolean getIsGovernmentInterferenceSatisfied(){
        return isGovernmentInterferenceSatisfied;
    }

    public ArrayList<City> getQuarantineMarkerOneLocations(){
        return quarantineMarkerOneLocations;
    }

    public ArrayList<City> getQuarantineMarkerTwoLocations(){
        return quarantineMarkerTwoLocations;
    }
}