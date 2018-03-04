package cards;

public class City {
	
	private GameManager.CityName name;
	private int region;
	
	public City(GameManager.CityName cn, int r){
		name = cn;
		region = r;
	}
	public GameManager.CityName getName(){
		return name;
	}
}
