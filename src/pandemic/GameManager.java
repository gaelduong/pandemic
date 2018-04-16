package pandemic;

import pandemic.eventcards.EventCard;

import java.util.*;
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
	private boolean gameLost;

	private Player bioTPlayer;
    private LinkedList<Player> activePlayersNonBT;

	public GameManager(int numOfPlayers, int numOfEpidemicCards, ChallengeKind challengeKind) {
        gameSettings = new GameSettings(numOfPlayers, numOfEpidemicCards, challengeKind);
		// Setup neighborsDict and regionsDict lookups
        //this.hostPlayer = hostPlayer;
        activePlayers = new LinkedList<Player>();

        //activePlayers.add(hostPlayer);
        gameLost = false;
		setRegionsDict();
		setNeighborsDict();
		setNameToRegionDict();
		setRegionToDiseaseTypeDict();

		//FOR TESTING:
		//createNewGame();
		//currentGame.getDiseaseByDiseaseType(DiseaseType.Blue).setCured(true);
		
		// FOR TESTING SHAREKNOWLEGE:
		//currentGame.setCurrentPlayerTurnStatus(CurrentPlayerTurnStatus.PlayingActions);
		
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
        /*System.out.println("------------------GAME UNIT BEFORE treat disease----------------------");
        currentGame.printGameUnits();
        playTreatDisease((DiseaseFlag) currentGame.getCityByName(CityName.Atlanta).getCityUnits().stream()
                                                                                                 .filter(unit ->
                                                                                                                    unit.getUnitType() == UnitType.DiseaseFlag)
                                                                                                 .findAny().orElse(null));*/
        //currentGame.printGameBoard();
        //System.out.println("------------------GAME UNIT after treat disease----------------------");
        //currentGame.printGameUnits();



	}

	public void createNewGame() {
        //System.out.println("Creating new game for hostPlayer " + hostPlayer.getPlayerUserName() + " ......");
	    currentGame = new Game(gameSettings, this);
	    currentGame.initializeGame();
       /* System.out.println("------ACTIVE PLAYER LIST:");
        activePlayers.forEach(p -> System.out.println("    Player username:" + p.getPlayerUserName() +
                ", role: " + p.getRoleType()));
        System.out.println("-------------------------");*/

       if(currentGame.isBioTChallengeActive()) {
           this.activePlayersNonBT = new LinkedList<Player>();
       } else {
           currentGame.setQuarantinesActive(true);
       }

    }

    public Player getHostPlayer() {
	    return hostPlayer;
    }
    public void leaveGameFromLobby(User user)
    {
        for(int i = 0; i < activePlayers.size(); ++i)
        {
            if(activePlayers.get(i).getPlayerUserName().equals(user.getUserName()))
            {
                currentGame.getGameManager().getGame().removePlayerFromBoard(activePlayers.get(i));
                activePlayers.remove(i);
                break;
            }
        }
    }

    public void joinGame(User user){
	    Player p = new Player(user);
	    Pawn playerPawn;

	    if(currentGame.isBioTChallengeActive() && activePlayers.size() == 1) {
	        playerPawn = currentGame.getBioTPawn();
        } else {
            playerPawn = currentGame.getRandomUnassignedPawn();
        }
// p.getPawn().getLocation().getName()
//
        p.setPawn(playerPawn);
        playerPawn.setPlayer(p);
        p.setRole(playerPawn.getRole());
        playerPawn.getRole().setAssigned(true);
        playerPawn.setAssigned(true);

        if(playerPawn.getRole().getRoleType() == RoleType.Colonel
                && !currentGame.isBioTChallengeActive()) {
            currentGame.addPlusTwoQuarantineMarkers();
        }

        if (playerPawn.getRole().getRoleType() != RoleType.BioTerrorist) {
            City atlCity = currentGame.getGameManager().getCityByName(CityName.Atlanta);
            atlCity.getCityUnits().add(playerPawn);
        } else {
            City miamiCity = currentGame.getGameManager().getCityByName(CityName.Miami);
            miamiCity.getCityUnits().add(playerPawn);
        }


        if(activePlayers.isEmpty()) {
            hostPlayer = p;
            currentGame.setCurrentPlayer(p);
        }

        activePlayers.add(p);
        System.out.println("------ACTIVE PLAYER LIST:");
        activePlayers.forEach(player -> System.out.println("    Player username:" + player.getPlayerUserName() +
                ", role: " + player.getRoleType()));
        System.out.println("-------------------------");

        if(activePlayers.size() == gameSettings.getNumOfPlayers())
            currentGame.dealCardsAndShuffleInEpidemicCards();

        if (currentGame.isBioTChallengeActive()) {
            if(p.getRoleType() == RoleType.BioTerrorist) {
                bioTPlayer = p;
            } else {
                activePlayersNonBT.add(p);
            }
        }



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

	public void setEventCardsEnabled(boolean b){
	    currentGame.setEventCardsEnabled(b);
	}
	
	public int getInfectionRate(){
	    if (currentGame.getCommercialTravelBanActive()){
	        return 1;
        }
        else {
            return currentGame.getInfectionRate();
        }
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
//		int numFlagsInCity = c.getNumOfDiseaseFlagsPlaced(regionToDiseaseTypeDict.get(c.getRegion()));
//		if (numFlagsInCity == 0) {
//            infectNextCity(c);
//            infectNextCity(c);
//            infectNextCity(c);
//        }
//        else{
//		    int numInfectionsRemaining = 4 - numFlagsInCity;
//		    while(numInfectionsRemaining > 0){
//		        infectNextCity(c);
//		        numInfectionsRemaining--;
//            }
//
//        }
	}

	public int infectCityForMutationCard(){
        if (!currentGame.getDiseaseByDiseaseType(DiseaseType.Purple).isEradicated()){
            CityInfectionCard card = (CityInfectionCard) currentGame.getInfectionDeck().drawLastCard();
            City city = currentGame.getCityByName(card.getCityName());
            System.out.println("InfectionCard drawn: " + card.getCityName());
            DiseaseType cityDiseaseType = DiseaseType.Purple;

            if (cityDiseaseType.equals(currentGame.getVirulentStrain()) && !currentGame.getDiseaseByDiseaseType(cityDiseaseType).isEradicated() && currentGame.getRateEffectActive() && !currentGame.getRateEffectAffectedInfection()) {
                currentGame.setInfectionsRemaining(currentGame.getInfectionsRemaining() + 1);
                currentGame.setRateEffectAffectedInfection(true);
            }

            ArrayList<DiseaseFlag> diseaseFlags = currentGame.getDiseaseSupplyByDiseaseType(cityDiseaseType);
            boolean gameStatus = (currentGame.getOutBreakMeterReading() < 8) && diseaseFlags.size() >= 1;
            if (!gameStatus) {
                //NOTIFY ALL PLAYERS LOST
                currentGame.setGamePhase(GamePhase.Completed);
                return 0;
            }

            // If Virulent Strain Challenge active and ChronicEffectEpidemicCard has been drawn, must check its effect:
            if (currentGame.getChronicEffectActive() && cityDiseaseType == getVirulentStrain() && city.getNumOfDiseaseFlagsPlaced(getVirulentStrain()) == 0 && diseaseFlags.size() < 2) {
                //NOTIFY ALL PLAYERS LOST
                currentGame.setGamePhase(GamePhase.Completed);
                return 0;
            }

            if (currentGame.getChronicEffectActive() && cityDiseaseType == getVirulentStrain() && city.getNumOfDiseaseFlagsPlaced(getVirulentStrain()) == 0 && diseaseFlags.size() >= 2) {
                currentGame.setChronicEffectInfection(true);
            }

            Disease cityDisease = currentGame.getDiseaseByDiseaseType(cityDiseaseType);
            boolean qsOrMedicPreventingInfectionInCity = currentGame.isQuarantineSpecialistInCity(city) || (currentGame.isMedicInCity(city) && cityDisease.isCured());
            boolean diseaseEradicated = currentGame.checkIfEradicated(cityDiseaseType);
            boolean qsPresentInNeighbor = false;

            ArrayList<City> cityNeighbors = city.getNeighbors();
            LinkedList<City> Q = new LinkedList<>();
            Q.addLast(city);

            for (City c : cityNeighbors) {
                qsPresentInNeighbor = currentGame.isQuarantineSpecialistInCity(c);
                if (qsPresentInNeighbor) break;
            }

            boolean infectStatus = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor || diseaseEradicated;

            // FOR TESTING:
            if (qsOrMedicPreventingInfectionInCity) {
                System.out.println("Quarantine Specialist or Medic preventing infection in this city.");
            } else if (qsPresentInNeighbor) {
                System.out.println("Quarantine Specialist in neighboring city.");
            } else if (diseaseEradicated) {
                System.out.println("Disease is eradicated.");
            }

            if (!infectStatus) {
                currentGame.infectAndResolveOutbreaks(cityDiseaseType, cityDisease, gameStatus, Q);
            }
            currentGame.getInfectionDiscardPile().addCard(card);
        }
        return 0;
    }

    public int infectCitiesForMutationSpreads(){
        if (!currentGame.getDiseaseByDiseaseType(DiseaseType.Purple).isEradicated()) {
            for (int i = 0; i < 3; i++) {
                CityInfectionCard card = (CityInfectionCard) currentGame.getInfectionDeck().drawLastCard();
                City city = currentGame.getCityByName(card.getCityName());
                System.out.println("InfectionCard drawn: " + card.getCityName());
                DiseaseType cityDiseaseType = DiseaseType.Purple;

                if (cityDiseaseType.equals(currentGame.getVirulentStrain()) && !currentGame.getDiseaseByDiseaseType(cityDiseaseType).isEradicated() && currentGame.getRateEffectActive() && !currentGame.getRateEffectAffectedInfection()) {
                    currentGame.setInfectionsRemaining(currentGame.getInfectionsRemaining() + 1);
                    currentGame.setRateEffectAffectedInfection(true);
                }

                ArrayList<DiseaseFlag> diseaseFlags = currentGame.getDiseaseSupplyByDiseaseType(cityDiseaseType);
                boolean gameStatus = (currentGame.getOutBreakMeterReading() < 8) && diseaseFlags.size() >= 1;
                if (!gameStatus) {
                    //NOTIFY ALL PLAYERS LOST
                    currentGame.setGamePhase(GamePhase.Completed);
                    return 0;
                }

                // If Virulent Strain Challenge active and ChronicEffectEpidemicCard has been drawn, must check its effect:
                if (currentGame.getChronicEffectActive() && cityDiseaseType == getVirulentStrain() && city.getNumOfDiseaseFlagsPlaced(getVirulentStrain()) == 0 && diseaseFlags.size() < 2) {
                    //NOTIFY ALL PLAYERS LOST
                    currentGame.setGamePhase(GamePhase.Completed);
                    return 0;
                }

                if (currentGame.getChronicEffectActive() && cityDiseaseType == getVirulentStrain() && city.getNumOfDiseaseFlagsPlaced(getVirulentStrain()) == 0 && diseaseFlags.size() >= 2) {
                    currentGame.setChronicEffectInfection(true);
                }

                Disease cityDisease = currentGame.getDiseaseByDiseaseType(cityDiseaseType);
                boolean qsOrMedicPreventingInfectionInCity = currentGame.isQuarantineSpecialistInCity(city) || (currentGame.isMedicInCity(city) && cityDisease.isCured());
                boolean diseaseEradicated = currentGame.checkIfEradicated(cityDiseaseType);
                boolean qsPresentInNeighbor = false;

                ArrayList<City> cityNeighbors = city.getNeighbors();
                LinkedList<City> Q = new LinkedList<>();
                Q.addLast(city);

                for (City c : cityNeighbors) {
                    qsPresentInNeighbor = currentGame.isQuarantineSpecialistInCity(c);
                    if (qsPresentInNeighbor) break;
                }

                boolean infectStatus = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor || diseaseEradicated;

                // FOR TESTING:
                if (qsOrMedicPreventingInfectionInCity) {
                    System.out.println("Quarantine Specialist or Medic preventing infection in this city.");
                } else if (qsPresentInNeighbor) {
                    System.out.println("Quarantine Specialist in neighboring city.");
                } else if (diseaseEradicated) {
                    System.out.println("Disease is eradicated.");
                }

                if (!infectStatus) {
                    currentGame.infectAndResolveOutbreaks(cityDiseaseType, cityDisease, gameStatus, Q);
                }
                currentGame.getInfectionDiscardPile().addCard(card);
            }
        }
        return 0;
    }

    public void infectCitiesForMutationIntensifies(){
        currentGame.infectMutationIntensifies();
    }

    public int infectCitiesForMutationThreatens(){
        if (!currentGame.getDiseaseByDiseaseType(DiseaseType.Purple).isEradicated()) {
            CityInfectionCard card = (CityInfectionCard) currentGame.getInfectionDeck().drawLastCard();
            City city = currentGame.getCityByName(card.getCityName());
            System.out.println("InfectionCard drawn: " + card.getCityName());
            DiseaseType cityDiseaseType = DiseaseType.Purple;

            if (cityDiseaseType.equals(currentGame.getVirulentStrain()) && !currentGame.getDiseaseByDiseaseType(cityDiseaseType).isEradicated() && currentGame.getRateEffectActive() && !currentGame.getRateEffectAffectedInfection()) {
                currentGame.setInfectionsRemaining(currentGame.getInfectionsRemaining() + 1);
                currentGame.setRateEffectAffectedInfection(true);
            }

            ArrayList<DiseaseFlag> diseaseFlags = currentGame.getDiseaseSupplyByDiseaseType(cityDiseaseType);
            boolean gameStatus = (currentGame.getOutBreakMeterReading() < 8) && diseaseFlags.size() >= 1;
            if (!gameStatus) {
                //NOTIFY ALL PLAYERS LOST
                currentGame.setGamePhase(GamePhase.Completed);
                return 0;
            }

            // If Virulent Strain Challenge active and ChronicEffectEpidemicCard has been drawn, must check its effect:
            if (currentGame.getChronicEffectActive() && cityDiseaseType == getVirulentStrain() && city.getNumOfDiseaseFlagsPlaced(getVirulentStrain()) == 0 && diseaseFlags.size() < 2) {
                //NOTIFY ALL PLAYERS LOST
                currentGame.setGamePhase(GamePhase.Completed);
                return 0;
            }

            if (currentGame.getChronicEffectActive() && cityDiseaseType == getVirulentStrain() && city.getNumOfDiseaseFlagsPlaced(getVirulentStrain()) == 0 && diseaseFlags.size() >= 2) {
                currentGame.setChronicEffectInfection(true);
            }

            Disease cityDisease = currentGame.getDiseaseByDiseaseType(cityDiseaseType);
            boolean qsOrMedicPreventingInfectionInCity = currentGame.isQuarantineSpecialistInCity(city) || (currentGame.isMedicInCity(city) && cityDisease.isCured());
            boolean diseaseEradicated = currentGame.checkIfEradicated(cityDiseaseType);
            boolean qsPresentInNeighbor = false;

            ArrayList<City> cityNeighbors = city.getNeighbors();
            LinkedList<City> Q = new LinkedList<>();
            Q.addLast(city);

            for (City c : cityNeighbors) {
                qsPresentInNeighbor = currentGame.isQuarantineSpecialistInCity(c);
                if (qsPresentInNeighbor) break;
            }

            boolean infectStatus = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor || diseaseEradicated;

            // FOR TESTING:
            if (qsOrMedicPreventingInfectionInCity) {
                System.out.println("Quarantine Specialist or Medic preventing infection in this city.");
            } else if (qsPresentInNeighbor) {
                System.out.println("Quarantine Specialist in neighboring city.");
            } else if (diseaseEradicated) {
                System.out.println("Disease is eradicated.");
            }

            if (!infectStatus) {
                currentGame.infectAndResolveOutbreaks(cityDiseaseType, cityDisease, gameStatus, Q);
                currentGame.infectAndResolveOutbreaks(cityDiseaseType, cityDisease, gameStatus, Q);
                currentGame.infectAndResolveOutbreaks(cityDiseaseType, cityDisease, gameStatus, Q);
            }
            currentGame.getInfectionDiscardPile().addCard(card);
        }
        return 0;
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

	public String endTurn(){
	// MUST BE MODIFIED TO HANDLE OTB CHALLENGES (i.e. Mutations, Bioterrorist win/lose)


        // REMOVE AFTER TESTING HIDDEN POCKET EPIDEMIC CARD
//        setVirulentStrain();
//        System.out.println("Virulent Strain: " + currentGame.getVirulentStrain());
//        setVirulentStrainIsEradicated(true);

        System.out.println("\t\t IN END TURN!!!");
		String status = "";
		if (currentGame.getMobileHospitalActive()){
		    currentGame.setMobileHospitalActive(false);
        }
        currentGame.setGamePhase(GamePhase.TurnPlayerCards);
		Player p = currentGame.getCurrentPlayer();
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
                System.out.println("Epidemic occurring...");
				((EpidemicCard) playerCard1).resolveEpidemic();
                System.out.println("Epidemic resolved");
                status = playerCard1.getCardName() + " is occurring...";
			}
			else if (playerCard1 instanceof MutationEventCard){
			    System.out.println("Mutation Event occurring...");
                ((MutationEventCard) playerCard1).resolveMutationEvent();
                status = playerCard1.getCardName();
            }
			else {
				p.addToHand(playerCard1);
				checkHandSize(p);
			}
			if (playerCard2 instanceof EpidemicCard){
                System.out.println("Epidemic occurring...");
				((EpidemicCard) playerCard2).resolveEpidemic();
                System.out.println("Epidemic resolved");
                status = playerCard2.getCardName() + " is occurring...";
			}
            else if (playerCard2 instanceof MutationEventCard){
                System.out.println("Mutation Event occurring...");
                ((MutationEventCard) playerCard2).resolveMutationEvent();
                status = playerCard2.getCardName();
            }
			else {
				p.addToHand(playerCard2);
				checkHandSize(p);
			}
			currentGame.setGamePhase(GamePhase.TurnInfection);
			currentGame.setInfectionsRemaining(currentGame.getInfectionRate());

		}

//		if (currentGame.getOneQuietNight()){
//			currentGame.setOneQuietNight(false);
//		}
//		else {
//		    setEventCardsEnabled(false);
//		    int currentInfectionRate = currentGame.getInfectionRate();
//
//		    for(int i = 0; i < currentInfectionRate; i++) {
//		        CityInfectionCard card = (CityInfectionCard) currentGame.getInfectionDeck().drawCard();
//		        City cardCity = currentGame.getCityByName(card.getCityName());
//
//                System.out.println("InfectionCard drawn: " + card.getCityName());
//
//                infectNextCity(cardCity);
//
//                DiseaseType cityDiseaseType = regionToDiseaseTypeDict.get(cardCity.getRegion());
//                ArrayList<DiseaseFlag> diseaseFlags =
//                        currentGame.getDiseaseSupplyByDiseaseType(cityDiseaseType);
//
//                boolean gameStatus = (currentGame.getOutBreakMeterReading() < 8) && diseaseFlags.size() >= 1;
//
//                if(!gameStatus) {
//                    //NOTIFY ALL PLAYERS LOST
//                    currentGame.setGamePhase(GamePhase.Completed);
//                    return 0;
//                }
//            }

//            setEventCardsEnabled(true);

//		}

//        // SET NEXT PLAYER TO CURRENT PLAYER
//        currentGame.setGamePhase(GamePhase.TurnActions);
//
//		// MUST MAKE SURE current player is at the head of the queue
//		activePlayers.addLast(activePlayers.removeFirst());
//		setCurrentPlayer(activePlayers.getFirst());
//		if (activePlayers.getFirst().equals(currentGame.getCommercialTravelBanPlayedBy())){
//		    currentGame.setCommercialTravelBanActive(false);
//		    currentGame.setCommercialTravelBanPlayedBy(null);
//        }
		return status;

	}


    public String infectNextCity(){
        System.out.println("IN INFECT NEXT CITY!!!!");
        if (currentGame.getInfectionsRemaining() > 0) {
            if (currentGame.getOneQuietNight()) {
                currentGame.setOneQuietNight(false);
            } else {
                System.out.println("DRAWING CARD IN INFECTING CITIES");
                setEventCardsEnabled(false);
                InfectionCard cardDrawn = currentGame.getInfectionDeck().drawCard();
                if (cardDrawn instanceof MutationCard) {
                    System.out.println("Mutation Card Drawn");
                    ((MutationCard) cardDrawn).resolveMutation();
                    return "Mutation Card drawn...";
                } else {
                    CityInfectionCard card = (CityInfectionCard) cardDrawn;
                    City city = currentGame.getCityByName(card.getCityName());

                    System.out.println("InfectionCard drawn: " + card.getCityName());

                    DiseaseType cityDiseaseType = regionToDiseaseTypeDict.get(city.getRegion());

                    if (cityDiseaseType.equals(currentGame.getVirulentStrain()) && !currentGame.getDiseaseByDiseaseType(cityDiseaseType).isEradicated() && currentGame.getRateEffectActive() && !currentGame.getRateEffectAffectedInfection()) {
                        currentGame.setInfectionsRemaining(currentGame.getInfectionsRemaining() + 1);
                        currentGame.setRateEffectAffectedInfection(true);
                    }

                    ArrayList<DiseaseFlag> diseaseFlags = currentGame.getDiseaseSupplyByDiseaseType(cityDiseaseType);
                    boolean gameStatus = (currentGame.getOutBreakMeterReading() < 8) && diseaseFlags.size() >= 1;
                    if (!gameStatus) {
                        //NOTIFY ALL PLAYERS LOST
                        currentGame.setGamePhase(GamePhase.Completed);
                        return "";
                    }

                    // If Virulent Strain Challenge active and ChronicEffectEpidemicCard has been drawn, must check its effect:
                    if (currentGame.getChronicEffectActive() && cityDiseaseType == getVirulentStrain() && city.getNumOfDiseaseFlagsPlaced(getVirulentStrain()) == 0 && diseaseFlags.size() < 2) {
                        //NOTIFY ALL PLAYERS LOST
                        currentGame.setGamePhase(GamePhase.Completed);
                        return "";
                    }

                    if (currentGame.getChronicEffectActive() && cityDiseaseType == getVirulentStrain() && city.getNumOfDiseaseFlagsPlaced(getVirulentStrain()) == 0 && diseaseFlags.size() >= 2) {
                        currentGame.setChronicEffectInfection(true);
                    }

                    Disease cityDisease = currentGame.getDiseaseByDiseaseType(cityDiseaseType);
                    boolean qsOrMedicPreventingInfectionInCity = currentGame.isQuarantineSpecialistInCity(city) || (currentGame.isMedicInCity(city) && cityDisease.isCured());
                    boolean diseaseEradicated = currentGame.checkIfEradicated(cityDiseaseType);
                    boolean qsPresentInNeighbor = false;

                    ArrayList<City> cityNeighbors = city.getNeighbors();
                    LinkedList<City> Q = new LinkedList<>();
                    Q.addLast(city);

                    for (City c : cityNeighbors) {
                        qsPresentInNeighbor = currentGame.isQuarantineSpecialistInCity(c);
                        if (qsPresentInNeighbor) break;
                    }

                    boolean infectStatus = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor || diseaseEradicated;

                    // FOR TESTING:
                    if (qsOrMedicPreventingInfectionInCity) {
                        System.out.println("Quarantine Specialist or Medic preventing infection in this city.");
                    } else if (qsPresentInNeighbor) {
                        System.out.println("Quarantine Specialist in neighboring city.");
                    } else if (diseaseEradicated) {
                        System.out.println("Disease is eradicated.");
                    }

                    if (!infectStatus) {
                        currentGame.infectAndResolveOutbreaks(cityDiseaseType, cityDisease, gameStatus, Q);
                        if(currentGame.isBioTChallengeActive()
                                || currentGame.getChallenge() == ChallengeKind.Mutation
                                || currentGame.getChallenge() == ChallengeKind.VirulentStrainAndMutation) {
                            boolean diseaseEradicatedPurple = currentGame.checkIfEradicated(DiseaseType.Purple);
                            if (city.containsPurpleDisease() && !diseaseEradicatedPurple) {
                                Disease purpleDisease = currentGame.getDiseaseByDiseaseType(DiseaseType.Purple);
                                ArrayList<DiseaseFlag> diseaseFlagsPurple = currentGame.getDiseaseSupplyByDiseaseType(DiseaseType.Purple);
                                boolean gameStatusPurple = (currentGame.getOutBreakMeterReading() < 8) && diseaseFlags.size() >= 1;
                                LinkedList<City> QPurple = new LinkedList<City>();
                                QPurple.addLast(city);

                                currentGame.infectAndResolveOutbreaks(DiseaseType.Purple, purpleDisease, gameStatusPurple, QPurple);
                            }
                        }
                    }

                    currentGame.decrementInfectionsRemaining();
                    currentGame.getInfectionDiscardPile().addCard(cardDrawn);
                    setEventCardsEnabled(true);
                }
            }
        }
        if (currentGame.getInfectionsRemaining() == 0){
            // Reset once-per-turn action flags
            currentGame.setArchivistActionUsed(false);
            currentGame.setEpidemiologistActionUsed(false);
            currentGame.setFieldOperativeActionUsed(false);

            currentGame.setRateEffectAffectedInfection(false);
            // SET NEXT PLAYER TO CURRENT PLAYER
            // MUST MAKE SURE current player is at the head of the queue
            currentGame.setGamePhase(GamePhase.TurnActions);
            Player p = currentGame.getCurrentPlayer();
            p.setActionsTaken(0);

            if (currentGame.isBioTChallengeActive()) {
                activePlayersNonBT.addLast(activePlayersNonBT.removeFirst());
                setCurrentPlayer(bioTPlayer);
            } else {
                activePlayers.addLast(activePlayers.removeFirst());
                setCurrentPlayer(activePlayers.getFirst());
            }

            //TODO: should setCurrentPlayerTurnStatus to PlayingActions after next player selected??

            if (activePlayers.getFirst().equals(currentGame.getCommercialTravelBanPlayedBy())){
                currentGame.setCommercialTravelBanActive(false);
                currentGame.setCommercialTravelBanPlayedBy(null);
            }
            return "";
        }
        return "";
	}

	public Player getBioTPlayer() {
	    return bioTPlayer;
    }

	public void endTurnBioT() {
	    //TODO: reset biotturntracker,
        //TODO: setCurrentPlayerTurnStatus to PlayingActions ??
        //TODO: select correct player for next turn

        Player currentPlayer = currentGame.getCurrentPlayer();

        if (currentPlayer.isBioTerrorist()) {
            currentPlayer.getBioTTurnTracker().resetTurnTracker();
            setCurrentPlayer(activePlayersNonBT.getFirst());
        }
    }

	// Checks if Player has too many cards in hand. Must resolve issue if Player has too many cards.
	public void checkHandSize(Player p){
		int numCardsInHand = p.getHandSize();
		// For OTB, must check if Player is Archivist
        RoleType playerRole = p.getRoleType();
		if ((playerRole.equals(RoleType.Archivist) && numCardsInHand > 8) || (!playerRole.equals(RoleType.Archivist) && numCardsInHand > 7)){
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
		// Must take a player as a parameter for when a non-current player has too many cards and must discard one
        System.out.println("Discarding Player Card");
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
        gameLost = true;
        System.out.println("GAME LOST");
	}

	public boolean isGameLost() {
	    return gameLost;
    }

	// FOR THESE MIGHT NEED TO CHANGE INPUTS DEPENDING ON SERIALIZED OBJECT
    // PASSED ACROSS NETWORK

	//returns 0 upon success, 1 upon failure
	public int playDriveFerry(City city) {
	    Player currentPlayer = currentGame.getCurrentPlayer();

	    if (currentGame.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayingActions) {
            Pawn currentPlayerPawn = currentPlayer.getPawn();
            Role currentPlayerRole = currentPlayerPawn.getRole();
            City currentPlayerCity = currentPlayerPawn.getLocation();

            if ((currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 5) || (!currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 4)) {
                currentPlayer.setActionsTaken(0);
                return 1;
            }

            if ((currentPlayerCity.getConnections().stream()
                    .filter(conn -> conn.getEnd1().getName().equals(city.getName())  || conn.getEnd2().getName().equals(city.getName()))
                    .collect(Collectors.toList())).isEmpty()) {
                return 1;
            }

            if(currentPlayer.isBioTerrorist()) {
                if(currentPlayer.getBioTTurnTracker().isType3ActionsCompleted())
                    return 1;
            }

            currentPlayerCity.getCityUnits().remove(currentPlayerPawn);
            city.getCityUnits().add(currentPlayerPawn);
            currentPlayerPawn.setLocation(city);

            if (currentPlayerRole.getRoleType() == RoleType.Medic) {
                medicEnterCity(city);
            }

            if (currentPlayer.getRoleType().equals(RoleType.ContainmentSpecialist)){
                containmentSpecialistEnterCity(city);
            }

            if(currentPlayer.getRoleType().equals(RoleType.Colonel)) {
                colonelEnterCity(city);
            }

            if (currentPlayer.isBioTerrorist()) {
                currentPlayer.getBioTTurnTracker().incrementType3ActionCounter();
            } else {
                currentPlayer.incrementActionTaken();
            }

            if(currentGame.isBioTChallengeActive()) {
                if(currentPlayer.getPawn().getLocation().isBioTSpotted()) {
                    currentGame.setBioTSpotted(true);
                    bioTPlayer.getBioTTurnTracker().setSpotted(true);
                }
            }

            if (currentGame.getGovernmentInterferenceActive()){
                currentGame.setGovernmentInterferenceSatisfied(false);
            }

            return 0;
        } else {
	        return 1;
        }


    }

    //returns 0 upon success, 1 upon failure
    public int playDirectFlight(MovingCard card) {
        Player currentPlayer = currentGame.getCurrentPlayer();
        if (currentGame.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayingActions) {
            Pawn currentPlayerPawn = currentPlayer.getPawn();
            Role currentPlayerRole = currentPlayerPawn.getRole();
            City currentPlayerCity = currentPlayerPawn.getLocation();
            System.out.println("\t\t MovingCard getCityName = " + card.getCityName());
            City city = currentGame.getCityByName(card.getCityName());

            if ((currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 5) ||
                    (!currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 4)) {
                currentPlayer.setActionsTaken(0);
                return 1;
            }

            if (!currentPlayer.isInHandMovingCard(card)) {
                return 1;
            }

            if(currentPlayer.isBioTerrorist()) {
                if(currentPlayer.getBioTTurnTracker().isType2ActionsCompleted())
                    return 1;
            }

            currentPlayerCity.getCityUnits().remove(currentPlayerPawn);
            city.getCityUnits().add(currentPlayerPawn);
            currentPlayerPawn.setLocation(city);

            if(currentPlayerRole.getRoleType() == RoleType.Medic)
                medicEnterCity(city);

            if (currentPlayer.getRoleType().equals(RoleType.ContainmentSpecialist)){
                containmentSpecialistEnterCity(city);
            }

            if(currentPlayer.getRoleType().equals(RoleType.Colonel)) {
                colonelEnterCity(city);
            }

            if (currentPlayer.isBioTerrorist()) {
                currentPlayer.getBioTTurnTracker().incrementType2ActionCounter();
            } else {
                currentPlayer.incrementActionTaken();
            }

            if(currentGame.isBioTChallengeActive()) {
                if(currentPlayer.getPawn().getLocation().isBioTSpotted()) {
                    currentGame.setBioTSpotted(true);
                    bioTPlayer.getBioTTurnTracker().setSpotted(true);
                }

                //TODO:
                //  announce that bioT airport sighting at city above
            }

            if (currentGame.getGovernmentInterferenceActive()){
                currentGame.setGovernmentInterferenceSatisfied(false);
            }

            return 0;
        } else {
            return 1;
        }
    }

    //returns 0 upon success, 1 upon failure
    public int playTreatDisease(DiseaseFlag flag) {
        Player currentPlayer = currentGame.getCurrentPlayer();
        if (currentGame.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayingActions) {
            Pawn currentPlayerPawn = currentPlayer.getPawn();
            Role currentPlayerRole = currentPlayerPawn.getRole();
            City city = currentPlayerPawn.getLocation();
            DiseaseType diseaseType = flag.getDiseaseType();
            Disease disease = currentGame.getDiseaseByDiseaseType(diseaseType);
            boolean cured = disease.isCured();

            if ((currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 5) || (!currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 4)) {
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

            if (currentGame.getGovernmentInterferenceActive()
                    && diseaseType.equals(currentGame.getVirulentStrain())
                    && !city.containsQuarantineMarker()){
                currentGame.setGovernmentInterferenceSatisfied(true);
            }

            if(cured && currentGame.checkIfEradicated(diseaseType)) {
                disease.setEradicated(true);
            }

            // Check if all diseases in game are cured:
            boolean allBasicDiseasesCured = false;
            if (currentGame.getChallenge().equals(ChallengeKind.BioTerrorist) || currentGame.getChallenge().equals(ChallengeKind.Mutation) || currentGame.getChallenge().equals(ChallengeKind.VirulentStrainAndMutation)){
                allBasicDiseasesCured = currentGame.getDiseaseByDiseaseType(DiseaseType.Black).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Blue).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Red).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Yellow).isCured();
            }
            if (allBasicDiseasesCured && currentGame.allFlagsRemoved(DiseaseType.Purple)){
                currentGame.setGamePhase(GamePhase.Completed);
                notifyAllNonBTPlayersGameWon();
            }

            return 0;
        } else {
            return 1;
        }
    }

    public void medicEnterCity(City city) {
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

    public void containmentSpecialistEnterCity(City destination){
        Player currentPlayer = currentGame.getCurrentPlayer();
        if(destination.getNumOfDiseaseFlagsPlaced(DiseaseType.Red) >= 2){
            destination.removeOneDiseaseFlag(DiseaseType.Red);
        }
        if(destination.getNumOfDiseaseFlagsPlaced(DiseaseType.Blue) >= 2){
            destination.removeOneDiseaseFlag(DiseaseType.Blue);
        }
        if(destination.getNumOfDiseaseFlagsPlaced(DiseaseType.Yellow) >= 2){
            destination.removeOneDiseaseFlag(DiseaseType.Yellow);
        }
        if(destination.getNumOfDiseaseFlagsPlaced(DiseaseType.Black) >= 2){
            destination.removeOneDiseaseFlag(DiseaseType.Black);
        }
        if(destination.getNumOfDiseaseFlagsPlaced(DiseaseType.Purple) >= 2){
            destination.removeOneDiseaseFlag(DiseaseType.Purple);
        }
    }

    public void colonelEnterCity(City city) {
	    if(city.containsQuarantineMarker()) {
	        QuarantineMarker cityQM = city.getQuarantineMarker();
	        if(cityQM == null) {
                System.err.println("ERROR -- city contains quarantine marker but" +
                        " can not get marker!");
                return;
            } else {
	            if(cityQM.getUpFace() == QuarantineMarkerFaceValue.ONE)
	                cityQM.flipMarker();
            }
        }
    }
    
    
    public Player getCurrentPlayer(){
    	return currentGame.getCurrentPlayer();
    }

    public void setOneQuietNight(boolean b){
    	currentGame.setOneQuietNight(b);
    }
    
    // TO TEST
    public void setCurrentPlayer(Player p){
    	currentGame.setCurrentPlayer(p);
    }

    public LinkedList<Player> getActivePlayers(){
	    return activePlayers;
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

    public Player getPlayerFromUsername(String username) {
        return activePlayers.stream().filter(p -> p.getPlayerUserName().equals(username)).findFirst().orElse(null);
    }

    public Game getGame(){
	    return currentGame;
    }

    // Returns 0 if succesful, 1 if failed
    // Pre: currentPlayer has actions remaining, currentPlayer is in a city with a research station, toDiscard contains 5
    // (or 4 if currentPlayer has Scientist role) cards of the color of toCure, and toCure is not already cured
    public int playDiscoverCure(DiseaseType toCure, List<PlayerCard> toDiscard){
        Player currentPlayer = currentGame.getCurrentPlayer();
       // if (currentGame.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayingActions) {
            PlayerDiscardPile dp = currentGame.getPlayerDiscardPile();
            for (PlayerCard c : toDiscard) {
               discardPlayerCard(currentPlayer, c);
            }
            Disease d = currentGame.getDiseaseByDiseaseType(toCure);
            d.setCured(true);

            System.out.println(d.getDiseaseType() + " IS CURED: " + d.isCured());

            currentGame.checkIfEradicated(toCure);
            currentPlayer.incrementActionTaken();

            // Check if all diseases in game are cured:
            boolean allDiseasesCured;
            boolean allBasicDiseasesCured = false;
            if (currentGame.getChallenge().equals(ChallengeKind.BioTerrorist) || currentGame.getChallenge().equals(ChallengeKind.Mutation) || currentGame.getChallenge().equals(ChallengeKind.VirulentStrainAndMutation)){
                allDiseasesCured = currentGame.getDiseaseByDiseaseType(DiseaseType.Black).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Blue).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Red).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Yellow).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Purple).isCured();
                allBasicDiseasesCured = currentGame.getDiseaseByDiseaseType(DiseaseType.Black).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Blue).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Red).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Yellow).isCured();
            }
            else {
                allDiseasesCured = currentGame.getDiseaseByDiseaseType(DiseaseType.Black).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Blue).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Red).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Yellow).isCured();
            }
            if (allDiseasesCured){
                currentGame.setGamePhase(GamePhase.Completed);
                notifyAllNonBTPlayersGameWon();
            }
            else if (allBasicDiseasesCured && currentGame.allFlagsRemoved(DiseaseType.Purple)){
                currentGame.setGamePhase(GamePhase.Completed);
                notifyAllNonBTPlayersGameWon();
            }
            return 0;
