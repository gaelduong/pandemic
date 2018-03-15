package pandemic;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class City {
	
	private CityName name;
	private Region region;
	private ArrayList<City> neighbors;
	private ArrayList<Connection> connections;
	private ArrayList<Unit> cityUnits;
	
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

    //TODO get a list of all disease tuples (disease, qty)
    @SuppressWarnings("unchecked")
    public List<Pair<DiseaseType, Integer>> getDiseaseFlags() {
        return null;
    }

}
