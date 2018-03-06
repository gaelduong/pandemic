package pandemic;

public class CityCard implements PlayerCard{
	
	private CityName name;
	private int region;
	
	public CityCard(CityName pName, int pRegion){
		name = pName;
		region = pRegion;
	}
	
	public CityName getCityName(){
		return name;
	}
}
