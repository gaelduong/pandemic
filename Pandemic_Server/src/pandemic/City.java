package pandemic;

public class City {
	
	private CityName name;
	private int region;
	
	public City(CityName cn, int r){
		name = cn;
		region = r;
	}
	public CityName getName(){
		return name;
	}
}
