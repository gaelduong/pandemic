package pandemic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Game {

    private GameManager gameManager;
  	private GameBoard myGameBoard;
  	private GamePhase currentPhase;
  	private GameSettings settings;
  	private Player currentPlayer;

  	private ArrayList<ResearchStation> unusedResearchStations;
  	private Disease blueDisease;
  	private Disease blackDisease;
  	private Disease redDisease;
  	private Disease yellowDisease;
  	private Disease purpleDisease;
  	private ArrayList<DiseaseFlag> blueUnusedDiseaseFlags;
    private ArrayList<DiseaseFlag> blackUnusedDiseaseFlags;
    private ArrayList<DiseaseFlag> redUnusedDiseaseFlags;
    private ArrayList<DiseaseFlag> yellowUnusedDiseaseFlags;
    private ArrayList<DiseaseFlag> purpleUnusedDiseaseFlags;
    private HashMap<DiseaseType, ArrayList<DiseaseFlag>> diseaseTypeToSupplyDict;
    private HashMap<DiseaseType, Disease> diseaseTypeToDiseaseDict;
    private ArrayList<Role> unusedRoles;
  	private ArrayList<Pawn> inGamePawns;
  	private Random randomRoleGenerator;
  	private Random randomPawnGenerator;

  	private InfectionDeck myInfectionDeck;
  	private InfectionDiscardPile myInfectionDiscardPile;
  	private PlayerDeck myPlayerDeck;
  	private PlayerDiscardPile myPlayerDiscardPile;

  	private boolean resolvingEpidemic;
  	private int infectionRate;

	private CurrentPlayerTurnStatus currentPlayerTurnStatus;
	private ConsentRequiringAction currentConsentRequiringAction;
	private Player playerDiscardingCards;
	private boolean oneQuietNightActivated;
  	
  	public Game(Player currentPlayer, GameSettings settings, GameManager gameManager) {
  	    this.currentPlayer = currentPlayer;
  	    this.settings = settings;
  	    this.gameManager = gameManager;
  	    randomRoleGenerator = new Random();
  	    randomPawnGenerator = new Random();
	}

	public void initializeGame() {
        //    - initialize myGameBoard
        myGameBoard = new GameBoard(initializeGameBoard());
        myGameBoard.printGameBoard();

        //    - initialize ResearchStations
        //    - initialize Diseases & DiseaseFlags
        //    - initialize PlayerPawns
        initializeGameUnits();
        System.out.println("Printing GameBoard after game unit initialization....");
        myGameBoard.printGameBoard();
        printGameUnits();

        //    - initialize PlayerDeck, PlayerDiscardPile, InfectionDeck, InfectionDiscardPile
        initializeCardDecks();
        printDecksAndDiscardPiles();

        //    - infecting initial cities
        infectInitialCities();
        myGameBoard.printGameBoard();
        myInfectionDeck.printDeck();
        myInfectionDiscardPile.printPile();

        // TESTING myGameBoard.isEradicated(DiseaseType)
        System.out.println("TESTING myGameBoard.isEradicated......");
        System.out.println("    black disease isEradicated: " + myGameBoard.isEradicated(DiseaseType.Black));
        System.out.println("    blue disease isEradicated: " + myGameBoard.isEradicated(DiseaseType.Blue));
        System.out.println("    red disease isEradicated: " + myGameBoard.isEradicated(DiseaseType.Red));
        System.out.println("    yellow disease isEradicated: " + myGameBoard.isEradicated(DiseaseType.Yellow));


        currentPhase = GamePhase.ReadyToJoin;
    }

    public boolean checkIfEradicated(DiseaseType d) {
  	    return myGameBoard.isEradicated(d);
    }

    private void infectInitialCities() {
  	    infectionRate = 2;
  	    int numOfDiseaseFlags = 0;
  	    for(int i = 1; i <= 9; i++) {
  	        CityInfectionCard card = (CityInfectionCard) myInfectionDeck.drawCard();
  	        CityName cardName = card.getCityName();
  	        DiseaseType cardDisease = gameManager.getDiseaseTypeByRegion(card.getRegion());
  	        ArrayList<DiseaseFlag> diseaseFlagsSupply = diseaseTypeToSupplyDict.get(cardDisease);
  	        City cardCity = myGameBoard.getCityByName(cardName);
            //System.out.println(".....infecting initial city name: " + cardName + ", card disease: " + cardDisease + "........");

  	        if(i >= 1 && i <= 3) {
  	            numOfDiseaseFlags = 3;
            } else if(i > 3 && i <= 6) {
  	            numOfDiseaseFlags = 2;
            } else {
  	            numOfDiseaseFlags = 1;
            }

            for(int j = 0; j < numOfDiseaseFlags; j++) {
  	            DiseaseFlag flag = diseaseFlagsSupply.remove(0);
  	            cardCity.getCityUnits().add(flag);
  	            flag.setLocation(cardCity);
  	            flag.setUsed(true);
            }
            myInfectionDiscardPile.addCard(card);

        }
    }

    private void printDecksAndDiscardPiles() {
  	    myPlayerDeck.printDeck();
  	    myInfectionDeck.printDeck();
  	    myPlayerDiscardPile.printPile();
  	    myInfectionDiscardPile.printPile();
    }

    private void initializeCardDecks() {
  	    myPlayerDeck = new PlayerDeck();
  	    myInfectionDeck = new InfectionDeck();
  	    myPlayerDiscardPile = new PlayerDiscardPile();
  	    myInfectionDiscardPile = new InfectionDiscardPile();

  	    // - populating PlayerDeck and InfectionDeck with CityCards and CityInfectionCards respectively
        for(CityName cName : CityName.values()) {
            Region cRegion = gameManager.getRegionByCityName(cName);
            myPlayerDeck.addCard(new CityCard(cName, cRegion));
            myInfectionDeck.addCard(new CityInfectionCard(cName, cRegion));
        }

        myPlayerDeck.addCard(new AirLiftEventCard(gameManager));
        myPlayerDeck.addCard(new ForecastEventCard(gameManager));
        myPlayerDeck.addCard(new GovernmentGrantEventCard(gameManager));
        myPlayerDeck.addCard(new OneQuietNightEventCard(gameManager));
        myPlayerDeck.addCard(new ResilientPopulationEventCard(gameManager));
        myPlayerDeck.shuffleDeck();

        // Dealing cards to Players:
        for(Player p : gameManager.getActivePlayers()){
            if (gameManager.getActivePlayers().size() == 2){
                // Each player begins with 4 cards
                for(int i = 0; i < 4; i++){
                    p.addToHand(myPlayerDeck.drawCard());
                }
            }
            else if (gameManager.getActivePlayers().size() == 3){
                // Each player begins with 3 cards
                for(int i = 0; i < 3; i++){
                    p.addToHand(myPlayerDeck.drawCard());
                }
            }
            else if (gameManager.getActivePlayers().size() == 4){
                // Each player begins with 2 cards
                for(int i = 0; i < 2; i++){
                    p.addToHand(myPlayerDeck.drawCard());
                }
            }
        }
        
        // - shuffling in Epidemic cards based on settings
        List<PlayerCard> epidemicCards = new ArrayList<PlayerCard>();
        int numOfEpidemicCards = settings.getNumOfEpidemicCards();
        for(int i = 0; i < numOfEpidemicCards; i++) {
            epidemicCards.add(new BasicEpidemicCard(gameManager));
        }

        /*System.out.println("......PlayerDeck before Epidemic Cards inserted (size: " + myPlayerDeck.getDeckSize()+ ")....");
        myPlayerDeck.printDeck();*/
        myPlayerDeck.insertAndShuffleEpidemicCards(epidemicCards);
        /*System.out.println("......PlayerDeck after Epidemic Cards inserted (size: " + myPlayerDeck.getDeckSize()+ ")....");
        myPlayerDeck.printDeck();*/

        // - shuffling infection deck
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // UNCOMMENT AFTER TESTING//////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //myInfectionDeck.shuffleDeck();



    }

    public void printGameUnits() {
        System.out.println("PRINTING GAME UNITS.....");
        System.out.println("    Disease Flag supplies:");
        for(DiseaseType d : DiseaseType.values()) {
            if (d != DiseaseType.Purple) {
                System.out.println("        disease d:" + d);
                System.out.println("            number of unused disease flags: " + diseaseTypeToSupplyDict.get(d).size());
            }
        }
        System.out.println("    Research Stations supply: ");
        System.out.println("        number of unused research stations: " + unusedResearchStations.size());

        System.out.println("    Unused Roles: ");
        System.out.println("        number of unused roles: " + unusedRoles.size());
        unusedRoles.forEach(role -> System.out.println("            role type: " + role.getRoleType() +
                                                       ", assigned: " + role.isAssigned()));

        System.out.println("Used Roles (as Pawns): ");
        System.out.println("        number of used roles: " + inGamePawns.size());
        inGamePawns.forEach(pawn -> System.out.println("            (pawn) role type: " + pawn.getRole().getRoleType() +
                                                       ", pawn location: " + pawn.getLocation().getName() +
                                                       ", pawn assigned: " + pawn.isAssigned()));
        System.out.println("Host Player pawn role: " + gameManager.getHostPlayer().getPawn().getRole().getRoleType());
        System.out.println("GAME UNITS PRINTED.");

    }

    private void initializeGameUnits() {

        //    - initialize ResearchStations
        unusedResearchStations = new ArrayList<ResearchStation>();
  	    for(int i = 0; i < 6; i++) {
  	        ResearchStation rs = new ResearchStation();
  	        unusedResearchStations.add(rs);
        }
        //    - placing research station in Atlanta
        ResearchStation atlResearchStation = unusedResearchStations.remove(0);
  	    City atlCity = myGameBoard.getCityByName(CityName.Atlanta);
  	    atlResearchStation.setLocation(atlCity);
  	    atlCity.getCityUnits().add(atlResearchStation);

        //    - initialize Diseases & DiseaseFlags
        blueDisease = new Disease(DiseaseType.Blue);
        blackDisease = new Disease(DiseaseType.Black);
        redDisease = new Disease(DiseaseType.Red);
        yellowDisease = new Disease(DiseaseType.Yellow);

        blueUnusedDiseaseFlags = new ArrayList<DiseaseFlag>();
        blackUnusedDiseaseFlags = new ArrayList<DiseaseFlag>();
        redUnusedDiseaseFlags = new ArrayList<DiseaseFlag>();
        yellowUnusedDiseaseFlags = new ArrayList<DiseaseFlag>();

        diseaseTypeToSupplyDict = new HashMap<DiseaseType, ArrayList<DiseaseFlag>>() {
            {
                put(blueDisease.getDiseaseType(), blueUnusedDiseaseFlags);
                put(blackDisease.getDiseaseType(), blackUnusedDiseaseFlags);
                put(redDisease.getDiseaseType(), redUnusedDiseaseFlags);
                put(yellowDisease.getDiseaseType(), yellowUnusedDiseaseFlags);
            }
        };

        diseaseTypeToDiseaseDict = new HashMap<DiseaseType, Disease>() {
            {
                put(DiseaseType.Blue, blueDisease);
                put(DiseaseType.Black, blackDisease);
                put(DiseaseType.Red, redDisease);
                put(DiseaseType.Yellow, yellowDisease);
            }
        };

        for(DiseaseType d : DiseaseType.values()) {
            if(d != DiseaseType.Purple) {
                ArrayList<DiseaseFlag> diseaseFlagsSupply = diseaseTypeToSupplyDict.get(d);
                for(int j = 0; j < 24; j++) {
                    diseaseFlagsSupply.add(new DiseaseFlag(d));
                }
            }
        }

        //    - initialize PlayerPawns
        unusedRoles = new ArrayList<Role>() {
            {
                for(RoleType r : RoleType.values()) {
                    add(new Role(r));
                }
            }
        };

        inGamePawns = new ArrayList<Pawn>();
        int numOfPlayers = settings.getNumOfPlayers();

        /////////// FOR TESTING ://////////////////////////////////////////////////////////////////////////////////////
        Role r = unusedRoles.stream().filter(role -> role.getRoleType() == RoleType.Medic).findAny().orElse(null);
        Pawn medic = new Pawn(r);
        inGamePawns.add(medic);
        unusedRoles.remove(r);
        medic.setLocation(atlCity);
        atlCity.getCityUnits().add(medic);
        //////////////////////////////////////////////////
        // also change k back to 0 after testing
        //////////////////////////////////////////////////
        for(int k = 1; k < numOfPlayers; k++) {
            Pawn playerPawn = new Pawn(getRandomUnassignedRole());
            inGamePawns.add(playerPawn);
            playerPawn.setLocation(atlCity);
            atlCity.getCityUnits().add(playerPawn);
        }

        // - assigning role to hostPlayer
        //*****************************************************************************************************
        // -MIGHT REQUIRE MODIFICATION TO ACCOUNT FOR ALL ACTIVE PLAYERS DEPENDING ON ORDER OF initializeGame()
        //   AND consecutive joinGame() calls
        //*****************************************************************************************************
        Player host = gameManager.getHostPlayer();

        // UNCOMMENT AFTER TESTING:
        //Pawn pawnHost = getRandomUnassignedPawn();

        //FOR TESTING drive/ferry, directflight, treat disease as medic:
        Pawn pawnHost = inGamePawns.stream().filter(pawn -> pawn.getRole().getRoleType() == RoleType.Medic)
                                            .findAny().orElse(null);

        host.setPawn(pawnHost);
        pawnHost.setPlayer(host);
        pawnHost.getRole().setAssigned(true);
        pawnHost.setAssigned(true);



    }

    public Disease getDiseaseByDiseaseType(DiseaseType d) {
  	    return diseaseTypeToDiseaseDict.get(d);
    }

    public ArrayList<DiseaseFlag> getDiseaseSupplyByDiseaseType(DiseaseType d) {
  	    return diseaseTypeToSupplyDict.get(d);
    }

    private Pawn getRandomUnassignedPawn() {
  	    Pawn p = inGamePawns.get(randomPawnGenerator.nextInt(inGamePawns.size()));
  	    while(p.isAssigned()){
            p = inGamePawns.get(randomPawnGenerator.nextInt(inGamePawns.size()));
        }
  	    return p;
    }

    private Role getRandomUnassignedRole() {
  	    int index = randomRoleGenerator.nextInt(unusedRoles.size());
  	    return unusedRoles.remove(index);
    }

    private void checkAndCreateNeighbors(ArrayList<City> createdCities, City c) {

        City createdCityNeighbor = null;
        ArrayList<CityName> cNeighborNames = gameManager.getCityNeighborNames(c.getName());

        for(CityName nName : cNeighborNames) {
            //System.out.println("        neighbor name: " + nName);
            try {
                createdCityNeighbor = createdCities.stream().filter(n -> (n.getName() == nName)).findAny().orElse(null);
            } catch(NullPointerException e) {

            }

            if(createdCityNeighbor == null) {
                //System.out.println("            neighbor not created yet!");
                City cNeighbor = new City(nName, gameManager.getRegionByCityName(nName));
                Connection cConnection = new Connection(c, cNeighbor);
                c.getNeighbors().add(cNeighbor);
                c.getConnections().add(cConnection);
                cNeighbor.getConnections().add(cConnection);
                cNeighbor.getNeighbors().add(c);
                createdCities.add(cNeighbor);
            } else {
                //System.out.println("            neighbor found!");
                //check if neighbor already added to city neighbors list
                CityName createdCityNeighborName = createdCityNeighbor.getName();
                City createdCityNeighborInCList = c.getNeighbors().stream()
                                                                  .filter(nL -> (nL.getName() == createdCityNeighborName))
                                                                  .findAny().orElse(null);
                City createdCityNeighborInNList = createdCityNeighbor.getNeighbors().stream()
                        .filter(nL -> (nL.getName() == c.getName()))
                        .findAny().orElse(null);

                Connection cConnection = new Connection(c, createdCityNeighbor);
                if(createdCityNeighborInCList == null) {
                    c.getNeighbors().add(createdCityNeighbor);
                    c.getConnections().add(cConnection);
                }

                if(createdCityNeighborInNList == null) {
                    createdCityNeighbor.getNeighbors().add(c);
                    createdCityNeighbor.getConnections().add(cConnection);
                }

            }
        }
    }


    private ArrayList<City> initializeGameBoard() {
        System.out.println("...initializing game board....");
        ArrayList<City> createdCities = new ArrayList<City>();
        ArrayList<CityName> citiesNamesInRegion;

        for(Region r : Region.values()) {

            citiesNamesInRegion = gameManager.getCityNamesByRegion(r);
            City createdCity = null;

            for(CityName name : citiesNamesInRegion) {

                try {
                    createdCity = createdCities.stream().filter(c -> (c.getName() == name)).findAny().orElse(null);
                } catch(NullPointerException e) {

                }

                if(createdCity == null) {
                    //System.out.println("Creating city " + name + ".......");
                    City c = new City(name, r);
                    createdCities.add(c);


                    //System.out.println("    Creating neighbors for city....");
                    checkAndCreateNeighbors(createdCities, c);

                } else {
                    //System.out.println("Found city " + name + ".......");

                    //System.out.println("    Checking/Creating neighbors for city....");
                    checkAndCreateNeighbors(createdCities, createdCity);
                }
            }
        }
        System.out.println("....game board initialized.");
        return createdCities;
    }

  	public void setResolvingEpidemic(boolean b){
  	    resolvingEpidemic = b;
	}

	public int getInfectionRate(){
  	    return infectionRate;
	}

	public void increaseInfectionRate(){
		if (infectionRate < 4){
			infectionRate++;
		}
	}

	public InfectionDeck getInfectionDeck(){
  	    return myInfectionDeck;
	}

	public InfectionDiscardPile getInfectionDiscardPile() {
  	    return myInfectionDiscardPile;
	}

	public City getCityByName(CityName cn){
  	    return myGameBoard.getCityByName(cn);
	}

	public void setGamePhase(GamePhase phase){
  	    currentPhase = phase;
	}

	public Player getCurrentPlayer(){
  	    return currentPlayer;
	}

	public PlayerDeck getPlayerDeck(){
  	    return myPlayerDeck;
	}

	public void printGameBoard() {
  	    myGameBoard.printGameBoard();
    }
	
	public CurrentPlayerTurnStatus getCurrentPlayerTurnStatus(){
		return currentPlayerTurnStatus;
	}
	
	public void setCurrentPlayerTurnStatus(CurrentPlayerTurnStatus status){
  	    currentPlayerTurnStatus = status;
	}
	
	public ConsentRequiringAction getCurrentConsentRequiringAction(){
  	    return currentConsentRequiringAction;
	}
	
	public void setCurrentConsentRequiringAction(ConsentRequiringAction action){
  	    currentConsentRequiringAction = action;
	}
	
	public PlayerDiscardPile getPlayerDiscardPile(){
  	    return myPlayerDiscardPile;
	}
	
	public Player getPlayerDiscardingCards(){
  	    return playerDiscardingCards;
	}
	
	public void setPlayerDiscardingCards(Player p){
  	    playerDiscardingCards = p;
	}
	
	public boolean getOneQuietNight(){
  	    return oneQuietNightActivated;
	}
	
	public void setOneQuietNight(boolean b){
  	    oneQuietNightActivated = b;
	}

	public boolean isQuarantineSpecialistOrMedicInCity(City c) {
  	    return myGameBoard.isQuarantineSpecialistOrMedicInCity(c);
    }

	// TO TEST
	public void setCurrentPlayer(Player p){
		currentPlayer = p;
	}
}