//        }
//        else {
//            return 1;
//        }
    }

    private void notifyAllNonBTPlayersGameWon() {
	    // TO DO
    }

    // Returns 0 if successful, 1 if failed
    // Pre: currentPlayer has actions remaining and is currently in a city with a research station and selected another city with a
    // research station as destination
    public int playShuttleFlight(City destination){
	    Player currentPlayer = currentGame.getCurrentPlayer();
	    Pawn playerPawn = currentPlayer.getPawn();
	    Role playerRole = playerPawn.getRole();
	    City currentCity = playerPawn.getLocation();

	    if (currentCity.getCityUnits().contains(playerPawn)){
            currentCity.getCityUnits().remove(playerPawn);
            destination.getCityUnits().add(playerPawn);
            playerPawn.setLocation(destination);

            if (playerRole.getRoleType() == RoleType.Medic) {
                medicEnterCity(destination);
            }
            if (playerRole.getRoleType().equals(RoleType.ContainmentSpecialist)){
                containmentSpecialistEnterCity(destination);
            }

            if(currentPlayer.getRoleType().equals(RoleType.Colonel)) {
                colonelEnterCity(destination);
            }

            if (currentGame.getGovernmentInterferenceActive()){
                currentGame.setGovernmentInterferenceSatisfied(false);
            }

            currentPlayer.incrementActionTaken();
            return 0;
        }
        else {
            return 1;
        }
    }

    // Return 0 if successful, 1 if failed
    // Pre: the player has actions remaining and selected CityCard toDiscard, which is the city their pawn is currently in, from their hand and has selected
    // a destination city

    public int playCharterFlight(/*MovingCard toDiscard,*/ City destination){
	    Player currentPlayer = currentGame.getCurrentPlayer();
	    Pawn playerPawn = currentPlayer.getPawn();
	    Role playerRole = playerPawn.getRole();
	    City currentCity = playerPawn.getLocation();

        if(currentPlayer.isBioTerrorist()) {
            if(currentPlayer.getBioTTurnTracker().isType2ActionsCompleted())
                return 1;
        }

	    if (currentCity.getCityUnits().contains(playerPawn)){
            currentCity.getCityUnits().remove(playerPawn);
            destination.getCityUnits().add(playerPawn);
            playerPawn.setLocation(destination);

            if (playerRole.getRoleType() == RoleType.Medic) {
                medicEnterCity(destination);
            }
            if (playerRole.getRoleType().equals(RoleType.ContainmentSpecialist)){
                containmentSpecialistEnterCity(destination);
            }

            if(currentPlayer.getRoleType().equals(RoleType.Colonel)) {
                colonelEnterCity(destination);
            }

            if (currentPlayer.isBioTerrorist()) {
                currentPlayer.getBioTTurnTracker().incrementType2ActionCounter();
            } else {
                currentPlayer.incrementActionTaken();
            }

            if(currentGame.isBioTChallengeActive()) {
                if(currentPlayer.getPawn().getLocation().isBioTSpotted()) {
                    currentGame.setBioTSpotted(true);
                    bioTPlayer.getBioTTurnTracker().setSpotted(true);
                    //TODO:
                    //  announce that bioT airport sighting at currentCity above
                }
            }

            if (currentGame.getGovernmentInterferenceActive()){
                currentGame.setGovernmentInterferenceSatisfied(false);
            }

            return 0;
            //return discardPlayerCard(currentPlayer, toDiscard);
        }
        else {
            return 1;
        }
    }

    private int infectBioT(Player currentPlayer, City city) {
        if(currentPlayer.getBioTTurnTracker().isType1ActionsCompleted())
            return 1;

        Disease purpleDisease = currentGame.getDiseaseByDiseaseType(DiseaseType.Purple);
        ArrayList<DiseaseFlag> diseaseFlags = currentGame.getDiseaseSupplyByDiseaseType(DiseaseType.Purple);
        boolean gameStatus = (currentGame.getOutBreakMeterReading() < 8) && diseaseFlags.size() >= 1;
        LinkedList<City> Q = new LinkedList<City>();
        Q.add(city);

        currentGame.infectAndResolveOutbreaks(DiseaseType.Purple, purpleDisease, gameStatus, Q);
        currentPlayer.getBioTTurnTracker().incrementType1ActionCounter();
        return 0;
    }

    public int playInfectLocallyBioT() {
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn playerPawn = currentPlayer.getPawn();
        City currentCity = playerPawn.getLocation();

        if(!currentPlayer.isBioTerrorist()) {
            return 1;
        } else {
            return infectBioT(currentPlayer, currentCity);
        }

    }

    public int playInfectRemotelyBioT(CityInfectionCard card) {
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn playerPawn = currentPlayer.getPawn();
        City cityToInfect = currentGame.getCityByName(card.getCityName());

        if(!currentPlayer.isBioTerrorist()) {
            return 1;
        } else {
            return infectBioT(currentPlayer, cityToInfect);
        }

    }

    public int playSabotageBioT(CityInfectionCard card) {
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn playerPawn = currentPlayer.getPawn();
        City currentCity = playerPawn.getLocation();
        City cityToInfect = currentGame.getCityByName(card.getCityName());

        if(!currentPlayer.isBioTerrorist() || cityToInfect.getRegion() != currentCity.getRegion()) {
            return 1;
        } else {
            ResearchStation removed = currentCity.removeOneResearchStation();
            currentGame.addUnusedResearchStation(removed);
            currentPlayer.getBioTTurnTracker().incrementType1ActionCounter();
            return 0;
        }
    }

    public int playEscapeBioT(CityInfectionCard card) {
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn currentPlayerPawn = currentPlayer.getPawn();
        City currentPlayerCity = currentPlayerPawn.getLocation();
        City cityToEscapeTo = currentGame.getCityByName(card.getCityName());

        if(!currentPlayer.isBioTerrorist() || !currentPlayer.getBioTTurnTracker().isCaptured()) {
            return 1;
        } else {
            currentPlayerCity.getCityUnits().remove(currentPlayerPawn);
            cityToEscapeTo.getCityUnits().add(currentPlayerPawn);
            currentPlayerPawn.setLocation(cityToEscapeTo);
            return 0;
        }
    }

    public int playCaptureBioT() {
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn playerPawn = currentPlayer.getPawn();
        City currentCity = playerPawn.getLocation();

        if(!bioTPlayer.getBioTTurnTracker().isSpotted()) {
            return 1;
        } else {
            bioTPlayer.getBioTTurnTracker().setCaptured(true);
            ArrayList<CityInfectionCard> cardsToDiscard = bioTPlayer.discardAllCards();
            currentGame.getInfectionDiscardPile().addCards(cardsToDiscard);
            return 0;
        }
    }

    // Returns 0 if successful, 1 if failed
    // Pre: player has selected to build a Research station (either as an action or with Government Grant Event Card) and has been prompted
    // to select a research station to remove from the board as there are no unused stations remaining
    public int removeResearchStation(ResearchStation toRemove){
	    City source = toRemove.getLocation();
	    if (source.getCityUnits().contains(toRemove)){
	        source.getCityUnits().remove(toRemove);
	        currentGame.addUnusedResearchStation(toRemove);
	        toRemove.setLocation(null);
	        return 0;
        }
        else {
	        return 1;
        }
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining and the CityCard for the city in which their pawn is currently, unless they are the
    // Operations Expert, in their hand and has selected to build a research station.
    public int playBuildResearchStation(){
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn playerPawn = currentPlayer.getPawn();
        RoleType playerRole = playerPawn.getRole().getRoleType();
        City currentCity = playerPawn.getLocation();

        ResearchStation newStation = getGame().getUnusedResearchStation();
        if (newStation != null) {
            currentCity.getCityUnits().add(newStation);
            newStation.setLocation(currentCity);
            if (!playerRole.equals(RoleType.OperationsExpert)) {
                ArrayList<PlayerCard> cardsInHand = currentPlayer.getCardsInHand();
                for (PlayerCard c : cardsInHand) {
                    if (c.getCardName().equals(currentCity.getName().toString())) {
                        discardPlayerCard(currentPlayer, c);
                        break;
                    } else {
                        return 1;
                    }
                }
            }
            currentPlayer.incrementActionTaken();
            return 0;
        }
        else {
            return 1;
        }
    }

    public int placeQuaratineMarker(City city, Player currentPlayer) {
        QuarantineMarker newMarker = currentGame.getUnusedQuarantineMarker();
        if(newMarker != null) {
            if (!city.containsQuarantineMarker()) {
                city.getCityUnits().add(newMarker);
                newMarker.setLocation(city);

                if (currentPlayer != null)
                    currentPlayer.incrementActionTaken();

                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public int playImposeAQuarantine() {
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn playerPawn = currentPlayer.getPawn();
        RoleType playerRole = playerPawn.getRole().getRoleType();
        City currentCity = playerPawn.getLocation();

        return placeQuaratineMarker(currentCity, currentPlayer);

    }

    public int removeQuarantineMarker(QuarantineMarker toRemove) {
	    City source = toRemove.getLocation();
	    if(source.getCityUnits().contains(toRemove)) {
	        source.getCityUnits().remove(toRemove);
	        currentGame.addUnusedQuarantineMarker(toRemove);
	        toRemove.setLocation(null);
	        toRemove.resetMarkerFace();
	        return 0;
        } else {
	        return 1;
        }
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining, is Operations Expert, is in a city with a
    // research station, has selected a CityCard from, their hand to discard, and has selected a destination city, and there are
    // unused research stations.
    public int playMoveAsOperationsExpert(CityCard toDiscard, City destination){
	    Player currentPlayer = currentGame.getCurrentPlayer();
	    Pawn playerPawn = currentPlayer.getPawn();
	    Role playerRole = playerPawn.getRole();
	    City currentCity = playerPawn.getLocation();

	    if (currentCity.getCityUnits().contains(playerPawn)) {
            currentCity.getCityUnits().remove(playerPawn);
            destination.getCityUnits().add(playerPawn);
            playerPawn.setLocation(destination);

            currentPlayer.incrementActionTaken();
            return discardPlayerCard(currentPlayer, toDiscard);
        }
        else {
	        return 1;
        }
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining, is Dispatcher, has selected another Player's pawn,
    // selected a destination city that contains other pawns, and has received permission from the other Player
    public int playDispatcherMovePawnToAnyCity(Pawn target, City destination){
        Player currentPlayer = currentGame.getCurrentPlayer();
        City source = target.getLocation();

        if (source.getCityUnits().contains(target)) {
            source.getCityUnits().remove(target);
            destination.getCityUnits().add(target);
            target.setLocation(destination);

            if(target.getRole().getRoleType() == RoleType.Medic) {
                medicEnterCity(destination);
            }
            if (target.getRole().getRoleType().equals(RoleType.ContainmentSpecialist)){
                containmentSpecialistEnterCity(destination);
            }

            if (currentGame.getGovernmentInterferenceActive()){
                currentGame.setGovernmentInterferenceSatisfied(false);
            }

            currentPlayer.incrementActionTaken();
            return 0;
        }
        else {
            return 1;
        }
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining, is Dispatcher, has selected another Player's pawn,
    // selected a destination city that neighbors that pawn's current city, and has received permission from the other Player
    public int playDispatcherDriveFerry(Pawn target, City destination){
	    return playDispatcherMovePawnToAnyCity(target, destination);
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining, is Dispatcher, has selected another Player's pawn,
    // selected a destination city for which they hold the CityCard in their hand, and has received permission from the other Player
    public int playDispatcherDirectFlight(Pawn target, CityCard card){
        Player currentPlayer = currentGame.getCurrentPlayer();
        City source = target.getLocation();
        City destination = getCityByName(card.getCityName());

        if (source.getCityUnits().contains(target)) {
            source.getCityUnits().remove(target);
            destination.getCityUnits().add(target);
            target.setLocation(destination);

            if(target.getRole().getRoleType() == RoleType.Medic) {
                medicEnterCity(destination);
            }
            if (target.getRole().getRoleType().equals(RoleType.ContainmentSpecialist)){
                containmentSpecialistEnterCity(destination);
            }

            if (currentGame.getGovernmentInterferenceActive()){
                currentGame.setGovernmentInterferenceSatisfied(false);
            }

            currentPlayer.incrementActionTaken();
            return discardPlayerCard(currentPlayer, card);
        }
        else {
            return 1;
        }
    }


    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining, is Dispatcher, has selected another Player's pawn,
    // has the CityCard for the city the pawn is currently in, has selected a destination city, and has received permission from the other
    // Player
    public int playDispatcherCharterFlight(Pawn target, CityCard card, City destination){
        Player currentPlayer = currentGame.getCurrentPlayer();
        City source = target.getLocation();

        if (source.getCityUnits().contains(target)) {
            source.getCityUnits().remove(target);
            destination.getCityUnits().add(target);
            target.setLocation(destination);

            if(target.getRole().getRoleType() == RoleType.Medic) {
                medicEnterCity(destination);
            }
            if (target.getRole().getRoleType().equals(RoleType.ContainmentSpecialist)){
                containmentSpecialistEnterCity(destination);
            }

            if (currentGame.getGovernmentInterferenceActive()){
                currentGame.setGovernmentInterferenceSatisfied(false);
            }

            currentPlayer.incrementActionTaken();
            return discardPlayerCard(currentPlayer, card);
        }
        else {
            return 1;
        }
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining, is Dispatcher, has selected another Player's
    // pawn that is currently in a city with a research station, has selected a destination city that has a research station, and has
    // received permission from the other Player
    public int playDispatcherShuttleFlight(Pawn target, City destination){
        return playDispatcherMovePawnToAnyCity(target, destination);
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining, is ContingencyPlanner, does not already have
    // an EventCard recovered, has viewed all EventCards in PlayerDiscardPile, and has selected one of these Event Cards
    public int playContingencyPlannerRecoverEventCard(EventCard card){
        Player currentPlayer = currentGame.getCurrentPlayer();
        ArrayList<PlayerCard> dPile = currentGame.getPlayerDiscardPile().getCardsInPile();
        if (dPile.contains(card)){
            dPile.remove(card);
            currentPlayer.incrementActionTaken();
            return currentGame.setContingencyPlannerEventCard(card);
        }
        else {
            return 1;
        }
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions and the Contingency Planner player has played their extra Event Card
    // Post: The Event Card that was played is removed from the game and the contingencyPlannerEventCard is null
    public int playContingencyPlannerEventCard(){
        EventCard playedCard = currentGame.getContingencyPlannerEventCard();
        if (playedCard != null){
            ArrayList<PlayerCard> dPile = currentGame.getPlayerDiscardPile().getCardsInPile();
            if (dPile.contains(playedCard)) {
                dPile.remove(playedCard);
                return currentGame.setContingencyPlannerEventCard(null);
            }
            else {
                return 1;
            }
        }
        else {
            return 1;
        }
    }

    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer is Archivist, currentPlayer has actions remaining, !archivistActionUsed,
    // the CityCard for the, City that the Archivist is currently in is in the PlayerDiscardPile, and the currentPlayer has selected to
    // recover this card
    public int playArchivistRecoverCard(){
	    Player currentPlayer = currentGame.getCurrentPlayer();
	    City currentCity = currentPlayer.getPawn().getLocation();
	    PlayerCard card = null;
	    for (PlayerCard c : currentGame.getPlayerDiscardPile().getCardsInPile()){
	        if (c.getCardName().equals(currentCity.getName().toString())){
	            card = c;
	            break;
            }
        }
        if (card != null){
	        currentGame.getPlayerDiscardPile().getCardsInPile().remove(card);
	        currentPlayer.addToHand(card);
	        checkHandSize(currentPlayer);
	        currentGame.setArchivistActionUsed(true);
	        currentPlayer.incrementActionTaken();
	        return 0;
        }
        else {
	        return 1;
        }
    }

    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer is Epidemiologist, currentPlayer has actions remaining,
    // !epidemiologistActionUsed, currentPlayer is in same City as owner, card is in owner's hand, and owner has given
    // permission
    public int playEpidemiologistTakeCard(Player owner, CityCard card){
        if (owner.isInHand(card)){
            owner.discardCard(card);
            getCurrentPlayer().addToHand(card);
            checkHandSize(getCurrentPlayer());
            currentGame.setEpidemiologistActionUsed(true);
            return 0;
        }
        else {
            return 1;
        }
    }

    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer is FieldOperative, currentPlayer has actions remaining,
    // !fieldOperativeActionUsed, currentPlayer has selected a DiseaseFlag from the city they are currently in
    public int playFieldOperativeCollectSample(DiseaseFlag sample){
        if (!currentGame.getFieldOperativeActionUsed()){
            currentGame.addSample(sample);
            currentGame.setFieldOperativeActionUsed(true);
            getCurrentPlayer().incrementActionTaken();
            return 0;
        }
        else {
            return 1;
        }
    }

    // Pre: CurrentPlayerTurnStatus is PlayingActions, FieldOperative Player has selected a disease flag sample to return to the supply
    public void playFieldOperativeReturnSample(DiseaseFlag sample){
	    currentGame.returnSampleToSupply(sample);
    }

    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer is FieldOperative, currentPlayer has actions remaining,
    // currentPlayer is in a City with a Research Station, currentPlayer has selected 3 cards of same colour from hand and 3
    // Disease Flag samples of this colour
    public int playFieldOperativeDiscoverCure(List<CityCard> cards, List<DiseaseFlag> samples){
        DiseaseType dType = getDiseaseTypeByRegion(cards.get(0).getRegion());
	    for (CityCard c : cards){
	        if (!getDiseaseTypeByRegion(c.getRegion()).equals(dType)){
	            return 1;
            }
            discardPlayerCard(getCurrentPlayer(), c);
        }
        for (DiseaseFlag f : samples){
	        if (!f.getDiseaseType().equals(dType)){
	            return 1;
            }
            currentGame.returnSampleToSupply(f);
        }

        Disease d = currentGame.getDiseaseByDiseaseType(dType);
        d.setCured(true);
        currentGame.checkIfEradicated(dType);
        getCurrentPlayer().incrementActionTaken();

        // Check if all diseases in game are cured:
        boolean allDiseasesCured;
        if (currentGame.getChallenge().equals(ChallengeKind.BioTerrorist) || currentGame.getChallenge().equals(ChallengeKind.Mutation)){
            allDiseasesCured = currentGame.getDiseaseByDiseaseType(DiseaseType.Black).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Blue).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Red).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Yellow).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Purple).isCured();
        }
        else {
            allDiseasesCured = currentGame.getDiseaseByDiseaseType(DiseaseType.Black).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Blue).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Red).isCured() && currentGame.getDiseaseByDiseaseType(DiseaseType.Yellow).isCured();
        }
        if (allDiseasesCured){
            currentGame.setGamePhase(GamePhase.Completed);
            notifyAllNonBTPlayersGameWon();
        }
        return 0;
    }

    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer is Troubleshooter, currentPlayer has actions remaining, currentPlayer
    // has CityCard for destination City in their hand
    public int playTroubleShooterDirectFlight(City destination){
        Player currentPlayer = currentGame.getCurrentPlayer();
        if (currentGame.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayingActions) {
            Pawn currentPlayerPawn = currentPlayer.getPawn();
            City currentPlayerCity = currentPlayerPawn.getLocation();
            currentPlayerCity.getCityUnits().remove(currentPlayerPawn);
            destination.getCityUnits().add(currentPlayerPawn);
            currentPlayerPawn.setLocation(destination);

            if (currentGame.getGovernmentInterferenceActive()){
                currentGame.setGovernmentInterferenceSatisfied(false);
            }

            currentPlayer.incrementActionTaken();
            return 0;
        } else {
            return 1;
        }
    }

    public int playColonelPlaceQuarantineMarker(/*MovingCard toDiscard,*/ City destination){
        Player currentPlayer = currentGame.getCurrentPlayer();
        Pawn playerPawn = currentPlayer.getPawn();
        Role playerRole = playerPawn.getRole();
        City currentCity = playerPawn.getLocation();

        if(playerRole.getRoleType() != RoleType.Colonel) {
            return 1;
        }

        return placeQuaratineMarker(destination, currentPlayer);

    }

    public void setCommercialTravelBanActive(boolean b){
	    currentGame.setCommercialTravelBanActive(b);
    }

    public void setCommercialTravelBanPlayedBy(Player p){
	    currentGame.setCommercialTravelBanPlayedBy(p);
    }

    public boolean getMobileHospitalActive(){
	    return currentGame.getMobileHospitalActive();
    }

    public void setMobileHospitalActive(boolean b){
	    currentGame.setMobileHospitalActive(b);
    }

    // Returns 0 if successful, 1 if failed
    // Pre: CurrentPlayerTurnStatus is PlayingActions, currentPlayer has actions remaining, played MobileHospitalEventCard earlier in this
    // turn, selected a neighboring city to drive/ferry to, and selected a Disease Flag to remove in that city if there are any. If the
    // destination city does not have any Disease Flags, then flag == null
    public int playDriveFerryMobileHospital(City destination, DiseaseFlag flag ){
        Player currentPlayer = currentGame.getCurrentPlayer();

        if (currentGame.getCurrentPlayerTurnStatus() == CurrentPlayerTurnStatus.PlayingActions) {
            Pawn currentPlayerPawn = currentPlayer.getPawn();
            Role currentPlayerRole = currentPlayerPawn.getRole();
            City currentPlayerCity = currentPlayerPawn.getLocation();

            if ((currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 5) || (!currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 4)) {
                currentPlayer.setActionsTaken(0);
                return 1;
            }

            if ((currentPlayerCity.getConnections().stream().filter(conn -> conn.getEnd1() == destination || conn.getEnd2() == destination)
                    .collect(Collectors.toList())).isEmpty()) {
                return 1;
            }

            currentPlayerCity.getCityUnits().remove(currentPlayerPawn);
            destination.getCityUnits().add(currentPlayerPawn);
            currentPlayerPawn.setLocation(destination);

            if (currentPlayerRole.getRoleType() == RoleType.Medic)
                medicEnterCity(destination);
            if (currentPlayer.getRoleType().equals(RoleType.ContainmentSpecialist)){
                containmentSpecialistEnterCity(destination);
            }

            if (flag != null) {
                DiseaseType diseaseType = flag.getDiseaseType();
                Disease disease = currentGame.getDiseaseByDiseaseType(diseaseType);
                boolean cured = disease.isCured();

                if ((currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 5) || (!currentPlayer.getRoleType().equals(RoleType.Generalist) && currentPlayer.getActionsTaken() == 4)) {
                    currentPlayer.setActionsTaken(0);
                    return 1;
                }

                currentGame.getDiseaseSupplyByDiseaseType(diseaseType).add(destination.removeOneDiseaseFlag(diseaseType));

                if (cured && currentGame.checkIfEradicated(diseaseType)) {
                    disease.setEradicated(true);
                }

                return 0;
            }
            currentPlayer.incrementActionTaken();
            return 0;

        } else {
            return 1;
        }
    }


    public void setSlipperySlopeActive(boolean b){
	    currentGame.setSlipperySlopeActive(b);
    }

    public DiseaseType getVirulentStrain(){
	    return currentGame.getVirulentStrain();
    }

    public void setVirulentStrain(){
	    currentGame.setVirulentStrain();
    }

    public boolean isVirulentStrainEradicated(){
	    return currentGame.checkIfEradicated(currentGame.getVirulentStrain());
    }

    public void setRateEffectActive(boolean b){
	    currentGame.setRateEffectActive(b);
    }

    public boolean infectionDiscardPileContainsVirulentStrain(){
	    for (InfectionCard card : currentGame.getInfectionDiscardPile().getCards()){
            if (card.getCardType().equals(CardType.CityInfectionCard)) {
                CityInfectionCard c = (CityInfectionCard)card;
                DiseaseType cityDiseaseType = regionToDiseaseTypeDict.get(c.getRegion());
                if (cityDiseaseType.equals(currentGame.getVirulentStrain())){
                    return true;
                }
            }
        }
        return false;
    }

    public void setVirulentStrainIsEradicated(boolean b){
	    if(currentGame.getVirulentStrain() != null) {
            Disease vStrain = currentGame.getDiseaseByDiseaseType(currentGame.getVirulentStrain());
            vStrain.setEradicated(false);
        }
    }

    public int infectVirulentStrainCitiesInInfectionDiscardPile(){
        for (InfectionCard card : currentGame.getInfectionDiscardPile().getCards()){
            if (card.getCardType().equals(CardType.CityInfectionCard)) {
                CityInfectionCard c = (CityInfectionCard)card;
                DiseaseType cityDiseaseType = regionToDiseaseTypeDict.get(c.getRegion());
                if (cityDiseaseType.equals(currentGame.getVirulentStrain())){
                    City city = currentGame.getCityByName(c.getCityName());
                    ArrayList<DiseaseFlag> diseaseFlags = currentGame.getDiseaseSupplyByDiseaseType(cityDiseaseType);
                    boolean gameStatus = (currentGame.getOutBreakMeterReading() < 8) && diseaseFlags.size() >= 1;
                    if (!gameStatus) {
                        //NOTIFY ALL PLAYERS LOST
                        currentGame.setGamePhase(GamePhase.Completed);
                        return 0;
                    }
                    Disease cityDisease = currentGame.getDiseaseByDiseaseType(cityDiseaseType);
                    boolean qsOrMedicPreventingInfectionInCity = currentGame.isQuarantineSpecialistInCity(city) || (currentGame.isMedicInCity(city) && cityDisease.isCured());
                    boolean qsPresentInNeighbor = false;

                    ArrayList<City> cityNeighbors = city.getNeighbors();
                    LinkedList<City> Q = new LinkedList<>();
                    Q.addLast(city);

                    for (City neighbor : cityNeighbors) {
                        qsPresentInNeighbor = currentGame.isQuarantineSpecialistInCity(neighbor);
                        if (qsPresentInNeighbor) break;
                    }

                    boolean infectStatus = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor;
                    if (!infectStatus) {
                        currentGame.infectAndResolveOutbreaksForHiddenPocket(cityDiseaseType, cityDisease, gameStatus, Q);
                    }
                }
            }
        }
        return 0;
    }

    public void infectUncountedPopulations(){
	    currentGame.infectUncountedPopulations();
    }

    public void resolveUnacceptableLoss(){
	    currentGame.resolveUnacceptableLoss();
    }

    public void setComplexMolecularStructureActive(boolean b){
	    currentGame.setComplexMolecularStructureActive(b);
    }

    public boolean getGovernmentInterferenceActive(){
	    return currentGame.getGovernmentInterferenceActive();
    }

    public void setGovernmentInterferenceActive(boolean b){
	    currentGame.setGovernmentInterferenceActive(b);
    }

    public void setGovernmentInterferenceSatisfied(boolean b){
	    currentGame.setGovernmentInterferenceSatisfied(b);
    }

    public void setChronicEffectActive(boolean b){
	    currentGame.setChronicEffectActive(b);
    }
}
