package cards;

import java.util.ArrayList;

// GameManager won't stay in this package
public class GameManager {
	
	private Game currentGame;

	enum CityName{
		SanFrancisco, LosAngeles, Chicago, Atlanta, MexicoCity, Montreal, NewYork, Washington, Miami, Bogota, Lima, Santiago, SaoPaulo, BuenosAires, London, Madrid, Essen, Paris, Algiers, StPetersburg, Moscow, Milan, Istanbul, Cairo, Khartoum, Lagos, Kinshasa, Johannesburg, Tehran, Baghdad, Riyach, Karachi, Mumbai, Delhi, Chennai, Kolksta, Bangkok, Jakarta, Beijing, Shanghai, HongKong, HoChiMinhCity, Seoul, Tokyo, Osaka, Taipei, Manila, Sydney
	};
	
	enum EventCardName{
		ResilientPopulation, OneQuietNight, Forecast, AirLift, GovernmentGrant
	};
	
	public void setResolvingEpidemic(boolean b){
		currentGame.setResolvingEpidemic(b);
	}
	
	public int getInfectionRate(){
		return currentGame.getInfectionRate();
	}
	
	public void increaseInfectionRate(){
		currentGame.increaseInfectionRate();
	}
	
	public InfectionCard drawLastInfectionCard(){
		return currentGame.getInfectionDeck().drawLastCard();
	}
	
	public void discardInfectionCard(InfectionCard ic){
		currentGame.getInfectionDiscardPile().addCard(ic);
	}
	
	public City getCityByName(CityName cn){
		return currentGame.getCityByName(cn);
	}
	
	public void infectCityForEpidemic(City c){
		// TO FILL IN LATER
	}
	
	public void shuffleInfectionDiscardPile(){
		InfectionDiscardPile idp = currentGame.getInfectionDiscardPile();
		idp.shuffle();
	}
	
	// Move InfectionDiscardPile to top of InfectionDeck during an Epidemic.
	// Post: InfectionDiscardPile is empty.
	public void combineInfectionDeckAndPile(){
		InfectionDeck id = currentGame.getInfectionDeck();
		InfectionDiscardPile idp = currentGame.getInfectionDiscardPile();
		ArrayList<InfectionCard> cardsInPile = idp.getCards();
		id.addPile(cardsInPile);
		idp.clearPile();
	}
}
