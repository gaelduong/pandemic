package pandemic;

import java.util.ArrayList;

public class GameBoard {

	private ArrayList<City> citiesOnBoard;

	public GameBoard(ArrayList<City> cities) {
	  citiesOnBoard = cities;
    }

	public City getCityByName(CityName cn){
		City found = new City(null, -1);
		// No Cities share a name, so only one City will match
		for(City c : citiesOnBoard){
			if (c.getName().equals(cn)){
				found = c;
			}
		}
		return found;
	}


}
