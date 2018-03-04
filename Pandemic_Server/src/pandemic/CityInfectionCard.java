package pandemic;

public class CityInfectionCard implements InfectionCard{

	private GameManager.CityName name;
	private int region;
	
	public CityInfectionCard(GameManager.CityName pName, int pRegion){
		name = pName;
		region = pRegion;
	}
	
	public GameManager.CityName getCityName(){
		return name;
	}
}
