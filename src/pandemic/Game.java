package pandemic;

import javafx.util.Pair;
import shared.GameState;

import java.util.*;
import java.util.stream.Collectors;


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
    private HashMap<DiseaseType, ConnectionStatus> diseaseTypeToConnectionStatusDict;
    private ArrayList<Role> unusedRoles;
  	private ArrayList<Pawn> inGamePawns;
  	private Random randomRoleGenerator;
  	private Random randomPawnGenerator;


  	private InfectionDeck myInfectionDeck;
  	private InfectionDiscardPile myInfectionDiscardPile;
  	private PlayerDeck myPlayerDeck;
  	private PlayerDiscardPile myPlayerDiscardPile;

  	private LinkedList<Integer> infectionRateMeter;
  	private int currentInfectionRate;
  	private boolean lastInfectionMarkerReached;


  	private int outBreakMeterReading;

	private CurrentPlayerTurnStatus currentPlayerTurnStatus;
	private Player playerDiscardingCards;
	private boolean oneQuietNightActivated;
	private EventCard contingencyPlannerEventCard;
	private boolean eventCardsEnabled;
	private boolean commercialTravelBanActive;
	private Player commercialTravelBanPlayedBy;
	private boolean mobileHospitalActive;
    private ArrayList<ResearchStation> allResearchStations;
    private int infectionsRemaining;
    private boolean archivistActionUsed;
    private boolean epidemiologistActionUsed;

  	
  	public Game(GameSettings settings, GameManager gameManager) {
  	    //this.currentPlayer = currentPlayer;
  	    this.settings = settings;
  	    this.gameManager = gameManager;
  	    randomRoleGenerator = new Random();
  	    randomPawnGenerator = new Random();

  	    infectionRateMeter = new LinkedList<Integer>() {
            {
                add(2);
                add(2);
                add(2);
                add(3);
                add(3);
                add(4);
                add(4);
            }
        };

  	    lastInfectionMarkerReached = false;

  	     diseaseTypeToConnectionStatusDict = new HashMap<DiseaseType, ConnectionStatus>() {
            {
                put(DiseaseType.Blue, ConnectionStatus.BlueDiseaseOutbreak);
                put(DiseaseType.Black, ConnectionStatus.BlackDiseaseOutbreak);
                put(DiseaseType.Red, ConnectionStatus.RedDiseaseOutbreak);
                put(DiseaseType.Yellow, ConnectionStatus.YellowDiseaseOutbreak);
                put(DiseaseType.Purple, ConnectionStatus.PurpleDiseaseOutbreak);
            }
        };


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
        initializePlayerPawns();
        myGameBoard.printGameBoard();
        myInfectionDeck.printDeck();
        myInfectionDiscardPile.printPile();

        // TESTING myGameBoard.isEradicated(DiseaseType)
        System.out.println("TESTING myGameBoard.isEradicated......");
        System.out.println("    black disease isNotPresentOnBoard: " + myGameBoard.isNotPresentOnBoard(DiseaseType.Black));
        System.out.println("    blue disease isNotPresentOnBoard: " + myGameBoard.isNotPresentOnBoard(DiseaseType.Blue));
        System.out.println("    red disease isNotPresentOnBoard: " + myGameBoard.isNotPresentOnBoard(DiseaseType.Red));
        System.out.println("    yellow disease isNotPresentOnBoard: " + myGameBoard.isNotPresentOnBoard(DiseaseType.Yellow));


        currentPhase = GamePhase.ReadyToJoin;
        currentPlayerTurnStatus = CurrentPlayerTurnStatus.PlayingActions;
        eventCardsEnabled = true;
    }

    public boolean checkIfEradicated(DiseaseType d) {
  	    return myGameBoard.isNotPresentOnBoard(d) &&
                diseaseTypeToDiseaseDict.get(d).isCured();
    }

    private void infectInitialCities() {
  	    currentInfectionRate = infectionRateMeter.get(0);
  	    int numOfDiseaseFlags;
  	    for(int i = 1; i <= 9; i++) {
  	        CityInfectionCard card = (CityInfectionCard) myInfectionDeck.drawCard();
  	        CityName cardName = card.getCityName();
  	        DiseaseType cardDisease = gameManager.getDiseaseTypeByRegion(card.getRegion());
  	        ArrayList<DiseaseFlag> diseaseFlagsSupply = diseaseTypeToSupplyDict.get(cardDisease);
  	        City cardCity = myGameBoard.getCityByName(cardName);



  	        // FOR TESTING:
  	        System.out.println(".....infecting initial city name: " + cardName + ", card disease: " + cardDisease + "........");




  	        if(i <= 3) {
  	            numOfDiseaseFlags = 3;
            } else if(i <= 6) {
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
        myInfectionDeck.shuffleDeck();

        myPlayerDeck.addCard(new AirLiftEventCard(gameManager));
        myPlayerDeck.addCard(new ForecastEventCard(gameManager));
        myPlayerDeck.addCard(new GovernmentGrantEventCard(gameManager));
        myPlayerDeck.addCard(new OneQuietNightEventCard(gameManager));
        myPlayerDeck.addCard(new ResilientPopulationEventCard(gameManager));
        myPlayerDeck.shuffleDeck();
        myInfectionDeck.shuffleDeck();

        // Dealing cards to Players:




    }

    public void dealCardsAndShuffleInEpidemicCards() {

        CityCard cardAtl = (CityCard) myPlayerDeck.getDeck().stream()
                .filter(c -> c.getCardType() == CardType.CityCard && c.getCardName().equals("Atlanta"))
                .findAny().orElse(null);
        myPlayerDeck.getDeck().add(0, cardAtl);
        myPlayerDeck.printDeck();

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
            else if (gameManager.getActivePlayers().size() >= 4){
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

        myPlayerDeck.insertAndShuffleEpidemicCards(epidemicCards, 1);
        myPlayerDeck.printDeck();



        //myPlayerDeck.getDeck().addAll(0, epidemicCards);

        /*System.out.println("......PlayerDeck after Epidemic Cards inserted (size: " + myPlayerDeck.getDeckSize()+ ")....");
        myPlayerDeck.printDeck();*/

        // - shuffling infection deck
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // UNCOMMENT AFTER TESTING//////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

//        System.out.println("    Unused Roles: ");
//        System.out.println("        number of unused roles: " + unusedRoles.size());
//        unusedRoles.forEach(role -> System.out.println("            role type: " + role.getRoleType() +
//                                                       ", assigned: " + role.isAssigned()));
//
////        System.out.println("Used Roles (as Pawns): ");
//        System.out.println("        number of used roles: " + inGamePawns.size());
//        inGamePawns.forEach(pawn -> System.out.println("            (pawn) role type: " + pawn.getRole().getRoleType() +
//                                                       ", pawn location: " + pawn.getLocation().getName() +
//                                                       ", pawn assigned: " + pawn.isAssigned()));
//        System.out.println("Host Player pawn role: " + gameManager.getHostPlayer().getPawn().getRole().getRoleType());
        System.out.println("GAME UNITS PRINTED.");

    }

    private void initializeGameUnits() {

        //    - initialize ResearchStations
        unusedResearchStations = new ArrayList<ResearchStation>();
        allResearchStations = new ArrayList<ResearchStation>();
  	    for(int i = 0; i < 6; i++) {
  	        ResearchStation rs = new ResearchStation();
            allResearchStations.add(rs);
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


    }

    public void initializePlayerPawns(){
        unusedRoles = new ArrayList<Role>() {
            {
                if(settings.getChallenge().equals(ChallengeKind.OriginalBaseGame)){
                    add(new Role(RoleType.ContingencyPlanner));
                    add(new Role(RoleType.OperationsExpert));
                    add(new Role(RoleType.Dispatcher));
                    add(new Role(RoleType.QuarantineSpecialist));
                    add(new Role(RoleType.Researcher));
                    add(new Role(RoleType.Medic));
                    add(new Role(RoleType.Scientist));
                }
                else {
                    for (RoleType r : RoleType.values()) {
                        add(new Role(r));
                    }
                }
            }
        };

        City atlCity = myGameBoard.getCityByName(CityName.Atlanta);
        inGamePawns = new ArrayList<Pawn>();
        int numOfPlayers = settings.getNumOfPlayers();
        for(int k = 0; k < numOfPlayers; k++) {
            Pawn playerPawn = new Pawn(getRandomUnassignedRole());
            inGamePawns.add(playerPawn);
            playerPawn.setLocation(atlCity);
            atlCity.getCityUnits().add(playerPawn);
        }

        // - assigning role to all active players (all joined players)
        //*****************************************************************************************************
        // - MODIFICATION TO ACCOUNT FOR ALL ACTIVE PLAYERS DEPENDING ON ORDER OF initializeGame()
        //   AND consecutive joinGame() calls
        //*****************************************************************************************************

        /*for(Player p : gameManager.getActivePlayers()) {
            Pawn playerPawn = getRandomUnassignedPawn();
            p.setPawn(playerPawn);
            playerPawn.setPlayer(p);
            p.setRole(playerPawn.getRole());
            playerPawn.getRole().setAssigned(true);
            playerPawn.setAssigned(true);



            System.out.println("Role assigned: " + playerPawn.getRole().getRoleType());

        }*/
    }

    public Disease getDiseaseByDiseaseType(DiseaseType d) {
  	    return diseaseTypeToDiseaseDict.get(d);
    }

    public ArrayList<DiseaseFlag> getDiseaseSupplyByDiseaseType(DiseaseType d) {
  	    return diseaseTypeToSupplyDict.get(d);
    }

    public Pawn getRandomUnassignedPawn() {

        int count = 0;
  	    Pawn p = inGamePawns.get(count);
  	    while(p.isAssigned() && count < inGamePawns.size()){
            //System.out.println("getrand")
            p = inGamePawns.get(count);
            count++;
        }
        System.out.println("Unassigned pawn found: " + p.getRole().getRoleType());
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

    public boolean getEventCardsEnabled(){
  	    return eventCardsEnabled;
    }

  	public void setEventCardsEnabled(boolean b){
  	    eventCardsEnabled = b;
	}

	public int getInfectionRate(){
  	    return currentInfectionRate;
	}

	public void increaseInfectionRate(){

		if (!lastInfectionMarkerReached){
		    infectionRateMeter.addLast(infectionRateMeter.removeFirst());
			currentInfectionRate = infectionRateMeter.getFirst();
		}

		if(currentInfectionRate == 4 && !lastInfectionMarkerReached)
		    lastInfectionMarkerReached = true;
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

	public boolean isQuarantineSpecialistInCity(City c) {
  	    return myGameBoard.isQuarantineSpecialistInCity(c);
    }

    public boolean isMedicInCity(City c){
  	    return myGameBoard.isMedicInCity(c);
    }

    public int getOutBreakMeterReading() {
        return outBreakMeterReading;
    }

    public void incrementOutbreakMeter() {
  	    outBreakMeterReading++;
    }

    public void infectAndResolveOutbreaks(DiseaseType cityDiseaseType, Disease cityDisease,
                                          boolean gameStatus, LinkedList<City> Q) {
  	    while(gameStatus && !Q.isEmpty() ) {
  	        City c = Q.removeFirst();
  	        int numberOfDiseaseFlagsPlaced = c.getNumOfDiseaseFlagsPlaced(cityDiseaseType);
            ArrayList<DiseaseFlag> freshFlags = diseaseTypeToSupplyDict.get(cityDiseaseType);

  	        if(numberOfDiseaseFlagsPlaced == 3) {
  	            //OUTBREAK
                incrementOutbreakMeter();

                // FOR TESTING:
                System.out.println(c.getName() + " is outbreaking");
                System.out.println("Incrementing Outbreak Meter");



                ArrayList<City> neighbors = c.getNeighbors();

                for(City connCity : neighbors) {
                    if(freshFlags.size() >= 1) {
                        int dFlagCount = connCity.getNumOfDiseaseFlagsPlaced(cityDiseaseType);
                        ArrayList<Connection> connections = connCity.getConnections();
                        Connection conn = connections.stream()
                               .filter(cNeigh -> cNeigh.getEnd1().getName().equals(c.getName()) ||
                                        cNeigh.getEnd2().getName().equals(c.getName()))
                                .findAny().orElse(null);
//
//                        ConnectionStatus diseaseTypeConnectionStatus = diseaseTypeToConnectionStatusDict.get(cityDiseaseType);

                        boolean alreadyAffectedByOutbreak = false;
                        for(Connection connection : connections){
                            if(connection.getStatus() == diseaseTypeToConnectionStatusDict.get(cityDiseaseType)){
                                alreadyAffectedByOutbreak = true;
                                break;
                            }
                        }

                        if(conn != null) {
                            if(dFlagCount == 3
                                    && !alreadyAffectedByOutbreak) {
                                // Chain reaction outbreak is occurring.
                                    Q.addLast(connCity);
                                    conn.setConnectionStatus(cityDiseaseType);


                                    // FOR TESTING:
                                    System.out.println(connCity.getName() + " is also outbreaking.");


                            } else if(dFlagCount < 3
                                    && !alreadyAffectedByOutbreak) {

                                boolean qsOrMedicPreventingInfectionInCity = isQuarantineSpecialistInCity(connCity) || (isMedicInCity(connCity) && cityDisease.isCured());
                                boolean diseaseEradicated = checkIfEradicated(cityDiseaseType);
                                boolean qsPresentInNeighbor = false;
                                ArrayList<City> cityNeighbors = connCity.getNeighbors();
                                for(City a : cityNeighbors) {
                                    qsPresentInNeighbor = isQuarantineSpecialistInCity(a);
                                    if( qsPresentInNeighbor) break;
                                }
                                boolean infectionPrevented = qsOrMedicPreventingInfectionInCity || diseaseEradicated || qsPresentInNeighbor;

                                if(!infectionPrevented) {
                                    DiseaseFlag flag;
                                    try {
                                        flag = freshFlags.remove(0);
                                    } catch (NullPointerException e) {
                                        //FOR TESTING, SHOULD NOT HAPPEN
                                        System.out.println("ERROR -- diseaseFlags not sufficient");
                                        return;
                                    }


                                    // FOR TESTING:
                                    System.out.println("Outbreak spread to " + connCity.getName());
                                    System.out.println("Number of disease cubes in city before outbreak:");
                                    for (DiseaseType d : DiseaseType.values()) {
                                        System.out.println("    " + d + ": " + connCity.getNumOfDiseaseFlagsPlaced(d));
                                    }


                                    connCity.getCityUnits().add(flag);
                                    flag.setUsed(true);


                                    // FOR TESTING:
                                    System.out.println("Number of disease cubes in city after outbreak:");
                                    for (DiseaseType d : DiseaseType.values()) {
                                        System.out.println("    " + d + ": " + connCity.getNumOfDiseaseFlagsPlaced(d));
                                    }


                                }




                                // FOR TESTING:
                                else {
                                    System.out.println("Outbreak prevented from spreading to " + connCity.getName());
                                }




                            }
                        }
                    } else {
                        gameManager.notifyAllPlayersGameLost();
                        setGamePhase(GamePhase.Completed);
                        System.out.println("Ran out of disease cubes.");
                        break;
                    }
                }

            } else {
                boolean qsOrMedicPreventingInfectionInCity = isQuarantineSpecialistInCity(c) || (isMedicInCity(c) && cityDisease.isCured());
                boolean diseaseEradicated = checkIfEradicated(cityDiseaseType);
                boolean qsPresentInNeighbor = false;
                ArrayList<City> cityNeighbors = c.getNeighbors();
                for(City a : cityNeighbors) {
                    qsPresentInNeighbor = isQuarantineSpecialistInCity(a);
                    if( qsPresentInNeighbor) break;
                }
                boolean infectionPrevented = qsOrMedicPreventingInfectionInCity || diseaseEradicated || qsPresentInNeighbor;

                if(!infectionPrevented && freshFlags.size() >= 1) {
                    DiseaseFlag flag;
                    try {
                        flag = freshFlags.remove(0);
                    } catch (NullPointerException e) {
                        //FOR TESTING, SHOULD NOT HAPPEN
                        System.out.println("ERROR -- diseaseFlags not sufficient");
                        return;
                    }


                    // FOR TESTING:
                    System.out.println("Infecting " + c.getName());
                    System.out.println("Number of disease cubes in city before infecting:");
                    for (DiseaseType d : DiseaseType.values()) {
                        System.out.println("    " + d + ": " + c.getNumOfDiseaseFlagsPlaced(d));
                    }


                    c.getCityUnits().add(flag);
                    flag.setUsed(true);


                    // FOR TESTING:
                    System.out.println("Number of disease cubes in city after infecting:");
                    for (DiseaseType d : DiseaseType.values()) {
                        System.out.println("    " + d + ": " + c.getNumOfDiseaseFlagsPlaced(d));
                    }


                }
                else{
                    gameManager.notifyAllPlayersGameLost();
                    setGamePhase(GamePhase.Completed);
                    System.out.println("Ran out of disease cubes.");
                }
            }
            gameStatus = (getOutBreakMeterReading() < 8) && (freshFlags.size() >= 1);
            if (!gameStatus){
                gameManager.notifyAllPlayersGameLost();
                setGamePhase(GamePhase.Completed);
                System.out.println("Ran out of disease cubes or Outbreak meter maxed out");
            }
        }

    }

    // TO TEST
	public void setCurrentPlayer(Player p){
  	    currentPlayer = p;
	}

	//the ultimate functional programming test
	public GameState generateCondensedGameState() {
        final Map<RoleType, String> userMap
                = gameManager.getActivePlayers().stream().collect(Collectors.toMap(Player::getRoleType, Player::getPlayerUserName));

        final Map<RoleType, List<PlayerCard>> cardMap
                = gameManager.getActivePlayers().stream().collect(Collectors.toMap(Player::getRoleType, Player::getCardsInHand));

//        final Map<RoleType, CityName> positionMap
//                = gameManager.getActivePlayers().stream().collect(Collectors.toMap(Player::getRoleType, p -> p.getPawn().getLocation().getName()));

        final Map<RoleType, City> positionMap
                = gameManager.getActivePlayers().stream().collect(Collectors.toMap(Player::getRoleType, p -> myGameBoard.getCityByName(p.getPawn().getLocation().getName())));


        final Map<CityName, List<Pair<DiseaseType,Integer>>> diseaseCubesMap
                = myGameBoard.getCitiesOnBoard().stream().collect(Collectors.toMap(City::getName, City::getDiseaseFlags));
        System.out.println("GAMESTATE DISEASE CUBES: " + diseaseCubesMap);

        final Map<DiseaseType, Integer> remainingDiseaseCubesMap
                = Arrays.stream(DiseaseType.values()).filter(d -> d != DiseaseType.Purple)
                                                     .collect(Collectors.toMap(d -> d,
                                                                               d -> getDiseaseSupplyByDiseaseType(d).size()));

        int actionsRemaining;
        if (currentPlayer.getRoleType().equals(RoleType.Generalist)) {
            actionsRemaining = 5 - currentPlayer.getActionsTaken();
        } else {
            actionsRemaining = 4 - currentPlayer.getActionsTaken();
        }

        ArrayList<DiseaseType> curedDiseases = new ArrayList<DiseaseType>();
        if(blueDisease.isCured()){
            curedDiseases.add(blueDisease.getDiseaseType());
        }
        if(redDisease.isCured()){
            curedDiseases.add(redDisease.getDiseaseType());
        }
        if(yellowDisease.isCured()){
            curedDiseases.add(yellowDisease.getDiseaseType());
        }
        if(blackDisease.isCured()){
            curedDiseases.add(blackDisease.getDiseaseType());
        }
//        if(purpleDisease.isCured()){
//            curedDiseases.add(purpleDisease.getDiseaseType());
//        }
        final ArrayList<City> researchStationLocations = new ArrayList<City>();
        for (ResearchStation rs : allResearchStations){
            if (rs.getLocation() != null){
                researchStationLocations.add(rs.getLocation());
            }
        }

        return new GameState(userMap, cardMap, positionMap, diseaseCubesMap, remainingDiseaseCubesMap,
                myInfectionDiscardPile, myPlayerDiscardPile, currentInfectionRate, outBreakMeterReading, actionsRemaining, curedDiseases, currentPlayer.getPlayerUserName(), researchStationLocations, eventCardsEnabled, currentPlayerTurnStatus, archivistActionUsed, epidemiologistActionUsed);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public GamePhase getGamePhase() {
  	    return currentPhase;
    }

    public ChallengeKind getChallenge(){
  	    return settings.getChallenge();
    }

    public ResearchStation getUnusedResearchStation() {
  	    if (!unusedResearchStations.isEmpty()){
  	        return unusedResearchStations.get(0);
        }
        else {
  	        return null;
        }
    }

    public void addUnusedResearchStation(ResearchStation rs){
  	    unusedResearchStations.add(rs);
    }

    public EventCard getContingencyPlannerEventCard(){
  	    return contingencyPlannerEventCard;
    }

    // Returns 0 if successful, 1 if failed
    public int setContingencyPlannerEventCard(EventCard card){
  	    if (contingencyPlannerEventCard == null){
  	        contingencyPlannerEventCard = card;
  	        return 0;
        }
        else if (card == null) {
            contingencyPlannerEventCard = null;
            return 0;
        }
        else {
  	        return 1;
        }
    }

    public boolean getCommercialTravelBanActive(){
  	    return commercialTravelBanActive;
    }

    public Player getCommercialTravelBanPlayedBy(){
  	    return commercialTravelBanPlayedBy;
    }

    public void setCommercialTravelBanActive(boolean b){
  	    commercialTravelBanActive = b;
    }

    public void setCommercialTravelBanPlayedBy(Player p){
  	    commercialTravelBanPlayedBy = p;
    }

    public boolean getMobileHospitalActive(){
  	    return mobileHospitalActive;
    }

    public void setMobileHospitalActive(boolean b){
  	    mobileHospitalActive = b;
    }

    public int getInfectionsRemaining(){
  	    return infectionsRemaining;
    }

    public void setInfectionsRemaining(int i){
  	    infectionsRemaining = i;
    }

    public void decrementInfectionsRemaining(){
  	    infectionsRemaining = infectionsRemaining - 1;
    }

    public boolean getArchivistActionUsed(){
  	    return archivistActionUsed;
    }

    public void setArchivistActionUsed(boolean b){
  	    archivistActionUsed = b;
    }

    public boolean getEpidemiologistActionUsed(){
  	    return epidemiologistActionUsed;
    }

    public void setEpidemiologistActionUsed(boolean b){
  	    epidemiologistActionUsed = b;
    }
}
