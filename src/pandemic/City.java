package pandemic;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class City implements Serializable {
	
	private CityName name;
	private Region region;
	private ArrayList<City> neighbors;
	private ArrayList<Connection> connections;
	private ArrayList<Unit> cityUnits;

	private QuarantineMarker cityQuarantineMarker;
	
	public City(CityName cn, Region r){
		name = cn;
		region = r;
		neighbors = new ArrayList<City>();
		connections = new ArrayList<Connection>();
		cityUnits = new ArrayList<Unit>();
	}

    public ArrayList<City> getNeighbors() {
	    return neighbors;
    }

    public ArrayList<Connection> getConnections() {
	    return connections;
    }

    public CityName getName(){
	    return name;
	}

	public Region getRegion() {
	    return region;
    }

    public ArrayList<Unit> getCityUnits() {
	    return cityUnits;
    }

    public int getNumOfDiseaseFlagsPlaced(DiseaseType d) {
        return (cityUnits.stream().filter(unit -> unit.getUnitType() == UnitType.DiseaseFlag
                                                        && ((DiseaseFlag) unit).getDiseaseType() == d)
                          .collect(Collectors.toList())).size();
    }

    public List<Unit> removeAllDiseaseFlags(DiseaseType d) {
        List<Unit> toRemove = cityUnits.stream().filter(unit -> unit.getUnitType() == UnitType.DiseaseFlag
                                                                    && ((DiseaseFlag) unit).getDiseaseType() == d)
                                                .collect(Collectors.toList());
        toRemove.forEach(unit -> {cityUnits.remove(unit);
                                  unit.setLocation(null); });
        return toRemove;

    }

    public DiseaseFlag removeOneDiseaseFlag(DiseaseType d) {
        DiseaseFlag toRemove = (DiseaseFlag) cityUnits.stream().filter(unit -> unit.getUnitType() == UnitType.DiseaseFlag
                                                                        && ((DiseaseFlag) unit).getDiseaseType() == d)
                                                               .findAny().orElse(null);
        cityUnits.remove(toRemove);
        toRemove.setLocation(null);
        return toRemove;
    }

    public boolean containsPurpleDisease() {
        DiseaseFlag purpleDiseaseFlag = (DiseaseFlag) cityUnits.stream().filter(unit -> unit.getUnitType() == UnitType.DiseaseFlag
                                                                        && ((DiseaseFlag) unit).getDiseaseType() == DiseaseType.Purple)
                                                               .findAny().orElse(null);

        return purpleDiseaseFlag != null;
    }

    public boolean containsQuarantineMarker() {
	    QuarantineMarker qm = (QuarantineMarker) cityUnits.stream().filter(unit -> unit.getUnitType() == UnitType.QuarantineMarker)
                                                         .findAny().orElse(null);

	    cityQuarantineMarker = qm != null ? qm : null;
	    return qm != null;
    }

    public QuarantineMarker getQuarantineMarker() {
	    if(cityQuarantineMarker == null) {
	        containsQuarantineMarker();
        }
        return cityQuarantineMarker;
    }

    public ResearchStation removeOneResearchStation() {
	    ResearchStation toRemove = (ResearchStation) cityUnits.stream().filter(unit -> unit.getUnitType() == UnitType.ResearchStation)
                .findAny().orElse(null);
	    cityUnits.remove(toRemove);
	    toRemove.setLocation(null);
	    return toRemove;
    }

    public boolean isBioTSpotted() {
        List<Unit> cityPawns = cityUnits.stream().filter(unit -> unit.getUnitType() == UnitType.Pawn)
                .collect(Collectors.toList());

        if(cityPawns.size() > 0) {
            Pawn bioT = (Pawn) cityPawns.stream().filter(pawn -> ((Pawn) pawn).getRole().getRoleType() == RoleType.BioTerrorist)
                    .findAny().orElse(null);

            if(bioT != null && cityPawns.size() > 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public List<Pair<DiseaseType, Integer>> getDiseaseFlags() {
	    List<Pair<DiseaseType, Integer>> result = new ArrayList<Pair<DiseaseType, Integer>>();

	    for(DiseaseType dType : DiseaseType.values()) {
	        int numOfFlags = (int) cityUnits.stream().filter(unit -> unit.getUnitType() == UnitType.DiseaseFlag
                    && ((DiseaseFlag) unit).getDiseaseType() == dType)
                    .count();
	        if(numOfFlags > 0)
	            result.add(new Pair<DiseaseType, Integer>(dType, numOfFlags));
        }

        /*System.out.println("City: " + name);
        System.out.println(result);
        System.out.println();*/

	    return result;
    }


}
