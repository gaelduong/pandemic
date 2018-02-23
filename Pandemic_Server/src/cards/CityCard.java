package cards;

public class CityCard implements PlayerCard{
	
	private GameManager.CityName name;
	private int region;
	
	public CityCard(GameManager.CityName pName, int pRegion){
		name = pName;
		region = pRegion;
	}
	
	public GameManager.CityName getCityName(){
		return name;
	}
}
