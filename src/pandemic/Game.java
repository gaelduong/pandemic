package pandemic;

import javafx.util.Pair;
import pandemic.eventcards.*;
import pandemic.eventcards.impl.*;
import shared.GameState;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class Game implements Serializable {

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
    private GameCardRemover myGameCardRemover;

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
    private boolean fieldOperativeActionUsed;
    private ArrayList<DiseaseFlag> fieldOperativeSamples;
    private boolean slipperySlopeActive;
    private DiseaseType virulentStrain;
    private boolean rateEffectActive;
    private boolean rateEffectAffectedInfection;
    private boolean complexMolecularStructureActive;
    private boolean governmentInterferenceActive;
    private boolean governmentInterferenceSatisfied;
    private boolean chronicEffectActive;
    private boolean chronicEffectInfection;

    private boolean bioTChallengeActive;
    private boolean bioTSpotted;

    private ArrayList<QuarantineMarker> unusedQuarantineMarkers;
    private ArrayList<QuarantineMarker> allQuarantineMarkers;
    private boolean quarantineMarkersInPlay;

    public boolean isLoadedFlag() {
        return loadedFlag;
    }

    public void setLoadedFlag(boolean loadedFlag) {
        this.loadedFlag = loadedFlag;
    }

    private boolean loadedFlag = false;

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

        if(settings.getChallenge() == ChallengeKind.BioTerrorist
                || settings.getChallenge() == ChallengeKind.VirulentStrainAndBioTerrorist) {
            bioTChallengeActive = true;
            bioTSpotted = false;
        }

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
        for (int i = 1; i <= 9; i++) {
            CityInfectionCard card = (CityInfectionCard) myInfectionDeck.drawCard();
            CityName cardName = card.getCityName();
            DiseaseType cardDisease = gameManager.getDiseaseTypeByRegion(card.getRegion());
            ArrayList<DiseaseFlag> diseaseFlagsSupply = diseaseTypeToSupplyDict.get(cardDisease);
            City cardCity = myGameBoard.getCityByName(cardName);


            // FOR TESTING:
            System.out.println(".....infecting initial city name: " + cardName + ", card disease: " + cardDisease + "........");


            if (i <= 3) {
                numOfDiseaseFlags = 3;
            } else if (i <= 6) {
                numOfDiseaseFlags = 2;
            } else {
                numOfDiseaseFlags = 1;
            }

            for (int j = 0; j < numOfDiseaseFlags; j++) {
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
        myGameCardRemover = new GameCardRemover(this);

        // - populating PlayerDeck and InfectionDeck with CityCards and CityInfectionCards respectively
        for (CityName cName : CityName.values()) {
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

        if(!bioTChallengeActive)
            myPlayerDeck.addCard(new LocalInitiativeEventCard(gameManager));

        myPlayerDeck.shuffleDeck();
        myInfectionDeck.shuffleDeck();

        if (getChallenge() == ChallengeKind.Mutation || getChallenge() == ChallengeKind.VirulentStrainAndMutation){
            myInfectionDiscardPile.addCard(new MutationCard(gameManager));
            myInfectionDiscardPile.addCard(new MutationCard(gameManager));
        }

        // Dealing cards to Players:




    }

    public void dealCardsAndShuffleInEpidemicCards() {

        // REMOVE AFTER TESTING: FORCES A PLAYER TO START WITH ATLANTA CARD
        CityCard cardAtl = (CityCard) myPlayerDeck.getDeck().stream()
                .filter(c -> c.getCardType() == CardType.CityCard && c.getCardName().equals("Atlanta"))
                .findAny().orElse(null);
        myPlayerDeck.getDeck().add(0, cardAtl);
        myPlayerDeck.printDeck();

        for (Player p : gameManager.getActivePlayers()) {
            if (gameManager.getActivePlayers().size() == 2) {
                // Each player begins with 4 cards
                for (int i = 0; i < 4; i++) {
                    p.addToHand(myPlayerDeck.drawCard());
                }
            } else if (gameManager.getActivePlayers().size() == 3) {
                // Each player begins with 3 cards
                for (int i = 0; i < 3; i++) {
                    p.addToHand(myPlayerDeck.drawCard());
                }
            } else if (gameManager.getActivePlayers().size() >= 4) {
                // Each player begins with 2 cards
                for (int i = 0; i < 2; i++) {
                    p.addToHand(myPlayerDeck.drawCard());
                }
            }
        }

        if (settings.getChallenge() == ChallengeKind.Mutation || settings.getChallenge() == ChallengeKind.VirulentStrainAndMutation){
            myPlayerDeck.addCard(new MutationIntensifiesEventCard(gameManager));
            myPlayerDeck.addCard(new MutationSpreadsEventCard(gameManager));
            myPlayerDeck.addCard(new MutationIntensifiesEventCard(gameManager));
            myPlayerDeck.shuffleDeck();
        }

        // - shuffling in Epidemic cards based on settings
        List<PlayerCard> epidemicCards = new ArrayList<>();
        int numOfEpidemicCards = settings.getNumOfEpidemicCards();


        List<EpidemicCard> allVirulentStrainEpidemicCards = new ArrayList<>();
        switch (settings.getChallenge()) {
            case OriginalBaseGame:
                for (int i = 0; i < numOfEpidemicCards; i++) {
                    epidemicCards.add(new BasicEpidemicCard(gameManager));
                }
                break;
            case VirulentStrain:
                allVirulentStrainEpidemicCards.add(new SlipperySlopeEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new RateEffectEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new HiddenPocketEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new UncountedPopulationsEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new UnacceptableLossEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new ChronicEffectEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new ComplexMolecularStructureEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new GovernmentInterferenceEpidemicCard(gameManager));

                Collections.shuffle(allVirulentStrainEpidemicCards);
                for (int i = 0; i < numOfEpidemicCards; i++) {
                    epidemicCards.add(allVirulentStrainEpidemicCards.get(i));
                }
                break;
            case VirulentStrainAndMutation:
                allVirulentStrainEpidemicCards.add(new SlipperySlopeEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new RateEffectEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new HiddenPocketEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new UncountedPopulationsEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new UnacceptableLossEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new ChronicEffectEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new ComplexMolecularStructureEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new GovernmentInterferenceEpidemicCard(gameManager));

                Collections.shuffle(allVirulentStrainEpidemicCards);
                for (int i = 0; i < numOfEpidemicCards; i++) {
                    epidemicCards.add(allVirulentStrainEpidemicCards.get(i));
                }
                break;
            case VirulentStrainAndBioTerrorist:
                allVirulentStrainEpidemicCards.add(new SlipperySlopeEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new RateEffectEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new HiddenPocketEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new UncountedPopulationsEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new UnacceptableLossEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new ChronicEffectEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new ComplexMolecularStructureEpidemicCard(gameManager));
                allVirulentStrainEpidemicCards.add(new GovernmentInterferenceEpidemicCard(gameManager));

                Collections.shuffle(allVirulentStrainEpidemicCards);
                for (int i = 0; i < numOfEpidemicCards; i++) {
                    epidemicCards.add(allVirulentStrainEpidemicCards.get(i));
                }
                break;
            case Mutation:
                for (int i = 0; i < numOfEpidemicCards; i++) {
                    epidemicCards.add(new BasicEpidemicCard(gameManager));
                }
                break;
            case BioTerrorist:
                for (int i = 0; i < numOfEpidemicCards; i++) {
                    epidemicCards.add(new BasicEpidemicCard(gameManager));
                }
                break;
            case OnTheBrinkNoChallenges:
                for (int i = 0; i < numOfEpidemicCards; i++) {
                    epidemicCards.add(new BasicEpidemicCard(gameManager));
                }
                break;
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
        for (DiseaseType d : DiseaseType.values()) {
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
        for (int i = 0; i < 6; i++) {
            ResearchStation rs = new ResearchStation();
            allResearchStations.add(rs);
            unusedResearchStations.add(rs);
        }
        //    - placing research station in Atlanta
        ResearchStation atlResearchStation = unusedResearchStations.remove(0);
        City atlCity = myGameBoard.getCityByName(CityName.Atlanta);
        atlResearchStation.setLocation(atlCity);
        atlCity.getCityUnits().add(atlResearchStation);


        // REMOVE AFTER TESTING
//        ResearchStation parisResearchStation = unusedResearchStations.remove(0);
//        City parisCity = myGameBoard.getCityByName(CityName.Paris);
//        parisResearchStation.setLocation(parisCity);
//        parisCity.getCityUnits().add(parisResearchStation);
//
//        ResearchStation tokyoResearchStation = unusedResearchStations.remove(0);
//        City tokyoCity = myGameBoard.getCityByName(CityName.Tokyo);
//        tokyoResearchStation.setLocation(tokyoCity);
//        tokyoCity.getCityUnits().add(tokyoResearchStation);
//
//        ResearchStation chennaiResearchStation = unusedResearchStations.remove(0);
//        City chennaiCity = myGameBoard.getCityByName(CityName.Chennai);
//        chennaiResearchStation.setLocation(chennaiCity);
//        chennaiCity.getCityUnits().add(chennaiResearchStation);
//
//        ResearchStation moscowResearchStation = unusedResearchStations.remove(0);
//        City moscowCity = myGameBoard.getCityByName(CityName.Moscow);
//        moscowResearchStation.setLocation(moscowCity);
//        moscowCity.getCityUnits().add(moscowResearchStation);
//
//        ResearchStation osakaResearchStation = unusedResearchStations.remove(0);
//        City osakaCity = myGameBoard.getCityByName(CityName.Osaka);
//        osakaResearchStation.setLocation(osakaCity);
//        osakaCity.getCityUnits().add(osakaResearchStation);





        // - initialize quarantine markers if bioT chanlenge is not active
        if(!bioTChallengeActive) {
            unusedQuarantineMarkers = new ArrayList<QuarantineMarker>();
            allQuarantineMarkers = new ArrayList<QuarantineMarker>();

            for(int i = 0; i < 4; i++) {
                QuarantineMarker qm = new QuarantineMarker();
                allQuarantineMarkers.add(qm);
                unusedQuarantineMarkers.add(qm);
            }

        }

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

        for (DiseaseType d : DiseaseType.values()) {
            if (d != DiseaseType.Purple) {
                ArrayList<DiseaseFlag> diseaseFlagsSupply = diseaseTypeToSupplyDict.get(d);
                for (int j = 0; j < 24; j++) {
                    diseaseFlagsSupply.add(new DiseaseFlag(d));
                }
            }
        }

        boolean purpleDiseaseChallenge = settings.getChallenge().equals(ChallengeKind.Mutation)
                || settings.getChallenge().equals(ChallengeKind.BioTerrorist)
                || settings.getChallenge().equals(ChallengeKind.VirulentStrainAndMutation)
                || settings.getChallenge().equals(ChallengeKind.VirulentStrainAndBioTerrorist);

        if(purpleDiseaseChallenge){
            purpleDisease = new Disease(DiseaseType.Purple);
            purpleUnusedDiseaseFlags = new ArrayList<DiseaseFlag>();
            for (int j = 0; j < 12; j++) {
                purpleUnusedDiseaseFlags.add(new DiseaseFlag(purpleDisease.getDiseaseType()));
            }
            diseaseTypeToSupplyDict.put(purpleDisease.getDiseaseType(), purpleUnusedDiseaseFlags);
            diseaseTypeToDiseaseDict.put(purpleDisease.getDiseaseType(), purpleDisease);


            // REMOVE AFTER TESTING: FORCES GAME TO START WITH PURPLE FLAG
            City atl = getCityByName(CityName.Atlanta);
            DiseaseFlag purpleFlag = purpleUnusedDiseaseFlags.get(0);
            purpleFlag.setLocation(atl);
            purpleFlag.setUsed(true);
            atl.getCityUnits().add(purpleFlag);

        }
    }

    public void initializePlayerPawns() {
        unusedRoles = new ArrayList<Role>() {
            {
                if (settings.getChallenge().equals(ChallengeKind.OriginalBaseGame)) {
                    add(new Role(RoleType.ContingencyPlanner));
                    add(new Role(RoleType.OperationsExpert));
                    add(new Role(RoleType.Dispatcher));
                    add(new Role(RoleType.QuarantineSpecialist));
                    add(new Role(RoleType.Researcher));
                    add(new Role(RoleType.Medic));
                    add(new Role(RoleType.Scientist));
                    add(new Role(RoleType.Colonel));
                } else {
                    for (RoleType r : RoleType.values()) {
                        if (!r.equals(RoleType.BioTerrorist)) {
                            add(new Role(r));
                        } else if(bioTChallengeActive && !r.equals(RoleType.Colonel)) {
                            add(new Role(r));
                        }

                    }
                }
            }
        };

        City atlCity = myGameBoard.getCityByName(CityName.Atlanta);
        inGamePawns = new ArrayList<Pawn>();
        int numOfPlayers = settings.getNumOfPlayers();
        for (int k = 0; k < numOfPlayers; k++) {
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
        while (p.isAssigned() && count < inGamePawns.size()) {
            //System.out.println("getrand")
            p = inGamePawns.get(count);
            count++;
        }
        System.out.println("Unassigned pawn found: " + p.getRole().getRoleType());
        return p;
    }

    public Pawn getBioTPawn() {
      //  Pawn bioTPawn = inGamePawns.stream().filter(pawn -> pawn.getRole().getRoleType() == RoleType.BioTerrorist)
                                           // .findAny().orElse(null);


        Pawn bioTPawn = new Pawn(new Role(RoleType.BioTerrorist));
        return bioTPawn;
    }

    private Role getRandomUnassignedRole() {
        int index = randomRoleGenerator.nextInt(unusedRoles.size());
        return unusedRoles.remove(index);
    }

    public void addPlusTwoQuarantineMarkers() {
        for(int i = 0; i < 2; i++) {
            QuarantineMarker qm = new QuarantineMarker();
            allQuarantineMarkers.add(qm);
            unusedQuarantineMarkers.add(qm);
        }
    }

    private void checkAndCreateNeighbors(ArrayList<City> createdCities, City c) {

        City createdCityNeighbor = null;
        ArrayList<CityName> cNeighborNames = gameManager.getCityNeighborNames(c.getName());

        for (CityName nName : cNeighborNames) {
            //System.out.println("        neighbor name: " + nName);
            try {
                createdCityNeighbor = createdCities.stream().filter(n -> (n.getName() == nName)).findAny().orElse(null);
            } catch (NullPointerException e) {

            }

            if (createdCityNeighbor == null) {
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
                if (createdCityNeighborInCList == null) {
                    c.getNeighbors().add(createdCityNeighbor);
                    c.getConnections().add(cConnection);
                }

                if (createdCityNeighborInNList == null) {
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

        for (Region r : Region.values()) {

            citiesNamesInRegion = gameManager.getCityNamesByRegion(r);
            City createdCity = null;

            for (CityName name : citiesNamesInRegion) {

                try {
                    createdCity = createdCities.stream().filter(c -> (c.getName() == name)).findAny().orElse(null);
                } catch (NullPointerException e) {

                }

                if (createdCity == null) {
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

    public boolean getEventCardsEnabled() {
        return eventCardsEnabled;
    }

    public void setEventCardsEnabled(boolean b) {
        eventCardsEnabled = b;
    }

    public int getInfectionRate() {
        return currentInfectionRate;
    }

    public void increaseInfectionRate() {

        if (!lastInfectionMarkerReached) {
            infectionRateMeter.addLast(infectionRateMeter.removeFirst());
            currentInfectionRate = infectionRateMeter.getFirst();
        }

        if (currentInfectionRate == 4 && !lastInfectionMarkerReached)
            lastInfectionMarkerReached = true;
    }

    public InfectionDeck getInfectionDeck() {
        return myInfectionDeck;
    }

    public InfectionDiscardPile getInfectionDiscardPile() {
        return myInfectionDiscardPile;
    }

    public City getCityByName(CityName cn) {
        return myGameBoard.getCityByName(cn);
    }

    public void setGamePhase(GamePhase phase) {
        currentPhase = phase;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public PlayerDeck getPlayerDeck() {
        return myPlayerDeck;
    }

    public void printGameBoard() {
        myGameBoard.printGameBoard();
    }

    public void removePlayerFromBoard(Player p)
    {
        myGameBoard.getCitiesOnBoard().forEach(
                (city) -> city.getCityUnits().remove(p.getPawn())
        );
    }

    public CurrentPlayerTurnStatus getCurrentPlayerTurnStatus() {
        return currentPlayerTurnStatus;
    }

    public void setCurrentPlayerTurnStatus(CurrentPlayerTurnStatus status) {
        currentPlayerTurnStatus = status;
    }

    public PlayerDiscardPile getPlayerDiscardPile() {
        return myPlayerDiscardPile;
    }

    public Player getPlayerDiscardingCards() {
        return playerDiscardingCards;
    }

    public void setPlayerDiscardingCards(Player p) {
        playerDiscardingCards = p;
    }

    public boolean getOneQuietNight() {
        return oneQuietNightActivated;
    }

    public void setOneQuietNight(boolean b) {
        oneQuietNightActivated = b;
    }

    public boolean isQuarantineSpecialistInCity(City c) {
        return myGameBoard.isQuarantineSpecialistInCity(c);
    }

    public boolean isMedicInCity(City c) {
        return myGameBoard.isMedicInCity(c);
    }

    public int getOutBreakMeterReading() {
        return outBreakMeterReading;
    }

    public void incrementOutbreakMeter() {
        outBreakMeterReading++;
    }

    private void checkForQuarantineMarker(City c) {
        if(c.containsQuarantineMarker()) {
            QuarantineMarker cityQM = c.getQuarantineMarker();
            if(cityQM == null) {
                System.err.println("ERROR -- city contains quarantine marker but" +
                        " can not get marker!");
                return;
            } else {
                if(!cityQM.flipMarker()) {
                    gameManager.removeQuarantineMarker(cityQM);
                    return;
                }
            }
        }
    }

    public void infectAndResolveOutbreaks(DiseaseType cityDiseaseType, Disease cityDisease,
                                          boolean gameStatus, LinkedList<City> Q) {

        List<City> completedCities = new ArrayList<City>();
        while (gameStatus && !Q.isEmpty()) {
            City c = Q.removeFirst();


            int numberOfDiseaseFlagsPlaced = c.getNumOfDiseaseFlagsPlaced(cityDiseaseType);
            ArrayList<DiseaseFlag> freshFlags = diseaseTypeToSupplyDict.get(cityDiseaseType);

            if (numberOfDiseaseFlagsPlaced == 3) {
                //OUTBREAK

                if(c.containsQuarantineMarker()) {
                    checkForQuarantineMarker(c);
                    return;
                }

                incrementOutbreakMeter();
                if (slipperySlopeActive && cityDiseaseType.equals(virulentStrain)) {
                    incrementOutbreakMeter();
                }

                if(bioTChallengeActive && cityDiseaseType == DiseaseType.Purple) {
                    c.removeOneDiseaseFlag(DiseaseType.Purple);
                    c.removeOneDiseaseFlag(DiseaseType.Purple);
                }
                // FOR TESTING:
                System.out.println(c.getName() + " is outbreaking");
                System.out.println("Incrementing Outbreak Meter");



                ArrayList<City> neighbors = c.getNeighbors();

                for (City connCity : neighbors) {
                    if (freshFlags.size() >= 1) {
                        int dFlagCount = connCity.getNumOfDiseaseFlagsPlaced(cityDiseaseType);
                        ArrayList<Connection> connections = connCity.getConnections();
                        Connection conn = connections.stream()
                                .filter(cNeigh -> cNeigh.getEnd1().getName().equals(c.getName()) ||
                                        cNeigh.getEnd2().getName().equals(c.getName()))
                                .findAny().orElse(null);
//
//                        ConnectionStatus diseaseTypeConnectionStatus = diseaseTypeToConnectionStatusDict.get(cityDiseaseType);

                        boolean alreadyAffectedByOutbreak = false;
                        for (Connection connection : connections) {
                            if (connection.getStatus() == diseaseTypeToConnectionStatusDict.get(cityDiseaseType)) {
                                alreadyAffectedByOutbreak = true;
                                break;
                            }
                        }

                        if (conn != null) {
                            if (dFlagCount == 3
                                    && !alreadyAffectedByOutbreak) {
                                // Chain reaction outbreak is occurring.

                                if(connCity.containsQuarantineMarker()) {
                                    checkForQuarantineMarker(connCity);
                                    return;
                                }

                                Q.addLast(connCity);
                                conn.setConnectionStatus(cityDiseaseType);


                                // FOR TESTING:
                                System.out.println(connCity.getName() + " is also outbreaking.");


                            } else if (dFlagCount < 3
                                    && !alreadyAffectedByOutbreak) {

                                boolean qsOrMedicPreventingInfectionInCity = isQuarantineSpecialistInCity(connCity) || (isMedicInCity(connCity) && cityDisease.isCured());
                                boolean diseaseEradicated = checkIfEradicated(cityDiseaseType);
                                boolean qsPresentInNeighbor = false;
                                ArrayList<City> cityNeighbors = connCity.getNeighbors();
                                for (City a : cityNeighbors) {
                                    qsPresentInNeighbor = isQuarantineSpecialistInCity(a);
                                    if (qsPresentInNeighbor) break;
                                }
                                boolean infectionPrevented = qsOrMedicPreventingInfectionInCity || diseaseEradicated || qsPresentInNeighbor;

                                if (!infectionPrevented) {
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
                for (City a : cityNeighbors) {
                    qsPresentInNeighbor = isQuarantineSpecialistInCity(a);
                    if (qsPresentInNeighbor) break;
                }
                boolean infectionPrevented = qsOrMedicPreventingInfectionInCity || diseaseEradicated || qsPresentInNeighbor;

                if (!infectionPrevented && freshFlags.size() >= 1) {
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

                    if (chronicEffectInfection && freshFlags.size() >= 1) {
                        DiseaseFlag flag2;
                        try {
                            flag2 = freshFlags.remove(0);
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


                        c.getCityUnits().add(flag2);
                        flag2.setUsed(true);


                        // FOR TESTING:
                        System.out.println("Number of disease cubes in city after infecting:");
                        for (DiseaseType d : DiseaseType.values()) {
                            System.out.println("    " + d + ": " + c.getNumOfDiseaseFlagsPlaced(d));

                            chronicEffectInfection = false;
                        }
                    }

                } else {
                    gameManager.notifyAllPlayersGameLost();
                    setGamePhase(GamePhase.Completed);
                    System.out.println("Ran out of disease cubes.");
                }
            }
            gameStatus = (getOutBreakMeterReading() < 8) && (freshFlags.size() >= 1);
            if (!gameStatus) {
                gameManager.notifyAllPlayersGameLost();
                setGamePhase(GamePhase.Completed);
                System.out.println("Ran out of disease cubes or Outbreak meter maxed out");
            }

           completedCities.add(c);
        }

        for(City compCity : completedCities) {
            ArrayList<Connection> connections = compCity.getConnections();
            connections.forEach(compConn -> compConn.setConnectionStatus(null));
        }

    }

    // For HiddenPocketEpidemicCard. This method infects cities with the Virulent Strain when it is eradicated.
    public void infectAndResolveOutbreaksForHiddenPocket(DiseaseType cityDiseaseType, Disease cityDisease,
                                                        boolean gameStatus, LinkedList<City> Q){
        while(gameStatus && !Q.isEmpty() ) {
            City c = Q.removeFirst();
            int numberOfDiseaseFlagsPlaced = c.getNumOfDiseaseFlagsPlaced(cityDiseaseType);
            ArrayList<DiseaseFlag> freshFlags = diseaseTypeToSupplyDict.get(cityDiseaseType);

            if(numberOfDiseaseFlagsPlaced == 3) {
                //OUTBREAK
                incrementOutbreakMeter();
                if (slipperySlopeActive && cityDiseaseType.equals(virulentStrain)) {
                    incrementOutbreakMeter();
                }

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
                                boolean qsPresentInNeighbor = false;
                                ArrayList<City> cityNeighbors = connCity.getNeighbors();
                                for(City a : cityNeighbors) {
                                    qsPresentInNeighbor = isQuarantineSpecialistInCity(a);
                                    if( qsPresentInNeighbor) break;
                                }
                                boolean infectionPrevented = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor;

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
                boolean qsPresentInNeighbor = false;
                ArrayList<City> cityNeighbors = c.getNeighbors();
                for(City a : cityNeighbors) {
                    qsPresentInNeighbor = isQuarantineSpecialistInCity(a);
                    if( qsPresentInNeighbor) break;
                }
                boolean infectionPrevented = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor;

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


                } else {
                    gameManager.notifyAllPlayersGameLost();
                    setGamePhase(GamePhase.Completed);
                    System.out.println("Ran out of disease cubes.");
                }
            }
            gameStatus = (getOutBreakMeterReading() < 8) && (freshFlags.size() >= 1);
            if (!gameStatus) {
                gameManager.notifyAllPlayersGameLost();
                setGamePhase(GamePhase.Completed);
                System.out.println("Ran out of disease cubes or Outbreak meter maxed out");
            }
        }

    }

    public void infectCitiesForMutationSpreads(){

    }

    // TO TEST
    public void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    //the ultimate functional programming test
    public GameState generateCondensedGameState() {
        System.out.println("----- ACTIVE PLAYER LIST IN GENERATE CONDESED GAME STATE ---- ");
        gameManager.getActivePlayers().forEach(player -> System.out.println("PLAYER: " + player.getPlayerUserName()
                + ", ROLETYPE: " + player.getRoleType()));
        System.out.println("-------------------------------------------------------------");
        final Map<RoleType, String> userMap
                = gameManager.getActivePlayers().stream().collect(Collectors.toMap(Player::getRoleType, Player::getPlayerUserName));

        final Map<RoleType, List<PlayerCard>> cardMap
                = gameManager.getActivePlayers().stream().collect(Collectors.toMap(Player::getRoleType, Player::getCardsInHand));

//        final Map<RoleType, CityName> positionMap
//                = gameManager.getActivePlayers().stream().collect(Collectors.toMap(Player::getRoleType, p -> p.getPawn().getLocation().getName()));

        final Map<RoleType, City> positionMap
                = gameManager.getActivePlayers().stream().collect(Collectors.toMap(Player::getRoleType, p -> myGameBoard.getCityByName(p.getPawn().getLocation().getName())));

        Map<BioTTurnStats, Boolean> bioTMap = null;
        if (settings.getChallenge().equals(ChallengeKind.BioTerrorist) || settings.getChallenge().equals(ChallengeKind.VirulentStrainAndBioTerrorist)) {
            bioTMap = gameManager.getBioTPlayer().getBioTTurnTracker().getBioTStatMap();
        }

        final Map<CityName, List<Pair<DiseaseType, Integer>>> diseaseCubesMap
                = myGameBoard.getCitiesOnBoard().stream().collect(Collectors.toMap(City::getName, City::getDiseaseFlags));
        System.out.println("GAMESTATE DISEASE CUBES: " + diseaseCubesMap);

        final Map<DiseaseType, Integer> remainingDiseaseCubesMap;
        if (settings.getChallenge() == ChallengeKind.Mutation || settings.getChallenge() == ChallengeKind.VirulentStrainAndMutation
                || settings.getChallenge() == ChallengeKind.BioTerrorist || settings.getChallenge() == ChallengeKind.VirulentStrainAndBioTerrorist) {
            remainingDiseaseCubesMap = Arrays.stream(DiseaseType.values()).collect(Collectors.toMap(d -> d,
                    d -> getDiseaseSupplyByDiseaseType(d).size()));
        } else {
            remainingDiseaseCubesMap = Arrays.stream(DiseaseType.values()).filter(d -> d != DiseaseType.Purple).collect(Collectors.toMap(d -> d,
                    d -> getDiseaseSupplyByDiseaseType(d).size()));
        }

        int actionsRemaining;
        if (currentPlayer.getRoleType().equals(RoleType.Generalist)) {
            actionsRemaining = 5 - currentPlayer.getActionsTaken();
        } else {
            actionsRemaining = 4 - currentPlayer.getActionsTaken();
        }

        ArrayList<DiseaseType> curedDiseases = new ArrayList<DiseaseType>();
        if (blueDisease.isCured()) {
            curedDiseases.add(blueDisease.getDiseaseType());
        }
        if (redDisease.isCured()) {
            curedDiseases.add(redDisease.getDiseaseType());
        }
        if (yellowDisease.isCured()) {
            curedDiseases.add(yellowDisease.getDiseaseType());
        }
        if (blackDisease.isCured()) {
            curedDiseases.add(blackDisease.getDiseaseType());
        }
//        if(purpleDisease.isCured()){
//            curedDiseases.add(purpleDisease.getDiseaseType());
//        }
        final ArrayList<City> researchStationLocations = new ArrayList<City>();
        for (ResearchStation rs : allResearchStations) {
            if (rs.getLocation() != null) {
                researchStationLocations.add(rs.getLocation());
            }
        }

        boolean purpleInPlay = false;
        if (settings.getChallenge() == ChallengeKind.Mutation || settings.getChallenge() == ChallengeKind.VirulentStrainAndMutation
                || settings.getChallenge() == ChallengeKind.BioTerrorist || settings.getChallenge() == ChallengeKind.VirulentStrainAndBioTerrorist) {
            purpleInPlay = true;
        }

        final Map<DiseaseType, Boolean> cures = new HashMap<DiseaseType, Boolean>();
        cures.put(DiseaseType.Blue, getDiseaseByDiseaseType(DiseaseType.Blue).isCured());
        cures.put(DiseaseType.Black, getDiseaseByDiseaseType(DiseaseType.Black).isCured());
        cures.put(DiseaseType.Red, getDiseaseByDiseaseType(DiseaseType.Red).isCured());
        cures.put(DiseaseType.Yellow, getDiseaseByDiseaseType(DiseaseType.Yellow).isCured());
        if (settings.getChallenge() == ChallengeKind.Mutation || settings.getChallenge() == ChallengeKind.VirulentStrainAndMutation
                || settings.getChallenge() == ChallengeKind.BioTerrorist || settings.getChallenge() == ChallengeKind.VirulentStrainAndBioTerrorist) {
            cures.put(DiseaseType.Purple, getDiseaseByDiseaseType(DiseaseType.Purple).isCured());
        }

        final ArrayList<City> quarantineMarkerLocations;

        if(!bioTChallengeActive) {
            quarantineMarkerLocations = new ArrayList<City>();
            for(QuarantineMarker qm : allQuarantineMarkers) {
                if(qm.getLocation() != null) {
                    quarantineMarkerLocations.add(qm.getLocation());
                }
            }

        } else {
            quarantineMarkerLocations = null;
        }

        return new GameState(userMap, cardMap, positionMap, diseaseCubesMap, remainingDiseaseCubesMap,
                myInfectionDiscardPile, myPlayerDiscardPile, currentInfectionRate, outBreakMeterReading, actionsRemaining, curedDiseases,
                currentPlayer.getPlayerUserName(), researchStationLocations, eventCardsEnabled, currentPlayerTurnStatus, archivistActionUsed,
                epidemiologistActionUsed, fieldOperativeActionUsed, fieldOperativeSamples, complexMolecularStructureActive,
                governmentInterferenceActive, governmentInterferenceSatisfied, infectionsRemaining, cures,
                bioTMap, quarantineMarkerLocations, purpleInPlay);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public GamePhase getGamePhase() {
        return currentPhase;
    }

    public ChallengeKind getChallenge() {
        return settings.getChallenge();
    }

    public ResearchStation getUnusedResearchStation() {
        if (!unusedResearchStations.isEmpty()) {
            return unusedResearchStations.remove(0);
        } else {
            return null;
        }
    }

    public QuarantineMarker getUnusedQuarantineMarker() {
        if(!unusedQuarantineMarkers.isEmpty()) {
            return unusedQuarantineMarkers.remove(0);
        } else {
            return null;
        }
    }

    public void addUnusedResearchStation(ResearchStation rs) {
        unusedResearchStations.add(rs);
    }

    public void addUnusedQuarantineMarker(QuarantineMarker qm) {
        unusedQuarantineMarkers.add(qm);
    }

    public EventCard getContingencyPlannerEventCard() {
        return contingencyPlannerEventCard;
    }

    // Returns 0 if successful, 1 if failed
    public int setContingencyPlannerEventCard(EventCard card) {
        if (contingencyPlannerEventCard == null) {
            contingencyPlannerEventCard = card;
            return 0;
        } else if (card == null) {
            contingencyPlannerEventCard = null;
            return 0;
        } else {
            return 1;
        }
    }

    public boolean getCommercialTravelBanActive() {
        return commercialTravelBanActive;
    }

    public Player getCommercialTravelBanPlayedBy() {
        return commercialTravelBanPlayedBy;
    }

    public void setCommercialTravelBanActive(boolean b) {
        commercialTravelBanActive = b;
    }

    public void setCommercialTravelBanPlayedBy(Player p) {
        commercialTravelBanPlayedBy = p;
    }

    public boolean getMobileHospitalActive() {
        return mobileHospitalActive;
    }

    public void setMobileHospitalActive(boolean b) {
        mobileHospitalActive = b;
    }

    public int getInfectionsRemaining() {
        return infectionsRemaining;
    }

    public void setInfectionsRemaining(int i) {
        infectionsRemaining = i;
    }

    public void decrementInfectionsRemaining() {
        infectionsRemaining = infectionsRemaining - 1;
    }

    public boolean getArchivistActionUsed() {
        return archivistActionUsed;
    }

    public void setArchivistActionUsed(boolean b) {
        archivistActionUsed = b;
    }

    public boolean getEpidemiologistActionUsed() {
        return epidemiologistActionUsed;
    }

    public void setEpidemiologistActionUsed(boolean b) {
        epidemiologistActionUsed = b;
    }

    public ArrayList<DiseaseFlag> getFieldOperativeSamples() {
        return fieldOperativeSamples;
    }

    public boolean isBioTChallengeActive() {
        return bioTChallengeActive;
    }

    public boolean isQuarantineMarkersInPlay() {
        return quarantineMarkersInPlay;
    }

    public void setBioTSpotted(boolean spotted) {
        bioTSpotted = spotted;
    }


    public void addSample(DiseaseFlag sample) {
        sample.setLocation(null);
        fieldOperativeSamples.add(sample);
    }

    public void returnSampleToSupply(DiseaseFlag sample) {
        fieldOperativeSamples.remove(sample);
        switch (sample.getDiseaseType()) {
            case Blue:
                blueUnusedDiseaseFlags.add(sample);
                sample.setUsed(false);
                sample.setLocation(null);
                break;
            case Black:
                blackUnusedDiseaseFlags.add(sample);
                sample.setUsed(false);
                sample.setLocation(null);
                break;
            case Red:
                redUnusedDiseaseFlags.add(sample);
                sample.setUsed(false);
                sample.setLocation(null);
                break;
            case Yellow:
                yellowUnusedDiseaseFlags.add(sample);
                sample.setUsed(false);
                sample.setLocation(null);
                break;
            case Purple:
                purpleUnusedDiseaseFlags.add(sample);
                sample.setUsed(false);
                sample.setLocation(null);
                break;
        }
    }

    public boolean getFieldOperativeActionUsed() {
        return fieldOperativeActionUsed;
    }

    public void setFieldOperativeActionUsed(boolean b) {
        fieldOperativeActionUsed = b;
    }

    public boolean getSlipperySlopeActive() {
        return slipperySlopeActive;
    }

    public void setSlipperySlopeActive(boolean b){
  	    slipperySlopeActive = b;
    }

    public DiseaseType getVirulentStrain(){
  	    return virulentStrain;
    }

    // Should only be called at most once per game, when first Virulent Strain Epidemic Card is drawn
    public void setVirulentStrain(){
        int numBlueFlags = 0;
        int numBlackFlags = 0;
        int numRedFlags = 0;
        int numYellowFlags = 0;
        int maxNumFlags = 0;

        for (City c : myGameBoard.getCitiesOnBoard()){
            numBlueFlags = numBlueFlags + c.getNumOfDiseaseFlagsPlaced(DiseaseType.Blue);
            if (numBlueFlags > maxNumFlags) {
               maxNumFlags = numBlueFlags;
            }
            numBlackFlags = numBlackFlags + c.getNumOfDiseaseFlagsPlaced(DiseaseType.Black);
            if (numBlackFlags > maxNumFlags){
                maxNumFlags = numBlackFlags;
            }
            numRedFlags = numRedFlags + c.getNumOfDiseaseFlagsPlaced(DiseaseType.Red);
            if (numRedFlags > maxNumFlags){
                maxNumFlags = numRedFlags;
            }
            numYellowFlags = numYellowFlags + c.getNumOfDiseaseFlagsPlaced(DiseaseType.Yellow);
            if (numYellowFlags > maxNumFlags){
                maxNumFlags = numYellowFlags;
            }
        }

        if (maxNumFlags == numBlueFlags){
            virulentStrain = DiseaseType.Blue;
        } else if (maxNumFlags == numBlackFlags){
            virulentStrain = DiseaseType.Black;
        } else if (maxNumFlags == numRedFlags){
            virulentStrain = DiseaseType.Red;
        } else if (maxNumFlags == numYellowFlags){
            virulentStrain = DiseaseType.Yellow;
        }
    }

    public boolean getRateEffectActive(){
  	    return rateEffectActive;
    }

    public void setRateEffectActive(boolean b){
  	    rateEffectActive = b;
    }

    public boolean getRateEffectAffectedInfection(){
  	    return rateEffectAffectedInfection;
    }

    public void setRateEffectAffectedInfection(boolean b){
  	    rateEffectAffectedInfection = b;
    }

    public GameCardRemover getMyGameCardRemover() {
        return myGameCardRemover;
    }

    public void infectUncountedPopulations(){
        ArrayList<DiseaseFlag> freshFlags = diseaseTypeToSupplyDict.get(virulentStrain);
  	    for (City c : myGameBoard.getCitiesOnBoard()){
  	        if (c.getNumOfDiseaseFlagsPlaced(virulentStrain) == 1){
                boolean qsOrMedicPreventingInfectionInCity = isQuarantineSpecialistInCity(c) || (isMedicInCity(c) && getDiseaseByDiseaseType(virulentStrain).isCured());
                boolean qsPresentInNeighbor = false;
                ArrayList<City> cityNeighbors = c.getNeighbors();
                for(City a : cityNeighbors) {
                    qsPresentInNeighbor = isQuarantineSpecialistInCity(a);
                    if( qsPresentInNeighbor) break;
                }
                boolean infectionPrevented = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor;

                if(!infectionPrevented && freshFlags.size() >= 1) {
                    DiseaseFlag flag;
                    try {
                        flag = freshFlags.remove(0);
                    } catch (NullPointerException e) {
                        //FOR TESTING, SHOULD NOT HAPPEN
                        System.out.println("ERROR -- diseaseFlags not sufficient");
                        return;
                    }
                    c.getCityUnits().add(flag);
                    flag.setUsed(true);
                }
                else{
                    gameManager.notifyAllPlayersGameLost();
                    setGamePhase(GamePhase.Completed);
                    System.out.println("Ran out of disease cubes.");
                }
            }
        }
    }

    public void infectMutationIntensifies(){
        ArrayList<DiseaseFlag> freshFlags = diseaseTypeToSupplyDict.get(DiseaseType.Purple);
        for (City c : myGameBoard.getCitiesOnBoard()){
            if (c.getNumOfDiseaseFlagsPlaced(DiseaseType.Purple) == 2){
                boolean qsOrMedicPreventingInfectionInCity = isQuarantineSpecialistInCity(c) || (isMedicInCity(c));
                boolean qsPresentInNeighbor = false;
                ArrayList<City> cityNeighbors = c.getNeighbors();
                for(City a : cityNeighbors) {
                    qsPresentInNeighbor = isQuarantineSpecialistInCity(a);
                    if( qsPresentInNeighbor) break;
                }
                boolean infectionPrevented = qsOrMedicPreventingInfectionInCity || qsPresentInNeighbor;

                if(!infectionPrevented && freshFlags.size() >= 1) {
                    DiseaseFlag flag1;
                    try {
                        flag1 = freshFlags.remove(0);
                    } catch (NullPointerException e) {
                        //FOR TESTING, SHOULD NOT HAPPEN
                        System.out.println("ERROR -- diseaseFlags not sufficient");
                        return;
                    }
                    c.getCityUnits().add(flag1);
                    flag1.setUsed(true);
                }
                else{
                    gameManager.notifyAllPlayersGameLost();
                    setGamePhase(GamePhase.Completed);
                    System.out.println("Ran out of disease cubes.");
                }
            }
        }
    }

    public void resolveUnacceptableLoss(){
        ArrayList<DiseaseFlag> freshFlags = diseaseTypeToSupplyDict.get(virulentStrain);
        if(freshFlags.size() >= 4){
            freshFlags.remove(0);
            freshFlags.remove(0);
            freshFlags.remove(0);
            freshFlags.remove(0);
        } else {
            while (freshFlags.size() > 0){
                freshFlags.remove(0);
            }
        }
    }

    public boolean allFlagsRemoved(DiseaseType d){
        boolean noFlagsRemaining = true;
        ArrayList<City> citiesOnBoard = myGameBoard.getCitiesOnBoard();
        for(City c : citiesOnBoard){
            if (c.getNumOfDiseaseFlagsPlaced(d) != 0){
                noFlagsRemaining = false;
                break;
            }
        }
        return noFlagsRemaining;
    }

    public boolean getComplexMolecularStructureActive(){
  	    return complexMolecularStructureActive;
    }

    public void setComplexMolecularStructureActive(boolean b){
  	    complexMolecularStructureActive = b;
    }

    public boolean getGovernmentInterferenceActive(){
        return governmentInterferenceActive;
    }

    public void setGovernmentInterferenceActive(boolean b){
        governmentInterferenceActive = b;
    }

    public boolean getGovernmentInterferenceSatisfied(){
        return governmentInterferenceSatisfied;
    }

    public void setGovernmentInterferenceSatisfied(boolean b){
        governmentInterferenceSatisfied = b;
    }

    public boolean getChronicEffectActive(){
        return chronicEffectActive;
    }

    public void setChronicEffectActive(boolean b){
        chronicEffectActive = b;
    }

    public void setChronicEffectInfection(boolean b){
        chronicEffectInfection = b;
    }

    public void setQuarantinesActive(boolean b) {
        quarantineMarkersInPlay = b;
    }
}


