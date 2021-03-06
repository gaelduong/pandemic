package pandemic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GameBoard implements Serializable {

	private ArrayList<City> citiesOnBoard;

	public GameBoard(ArrayList<City> cities) {
	  citiesOnBoard = cities;
    }

	public City getCityByName(CityName cn){
		/*City found = new City(null, null);
		// No Cities share a name, so only one City will match
		for(City c : citiesOnBoard){
			if (c.getName().equals(cn)){
				found = c;
			}
		}
		return found;*/
		return citiesOnBoard.stream().filter(n -> n.getName().equals(cn)).findAny().orElse(null);
	}

	public void printGameBoard() {
        System.out.println("PRINTING OUT GAMEBOARD......");
	    for(City c : citiesOnBoard) {
            System.out.println("    City: " + c.getName() + ", Region: " + c.getRegion());
            System.out.println("        neighbors: ");
            c.getNeighbors().forEach(n -> System.out.println("          name: " + n.getName()
                    + ", region: " + n.getRegion()));
            System.out.println("        connections: ");
            c.getConnections().forEach(conn -> System.out.println("            end1: "
                    + conn.getEnd1().getName() + ", end2: " + conn.getEnd2().getName()));
            System.out.println("        units: ");
            c.getCityUnits().forEach(unit -> System.out.println("           unitType: "
                    + unit.getUnitType() + ", unit city location: " + unit.getLocation().getName()));

            List<Unit> cPawns = c.getCityUnits().stream().filter(unit -> unit.getUnitType() == UnitType.Pawn)
                                                         .collect(Collectors.toList());
            System.out.println("            pawns: ");
            cPawns.forEach(pawn -> System.out.println("             pawn role type: " + ((Pawn) pawn).getRole().getRoleType() +
                                                      ", pawn city location: " + pawn.getLocation().getName() +
                                                      ", assigned: " + ((Pawn) pawn).isAssigned()));

            List<Unit> cResearchStations = c.getCityUnits().stream().filter(unit -> unit.getUnitType() == UnitType.ResearchStation)
                                                                    .collect(Collectors.toList());
            System.out.println("            research stations: ");
            cResearchStations.forEach(rs -> System.out.println("             (research station) unit type: " + rs.getUnitType() +
                    ", research station location: " + rs.getLocation().getName()));

            List<Unit> cDiseaseFlags = c.getCityUnits().stream().filter(unit -> unit.getUnitType() == UnitType.DiseaseFlag)
                                                                .collect(Collectors.toList());
            System.out.println("            disease flags: ");
            cDiseaseFlags.forEach(flag -> System.out.println("             disease flag type: " + ((DiseaseFlag) flag).getDiseaseType() +
                    ", disease flag location: " + flag.getLocation().getName()));



        }
        System.out.println("Number of cities in board: " + citiesOnBoard.size());
        System.out.println("GAMEBOARD PRINTED.");
    }

    public boolean isNotPresentOnBoard(DiseaseType d) {
        List<Unit> flagsOnBoard = new ArrayList<Unit>();
        for(City c : citiesOnBoard) {
            flagsOnBoard.addAll(c.getCityUnits().stream().filter(unit ->
                                                                    unit.getUnitType() == UnitType.DiseaseFlag &&
                                                                            ((DiseaseFlag) unit).getDiseaseType() == d)
                                                         .collect(Collectors.toList()));
        }

        return flagsOnBoard.size() > 0 ? false : true;
    }

    public boolean isQuarantineSpecialistInCity(City c) {
        List<Unit> cPawns = c.getCityUnits().stream().filter(unit -> unit.getUnitType() == UnitType.Pawn)
                .collect(Collectors.toList());
        boolean result = false;

        for (Unit pawn : cPawns) {
            result = ((Pawn) pawn).getRole().getRoleType() == RoleType.QuarantineSpecialist;
            if(result == true) break;
        }

        return result;

    }

    public boolean isMedicInCity(City c) {
        List<Unit> cPawns = c.getCityUnits().stream().filter(unit -> unit.getUnitType() == UnitType.Pawn)
                .collect(Collectors.toList());
        boolean result = false;

        for (Unit pawn : cPawns) {
            result = ((Pawn) pawn).getRole().getRoleType() == RoleType.Medic;
            if(result == true) break;
        }

        return result;

    }

    public ArrayList<City> getCitiesOnBoard() {
        return citiesOnBoard;
    }
}
