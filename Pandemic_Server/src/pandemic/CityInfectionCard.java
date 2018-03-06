package pandemic;

public class CityInfectionCard implements InfectionCard{

	private CityName name;
	private int region;
	
	public CityInfectionCard(CityName pName, int pRegion){
		name = pName;
		region = pRegion;
	}
	
	public CityName getCityName(){
		return name;
	}
}
