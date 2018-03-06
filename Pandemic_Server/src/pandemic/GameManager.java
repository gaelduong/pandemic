package pandemic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

// GameManager won't stay in this package
public class GameManager {

	private HashMap<CityName, ArrayList<CityName>> neighborsDict;
	private HashMap<Region, ArrayList<CityName>> regionsDict;
	private LinkedList<Player> activePlayers;
	private Player hostPlayer;
	private Game currentGame;

	public GameManager(Player hostPlayer, int numOfPlayers, int numOfEpidemicCards, ChallengeKind challengeKind) {
		GameSettings gameSettings = new GameSettings(numOfPlayers, numOfEpidemicCards, challengeKind);
		// Setup neighborsDict and regionsDict lookups
		setRegionsDict();
		setNeighborsDict();
		//REFACTOR BELOW INTO createNewGame()
		// Call Game constructor which constructs GameBoard
		currentGame = new Game(hostPlayer, gameSettings, this); // -> createNewGame()
        // ..........TODO
	}

	private void setNeighborsDict() {
      neighborsDict = new HashMap<CityName, ArrayList<CityName>>() {
        {
          put(CityName.SanFrancisco, new ArrayList<CityName>(
                  Arrays.asList(CityName.Chicago, CityName.Tokyo, CityName.Manila)
          ));

          put(CityName.LosAngeles, new ArrayList<CityName>(
                  Arrays.asList(CityName.Sydney, CityName.Chicago, CityName.MexicoCity)
          ));

          put(CityName.MexicoCity, new ArrayList<CityName>(
                  Arrays.asList(CityName.Miami, CityName.Bogota, CityName.Lima, CityName.LosAngeles,
                                CityName.Chicago)
          ));

          put(CityName.Lima, new ArrayList<CityName>(
                  Arrays.asList(CityName.MexicoCity, CityName.Bogota, CityName.Santiago)
          ));

          put(CityName.Santiago, new ArrayList<CityName>(
                  Arrays.asList(CityName.Lima)
          ));

          put(CityName.Chicago, new ArrayList<CityName>(
                  Arrays.asList(CityName.SanFrancisco, CityName.LosAngeles, CityName.MexicoCity,
                                CityName.Atlanta, CityName.Montreal)
          ));

          put(CityName.Atlanta, new ArrayList<CityName>(
                  Arrays.asList(CityName.Chicago, CityName.Washington, CityName.Miami)
          ));

          put(CityName.Miami, new ArrayList<CityName>(
                  Arrays.asList(CityName.Atlanta, CityName.MexicoCity, CityName.Washington,
                                CityName.Bogota)
          ));

          put(CityName.Bogota, new ArrayList<CityName>(
                  Arrays.asList(CityName.MexicoCity, CityName.Miami, CityName.Lima,
                                CityName.BuenosAires, CityName.SaoPaulo)
          ));

          put(CityName.SaoPaulo, new ArrayList<CityName>(
                  Arrays.asList(CityName.Bogota, CityName.Madrid, CityName.Lagos,
                          CityName.BuenosAires)
          ));

          put(CityName.BuenosAires, new ArrayList<CityName>(
                  Arrays.asList(CityName.Bogota, CityName.SaoPaulo)
          ));

          put(CityName.Montreal, new ArrayList<CityName>(
                  Arrays.asList(CityName.Chicago, CityName.NewYork)
          ));

          put(CityName.Washington, new ArrayList<CityName>(
                  Arrays.asList(CityName.Atlanta, CityName.Miami, CityName.Montreal,
                                CityName.NewYork)
          ));

          put(CityName.NewYork, new ArrayList<CityName>(
                  Arrays.asList(CityName.Montreal, CityName.Washington, CityName.London,
                                CityName.Madrid)
          ));

          put(CityName.London, new ArrayList<CityName>(
                  Arrays.asList(CityName.NewYork, CityName.Madrid, CityName.Paris,
                                CityName.Essen)
          ));

          put(CityName.Madrid, new ArrayList<CityName>(
                  Arrays.asList(CityName.NewYork, CityName.SaoPaulo, CityName.London, CityName.Paris,
                                CityName.Algiers)
          ));

          put(CityName.Essen, new ArrayList<CityName>(
                  Arrays.asList(CityName.London, CityName.Paris, CityName.Milan,
                                CityName.StPetersburg)
          ));

          put(CityName.Paris, new ArrayList<CityName>(
                  Arrays.asList(CityName.London, CityName.Essen, CityName.Milan,
                                CityName.Algiers, CityName.Madrid)
          ));

          put(CityName.Algiers, new ArrayList<CityName>(
                  Arrays.asList(CityName.Madrid, CityName.Paris, CityName.Istanbul,
                                CityName.Cairo)
          ));

          put(CityName.Lagos, new ArrayList<CityName>(
                  Arrays.asList(CityName.Khartoum, CityName.Kinshasa, CityName.SaoPaulo)
          ));

          put(CityName.Kinshasa, new ArrayList<CityName>(
                  Arrays.asList(CityName.Lagos, CityName.Khartoum, CityName.Johannesburg)
          ));

          put(CityName.Johannesburg, new ArrayList<CityName>(
                  Arrays.asList(CityName.Kinshasa, CityName.Khartoum)
          ));

          put(CityName.Khartoum, new ArrayList<CityName>(
                  Arrays.asList(CityName.Cairo, CityName.Johannesburg)
          ));

          put(CityName.Cairo, new ArrayList<CityName>(
                  Arrays.asList(CityName.Algiers, CityName.Istanbul, CityName.Baghdad,
                                CityName.Riyadh, CityName.Khartoum)
          ));

          put(CityName.Istanbul, new ArrayList<CityName>(
                  Arrays.asList(CityName.Milan, CityName.StPetersburg, CityName.Moscow,
                                CityName.Baghdad, CityName.Cairo, CityName.Algiers)
          ));

          put(CityName.Milan, new ArrayList<CityName>(
                  Arrays.asList(CityName.Essen, CityName.Paris, CityName.Istanbul)
          ));

          put(CityName.StPetersburg, new ArrayList<CityName>(
                  Arrays.asList(CityName.Essen, CityName.Istanbul, CityName.Moscow)
          ));

          put(CityName.Moscow, new ArrayList<CityName>(
                  Arrays.asList(CityName.StPetersburg, CityName.Tehran, CityName.Istanbul)
          ));

          put(CityName.Baghdad, new ArrayList<CityName>(
                  Arrays.asList(CityName.Istanbul, CityName.Cairo, CityName.Riyadh, CityName.Karachi,
                                CityName.Tehran)
          ));

          put(CityName.Riyadh, new ArrayList<CityName>(
                  Arrays.asList(CityName.Cairo, CityName.Baghdad, CityName.Karachi)
          ));

          put(CityName.Tehran, new ArrayList<CityName>(
                  Arrays.asList(CityName.Moscow, CityName.Baghdad, CityName.Karachi,
                                CityName.Delhi)
          ));

          put(CityName.Karachi, new ArrayList<CityName>(
                  Arrays.asList(CityName.Tehran, CityName.Baghdad, CityName.Riyadh,
                                CityName.Mumbai, CityName.Delhi)
          ));

          put(CityName.Mumbai, new ArrayList<CityName>(
                  Arrays.asList(CityName.Karachi, CityName.Delhi)
          ));

          put(CityName.Delhi, new ArrayList<CityName>(
                  Arrays.asList(CityName.Tehran, CityName.Karachi, CityName.Mumbai,
                                CityName.Chennai, CityName.Kolkata)
          ));

          put(CityName.Chennai, new ArrayList<CityName>(
                  Arrays.asList(CityName.Mumbai, CityName.Delhi, CityName.Kolkata,
                                CityName.Bangkok, CityName.Jakarta)
          ));

          put(CityName.Kolkata, new ArrayList<CityName>(
                  Arrays.asList(CityName.Delhi, CityName.Chennai, CityName.Bangkok,
                                CityName.HongKong)
          ));

          put(CityName.Bangkok, new ArrayList<CityName>(
                  Arrays.asList(CityName.Kolkata, CityName.HongKong, CityName.HoChiMinhCity,
                                CityName.Jakarta, CityName.Chennai)
          ));

          put(CityName.Jakarta, new ArrayList<CityName>(
                  Arrays.asList(CityName.Chennai, CityName.Bangkok, CityName.HoChiMinhCity,
                                CityName.Sydney)
          ));

          put(CityName.Beijing, new ArrayList<CityName>(
                  Arrays.asList(CityName.Seoul, CityName.Shanghai)
          ));

          put(CityName.Shanghai, new ArrayList<CityName>(
                  Arrays.asList(CityName.Beijing, CityName.Seoul, CityName.Tokyo,
                                CityName.Taipei, CityName.HongKong)
          ));

          put(CityName.HongKong, new ArrayList<CityName>(
                  Arrays.asList(CityName.Shanghai, CityName.Taipei, CityName.Manila,
                                CityName.HoChiMinhCity, CityName.Bangkok, CityName.Kolkata)
          ));

          put(CityName.HoChiMinhCity, new ArrayList<CityName>(
                  Arrays.asList(CityName.Bangkok, CityName.HongKong, CityName.Manila,
                                CityName.Jakarta)
          ));

          put(CityName.Seoul, new ArrayList<CityName>(
                  Arrays.asList(CityName.Beijing, CityName.Shanghai, CityName.Tokyo)
          ));

          put(CityName.Tokyo, new ArrayList<CityName>(
                  Arrays.asList(CityName.Seoul, CityName.Shanghai, CityName.SanFrancisco)
          ));

          put(CityName.Osaka, new ArrayList<CityName>(
                  Arrays.asList(CityName.Tokyo, CityName.Taipei)
          ));

          put(CityName.Taipei, new ArrayList<CityName>(
                  Arrays.asList(CityName.Shanghai, CityName.Osaka, CityName.HongKong,
                                CityName.Manila)
          ));

          put(CityName.Manila, new ArrayList<CityName>(
                  Arrays.asList(CityName.HongKong, CityName.Taipei, CityName.SanFrancisco,
                                CityName.Sydney, CityName.HoChiMinhCity)
          ));

          put(CityName.Sydney, new ArrayList<CityName>(
                  Arrays.asList(CityName.Jakarta, CityName.Manila, CityName.LosAngeles)
          ));
        }
      };
    }

