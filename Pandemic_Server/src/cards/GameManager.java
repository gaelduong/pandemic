package cards;

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
}
