package pandemic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class GameManager {

	private HashMap<CityName, ArrayList<CityName>> neighborsDict;
	private HashMap<Region, ArrayList<CityName>> regionsDict;
	private HashMap<CityName, Region> nameToRegionDict;
	private HashMap<Region, DiseaseType> regionToDiseaseTypeDict;
	private LinkedList<Player> activePlayers;
	private Player hostPlayer;
	private Game currentGame;
	private GameSettings gameSettings;

	public GameManager(Player hostPlayer, int numOfPlayers, int numOfEpidemicCards, ChallengeKind challengeKind) {
        gameSettings = new GameSettings(numOfPlayers, numOfEpidemicCards, challengeKind);
		// Setup neighborsDict and regionsDict lookups
        this.hostPlayer = hostPlayer;
        activePlayers = new LinkedList<Player>();
        activePlayers.add(hostPlayer);
		setRegionsDict();
		setNeighborsDict();
		setNameToRegionDict();
		setRegionToDiseaseTypeDict();

		//FOR TESTING:
		createNewGame();
		currentGame.getDiseaseByDiseaseType(DiseaseType.Blue).setCured(true);

		//TESTING DRIVE/FERRY:
		/*System.out.println("------------------GAME UNIT BEFORE drive/ferry----------------------");
        currentGame.printGameUnits();
		playDriveFerry(currentGame.getCityByName(CityName.Chicago));
		currentGame.printGameBoard();
        System.out.println("------------------GAME UNIT AFTER drive/ferry----------------------");
		currentGame.printGameUnits();*/

		// TESTING DIRECT FLIGHT:
        /*PlayerCard mtl = currentGame.getPlayerDeck().getCard("Montreal");
		currentGame.getCurrentPlayer().addToHand(mtl);
        System.out.println("------------------GAME UNIT BEFORE direct flight----------------------");
        currentGame.printGameUnits();
        System.out.println("CityCard name: " + ((CityCard) mtl).getCityName());
        playDirectFlight((CityCard) mtl);
        currentGame.printGameBoard();
        System.out.println("------------------GAME UNIT AFTER direct flight----------------------");
        currentGame.printGameUnits();*/

        // TESTING TREAT DISEASE:
        System.out.println("------------------GAME UNIT BEFORE treat disease----------------------");
        currentGame.printGameUnits();
        playTreatDisease((DiseaseFlag) currentGame.getCityByName(CityName.Atlanta).getCityUnits().stream()
                                                                                                 .filter(unit ->
                                                                                                                    unit.getUnitType() == UnitType.DiseaseFlag)
                                                                                                 .findAny().orElse(null));
        currentGame.printGameBoard();
        System.out.println("------------------GAME UNIT after treat disease----------------------");
        currentGame.printGameUnits();



	}

	public void createNewGame() {
        System.out.println("Creating new game for hostPlayer " + hostPlayer.getPlayerUserName() + " ......");
	    currentGame = new Game(hostPlayer, gameSettings, this);
	    currentGame.initializeGame();

    }

    public Player getHostPlayer() {
	    return hostPlayer;
    }

    private void setRegionToDiseaseTypeDict() {
	    regionToDiseaseTypeDict = new HashMap<Region, DiseaseType>() {
            {
                put(Region.Blue, DiseaseType.Blue);
                put(Region.Black, DiseaseType.Black);
                put(Region.Red, DiseaseType.Red);
                put(Region.Yellow, DiseaseType.Yellow);
            }
        };
    }
    private void setNameToRegionDict() {
	    nameToRegionDict = new HashMap<CityName, Region>() {
            {
                put(CityName.SanFrancisco, Region.Blue);
                put(CityName.Chicago, Region.Blue);
                put(CityName.Atlanta, Region.Blue);
                put(CityName.Montreal, Region.Blue);
                put(CityName.Washington, Region.Blue);
                put(CityName.NewYork, Region.Blue);
                put(CityName.London, Region.Blue);
                put(CityName.Madrid, Region.Blue);
                put(CityName.Paris, Region.Blue);
                put(CityName.Essen, Region.Blue);
                put(CityName.Milan, Region.Blue);
                put(CityName.StPetersburg, Region.Blue);

                put(CityName.LosAngeles, Region.Yellow);
                put(CityName.MexicoCity, Region.Yellow);
                put(CityName.Miami, Region.Yellow);
                put(CityName.Bogota, Region.Yellow);
                put(CityName.Lima, Region.Yellow);
                put(CityName.Santiago, Region.Yellow);
                put(CityName.BuenosAires, Region.Yellow);
                put(CityName.SaoPaulo, Region.Yellow);
                put(CityName.Lagos, Region.Yellow);
                put(CityName.Kinshasa, Region.Yellow);
                put(CityName.Johannesburg, Region.Yellow);
                put(CityName.Khartoum, Region.Yellow);

                put(CityName.Algiers, Region.Black);
                put(CityName.Istanbul, Region.Black);
                put(CityName.Cairo, Region.Black);
                put(CityName.Moscow, Region.Black);
                put(CityName.Baghdad, Region.Black);
                put(CityName.Riyadh, Region.Black);
                put(CityName.Tehran, Region.Black);
                put(CityName.Karachi, Region.Black);
                put(CityName.Mumbai, Region.Black);
                put(CityName.Delhi, Region.Black);
                put(CityName.Chennai, Region.Black);
                put(CityName.Kolkata, Region.Black);

                put(CityName.Bangkok, Region.Red);
                put(CityName.Jakarta, Region.Red);
                put(CityName.Beijing, Region.Red);
                put(CityName.Shanghai, Region.Red);
                put(CityName.HongKong, Region.Red);
                put(CityName.HoChiMinhCity, Region.Red);
                put(CityName.Seoul, Region.Red);
                put(CityName.Taipei, Region.Red);
                put(CityName.Manila, Region.Red);
                put(CityName.Sydney, Region.Red);
                put(CityName.Tokyo, Region.Red);
                put(CityName.Osaka, Region.Red);
            }
        };
    }

	private void setNeighborsDict() {
      neighborsDict = new HashMap<CityName, ArrayList<CityName>>() {
        {
          put(CityName.SanFrancisco, new ArrayList<CityName>(
                  Arrays.asList(CityName.Chicago, CityName.Tokyo, CityName.Manila,
                                CityName.LosAngeles)
          ));

          put(CityName.LosAngeles, new ArrayList<CityName>(
                  Arrays.asList(CityName.Sydney, CityName.Chicago, CityName.MexicoCity,
                                CityName.SanFrancisco)
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

    public DiseaseType getDiseaseTypeByRegion(Region r) {
	    return regionToDiseaseTypeDict.get(r);
    }

    public Region getRegionByCityName(CityName name) {
	    return nameToRegionDict.get(name);
    }

    public ArrayList<CityName> getCityNamesByRegion(Region r) {
	    return regionsDict.get(r);
    }

    public ArrayList<CityName> getCityNeighborNames(CityName name) {
	    return neighborsDict.get(name);
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
		currentGame.setCurrentPlayerTurnStatus(CurrentPlayerTurnStatus.ActionsCompleted);
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
				p.addToHand(playerCard1);
			}
			if (playerCard2 instanceof EpidemicCard){
				((EpidemicCard) playerCard2).resolveEpidemic();
			}
			else {
				p.addToHand(playerCard2);
			}
			currentGame.setGamePhase(GamePhase.TurnInfection);
		}
			// CALL INFECTNEXTCITY HERE? SET NEXT PLAYER TO CURRENTPLAYER AND SET
			// CURRENTPLAYERTURNSTATUS TO PLAYINGACTIONS??
		
		if (currentGame.getOneQuietNight()){
			currentGame.setOneQuietNight(false);
			// must set up next player's turn
		}
		else {
			infectNextCity();
		}
		
	}
	
	// Checks if Player has too many cards in hand. Must resolve issue if Player has
	// too many cards.
	public void checkHandSize(Player p){
		int numCardsInHand = p.getHandSize();
		// For OTB, must check if Player is Generalist
		if (numCardsInHand > 7){
			currentGame.setCurrentPlayerTurnStatus(CurrentPlayerTurnStatus.PlayerDiscardingCards);
			currentGame.setPlayerDiscardingCards(p);
			promptDiscardCards(p);
		}
		else if (currentGame.getCurrentPlayerTurnStatus().equals(CurrentPlayerTurnStatus.PlayerDiscardingCards)){
			// Player has already been prompted to discard a card/play event card and the player did it
			currentGame.setPlayerDiscardingCards(null);
			currentGame.setCurrentPlayerTurnStatus(CurrentPlayerTurnStatus.PlayingActions);
		}
	}
	
	public Player getPlayerDiscardingCards(){
		return currentGame.getPlayerDiscardingCards();
	}
	
	public CurrentPlayerTurnStatus getCurrentPlayerTurnStatus(){
		return currentGame.getCurrentPlayerTurnStatus();
	}
	
	// returns 0 if successful, 1 if failed
	// Removes card from player's hand and adds it to PlayerDiscardPile
	public int discardPlayerCard(Player p, PlayerCard c){
		// Must take a player as a parameter for when a non-current player has too
		// many cards and must discard one
		if (p.isInHand(c)){
			p.discardCard(c);
			PlayerDiscardPile pile = currentGame.getPlayerDiscardPile();
			pile.addCard(c);
			if (currentGame.getCurrentPlayerTurnStatus().equals(CurrentPlayerTurnStatus.PlayerDiscardingCards)){
				checkHandSize(p);
			}
			return 0;
		}
		return 1;
	}
	
	public void promptDiscardCards(Player p){
		// TO DO
		// MUST INFORM PLAYER P THAT THEY HAVE TOO MANY CARDS AND MUST SELECT ONE CARD
		// TO DISCARD OR PLAY AN EVENT CARD
	}
	
	public void notifyAllPlayersGameLost(){
		// TO FILL IN LATER
	}

	// FOR THESE MIGHT NEED TO CHANGE INPUTS DEPENDING ON SERIALIZED OBJECT
    // PASSED ACROSS NETWORK

	//returns 0 upon success, 1 upon failure
	public int playDriveFerry(City city) {
	    Player currentPlayer = currentGame.getCurrentPlayer();
	    Pawn currentPlayerPawn = currentPlayer.getPawn();
        Role currentPlayerRole = currentPlayerPawn.getRole();
        City currentPlayerCity = currentPlayerPawn.getLocation();

	    if(currentPlayer.getActionsTaken() == 4) {
	        currentPlayer.setActionsTaken(0);
	        return 1;
        }

        if ((currentPlayerCity.getConnections().stream().filter(conn -> conn.getEnd1() == city || conn.getEnd2() == city)
                                      .collect(Collectors.toList())).isEmpty()) {
            return 1;
        }

        currentPlayerCity.getCityUnits().remove(currentPlayerPawn);
	    city.getCityUnits().add(currentPlayerPawn);
	    currentPlayerPawn.setLocation(city);

        if(currentPlayerRole.getRoleType() == RoleType.Medic)
            medicEnterCity(city);

        // OTB EXTENSION----
        /*if(currentPlayerRole.getRoleType() == RoleType.ContainmentSpecialist) {


        }*/

        currentPlayer.incrementActionTaken();
        return 0;

    }

    //returns 0 upon success, 1 upon failure
    public int playDirectFlight(CityCard card) {
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn currentPlayerPawn = currentPlayer.getPawn();
        Role currentPlayerRole = currentPlayerPawn.getRole();
        City currentPlayerCity = currentPlayerPawn.getLocation();
        City city = currentGame.getCityByName(card.getCityName());

        if(currentPlayer.getActionsTaken() == 4) {
            currentPlayer.setActionsTaken(0);
            return 1;
        }

        if (!currentPlayer.isInHand(card)) {
            return 1;
        }

        currentPlayerCity.getCityUnits().remove(currentPlayerPawn);
        city.getCityUnits().add(currentPlayerPawn);
        currentPlayerPawn.setLocation(city);

        if(currentPlayerRole.getRoleType() == RoleType.Medic)
            medicEnterCity(city);

        // OTB EXTENSION----
        /*if(currentPlayerRole.getRoleType() == RoleType.TroubleShooter) {


        }*/

        currentPlayer.incrementActionTaken();
        // MUST DISCARD CARD AFTER PLAY?
        return 0;
    }

    //returns 0 upon success, 1 upon failure
    public int playTreatDisease(DiseaseFlag flag) {
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn currentPlayerPawn = currentPlayer.getPawn();
        Role currentPlayerRole = currentPlayerPawn.getRole();
        City city = currentPlayerPawn.getLocation();
        DiseaseType diseaseType = flag.getDiseaseType();
        Disease disease = currentGame.getDiseaseByDiseaseType(diseaseType);
        boolean cured = disease.isCured();

        if(currentPlayer.getActionsTaken() == 4) {
            currentPlayer.setActionsTaken(0);
            return 1;
        }

        if(currentPlayerRole.getRoleType() == RoleType.Medic || cured) {
            city.removeAllDiseaseFlags(diseaseType).forEach(unit -> currentGame.getDiseaseSupplyByDiseaseType(diseaseType).add((DiseaseFlag) unit));
            if(currentPlayerRole.getRoleType() != RoleType.Medic)
                currentPlayer.incrementActionTaken();
        } else {
            currentGame.getDiseaseSupplyByDiseaseType(diseaseType).add(city.removeOneDiseaseFlag(diseaseType));
            currentPlayer.incrementActionTaken();
        }

        if(cured && currentGame.checkIfEradicated(diseaseType)) {
            disease.setEradicated(true);
        }

        return 0;
    }

    private void medicEnterCity(City city) {
	    for(DiseaseType d : DiseaseType.values()) {
                if(d != DiseaseType.Purple) {
                    Disease disease = currentGame.getDiseaseByDiseaseType(d);
                    boolean cured = disease.isCured();
                    if(city.getNumOfDiseaseFlagsPlaced(d) > 0 && cured) {
                        city.removeAllDiseaseFlags(d).forEach(unit -> currentGame.getDiseaseSupplyByDiseaseType(d).add((DiseaseFlag) unit));
                    }

                    if(currentGame.checkIfEradicated(d) && cured) {
                        disease.setEradicated(true);
                    }
                }
	    }

    }

    
    public void playShareKnowledgeRequest(Player participant, CityCard c) {
    	ShareKnowledgeAction share = new ShareKnowledgeAction(participant, c);
    	currentGame.setCurrentConsentRequiringAction(share);
    	if (currentGame.getCurrentPlayer().isInHand(c)){
    		promptGive(participant, c);
    	}
    	else {
    		promptTake(participant, c);
    	}
    	// currentPlayer cannot play any actions until participant replies
    	// Client must check currentPlayerTurnStatus before sending any actions
    	currentGame.setCurrentPlayerTurnStatus(CurrentPlayerTurnStatus.WaitingForReply);
    	
    }
    
    public void replyConsentRequest(Boolean b){
    	if (b){
    		notifyConsentRequestApproved();
    		currentGame.getCurrentConsentRequiringAction().playAction();
    	}
    	else {
    		notifyConsentRequestDenied();
    	}
    	currentGame.setCurrentConsentRequiringAction(null);
    	currentGame.setCurrentPlayerTurnStatus(CurrentPlayerTurnStatus.PlayingActions);
    }
    
    
    public Player getCurrentPlayer(){
    	return currentGame.getCurrentPlayer();
    }
    
    
//    public int playShareKnowledgeReply(ConsentRequiringAction a) {
//	    return 0;
//    }
    
    // TO DO
    // Prompts toPlayer for permission for currentPlayer to give card to toPlayer
    public void promptGive(Player toPlayer, PlayerCard card){
    	
    }
    
    // TO DO
    // Prompts fromPlayer for permission for currentPlayer to take card from fromPlayer
    public void promptTake(Player fromPlayer, PlayerCard card){
    	
    }
    
    // TO DO (notifies currentPlayer)
    // Do we want to also notify other players?
    public void notifyConsentRequestApproved(){
    	
    }
    
    // TO DO (notifies currentPlayer)
    public void notifyConsentRequestDenied(){
    	
    }
    
    // TO DO
    public void infectNextCity(){
    	
    }

    public void setOneQuietNight(boolean b){
    	currentGame.setOneQuietNight(b);
    }
}