	private void setRegionsDict() {
      regionsDict = new HashMap<Region, ArrayList<CityName>>() {
        {
          put(Region.Blue, new ArrayList<CityName>(
                  Arrays.asList(CityName.SanFrancisco, CityName.Chicago, CityName.Montreal,
                          CityName.NewYork, CityName.Atlanta, CityName.Washington,
                          CityName.London, CityName.Madrid, CityName.Essen, CityName.Paris,
                          CityName.StPetersburg, CityName.Milan)
          ));

          put(Region.Yellow, new ArrayList<CityName>(
                  Arrays.asList(CityName.LosAngeles, CityName.MexicoCity, CityName.Miami,
                          CityName.Bogota, CityName.Lima, CityName.Santiago, CityName.BuenosAires,
                          CityName.SaoPaulo, CityName.Lagos, CityName.Khartoum, CityName.Kinshasa,
                          CityName.Johannesburg)
          ));

          put(Region.Black, new ArrayList<CityName>(
                  Arrays.asList(CityName.Algiers, CityName.Istanbul, CityName.Cairo,
                          CityName.Baghdad, CityName.Riyadh, CityName.Moscow,
                          CityName.Tehran, CityName.Karachi, CityName.Mumbai,
                          CityName.Delhi, CityName.Kolkata, CityName.Chennai)
          ));

          put(Region.Red, new ArrayList<CityName>(
                  Arrays.asList(CityName.Bangkok, CityName.Jakarta, CityName.Beijing,
                                CityName.Shanghai, CityName.HongKong, CityName.HoChiMinhCity,
                                CityName.Seoul, CityName.Tokyo, CityName.Osaka, CityName.Taipei,
                                CityName.Manila, CityName.Sydney)
          ));

        }
      };
    }
	
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
		currentGame.setGamePhase(GamePhase.TurnPlayerCards);
		Player p = currentGame.getCurrentPlayer();
		p.setActionsTaken(0);
		p.setOncePerTurnActionTaken(false);
		PlayerDeck pd = currentGame.getPlayerDeck();
		int numCardsRemaining = pd.getDeckSize();
		if (numCardsRemaining < 2){
			notifyAllPlayersGameLost();
			currentGame.setGamePhase(GamePhase.Completed);
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
			currentGame.setGamePhase(GamePhase.TurnInfection);
		}
	}
	
	public void notifyAllPlayersGameLost(){
		// TO FILL IN LATER
	}
}
