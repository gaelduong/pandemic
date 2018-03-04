package pandemic;

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
	
	public void endTurn(){
	// MUST BE MODIFIED TO HANDLE OTB CHALLENGES (i.e. Mutations, Bioterrorist win/lose)
		currentGame.setGamePhase(Game.GamePhase.TurnPlayerCards);
		Player p = currentGame.getCurrentPlayer();
		p.setActionsTaken(0);
		p.setOncePerTurnActionTaken(false);
		PlayerDeck pd = currentGame.getPlayerDeck();
		int numCardsRemaining = pd.getDeckSize();
		if (numCardsRemaining < 2){
			notifyAllPlayersGameLost();
			currentGame.setGamePhase(Game.GamePhase.Completed);
		}
		else {
			PlayerCard playerCard1 = pd.drawCard();
			PlayerCard playerCard2 = pd.drawCard();
			if (playerCard1 instanceof EpidemicCard){
				((EpidemicCard) playerCard1).resolveEpidemic();
			}
			else {
				// MUST HANDLE CHECKIGN IF PLAYER HAS TOO MANY CARDS
				// SHOULD CHECKING BE DONE IN addToHand, OR SHOULD IT BE DONE FROM WHEREVER IT IS CALLED?
				p.addToHand(playerCard1);
			}
			if (playerCard2 instanceof EpidemicCard){
				((EpidemicCard) playerCard2).resolveEpidemic();
			}
			else {
				// MUST HANDLE CHECKIGN IF PLAYER HAS TOO MANY CARDS
				// SHOULD CHECKING BE DONE IN addToHand, OR SHOULD IT BE DONE FROM WHEREVER IT IS CALLED?
				p.addToHand(playerCard2);
			}
			currentGame.setGamePhase(Game.GamePhase.TurnInfection);
		}
	}
	
	public void notifyAllPlayersGameLost(){
		// TO FILL IN LATER
	}
}
