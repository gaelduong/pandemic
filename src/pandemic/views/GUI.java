package pandemic.views;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import api.gui.ListDialog;
import api.socketcomm.Client;
import client.ClientCommands;
import client.PandemicClient;
import javafx.util.Pair;
import pandemic.*;
//import pandemic.GameManager;
//import pandemic.Player;
//import pandemic.User;
import server.PandemicServer;
import server.ServerCommands;
import server.premadeURs.GameURs;
import shared.*;
import shared.request.PostCondition;
import shared.request.UpdateRequest;

import static java.lang.Thread.sleep;

/**
 * Purpose: Create & draw GUI components + event listeners
 *
 **/

/**TO DO: 
 * Discard
 * Build RS
 * Play eventCard
 * -....
 **/
//only click on ok city-directflight
public class GUI extends JFrame {


	private GameState gs;
	private String username;
	private RoleType userRole;
	private PandemicClient client;
	private City currentUserCity;
	private List<Pair<DiseaseType, Integer>> cubesInCity;

	private boolean hasNeverBeenDrawnBefore = true;
	private int controller = 0;

	//moves map
	//private boolean[] moves = {driveFerrySelected,directFlightSelected,treatDiseaseSelected,shareKnowledgeSelected};
	private Map<String, Boolean> moves = new HashMap<String, Boolean>() {{
		put("drive", false);
		put("directFlight", false);
		put("treatDisease", false);
		put("shareKnowledge", false);

		put("charterFlight", false);
		put("shuttleFlight", false);
		put("buildResearch", false);
		put("discoverCure", false);

		put("discard", false);
		put("playEventCard",false);
	}};
	private CityName cityNameSelected = null;


	//================================================================================================
	//================================================================================================
	//====================================LABELS======================================================
	//================================================================================================
	//================================================================================================


	/*Pawn icons*/


	/*Pawns*/
	private JLabel orangePawn = new JLabel("/pandemic/resources/Pawns/orangePawn.png");
	private JLabel greenPawn = new JLabel("/pandemic/resources/Pawns/greenPawn.png");
	private JLabel bluePawn = new JLabel("/pandemic/resources/Pawns/bluePawn.png");
	private JLabel purplePawn = new JLabel("/pandemic/resources/Pawns/purplePawn.png");
	private JLabel yellowPawn = new JLabel("/pandemic/resources/Pawns/yellowPawn.png");
	private JLabel whitePawn = new JLabel("/pandemic/resources/Pawns/whitePawn.png");
	private JLabel pinkPawn = new JLabel("/pandemic/resources/Pawns/pinkPawn.png");

	/*Pawn controlled by user*/
	private JLabel controlPawn;

	private JLabel researchStation = new JLabel("/pandemic/resources/researchStation.png");


	/*Cities - idea: store each city's position (x,y) in itself (label text) (no need to create a new list for positions)  */
	//syntax: city = new JLabel(color,x,y)
	private JLabel LondonCityLabel = new JLabel("b,566,99");
	private JLabel StPetersburgCityLabel = new JLabel("b,716,64");
	private JLabel WashingtonCityLabel = new JLabel("b,393,238");
	private JLabel NewYorkCityLabel = new JLabel("b,435,145");
	private JLabel SanFranciscoCityLabel = new JLabel("b,230,160");
	private JLabel ChicagoCityLabel = new JLabel("b,306,150");
	private JLabel AtlantaCityLabel = new JLabel("b,328,228");
	private JLabel MontrealCityLabel = new JLabel("b,376,132");
	private JLabel MadridCityLabel = new JLabel("b,544,168");
	private JLabel ParisCityLabel = new JLabel("b,630,165");
	private JLabel MilanCityLabel = new JLabel("b,688,122");
	private JLabel EssenCityLabel = new JLabel("b,642,66");

	private JLabel SantiagoCityLabel = new JLabel("y,346,508");
	private JLabel JohannesburgCityLabel = new JLabel("y,716,467");
	private JLabel LimaCityLabel = new JLabel("y,364,467");
	private JLabel SaoPauloCityLabel = new JLabel("y,508,433");
	private JLabel LosAngelesCityLabel = new JLabel("y,285,270");
	private JLabel MexicoCityCityLabel = new JLabel("y,390,295");
	private JLabel MiamiCityLabel = new JLabel("y,435,334");
	private JLabel BogotaCityLabel = new JLabel("y,393,407");
	private JLabel BuenosAiresCityLabel = new JLabel("y,468,509");
	private JLabel LagosCityLabel = new JLabel("y,605,348");
	private JLabel KinshasaCityLabel = new JLabel("y,666,392");
	private JLabel KhartoumCityLabel = new JLabel("y,716,334");

	private JLabel ChennaiCityLabel = new JLabel("w,880,358");
	private JLabel CairoCityLabel = new JLabel("w,680,258");
	private JLabel DelhiCityLabel = new JLabel("w,880,208");
	private JLabel BaghdadCityLabel = new JLabel("w,759,205");
	private JLabel MumbaiCityLabel = new JLabel("w,838,317");
	private JLabel KolkataCityLabel = new JLabel("w,909,295");
	private JLabel TehranCityLabel = new JLabel("w,868,139");
	private JLabel RiyadhCityLabel = new JLabel("w,761,295");
	private JLabel KarachiCityLabel = new JLabel("w,838,258");
	private JLabel IstanbulCityLabel = new JLabel("w,721,162");
	private JLabel AlgiersCityLabel = new JLabel("w,618,215");
	private JLabel MoscowCityLabel = new JLabel("w,799,106");

	private JLabel TokyoCityLabel = new JLabel("r,1125,128");
	private JLabel JakataCityLabel = new JLabel("r,963,393");
	private JLabel SeoulCityLabel = new JLabel("r,1072,75");
	private JLabel ManilaCityLabel = new JLabel("r,1115,340");
	private JLabel OsakaCityLabel = new JLabel("r,1125,198");
	private JLabel BeijingCityLabel = new JLabel("r,986,86");
	private JLabel HongKongCityLabel = new JLabel("r,986,230");
	private JLabel TapeiCityLabel = new JLabel("r,1059,230");
	private JLabel ShanghaiCityLabel = new JLabel("r,986,155");
	private JLabel BangkokCityLabel = new JLabel("r,934,337");
	private JLabel HoChiMinhCityCityLabel = new JLabel("r,1026,345");
	private JLabel SydneyCityLabel = new JLabel("r,1138,445");


	/*citiesLabels contains all citiesLabels*/
	private ArrayList<JLabel> citiesLabels = new ArrayList<JLabel>(Arrays.asList(
			LondonCityLabel, StPetersburgCityLabel, WashingtonCityLabel, NewYorkCityLabel, SanFranciscoCityLabel, ChicagoCityLabel, AtlantaCityLabel, MontrealCityLabel, MadridCityLabel, ParisCityLabel, MilanCityLabel, EssenCityLabel,
			SantiagoCityLabel, JohannesburgCityLabel, LimaCityLabel, SaoPauloCityLabel, LosAngelesCityLabel, MexicoCityCityLabel, MiamiCityLabel, BogotaCityLabel, BuenosAiresCityLabel, LagosCityLabel, KinshasaCityLabel, KhartoumCityLabel,
			ChennaiCityLabel, CairoCityLabel, DelhiCityLabel, BaghdadCityLabel, MumbaiCityLabel, KolkataCityLabel, TehranCityLabel, RiyadhCityLabel, KarachiCityLabel, IstanbulCityLabel, AlgiersCityLabel, MoscowCityLabel,
			TokyoCityLabel, JakataCityLabel, SeoulCityLabel, ManilaCityLabel, OsakaCityLabel, BeijingCityLabel, HongKongCityLabel, TapeiCityLabel, ShanghaiCityLabel, BangkokCityLabel, HoChiMinhCityCityLabel, SydneyCityLabel
	));


	/*Event Cards*/
	private JLabel AirLiftCardLabel = new JLabel("/pandemic/resources/PlayerCards/AirLiftEventCard.png");
	private JLabel OneQuietNightCardLabel = new JLabel("/pandemic/resources/PlayerCards/OneQuietNightEventCard.png");
	private JLabel ResilientPopulationCardLabel = new JLabel("/pandemic/resources/PlayerCards/ResilientPopulationEventCard.png");
	private JLabel GovernmentGrantCardLabel = new JLabel("/pandemic/resources/PlayerCards/GovernmentGrantEventCard.png");
	private JLabel ForecastCardLabel = new JLabel("/pandemic/resources/PlayerCards/ForecastEventCard.png");
	private JLabel BasicEpidemicCardLabel = new JLabel("/pandemic/resources/PlayerCards/BasicEpidemic.png");

	/*City Cards*/
	private JLabel SanFranciscoCardLabel = new JLabel("/pandemic/resources/CityCards/SanFrancisco.png");
	private JLabel ChicagoCardLabel = new JLabel("/pandemic/resources/CityCards/Chicago.png");
	private JLabel MontrealCardLabel = new JLabel("/pandemic/resources/CityCards/Montreal.png");
	private JLabel NewYorkCardLabel = new JLabel("/pandemic/resources/CityCards/NewYork.png");
	private JLabel AtlantaCardLabel = new JLabel("/pandemic/resources/CityCards/Atlanta.png");
	private JLabel WashingtonCardLabel = new JLabel("/pandemic/resources/CityCards/Washington.png");
	private JLabel LondonCardLabel = new JLabel("/pandemic/resources/CityCards/London.png");
	private JLabel EssenCardLabel = new JLabel("/pandemic/resources/CityCards/Essen.png");
	private JLabel StPetersburgCardLabel = new JLabel("/pandemic/resources/CityCards/StPetersburg.png");
	private JLabel MadridCardLabel = new JLabel("/pandemic/resources/CityCards/Madrid.png");
	private JLabel ParisCardLabel = new JLabel("/pandemic/resources/CityCards/Paris.png");
	private JLabel MilanCardLabel = new JLabel("/pandemic/resources/CityCards/Milan.png");
	private JLabel LosAngelesCardLabel = new JLabel("/pandemic/resources/CityCards/LosAngeles.png");
	private JLabel MexicoCityCardLabel = new JLabel("/pandemic/resources/CityCards/MexicoCity.png");
	private JLabel MiamiCardLabel = new JLabel("/pandemic/resources/CityCards/Miami.png");
	private JLabel BogotaCardLabel = new JLabel("/pandemic/resources/CityCards/Bogota.png");
	private JLabel LimaCardLabel = new JLabel("/pandemic/resources/CityCards/Lima.png");
	private JLabel SantiagoCardLabel = new JLabel("/pandemic/resources/CityCards/Santiago.png");
	private JLabel BuenosAiresCardLabel = new JLabel("/pandemic/resources/CityCards/BuenosAires.png");
	private JLabel SaoPauloCardLabel = new JLabel("/pandemic/resources/CityCards/SaoPaulo.png");
	private JLabel LagosCardLabel = new JLabel("/pandemic/resources/CityCards/Lagos.png");
	private JLabel KinshasaCardLabel = new JLabel("/pandemic/resources/CityCards/Kinshasa.png");
	private JLabel JohannesburgCardLabel = new JLabel("/pandemic/resources/CityCards/Johannesburg.png");
	private JLabel KhartoumCardLabel = new JLabel("/pandemic/resources/CityCards/Khartoum.png");
	private JLabel AlgiersCardLabel = new JLabel("/pandemic/resources/CityCards/Algiers.png");
	private JLabel IstanbulCardLabel = new JLabel("/pandemic/resources/CityCards/Istanbul.png");
	private JLabel MoscowCardLabel = new JLabel("/pandemic/resources/CityCards/Moscow.png");
	private JLabel CairoCardLabel = new JLabel("/pandemic/resources/CityCards/Cairo.png");
	private JLabel BaghdadCardLabel = new JLabel("/pandemic/resources/CityCards/Baghdad.png");
	private JLabel TehranCardLabel = new JLabel("/pandemic/resources/CityCards/Tehran.png");
	private JLabel RiyadhCardLabel = new JLabel("/pandemic/resources/CityCards/Riyadh.png");
	private JLabel KarachiCardLabel = new JLabel("/pandemic/resources/CityCards/Karachi.png");
	private JLabel DelhiCardLabel = new JLabel("/pandemic/resources/CityCards/Delhi.png");
	private JLabel MumbaiCardLabel = new JLabel("/pandemic/resources/CityCards/Mumbai.png");
	private JLabel KolkataCardLabel = new JLabel("/pandemic/resources/CityCards/Kolkata.png");
	private JLabel ChennaiCardLabel = new JLabel("/pandemic/resources/CityCards/Chennai.png");
	private JLabel JakartaCardLabel = new JLabel("/pandemic/resources/CityCards/Jakarta.png");
	private JLabel BangkokCardLabel = new JLabel("/pandemic/resources/CityCards/Bangkok.png");
	private JLabel HongKongCardLabel = new JLabel("/pandemic/resources/CityCards/HongKong.png");
	private JLabel ShanghaiCardLabel = new JLabel("/pandemic/resources/CityCards/Shanghai.png");
	private JLabel BeijingCardLabel = new JLabel("/pandemic/resources/CityCards/Beijing.png");
	private JLabel SeoulCardLabel = new JLabel("/pandemic/resources/CityCards/Seoul.png");
	private JLabel TokyoCardLabel = new JLabel("/pandemic/resources/CityCards/Tokyo.png");
	private JLabel OsakaCardLabel = new JLabel("/pandemic/resources/CityCards/Osaka.png");
	private JLabel TaipeiCardLabel = new JLabel("/pandemic/resources/CityCards/Taipei.png");
	private JLabel ManilaCardLabel = new JLabel("/pandemic/resources/CityCards/Manila.png");
	private JLabel HoChiMinhCityCardLabel = new JLabel("/pandemic/resources/CityCards/HoChiMinhCity.png");
	private JLabel SydneyCardLabel = new JLabel("/pandemic/resources/CityCards/Sydney.png");


	/*Infection Cards*/
	private JLabel SanFranciscoInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/SanFranciscoInfection.png");
	private JLabel ChicagoInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/ChicagoInfection.png");
	private JLabel MontrealInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/MontrealInfection.png");
	private JLabel NewYorkInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/NewYorkInfection.png");
	private JLabel AtlantaInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/AtlantaInfection.png");
	private JLabel WashingtonInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/WashingtonInfection.png");
	private JLabel LondonInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/LondonInfection.png");
	private JLabel EssenInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/EssenInfection.png");
	private JLabel StPetersburgInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/StPetersburgInfection.png");
	private JLabel MadridInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/MadridInfection.png");
	private JLabel ParisInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/ParisInfection.png");
	private JLabel MilanInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/MilanInfection.png");
	private JLabel LosAngelesInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/LosAngelesInfection.png");
	private JLabel MexicoCityInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/MexicoCityInfection.png");
	private JLabel MiamiInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/MiamiInfection.png");
	private JLabel BogotaInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/BogotaInfection.png");
	private JLabel LimaInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/LimaInfection.png");
	private JLabel SantiagoInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/SantiagoInfection.png");
	private JLabel BuenosAiresInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/BuenosAiresInfection.png");
	private JLabel SaoPauloInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/SaoPauloInfection.png");
	private JLabel LagosInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/LagosInfection.png");
	private JLabel KinshasaInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/KinshasaInfection.png");
	private JLabel JohannesburgInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/JohannesburgInfection.png");
	private JLabel KhartoumInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/KhartoumInfection.png");
	private JLabel AlgiersInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/AlgiersInfection.png");
	private JLabel IstanbulInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/IstanbulInfection.png");
	private JLabel MoscowInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/MoscowInfection.png");
	private JLabel CairoInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/CairoInfection.png");
	private JLabel BaghdadInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/BaghdadInfection.png");
	private JLabel TehranInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/TehranInfection.png");
	private JLabel RiyadhInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/RiyadhInfection.png");
	private JLabel KarachiInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/KarachiInfection.png");
	private JLabel DelhiInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/DelhiInfection.png");
	private JLabel MumbaiInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/MumbaiInfection.png");
	private JLabel KolkataInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/KolkataInfection.png");
	private JLabel ChennaiInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/ChennaiInfection.png");
	private JLabel JakartaInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/JakartaInfection.png");
	private JLabel BangkokInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/BangkokInfection.png");
	private JLabel HongKongInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/HongKongInfection.png");
	private JLabel ShanghaiInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/ShanghaiInfection.png");
	private JLabel BeijingInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/BeijingInfection.png");
	private JLabel SeoulInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/SeoulInfection.png");
	private JLabel TokyoInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/TokyoInfection.png");
	private JLabel OsakaInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/OsakaInfection.png");
	private JLabel TaipeiInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/TaipeiInfection.png");
	private JLabel ManilaInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/ManilaInfection.png");
	private JLabel HoChiMinhCityInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/HoChiMinhCityInfection.png");
	private JLabel SydneyInfectionLabel = new JLabel("/pandemic/resources/InfectionCityCards/SydneyInfection.png");
	/*Content pane which contains all GUI components*/
	private JPanel contentPane;

	private ArrayList<JLabel> targetsDrive = new ArrayList<JLabel>();
	private ArrayList<JLabel> targetsDirectFlight = new ArrayList<JLabel>();
	private ArrayList<JLabel> targetsCharterFlight = new ArrayList<>();
	private ArrayList<JLabel> targetsShuttleFlight = new ArrayList<JLabel>();
	private ArrayList<JLabel> targetsRS = new ArrayList<JLabel>();
	/*TreatdiseaseOptions container*/
	private JLabel genericBox;

	private JLabel treatDiseaseBox = new JLabel();
	private JLabel optionRedDisease = new JLabel();
	private JLabel optionBlueDisease = new JLabel();
	private JLabel optionYellowDisease = new JLabel();
	private JLabel optionBlackDisease = new JLabel();


	private JButton buildRSButton = new JButton();

	private JButton btnEndTurn = new JButton("END TURN");

	private JButton btnInfectNextCity = new JButton("INFECT NEXT");
	//private ArrayList<JLabel> optionsTreatDisease = new ArrayList<JLabel>(Arrays.asList(genericBox));
	private ArrayList<ArrayList<JLabel>> displayOptions = new ArrayList<ArrayList<JLabel>>
			(Arrays.asList(targetsDrive, targetsDirectFlight, targetsShuttleFlight));
	/*Pawn dimensions*/
	private final int[] pawnDims = {30, 40}; //{width,height}

	/*city size (width)*/
	private final int citySize = 30;

	/*cityCardsLabels */
	private ArrayList<JLabel> cityCardsLabels = new ArrayList<JLabel>();
	private ArrayList<String> cityCardImagePaths = new ArrayList<String>();


	/*PlayerDeck*/
	private JLabel playerDeck = new JLabel(" Player Deck");
	;

	/*InfectionDeck*/
	private JLabel infectionDeck = new JLabel(" Infection Deck");
	;

	/*PlayerDiscard*/
	private JLabel playerDiscard = new JLabel();

	/*InfectionDiscard*/
	private JLabel infectionDiscard = new JLabel();

	/*Cards container*/
	private JLabel cardsContainer = new JLabel();


	/*Accept label*/
	private JLabel acceptButton;

	/*Decline label*/
	private JLabel declineButton;

	/*Discard card button */
	private JLabel discardCardButton;

	private JButton discardOptionButton = new JButton();
	private JButton playEventOptionButton = new JButton();
	private JButton optionDiscardButton = new JButton();

	/*Discover cure button*/
	private JLabel discoverCureButton = new JLabel();

	private List<CityCard> discoverCureDiscardCards = new ArrayList<CityCard>();

	private PlayerCard playerCardToDiscard = null;

	/*Action buttons*/
	private JButton btnDriveFerry = new JButton("Drive");
	;
	private JButton btnDirectFlight = new JButton("<html>Direct <br> Flight</html>");
	;
	private JButton btnCharterFlight = new JButton("<html>Charter<br> Flight</html>");
	private JButton btnShuttleFlight = new JButton("<html>Shuttle <br> Flight</html>");
	;
	private JButton btnBuildResearch = new JButton("<html>Build<br>research</html>");
	private JButton btnTreatDisease = new JButton("<html>Treat <br> Disease</html>");
	private JButton btnShareKnowledge = new JButton("<html>Share<br> Knowledge</html>");
	private JButton btnDiscoverCure = new JButton("<html>Discover <br> a Cure</html>");

	private List<JButton> actionBtns = new ArrayList<JButton>
			(Arrays.asList(btnDriveFerry, btnDirectFlight, btnCharterFlight, btnShuttleFlight,
					btnBuildResearch, btnTreatDisease, btnShareKnowledge, btnDiscoverCure));

	/*Actions remaining*/
	private JLabel actionsRemaining = new JLabel();

	/*Cubes remaining indicators*/
	private JLabel redRemaining;
	private JLabel blueRemaining;
	private JLabel yellowRemaining;
	private JLabel blackRemaining;

	/*Player cards*/

	/*Infection rate indicator*/
	private JLabel infectionRate;

	/*outbreak meter count indicator*/
	private JLabel outbreakMeterCount;

	/*Instruction*/
	private JLabel instruction;

	JPanel topBar = new JPanel();

	JLabel userRoleLabel = new JLabel();

	/*ICON/IMAGE PATHS (FINAL FIELDS)*/
	//Including: MAP, pawn icons, city icons, card pics

	/*Board Map*/
	private final String boardMapPath = "/pandemic/resources/Map/pandemic9.jpeg";
	private final String boardMapLinesPath = "/pandemic/resources/Map/mapLines6.png";


	/*City icons*/
	private final String blueCityIconPath = "/pandemic/resources/Cities/blueCity.png";
	private final String yellowCityIconPath = "/pandemic/resources/Cities/yellowCity.png";
	private final String redCityIconPath = "/pandemic/resources/Cities/redCity.png";
	private final String whiteCityIconPath = "/pandemic/resources/Cities/whiteCity.png";

	/*Top bar icons*/
	private final String outbreakMeterCountIconPath = "/pandemic/resources/TopBar/outbreakSymbol.png";
	private final String infectionRateIconPath = "/pandemic/resources/TopBar/infectionRateSymbol.png";
	private final String instructionIconPath = "/pandemic/resources/TopBar/instructionSymbol.png";
	private final String blueCubesRemPath = "/pandemic/resources/TopBar/blueRem.png";
	private final String redCubesRemPath = "/pandemic/resources/TopBar/redRem.png";
	private final String yellowCubesRemPath = "/pandemic/resources/TopBar/yellowRem.png";
	private final String blackCubesRemPath = "/pandemic/resources/TopBar/blackRem.png";
	private final String purpleCubesRemPath = "/pandemic/resources/TopBar/purpleRem.png";

	private JLabel orangePawnProf = new JLabel("/pandemic/resources/Pawns/orangePawn.png");
	private JLabel greenPawnProf = new JLabel("/pandemic/resources/Pawns/greenPawn.png");
	private JLabel bluePawnProf = new JLabel("/pandemic/resources/Pawns/bluePawn.png");
	private JLabel purplePawnProf = new JLabel("/pandemic/resources/Pawns/purplePawn.png");
	private JLabel yellowPawnProf = new JLabel("/pandemic/resources/Pawns/yellowPawn.png");
	private JLabel whitePawnProf = new JLabel("/pandemic/resources/Pawns/whitePawn.png");
	private JLabel pinkPawnProf = new JLabel("/pandemic/resources/Pawns/pinkPawn.png");

	private ArrayList<JLabel> profPawns = new ArrayList<JLabel>();

	private JLabel showRole = new JLabel();

	private boolean profPawnClicked = false;
	private Map<JLabel, Boolean> profPawnOptions = new HashMap<JLabel, Boolean>() {{
		put(orangePawnProf, false);
		put(greenPawnProf, false);
		put(bluePawnProf, false);
		put(purplePawnProf, false);
		put(yellowPawnProf, false);
		put(whitePawnProf, false);
		put(pinkPawnProf, false);
	}};

	/*Target icon*/
	private final String targetIconPath = "/pandemic/resources/target.png";

	private Icon iconTarget = new ImageIcon(new ImageIcon(GUI.class.getResource(targetIconPath))
			.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

	/*Player Deck Picture*/
	private final String playerDeckPicPath = "/pandemic/resources/Deck_Discard/playerDeck.png";

	/*Infection Deck Picture*/
	private final String infectionDeckPicPath = "/pandemic/resources/Deck_Discard/infectionDeck.png";

	/*Player Discard Picture*/
	private final String playerDiscardPicPath = "/pandemic/resources/Deck_Discard/playerDisCard.png";

	/*Infection Discard Picture*/
	private final String infectionDiscardPicPath = "/pandemic/resources/Deck_Discard/infectionDisCard.png";

	/////////Ignore these (for testing)////////////
	private JLabel message = new JLabel();
	private JLabel target = new JLabel();

	private JLabel lblCard3;

	private ArrayList<JLabel> cityNameLabels;
	private ArrayList<JLabel> cityDiseaseCubes;

	//================================================================================================
	//================================================================================================
	//====================================MAP=========================================================
	//================================================================================================
	//================================================================================================
	/*RoleType <---> PawnLabel*/
	private Map<RoleType, JLabel> mapPawnLabels = new HashMap<RoleType, JLabel>() {{
		put(RoleType.Medic, pinkPawn);
		put(RoleType.Scientist, greenPawn);
		put(RoleType.Researcher, yellowPawn);
		put(RoleType.ContingencyPlanner, orangePawn);
		put(RoleType.Dispatcher, purplePawn);
		put(RoleType.OperationsExpert, bluePawn);
		put(RoleType.QuarantineSpecialist, whitePawn);
	}};


	/*CityName <---> CityLabel*/
	private Map<CityName, JLabel> mapCityLabels = new HashMap<CityName, JLabel>() {{

		put(CityName.SanFrancisco, SanFranciscoCityLabel);
		put(CityName.Chicago, ChicagoCityLabel);
		put(CityName.Montreal, MontrealCityLabel);
		put(CityName.NewYork, NewYorkCityLabel);//
		put(CityName.Atlanta, AtlantaCityLabel);
		put(CityName.StPetersburg, StPetersburgCityLabel);//
		put(CityName.London, LondonCityLabel);//
		put(CityName.Essen, EssenCityLabel);
		put(CityName.Washington, WashingtonCityLabel);//
		put(CityName.Madrid, MadridCityLabel);
		put(CityName.Paris, ParisCityLabel);
		put(CityName.Milan, MilanCityLabel);

		put(CityName.LosAngeles, LosAngelesCityLabel);
		put(CityName.MexicoCity, MexicoCityCityLabel);
		put(CityName.Miami, MiamiCityLabel);
		put(CityName.Bogota, BogotaCityLabel);
		put(CityName.Lima, LimaCityLabel);
		put(CityName.Santiago, SantiagoCityLabel);
		put(CityName.BuenosAires, BuenosAiresCityLabel);
		put(CityName.SaoPaulo, SaoPauloCityLabel);
		put(CityName.Lagos, LagosCityLabel);
		put(CityName.Kinshasa, KinshasaCityLabel);
		put(CityName.Johannesburg, JohannesburgCityLabel);
		put(CityName.Khartoum, KhartoumCityLabel);

		put(CityName.Algiers, AlgiersCityLabel);
		put(CityName.Istanbul, IstanbulCityLabel);
		put(CityName.Moscow, MoscowCityLabel);
		put(CityName.Cairo, CairoCityLabel);
		put(CityName.Baghdad, BaghdadCityLabel);
		put(CityName.Tehran, TehranCityLabel);
		put(CityName.Riyadh, RiyadhCityLabel);
		put(CityName.Karachi, KarachiCityLabel);
		put(CityName.Delhi, DelhiCityLabel);
		put(CityName.Mumbai, MumbaiCityLabel);
		put(CityName.Kolkata, KolkataCityLabel);
		put(CityName.Chennai, ChennaiCityLabel);

		put(CityName.Jakarta, JakataCityLabel);//
		put(CityName.Bangkok, BangkokCityLabel);//
		put(CityName.HongKong, HongKongCityLabel);//
		put(CityName.Shanghai, ShanghaiCityLabel);//
		put(CityName.Beijing, BeijingCityLabel);//
		put(CityName.Seoul, SeoulCityLabel);//
		put(CityName.Tokyo, TokyoCityLabel);//
		put(CityName.Osaka, OsakaCityLabel);//
		put(CityName.Taipei, TapeiCityLabel);//
		put(CityName.Manila, ManilaCityLabel);//
		put(CityName.HoChiMinhCity, HoChiMinhCityCityLabel);//
		put(CityName.Sydney, SydneyCityLabel);//
	}};

	private Map<String, JLabel> mapPlayerCardLabels = new HashMap<String, JLabel>() {{

		put("SanFrancisco", SanFranciscoCardLabel);
		put("Chicago", ChicagoCardLabel);
		put("Montreal", MontrealCardLabel);
		put("NewYork", NewYorkCardLabel);//
		put("Atlanta", AtlantaCardLabel);
		put("Washington", WashingtonCardLabel);//
		put("London", LondonCardLabel);//
		put("Essen", EssenCardLabel);
		put("StPetersburg", StPetersburgCardLabel);//
		put("Madrid", MadridCardLabel);
		put("Paris", ParisCardLabel);
		put("Milan", MilanCardLabel);

		put("LosAngeles", LosAngelesCardLabel);
		put("MexicoCity", MexicoCityCardLabel);
		put("Miami", MiamiCardLabel);
		put("Bogota", BogotaCardLabel);
		put("Lima", LimaCardLabel);
		put("Santiago", SantiagoCardLabel);
		put("BuenosAires", BuenosAiresCardLabel);
		put("SaoPaulo", SaoPauloCardLabel);
		put("Lagos", LagosCardLabel);
		put("Kinshasa", KinshasaCardLabel);
		put("Johannesburg", JohannesburgCardLabel);
		put("Khartoum", KhartoumCardLabel);

		put("Algiers", AlgiersCardLabel);
		put("Istanbul", IstanbulCardLabel);
		put("Moscow", MoscowCardLabel);
		put("Cairo", CairoCardLabel);
		put("Baghdad", BaghdadCardLabel);
		put("Tehran", TehranCardLabel);
		put("Riyadh", RiyadhCardLabel);
		put("Karachi", KarachiCardLabel);
		put("Delhi", DelhiCardLabel);
		put("Mumbai", MumbaiCardLabel);
		put("Kolkata", KolkataCardLabel);
		put("Chennai", ChennaiCardLabel);

		put("Jakarta", JakartaCardLabel);//
		put("Bangkok", BangkokCardLabel);//
		put("HongKong", HongKongCardLabel);//
		put("Shanghai", ShanghaiCardLabel);//
		put("Beijing", BeijingCardLabel);//
		put("Seoul", SeoulCardLabel);//
		put("Tokyo", TokyoCardLabel);//
		put("Osaka", OsakaCardLabel);//
		put("Taipei", TaipeiCardLabel);//
		put("Manila", ManilaCardLabel);//
		put("HoChiMinhCity", HoChiMinhCityCardLabel);//
		put("Sydney", SydneyCardLabel);//

		put("AirLift", AirLiftCardLabel);
		put("Forecast", ForecastCardLabel);
		put("OneQuietNight", OneQuietNightCardLabel);
		put("GovernmentGrant", GovernmentGrantCardLabel);
		put("ResilientPopulation", ResilientPopulationCardLabel);
		put("BasicEpidemicCard", BasicEpidemicCardLabel);


	}};

	private Map<String, JLabel> mapInfectionCardLabels = new HashMap<String, JLabel>() {{
		put("SanFrancisco", SanFranciscoInfectionLabel);
		put("Chicago", ChicagoInfectionLabel);
		put("Montreal", MontrealInfectionLabel);
		put("NewYork", NewYorkInfectionLabel);//
		put("Atlanta", AtlantaInfectionLabel);
		put("Washington", WashingtonInfectionLabel);//
		put("London", LondonInfectionLabel);//
		put("Essen", EssenInfectionLabel);
		put("StPetersburg", StPetersburgInfectionLabel);//
		put("Madrid", MadridInfectionLabel);
		put("Paris", ParisInfectionLabel);
		put("Milan", MilanInfectionLabel);

		put("LosAngeles", LosAngelesInfectionLabel);
		put("MexicoCity", MexicoCityInfectionLabel);
		put("Miami", MiamiInfectionLabel);
		put("Bogota", BogotaInfectionLabel);
		put("Lima", LimaInfectionLabel);
		put("Santiago", SantiagoInfectionLabel);
		put("BuenosAires", BuenosAiresInfectionLabel);
		put("SaoPaulo", SaoPauloInfectionLabel);
		put("Lagos", LagosInfectionLabel);
		put("Kinshasa", KinshasaInfectionLabel);
		put("Johannesburg", JohannesburgInfectionLabel);
		put("Khartoum", KhartoumInfectionLabel);

		put("Algiers", AlgiersInfectionLabel);
		put("Istanbul", IstanbulInfectionLabel);
		put("Moscow", MoscowInfectionLabel);
		put("Cairo", CairoInfectionLabel);
		put("Baghdad", BaghdadInfectionLabel);
		put("Tehran", TehranInfectionLabel);
		put("Riyadh", RiyadhInfectionLabel);
		put("Karachi", KarachiInfectionLabel);
		put("Delhi", DelhiInfectionLabel);
		put("Mumbai", MumbaiInfectionLabel);
		put("Kolkata", KolkataInfectionLabel);
		put("Chennai", ChennaiInfectionLabel);

		put("Jakarta", JakartaInfectionLabel);//
		put("Bangkok", BangkokInfectionLabel);//
		put("HongKong", HongKongInfectionLabel);//
		put("Shanghai", ShanghaiInfectionLabel);//
		put("Beijing", BeijingInfectionLabel);//
		put("Seoul", SeoulInfectionLabel);//
		put("Tokyo", TokyoInfectionLabel);//
		put("Osaka", OsakaInfectionLabel);//
		put("Taipei", TaipeiInfectionLabel);//
		put("Manila", ManilaInfectionLabel);//
		put("HoChiMinhCity", HoChiMinhCityInfectionLabel);//
		put("Sydney", SydneyInfectionLabel);//
	}};

	private Map<RoleType, JLabel> mapPawnProf = new HashMap<RoleType, JLabel>() {{
		put(RoleType.Medic, pinkPawnProf);
		put(RoleType.Scientist, greenPawnProf);
		put(RoleType.Researcher, yellowPawnProf);
		put(RoleType.ContingencyPlanner, orangePawnProf);
		put(RoleType.Dispatcher, purplePawnProf);
		put(RoleType.OperationsExpert, bluePawnProf);
		put(RoleType.QuarantineSpecialist, whitePawnProf);
	}};

	private Map<Region, Integer> mapCardColors = new HashMap<Region, Integer>() {{
		put(Region.Red, 0);
		put(Region.Blue, 0);
		put(Region.Yellow, 0);
		put(Region.Black, 0);

	}};

	//========================================================================
	//========================================================================
	//========================================================================
	//========================================================================
	/*Constructor does 2 things: (1) setting up GUI components (2) create event listener for each component*/
	public GUI(String username, PandemicServer server, GameState gameStateTest) {


		this.gs = gameStateTest;
		this.username = username;
		this.userRole = getUserRole();

	}

	public GUI(String username, PandemicClient client) {
		this.username = username;
		//this.userRole = getUserRole();
		System.out.println("userRole in constructor client: " + userRole);
		this.client = client;

		initComponents();

	}

	//public void receiveNewGameState(GameState gs) {}
	public String getUsername() {
		return username;
	}

	public void draw() {

		if (gs == null)
			return;
		String currentPlayer = gs.getCurrentPlayer();
		if (currentPlayer != null && username.equals(currentPlayer) && gs.getCurrentPlayerActionsRemaining() > 0) {
//			//Drive Ferry
			btnDriveFerry.setVisible(true);
//
//			//Direct Flight
			btnDirectFlight.setVisible(true);
//
//			//Charter Flight
			btnCharterFlight.setVisible(true);
//
//			//Shuttle Flight
			btnShuttleFlight.setVisible(true);
//
//			//Treat Disease
			btnTreatDisease.setVisible(true);
//
//			//Discover Cure
			btnDiscoverCure.setVisible(true);
//
//			//Build Research
			btnBuildResearch.setVisible(true);
//
//			//Share Knowledge
			btnShareKnowledge.setVisible(true);
		} else {
			btnDriveFerry.setVisible(false);
			btnDirectFlight.setVisible(false);
			btnCharterFlight.setVisible(false);
			btnShuttleFlight.setVisible(false);
			btnTreatDisease.setVisible(false);
			btnDiscoverCure.setVisible(false);
			btnBuildResearch.setVisible(false);
			btnShareKnowledge.setVisible(false);
			btnDiscoverCure.setVisible(false);
		}

		actionBtns.forEach(b -> b.setForeground(Color.BLACK));
		actionBtns.forEach(b -> b.setBackground(new Color(173, 188, 204)));

		for (String m : moves.keySet()) moves.put(m, false);


		buildRSButton.setVisible(false);

		optionRedDisease.setVisible(false);
		optionYellowDisease.setVisible(false);
		optionBlackDisease.setVisible(false);
		optionBlueDisease.setVisible(false);
		treatDiseaseBox.setVisible(false);


		//loadAllPlayerCards();
		loadTopBar();
		loadPlayerCards(); //lpc
		loadPlayerDiscardCards();
		loadInfectionDiscardCards();
		//loadOtherPlayersProfile();


		/*----------Set up pawns ----------*/
		loadPawns();
		loadProfPawns();
		//

		/*----------Set up controlPawn------*/
		loadControlPawn();

		loadBtnEndTurn();
		loadInfectNextCityButton();
		loadActionsRemaining();
		loadGenericMessageBox();

		/*----------Set up citiesLabels ----------*/
		//loadCities();

		//----------Set up disease cubes on map---------
		loadCubesOnMap();

		//------Display research stations------
		loadResearchStations();


		loadTargetsDrive();
		loadTargetsDirectFlight();
		loadTargetsCharterFlight();
		loadTargetsShuttleFlight();
		loadTargetsRS();
//		loadGenericMessageBox();

		//if (gs.getCardMap().get(userRole).size() > 7)
			loadTooManyCardsMessage();







/*
		for(PlayerCard cityCard : gs.getCardMap().get(userRole))
		{
			JLabel cityCardLabel = mapPlayerCardLabels.get(cityCard.getCardName());
			if(cityCardLabel != null)
			{
				contentPane.setComponentZOrder(cityCardLabel,1);
				//cityCardLabel.setVisible(true);
			}
		}

		for(JLabel diseaseCubes : cityDiseaseCubes){
			contentPane.setComponentZOrder(diseaseCubes, 1);
			//diseaseCubes.setVisible(true);
		}
*/

		//----------GREY OUT BUTTONS-------------
		//grey out btnDiscoverCure if player doesn't have 5 (or 4 if scientist) cards of same color

		for (Region region : mapCardColors.keySet())
			mapCardColors.put(region, 0);


		int isSci = (userRole == RoleType.Scientist) ? 1 : 0;

		for (PlayerCard playerCard : gs.getCardMap().get(userRole)) {
			if (playerCard.getCardType().equals(CardType.CityCard)) {
				if (((CityCard) (playerCard)).getRegion().equals(Region.Red))
					mapCardColors.put(Region.Red, mapCardColors.get(Region.Red) + 1);
				else if (((CityCard) (playerCard)).getRegion().equals(Region.Blue))
					mapCardColors.put(Region.Blue, mapCardColors.get(Region.Blue) + 1);
				else if (((CityCard) (playerCard)).getRegion().equals(Region.Yellow))
					mapCardColors.put(Region.Yellow, mapCardColors.get(Region.Yellow) + 1);
				else if (((CityCard) (playerCard)).getRegion().equals(Region.Black))
					mapCardColors.put(Region.Black, mapCardColors.get(Region.Black) + 1);
				;
			}
		}
		System.out.println("Count card colors: R:" + mapCardColors.get(Region.Red) + " B:" + mapCardColors.get(Region.Blue)
				+ " Y:" + mapCardColors.get(Region.Yellow) + " BL:" + mapCardColors.get(Region.Black));
		btnDiscoverCure.setEnabled(false);
		for (Integer n : mapCardColors.values()) {
			if (n + isSci >= 2) {
				btnDiscoverCure.setEnabled(true);
				break;
			}
		}


		City currCity = gs.getPositionMap().get(userRole);
		String citName = currCity.getName().toString();

		//grey out directFlight if player has no city card
		//or if he has exactly one city card which matches his current position
		btnDirectFlight.setEnabled(false);
		for (PlayerCard pc : gs.getCardMap().get(userRole)) {
			if (pc.getCardType().equals(CardType.CityCard)) {
				if (!pc.getCardName().equals(citName)) {
					btnDirectFlight.setEnabled(true);
					break;
				} else continue;
			}
		}

		//grey out charterFlight if the player doesn't have the card that matches the city he's in
		btnCharterFlight.setEnabled(false);
		for (PlayerCard pc : gs.getCardMap().get(userRole)) {
			if (pc.getCardName().equals(citName)) {
				btnCharterFlight.setEnabled(true);
				break;
			}
		}

		//grey out treatdisease if the city has 0 cube
		btnTreatDisease.setEnabled(false);
		for (Pair<DiseaseType, Integer> p : gs.getDiseaseCubesMap().get(currCity.getName())) {
			if (p.getValue() > 0) {
				btnTreatDisease.setEnabled(true);
				break;
			}
		}

		//grey out shareknowledge if there's only 1 player in 1 city
		// OR if no one has the card that matches the city they're in

		btnShareKnowledge.setEnabled(false);

		boolean moreThanOne = false;
		boolean hasCard = false;

		for (Map.Entry<RoleType, City> e : gs.getPositionMap().entrySet()) {
			if (!e.getKey().equals(userRole)) {
				if (e.getValue().equals(currCity)) {
					moreThanOne = true;
					break;
				}
			}
		}

		for (Map.Entry<RoleType, List<PlayerCard>> e : gs.getCardMap().entrySet()) {
			for (PlayerCard pc : e.getValue()) {
				if (pc.getCardName().equals(citName)) {
					hasCard = true;
					break;
				}
			}
		}
		if (moreThanOne && hasCard) btnShareKnowledge.setEnabled(true);


		//grey out buildRS if player has no card that matches the city he's in
		//OR if a station already exists in the city he's in
		btnBuildResearch.setEnabled(false);

		for (PlayerCard pc : gs.getCardMap().get(userRole)) {
			if (pc.getCardName().equals(citName)) {
				btnBuildResearch.setEnabled(true);
				break;
			}
		}
		for (City c : gs.getResearchStationLocations()) {
			if (c.getName().toString().equals(citName)) {
				btnBuildResearch.setEnabled(false);
				break;
			}
		}

		//greyout shuttle flight if |RS| < 2
		//OR if there's no RS in the city he's in
		btnShuttleFlight.setEnabled(false);

		for (City c : gs.getResearchStationLocations()) {
			if (c.getName().equals(currCity.getName())) btnShuttleFlight.setEnabled(true);
		}
		if (gs.getResearchStationLocations().size() < 2) btnShuttleFlight.setEnabled(false);

		revalidate();
		repaint();
	}

	public void createNewEventWrapper() {
		if (controller == 0) {
			controller++;
			System.out.println("print CONTROLLER");

			draw();
			createEvents();
		}
	}

	private void initComponents() {

	/*	//Load player cards
		for(JLabel label : mapPlayerCardLabels.values())
		{
			//System.out.println(entry.getKey());
			label.setIcon(new ImageIcon(new ImageIcon(GUI.class
					.getResource(label.getText()))
					.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
			label.setVisible(false);
		}


		for(JLabel label : mapInfectionCardLabels.values())
		{
			label.setIcon(new ImageIcon(new ImageIcon(GUI.class
					.getResource(label.getText()))
					.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
			label.setVisible(false);
		}

		/*------Set up JFrame and contentPane-----*/
		setTitle("Pandemic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 900);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// -------Set up action buttons --------
		//Drive Ferry
		//btnDriveFerry.setIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/icon.png")));
		btnDriveFerry.setBounds(11, 370, 90, 40);
		btnDriveFerry.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDriveFerry.setBackground(new Color(173, 188, 204));
		contentPane.add(btnDriveFerry);
		btnDriveFerry.setVisible(false);
		btnDriveFerry.setFocusPainted(false);
		//btnDriveFerry.setFont(new Font("Tahoma", Font.BOLD, 12));

		//Direct Flight
		//btnDirectFlight.setIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/icon.png")));
		btnDirectFlight.setBounds(100, 370, 90, 40);
		btnDirectFlight.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnDirectFlight.setBackground(new Color(173, 188, 204));
		contentPane.add(btnDirectFlight);
		btnDirectFlight.setVisible(false);
		btnDirectFlight.setFocusPainted(false);

		//Charter Flight
		btnCharterFlight.setBounds(11, 407, 90, 40);
		btnCharterFlight.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnCharterFlight.setBackground(new Color(173, 188, 204));
		contentPane.add(btnCharterFlight);
		btnCharterFlight.setVisible(false);
		btnCharterFlight.setFocusPainted(false);

		//Shuttle Flight
		btnShuttleFlight.setBounds(100, 407, 90, 40);
		btnShuttleFlight.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnShuttleFlight.setBackground(new Color(173, 188, 204));
		contentPane.add(btnShuttleFlight);
		btnShuttleFlight.setVisible(false);
		btnShuttleFlight.setFocusPainted(false);

		//Treat Disease
		btnTreatDisease.setBounds(100, 445, 90, 40);
		btnTreatDisease.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnTreatDisease.setBackground(new Color(173, 188, 204));
		contentPane.add(btnTreatDisease);
		btnTreatDisease.setVisible(false);
		btnTreatDisease.setFocusPainted(false);

		//Discover Cure
		btnDiscoverCure.setBounds(100, 482, 90, 40);
		btnDiscoverCure.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnDiscoverCure.setBackground(new Color(173, 188, 204));
		contentPane.add(btnDiscoverCure);
		btnDiscoverCure.setVisible(false);
		btnDiscoverCure.setFocusPainted(false);

		//Build Research
		btnBuildResearch.setBounds(11, 445, 90, 40);
		btnBuildResearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnBuildResearch.setBackground(new Color(173, 188, 204));
		contentPane.add(btnBuildResearch);
		btnBuildResearch.setVisible(false);
		btnBuildResearch.setFocusPainted(false);

		//Share Knowledge
		btnShareKnowledge.setBounds(11, 482, 90, 40);
		btnShareKnowledge.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnShareKnowledge.setBackground(new Color(173, 188, 204));
		contentPane.add(btnShareKnowledge);
		btnShareKnowledge.setVisible(false);
		btnShareKnowledge.setFocusPainted(false);

		/*-------Set up Actions remaining-------*/

		actionsRemaining.setBounds(19, 348, 131, 16);
		actionsRemaining.setForeground(Color.WHITE);
		contentPane.add(actionsRemaining);
		actionsRemaining.setVisible(true);

		/*----------Set up playerDeck----------*/
		playerDeck.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(playerDeckPicPath))
				.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH)));
		playerDeck.setBounds(7, 56, 100, 130);
		contentPane.add(playerDeck);
		playerDeck.setVisible(true);

		/*----------Set up infectionDeck----------*/
		infectionDeck.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(infectionDeckPicPath))
				.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH)));
		infectionDeck.setBounds(7, 185, 100, 130);
		contentPane.add(infectionDeck);
		infectionDeck.setVisible(true);

		/*----------Set up playerDiscard----------*/
		playerDiscard.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(playerDiscardPicPath))
				.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH)));
		playerDiscard.setBounds(106, 56, 100, 130);
		contentPane.add(playerDiscard);
		playerDiscard.setVisible(true);

		/*----------Set up infectionDiscard----------*/
		infectionDiscard.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(infectionDiscardPicPath))
				.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH)));
		infectionDiscard.setBounds(106, 185, 100, 130);
		contentPane.add(infectionDiscard);
		infectionDiscard.setVisible(true);

		// ------------- Set up Player Card Container---------
		cardsContainer.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/cardsContainer.png")).getImage().getScaledInstance(850, 191, Image.SCALE_SMOOTH)));
		cardsContainer.setBounds(270, 560, 850, 191);
		contentPane.add(cardsContainer);
		cardsContainer.setVisible(true);

		// --------- Set up map ---------
		JLabel map = new JLabel();
		map.setBounds(214, 0, 980, 650);
		map.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(boardMapPath))
				.getImage().getScaledInstance(980, 650, Image.SCALE_SMOOTH)));
		contentPane.add(map);
		map.setVisible(true);
		map.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//System.out.println(MouseInfo.getPointerInfo().getLocation());
			}
		});

		JLabel mapLines = new JLabel();
		mapLines.setBounds(210, -14, 980, 650);
		mapLines.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(boardMapLinesPath))
				.getImage().getScaledInstance(980, 560, Image.SCALE_SMOOTH)));
		contentPane.add(mapLines);
		mapLines.setVisible(true);

		// Set up cities
		Icon blueCityIcon = new ImageIcon((new ImageIcon(GUI.class.getResource(blueCityIconPath)))
				.getImage().getScaledInstance(citySize, citySize, Image.SCALE_SMOOTH));
		Icon redCityIcon = new ImageIcon((new ImageIcon(GUI.class.getResource(redCityIconPath)))
				.getImage().getScaledInstance(citySize, citySize, Image.SCALE_SMOOTH));
		Icon yellowCityIcon = new ImageIcon((new ImageIcon(GUI.class.getResource(yellowCityIconPath)))
				.getImage().getScaledInstance(citySize, citySize, Image.SCALE_SMOOTH));
		Icon whiteCityIcon = new ImageIcon((new ImageIcon(GUI.class.getResource(whiteCityIconPath)))
				.getImage().getScaledInstance(citySize, citySize, Image.SCALE_SMOOTH));
		cityNameLabels = new ArrayList<JLabel>();
		for (JLabel city : citiesLabels) {
			int x = getCityPosition(city.getText())[0];
			int y = getCityPosition(city.getText())[1];
			city.setBounds(x, y, citySize, citySize);
			switch (city.getText().charAt(0)) {
				case 'b':
					city.setIcon(blueCityIcon);
					break;
				case 'y':
					city.setIcon(yellowCityIcon);
					break;
				case 'r':
					city.setIcon(redCityIcon);
					break;
				case 'w':
					city.setIcon(whiteCityIcon);
					break;
				default:
					break;
			}
			contentPane.add(city);
			contentPane.setComponentZOrder(city, 2);
			city.setVisible(true);
			//add city names
			CityName key = null;
			JLabel value = city;
			for (Map.Entry entry : mapCityLabels.entrySet()) {
				if (value.equals(entry.getValue())) {
					key = (CityName) entry.getKey();
					break; //breaking because its one to one map
				}
			}
			JLabel cityNameLabel = new JLabel(key.toString());
			cityNameLabel.setFont(new Font("Lao MN", Font.PLAIN, 10));
			cityNameLabel.setForeground(Color.WHITE);
			cityNameLabel.setBounds(city.getX() - key.toString().length() + 5, city.getY() + 23, 90, 16);
			contentPane.add(cityNameLabel);
			cityNameLabel.setVisible(true);
			cityNameLabels.add(cityNameLabel);
		}
		cityDiseaseCubes = new ArrayList<JLabel>();

		/*---popups/messages/optionDisplay..---*/
		acceptButton = new JLabel();
		declineButton = new JLabel();
		genericBox = new JLabel();
		discardCardButton = new JLabel();



		discardOptionButton.setText("DISCARD A CARD");
		discardOptionButton.setBounds(511, 310, 106, 20);
		discardOptionButton.setBackground(Color.BLACK);
		discardOptionButton.setForeground(Color.WHITE);


		discardOptionButton.setFocusPainted(false);

		playEventOptionButton.setText("PLAY EVENT CARD");
		playEventOptionButton.setBounds(501, 530, 106, 20);
		playEventOptionButton.setBackground(Color.BLACK);
		playEventOptionButton.setForeground(Color.WHITE);


		playEventOptionButton.setFocusPainted(false);

		contentPane.add(discardOptionButton);
		contentPane.add(playEventOptionButton);

		contentPane.setComponentZOrder(discardOptionButton,0);
		contentPane.setComponentZOrder(playEventOptionButton,0);


		discardOptionButton.setVisible(true);
		playEventOptionButton.setVisible(true);










		contentPane.add(optionRedDisease);
		contentPane.add(optionYellowDisease);
		contentPane.add(optionBlueDisease);
		contentPane.add(optionBlackDisease);
		contentPane.add(treatDiseaseBox);


		contentPane.add(discardCardButton);
		contentPane.add(acceptButton);
		contentPane.add(declineButton);
		contentPane.add(discoverCureButton);

		// ----- End Turn Button ----
		btnEndTurn.setText("END TURN");
		btnEndTurn.setBounds(11, 530, 176, 20);
		btnEndTurn.setBackground(Color.RED);
		btnEndTurn.setForeground(Color.WHITE);
		contentPane.add(btnEndTurn);
		btnEndTurn.setVisible(true);
		btnEndTurn.setFocusPainted(false);

		//----Infect next city Button ---
		btnInfectNextCity.setText("INFECT NEXT");
		btnInfectNextCity.setBounds(11, 550, 176, 20);
		btnInfectNextCity.setBackground(Color.GREEN);
		btnInfectNextCity.setForeground(Color.WHITE);
		contentPane.add(btnInfectNextCity);
		btnInfectNextCity.setEnabled(false);
		btnInfectNextCity.setFocusPainted(false);

		// Generic message box
		genericBox.setForeground(Color.WHITE);
		genericBox.setOpaque(false);
		genericBox.setBounds(600, 520, 200, 50);
		genericBox.setVisible(true);
		contentPane.add(genericBox);

		// Top bar
		topBar.setBackground(Color.BLACK);
		topBar.setBounds(214, 0, 980, 30);
		contentPane.add(topBar);
		topBar.setVisible(true);
		topBar.setLayout(null);
		//+gameManager().getDiseaseSupplyByDiseaseType(DiseaseType.Red).size()
		//red cubes remaining
		redRemaining = new JLabel();
		redRemaining.setBackground(Color.BLACK);
		redRemaining.setForeground(Color.WHITE);
		redRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(redCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		redRemaining.setBounds(286, 6, 48, 16);
		topBar.add(redRemaining);
		redRemaining.setOpaque(true);

		//blue cubes remaining
		blueRemaining = new JLabel();
		blueRemaining.setBackground(Color.BLACK);
		blueRemaining.setForeground(Color.WHITE);
		blueRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(blueCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		blueRemaining.setBounds(341, 6, 48, 16);
		topBar.add(blueRemaining);
		blueRemaining.setOpaque(true);

		//yellow cubes remaining
		yellowRemaining = new JLabel();
		yellowRemaining.setBackground(Color.BLACK);
		yellowRemaining.setForeground(Color.WHITE);
		yellowRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(yellowCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		yellowRemaining.setBounds(396, 6, 48, 16);
		topBar.add(yellowRemaining);
		yellowRemaining.setOpaque(true);

		//black cubes remaining
		blackRemaining = new JLabel();
		blackRemaining.setBackground(Color.BLACK);
		blackRemaining.setForeground(Color.WHITE);
		blackRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(blackCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		blackRemaining.setBounds(451, 6, 48, 16);
		topBar.add(blackRemaining);
		blackRemaining.setOpaque(true);
		;
		//instruction
		instruction = new JLabel("");
		instruction.setBackground(Color.BLACK);
		instruction.setForeground(Color.WHITE);
		instruction.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(instructionIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		instruction.setBounds(616, 6, 48, 16);
		topBar.add(instruction);
		instruction.setOpaque(true);

		//infection rate
		infectionRate = new JLabel();
		infectionRate.setBackground(Color.BLACK);
		infectionRate.setForeground(Color.WHITE);
		infectionRate.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(infectionRateIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		infectionRate.setBounds(506, 6, 48, 16);
		topBar.add(infectionRate);
		infectionRate.setOpaque(true);

		//outbreak meter count
		outbreakMeterCount = new JLabel();
		outbreakMeterCount.setBackground(Color.BLACK);
		outbreakMeterCount.setForeground(Color.WHITE);
		outbreakMeterCount.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(outbreakMeterCountIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		outbreakMeterCount.setBounds(561, 6, 48, 16);
		topBar.add(outbreakMeterCount);
		outbreakMeterCount.setOpaque(true);

		//Display User role
		//System.out.println("username  in lcp: " + username);
		//userRoleLabel.setFont(new Font("Lao MN", Font.PLAIN, 12));
		userRoleLabel.setForeground(Color.WHITE);
		contentPane.add(userRoleLabel);
		userRoleLabel.setVisible(true);
		controlPawn = new JLabel();
		controlPawn.setBounds(74, 595, 80, 106);
		contentPane.add(controlPawn);
		controlPawn.setVisible(true);


		contentPane.setComponentZOrder(btnDriveFerry, 1);
		contentPane.setComponentZOrder(btnDirectFlight, 1);
		contentPane.setComponentZOrder(btnCharterFlight, 1);
		contentPane.setComponentZOrder(btnShuttleFlight, 1);
		contentPane.setComponentZOrder(btnTreatDisease, 1);
		contentPane.setComponentZOrder(btnDiscoverCure, 1);
		contentPane.setComponentZOrder(btnBuildResearch, 1);
		contentPane.setComponentZOrder(btnShareKnowledge, 1);
		contentPane.setComponentZOrder(actionsRemaining, 1);
		contentPane.setComponentZOrder(playerDeck, 1);
		contentPane.setComponentZOrder(infectionDeck, 1);
		contentPane.setComponentZOrder(playerDiscard, 100);
		contentPane.setComponentZOrder(infectionDiscard, 2);
		contentPane.setComponentZOrder(cardsContainer, 2);
		contentPane.setComponentZOrder(map, 4);
		contentPane.setComponentZOrder(mapLines, 3);
		for (JLabel city : citiesLabels) {
			contentPane.setComponentZOrder(city, 2);
		}
		for (JLabel cityName : cityNameLabels) {
			contentPane.setComponentZOrder(cityName, 2);
		}
		contentPane.setComponentZOrder(btnEndTurn, 1);
		contentPane.setComponentZOrder(genericBox, 0);
		contentPane.setComponentZOrder(topBar, 2);
		contentPane.setComponentZOrder(redRemaining, 1);
		contentPane.setComponentZOrder(blueRemaining, 1);
		contentPane.setComponentZOrder(yellowRemaining, 1);
		contentPane.setComponentZOrder(blackRemaining, 1);
		contentPane.setComponentZOrder(infectionRate, 1);
		contentPane.setComponentZOrder(outbreakMeterCount, 1);
		contentPane.setComponentZOrder(userRoleLabel, 1);
		contentPane.setComponentZOrder(controlPawn, 1);


	}

	private void createEvents() {
		/*Create event listeners for each city*/
		for (JLabel city : citiesLabels) {
			// System.out.println("city (in createEvents): "+ city.getText() +  " " + citiesLabels.size());
			/*Mouse entered, cursor change to pointer*/
			city.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
					setCursor(cursor);
				}

				/*Mouse exit, pointer to cursor*/
				public void mouseExited(MouseEvent e) {
					Cursor cursor = Cursor.getDefaultCursor();
					setCursor(cursor);
				}

				/*When click on city*/
				public void mouseReleased(MouseEvent e) {

					JLabel value = city;
					for (Map.Entry entry : mapCityLabels.entrySet()) {
						if (value.equals(entry.getValue())) {
							cityNameSelected = (CityName) entry.getKey();
							break; //breaking because its one to one map
						}
					}
					// System.out.println("City Clicked: " + cityNameSelected);
					//System.out.println("mouse event: " + e.getClickCount());

					if (moves.get("drive") &&
							gs.getPositionMap().get(userRole).getNeighbors().stream().anyMatch(city -> city.getName().equals(cityNameSelected))) {
						//System.out.println("yey" + moves.get("drive"));

						GameURs.sendDriveFerryUR(client, username, cityNameSelected.toString());


					} else if (moves.get("directFlight")) {
						GameURs.sendDirectFlightUR(client, username, cityNameSelected.toString());

					} else if (moves.get("charterFlight")) {
						//client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
						//	new UpdateRequest(new PostCondition(PostCondition.ACTION.MOVE_PLAYER_POS, username, cityNameSelected.toString(), TravelType.CHARTER_FLIGHT)));
					} else if (moves.get("shuttleFlight")) {
						//client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
						//	new UpdateRequest(new PostCondition(PostCondition.ACTION.MOVE_PLAYER_POS, username, cityNameSelected.toString(), TravelType.SHUTTLE_FLIGHT)));
					} else if (moves.get("buildResearch")) {

						//GameURs.sendBuildResearchStation(client, gs.getPositionMap().get(userRole).getName().toString(),  cityNameSelected.toString());
					}


					//mapPawnLabels.get(userRole).setLocation(city.getX(),city.getY()-20);
				}
			});
		}

		discoverCureButton.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				discoverCureButton.setVisible(false);
				//btnDriveFerry.setText(""+discoverCureDiscardCards.size());
				//send server list of cards of the same color to discard (e.g 5 (or 4) blue cards)
				// i.e List<CityCard>discoverCureDiscardCards
				//(and probably the color? probably not necessary since you can get the color from the card)
				//(and probably role? i.e userRole)

				GameURs.sendDiscoverCureUR(client, discoverCureDiscardCards);


			}
		});


		buildRSButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buildRSButton.setVisible(false);

				//send server the city user is in

				//GameURs.sendBuildResearchStation(client, gs.getPositionMap().get(userRole).getName().toString(),  null);

			}


		});

		acceptButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				GameURs.sendAnswerToShareKnowledge(client, true);
				//client.sendMessageToServer(ServerCommands.ANSWER_CONSENT_PROMPT.name(), true);
				genericBox.setVisible(false);
				acceptButton.setVisible(false);
				declineButton.setVisible(false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				setCursor(cursor);
			}

			/*Mouse exit, pointer to cursor*/
			public void mouseExited(MouseEvent e) {
				Cursor cursor = Cursor.getDefaultCursor();
				setCursor(cursor);
			}
		});

		declineButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				genericBox.setVisible(false);
				acceptButton.setVisible(false);
				declineButton.setVisible(false);
				GameURs.sendAnswerToShareKnowledge(client, false);
				//client.sendMessageToServer(ServerCommands.ANSWER_CONSENT_PROMPT.name(), false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				setCursor(cursor);
			}

			/*Mouse exit, pointer to cursor*/
			public void mouseExited(MouseEvent e) {
				Cursor cursor = Cursor.getDefaultCursor();
				setCursor(cursor);
			}
		});

		discardCardButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				discardCardButton.setVisible(false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				setCursor(cursor);
			}

			/*Mouse exit, pointer to cursor*/
			public void mouseExited(MouseEvent e) {
				Cursor cursor = Cursor.getDefaultCursor();
				setCursor(cursor);
			}
		});

		optionRedDisease.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				hideTreatDiseaseOptions();
				GameURs.sendTreatDiseaseUR(client, DiseaseType.Red);
				//client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.TREAT_DISEASE, username, DiseaseType.Red)));
			}

			//cursor => pointer
			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				setCursor(cursor);
			}

			/*Mouse exit, pointer to cursor*/
			public void mouseExited(MouseEvent e) {
				Cursor cursor = Cursor.getDefaultCursor();
				setCursor(cursor);
			}
		});

		optionBlueDisease.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				hideTreatDiseaseOptions();
				GameURs.sendTreatDiseaseUR(client, DiseaseType.Blue);
				//client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.TREAT_DISEASE, username, DiseaseType.Blue)));

				System.out.println("TREAT BLUE DISEASE SELECTED");
			}

			//cursor => pointer
			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				setCursor(cursor);
			}

			/*Mouse exit, pointer to cursor*/
			public void mouseExited(MouseEvent e) {
				Cursor cursor = Cursor.getDefaultCursor();
				setCursor(cursor);
			}
		});


		optionYellowDisease.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				hideTreatDiseaseOptions();
				GameURs.sendTreatDiseaseUR(client, DiseaseType.Yellow);
				//client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.TREAT_DISEASE, username, DiseaseType.Yellow)));
			}

			//cursor => pointer
			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				setCursor(cursor);
			}

			/*Mouse exit, pointer to cursor*/
			public void mouseExited(MouseEvent e) {
				Cursor cursor = Cursor.getDefaultCursor();
				setCursor(cursor);
			}
		});

		optionBlackDisease.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				hideTreatDiseaseOptions();
				GameURs.sendTreatDiseaseUR(client, DiseaseType.Black);
				//client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.TREAT_DISEASE, username, DiseaseType.Black)));
			}

			//cursor => pointer
			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				setCursor(cursor);
			}

			/*Mouse exit, pointer to cursor*/
			public void mouseExited(MouseEvent e) {
				Cursor cursor = Cursor.getDefaultCursor();
				setCursor(cursor);
			}
		});
		//etadd
		btnEndTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("End turn pressed");
				btnEndTurn.setEnabled(false);
				btnEndTurn.setBackground(Color.DARK_GRAY);
				//showInfectCities();

				GameURs.sendEndTurnRequest(client);
				//client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.END_TURN)));
				//showInfectCities();
			}

		});

		btnInfectNextCity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("End turn pressed");
				btnInfectNextCity.setEnabled(false);
				btnInfectNextCity.setBackground(Color.DARK_GRAY);


				//GameURs.sendInfectNextCityRequest(client);

			}

		});
		//discard option add
		discardOptionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				discardOptionButton.setVisible(false);
				playEventOptionButton.setVisible(false);
				moves.put("discard",true);



			}

		});

		playEventOptionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				discardOptionButton.setVisible(false);
				playEventOptionButton.setVisible(false);
				moves.put("discard",true);



			}

		});

		/*-------Events for 8 buttons-------*/
		//Drive Ferry button
		btnDriveFerry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (btnDriveFerry.getForeground().equals(Color.BLACK)) {
					showHideOtherActionButtons(actionBtns, btnDriveFerry);
					moves.put("drive", !moves.get("drive"));
					showHideTargetsDrive();
					//System.out.println("Drive ferry pressed");
					//resetMovesSelected(moves);
					//resetDisplayOptions(displayOptions);
					//loadDriveAndFlightMessage();
					//hideTreatDiseaseOptions();

					//System.out.println(citiesLabels.size());
				}

			}

			private void showHideTargetsDrive() {
				targetsDrive.forEach(t -> {
					contentPane.setComponentZOrder(t, 1);
					t.setVisible(!t.isVisible());
				});
			}
		});

		//Direct Flight button
		btnDirectFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnDirectFlight.getForeground().equals(Color.BLACK)) {
					showHideOtherActionButtons(actionBtns, btnDirectFlight);
					moves.put("directFlight", !moves.get("directFlight"));
					showHideTargetsDirectFlight();

					//resetMovesSelected(moves);
					//showHideDriveTargets
					//resetDisplayOptions(displayOptions);
					//loadDriveAndFlightMessage();
					//hideTreatDiseaseOptions();

				}
			}

			private void showHideTargetsDirectFlight() {
				targetsDirectFlight.forEach(t -> {
					contentPane.setComponentZOrder(t, 1);
					t.setVisible(!t.isVisible());
				});
			}
		});

		//Charter Flight button
		btnCharterFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnCharterFlight.getForeground().equals(Color.BLACK)) {
					showHideOtherActionButtons(actionBtns, btnCharterFlight);
					moves.put("charterFlight", !moves.get("charterFlight"));
					showHideTargetsCharterFlight();


					//loadDriveAndFlightMessage();
					//resetMovesSelected(moves);
					//showHideDirectFlightTargets
					//resetDisplayOptions(displayOptions);
					//hideTreatDiseaseOptions();
				}

			}

			private void showHideTargetsCharterFlight() {
				targetsCharterFlight.forEach(t -> {
					contentPane.setComponentZOrder(t, 1);
					t.setVisible(!t.isVisible());
				});
			}

		});

		//Shuttle Flight button
		btnShuttleFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnShuttleFlight.getForeground().equals(Color.BLACK)) {
					showHideOtherActionButtons(actionBtns, btnShuttleFlight);
					moves.put("shuttleFlight", !moves.get("shuttleFlight"));

					//resetMovesSelected(moves);
					//resetDisplayOptions(displayOptions);
					//loadDriveAndFlightMessage();
					//hideTreatDiseaseOptions();
					showHideTargetsShuttleFlight();
				}


			}

			private void showHideTargetsShuttleFlight() {

				targetsShuttleFlight.forEach(t -> {
					contentPane.setComponentZOrder(t, 1);
					t.setVisible(!t.isVisible());
				});
			}
		});

		//Build Research button
		btnBuildResearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnBuildResearch.getForeground().equals(Color.BLACK)) {
					showHideOtherActionButtons(actionBtns, btnBuildResearch);
					moves.put("buildResearch", !moves.get("buildResearch"));

					//resetMovesSelected(moves);
					//if RS < 6
					//if()

					if (gs.getResearchStationLocations().size() < 6)
						showHideBuildRSButton();
					else
						showHideTargetsRS();

					//else if RS=6


				}

			}

			private void showHideTargetsRS() {
				targetsRS.forEach(t -> {
					contentPane.setComponentZOrder(t, 1);
					t.setVisible(!t.isVisible());
				});
			}
		});

		//TreatDisease button
		btnTreatDisease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnTreatDisease.getForeground().equals(Color.BLACK)) {
					showHideOtherActionButtons(actionBtns, btnTreatDisease);
					//resetMovesSelected(moves);
					moves.put("treatDisease", !moves.get("treatDisease"));

					//showHideTreatDiseaseBox()

					resetDisplayOptions(displayOptions);
					showHideTreatDiseaseMessage();

				}

			}
		});

		//Discover a cure button
		btnDiscoverCure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnDiscoverCure.getForeground().equals(Color.BLACK)) {
					showHideOtherActionButtons(actionBtns, btnDiscoverCure);
					//resetMovesSelected(moves);
					moves.put("discoverCure", !moves.get("discoverCure"));

					//reset cards positions
					loadPlayerCards();

				}

			}

		});

		//Share Knowledge button
		btnShareKnowledge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnShareKnowledge.getForeground().equals(Color.BLACK)) {
					//resetMovesSelected(moves);
					//showHideOtherActionButtons(actionBtns, btnShareKnowledge);
					moves.put("shareKnowledge", !moves.get("shareKnowledge"));

					//Russel's code
					String currentPlayer = getGameState().getCurrentPlayer();

					List<String> playerList = getGameState().getUserMap().entrySet().stream()
							.filter(entry -> !entry.getValue().equals(currentPlayer))
							.map(entry -> entry.getKey().toString()).collect(Collectors.toList());

					final JList<String> list = new JList<>(new Vector<>(playerList));
					final ListDialog dialog = new ListDialog("Share Knowledge", "Please select another player: ", list);
					dialog.setOnOk(ev -> {
						final List<String> chosenItems = dialog.getSelectedItems();
						String sharingKnowlegeTarget = chosenItems.get(0);
						//System.out.println("Sharing knowledge with " + sharingKnowlegeTarget);

						GameURs.sendShareKnowledgeConsentRequest(
								client, /*client*/
								"Player " + currentPlayer + " wants to share knowlegdge with you.", /*consentPrompt*/
								getGameState().getUserMap().get(Utils.getEnum(RoleType.class, sharingKnowlegeTarget)), /*receivingPlayerName*/
								getGameState().getPositionMap().get(userRole).getName().toString(), /*receivingCityName*/
								getGameState().getUserMap().entrySet().stream().filter(entry -> entry.getValue().equals(currentPlayer))
										.map(Map.Entry::getKey).findFirst().orElse(null), /*givingPlayerRole*/
								RoleType.valueOf(sharingKnowlegeTarget)); /*receivingPlayerRole*/

						//old
						/*client.sendMessageToServer(ServerCommands.INITIATE_CONSENT_REQUIRING_MOVE.name(),
								getGameState().getUserMap().get(Utils.getEnum(RoleType.class, sharingKnowlegeTarget)), "Player " + currentPlayer + " wants to share knowlegdge with you.",
								new UpdateRequest(new PostCondition(PostCondition.ACTION.MOVE_CARD,
										new PlayerCardSimple(CardType.CityCard, "Atlanta"),
										getGameState().getUserMap().entrySet().stream().filter(entry -> entry.getValue().equals(currentPlayer))
												.map(Map.Entry::getKey).findFirst().orElse(null),
										sharingKnowlegeTarget)));
										*/

					});
					dialog.show();
				}
			}
		});

		btnEndTurn.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.END_TURN)));
			}
		});

		for (JLabel j : profPawns) {
			j.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					//reset all profpawnclick = false
					resetProfPawnSelected(profPawnOptions, j);
					//set currentprofpawn click = true or false;
					profPawnOptions.put(j, !profPawnOptions.get(j));
					//get key = role type of j
					RoleType key = null;
					JLabel value = j;
					for (Map.Entry entry : mapPawnProf.entrySet()) {
						if (value.equals(entry.getValue())) {
							key = (RoleType) entry.getKey();
							break; //breaking because its one to one map
						}
					}
					for (Map.Entry<RoleType, List<PlayerCard>> entry : gs.getCardMap().entrySet()) {
						// only load other players' hands, don't load current user's hand
						if (!entry.getKey().equals(userRole)) {
							//List<PlayerCard> cardsInHand = gs.getCardMap().get(key);
							List<PlayerCard> cardsInHand = entry.getValue();
							int i = 0;
							for (PlayerCard card : cardsInHand) {
								JLabel cityCardLabel = mapPlayerCardLabels.get(card.getCardName());
								if (cityCardLabel != null) {
									//cityCardLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(cityCardLabel.getText())).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
									cityCardLabel.setBounds(202 + 115 * i, 55, 100, 140);
									contentPane.add(cityCardLabel);
									contentPane.setComponentZOrder(cityCardLabel, 0);
									i++;
									//if it's not the profpawn selected then hide
									if (entry.getKey() != key) {
										cityCardLabel.setVisible(false);
									}
									//else if it's the profpawn selected, then show if profpawnselected = true, otherwise hide
									else {
										if (profPawnOptions.get(j) == false) cityCardLabel.setVisible(false);
										else if (profPawnOptions.get(j) == true) cityCardLabel.setVisible(true);
									}
								}
							}
						}

					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
					setCursor(cursor);

					RoleType key = null;
					JLabel value = j;
					for (Map.Entry entry : mapPawnProf.entrySet()) {
						if (value.equals(entry.getValue())) {
							key = (RoleType) entry.getKey();
							break; //breaking because its one to one map
						}
					}
					showRole.setText(key.toString());
					showRole.setForeground(Color.WHITE);
					showRole.setBounds(20, 30, 160, 20);
					showRole.setOpaque(false);
					showRole.setVisible(true);
					contentPane.add(showRole);
					contentPane.setComponentZOrder(showRole, 0);

				}

				/*Mouse exit, pointer to cursor*/
				public void mouseExited(MouseEvent e) {
					Cursor cursor = Cursor.getDefaultCursor();
					setCursor(cursor);

					showRole.setVisible(false);
				}
			});
		}

	}

	/*Helper method*/
	private int[] getCityPosition(String labelText) {
		String X = "", Y = "";
		int i = 0;
		for (i = 2; i < labelText.length(); i++) {
			if (labelText.charAt(i) == ',') {
				i++;
				break;
			}
			X += labelText.charAt(i);
		}
		for (int j = i; j < labelText.length(); j++) Y += labelText.charAt(j);
		return new int[]{Integer.parseInt(X), Integer.parseInt(Y)};
	}

	private void setShadow(JLabel label, Color c, int thickness) {

		Border border = BorderFactory.createLineBorder(c, thickness);
		label.setBorder(border);

	}

	private void loadTopBar() {
		redRemaining.setText("" + gs.getRemainingDiseaseCubesMap().get(DiseaseType.Red));
		blueRemaining.setText("" + gs.getRemainingDiseaseCubesMap().get(DiseaseType.Blue));
		yellowRemaining.setText("" + gs.getRemainingDiseaseCubesMap().get(DiseaseType.Yellow));
		blackRemaining.setText("" + gs.getRemainingDiseaseCubesMap().get(DiseaseType.Black));
		infectionRate.setText("" + gs.getCurrentInfectionRate());
		outbreakMeterCount.setText("" + gs.getCurrentOutbreakMeter());
	}

	private void loadCubesOnMap() {
		for (JLabel cubes : cityDiseaseCubes) {
			cubes.setVisible(false);
		}
		cityDiseaseCubes.clear();

		for (JLabel city : citiesLabels) {
			//get city names
			CityName key = null;
			JLabel value = city;
			for (Map.Entry entry : mapCityLabels.entrySet()) {
				if (value.equals(entry.getValue())) {
					key = (CityName) entry.getKey();
					break; //breaking because its one to one map
				}
			}

			//add cubes
			int[][] cubePos = {{city.getX() - 2, city.getY() - 5}, {city.getX() - 2, city.getY() + 5},
					{city.getX() - 9, city.getY() - 5}, {city.getX() - 9, city.getY() + 5}, {city.getX() - 16, city.getY()}};
			if (gs.getDiseaseCubesMap().get(key) != null) {

				List<javafx.util.Pair<DiseaseType, Integer>> cubesTuples = gs.getDiseaseCubesMap().get(key);
				//System.out.println(cubesTuples);
				for (int i = 0; i < cubesTuples.size(); i++) {

					JLabel cubeLabel = new JLabel("" + cubesTuples.get(i).getValue());
					cubeLabel.setFont(new Font("Lao MN", Font.PLAIN, 12));
					switch (cubesTuples.get(i).getKey()) {
						case Red:
							cubeLabel.setForeground(Color.RED);
							break;
						case Blue:
							cubeLabel.setForeground(new Color(30, 144, 255));
							break;
						case Yellow:
							cubeLabel.setForeground(Color.YELLOW);
							break;
						case Purple:
							cubeLabel.setForeground(Color.MAGENTA);
							break;
						case Black:
							cubeLabel.setForeground(Color.WHITE);
							break;
						default:
							break;
					}

					cubeLabel.setBounds(cubePos[i][0], cubePos[i][1], 90, 16);
					contentPane.add(cubeLabel);
					contentPane.setComponentZOrder(cubeLabel, 2);
					cubeLabel.setVisible(true);
					cityDiseaseCubes.add(cubeLabel);

				}
			}
		}
	}


	private void loadResearchStations() {

		//get list<City> from gs
		//draw a station on each list[i], i.e City
/*
		for (City atCity : gs.getResearchStationLocations())
		{
            System.out.println();
			JLabel cityLabel = mapCityLabels.get(atCity.getName());

			//System.out.println(entry.getKey() + "/" + entry.getValue());


			int rsX =  getCityPosition(cityLabel.getText())[0];
			int rsY =  getCityPosition(cityLabel.getText())[1];

			JLabel rsLabel = new JLabel("/pandemic/resources/researchStation.png");


			rsLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(rsLabel.getText())).getImage().getScaledInstance(citySize-2, citySize-2, Image.SCALE_SMOOTH)));
			rsLabel.setBounds(rsX, rsY, citySize-2, citySize-2);
			contentPane.add(rsLabel);
			contentPane.setComponentZOrder(rsLabel, 1);
			rsLabel.setVisible(true);

		}
*/


	}

	private void loadAllPlayerCards() {
		for (Entry<String, JLabel> entry : mapPlayerCardLabels.entrySet()) {
			contentPane.add(entry.getValue());
			contentPane.setComponentZOrder(entry.getValue(), 1);
			entry.getValue().setVisible(true);
		}
	}

	private void loadProfPawns() {
		ArrayList<JLabel> profPawns2 = new ArrayList<JLabel>();
		int i = 0;
		for (RoleType r : gs.getUserMap().keySet()) {
			if (!r.equals(userRole)) {
				//System.out.println(mapPawnProf.get(r));
				profPawns2.add(mapPawnProf.get(r));
				mapPawnProf.get(r).setBounds(10 + 30 * i, 10, 26, 30);
				mapPawnProf.get(r).setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(mapPawnProf.get(r).getText())).getImage().getScaledInstance(26, 30, Image.SCALE_SMOOTH)));

				//System.out.println("Icon path: " +new ImageIcon(new ImageIcon(GUI.class.getResource(mapPawnProf.get(r).getText())).getImage().getScaledInstance(80, 106, Image.SCALE_SMOOTH) ));
				contentPane.add(mapPawnProf.get(r));
				contentPane.setComponentZOrder(mapPawnProf.get(r), 1);
				mapPawnProf.get(r).setVisible(true);
				i++;
			}
		}
		profPawns = profPawns2;
	}

	private void loadPawns() {

		/*Load blue pawn*/

		//System.out.println(gs.getPositionMap().get(medic));

		//load all pawnLabels from positionMap - loop through positionMap

		for (Entry<RoleType, City> entry : gs.getPositionMap().entrySet()) {

			RoleType pawnRole = entry.getKey();
			City atCity = gs.getPositionMap().get(pawnRole);
			JLabel cityLabel = mapCityLabels.get(atCity.getName());
			JLabel pawnLabel = mapPawnLabels.get(pawnRole);
			//System.out.println(entry.getKey() + "/" + entry.getValue());

			List<Unit> cPawns = atCity.getCityUnits().stream().filter(unit -> unit.getUnitType() == UnitType.Pawn)
					.collect(Collectors.toList());
			int numOfPawns = cPawns.size();

			int pawnX = getCityPosition(cityLabel.getText())[0];
			int pawnY = getCityPosition(cityLabel.getText())[1] - 23;

			pawnLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(pawnLabel.getText())).getImage().getScaledInstance(pawnDims[0], pawnDims[1], Image.SCALE_SMOOTH)));
			pawnLabel.setBounds(pawnX, pawnY, pawnDims[0], pawnDims[1]);
			contentPane.add(pawnLabel);
			contentPane.setComponentZOrder(pawnLabel, 1);
			pawnLabel.setVisible(true);

		}

	}

	//draw pawn controlled by user (so it's based on the username, which determines the RoleType)
	private void loadControlPawn() {

//		//Display
		//System.out.println("username  in lcp: " + username);
		userRoleLabel.setText("Role: " + userRole.toString());
//		//userRoleLabel.setFont(new Font("Lao MN", Font.PLAIN, 12));
//		userRoleLabel.setForeground(Color.WHITE);
		userRoleLabel.setBounds(74 - userRole.toString().length(), 575, 190, 16);
//		contentPane.add(userRoleLabel);
//		contentPane.setComponentZOrder(userRoleLabel, 1);
//		userRoleLabel.setVisible(true);
//
//
//		controlPawn = new JLabel();
		String controlPawnIconPath = mapPawnLabels.get(userRole).getText();
//		//System.out.println(controlPawnIconPath);
		controlPawn.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(controlPawnIconPath)).getImage().getScaledInstance(80, 106, Image.SCALE_SMOOTH)));
//		controlPawn.setBounds(74, 595, 80, 106);
//		contentPane.add(controlPawn);
//		contentPane.setComponentZOrder(controlPawn, 1);
//		controlPawn.setVisible(true);

	}

	private void loadActionsRemaining() {
		if (username.equals(gs.getCurrentPlayer())) {
			actionsRemaining.setText("Actions remaining: " + gs.getCurrentPlayerActionsRemaining());
		} else {
			actionsRemaining.setText("Actions remaining: 0");
		}
	}

	//lpc
	private void loadPlayerCards() {
		RoleType userRole = null;
		String value = username;
		for (Entry entry : gs.getUserMap().entrySet()) {
			if (value.equals(entry.getValue())) {
				userRole = (RoleType) entry.getKey();
				break; //breaking because its one to one map
			}
		}

		int i = 1;
		// System.out.println("gs : " + gs);
		// System.out.println("gs.getCardMap: " +gs.getCardMap());
		// System.out.println("gs.getUserMap: " +gs.getUserMap());
		//System.out.println("gs.getCardMap.get(userRole): " +gs.getCardMap().get(userRole));
		System.out.println("CARD MAP TEST: ");

		for (Entry<RoleType, List<PlayerCard>> entry : gs.getCardMap().entrySet()) {
			System.out.println(entry.getKey() + ":");
			// System.out.println("Cards:");
			entry.getValue().forEach(c -> System.out.println("          " + c.getCardName() + " " + c.getCardType()));
		}

		discoverCureButton.setVisible(false);
		discoverCureDiscardCards.clear();

		// System.out.println("username: " +username);
		// System.out.println("userRole: " + userRole);
		for (PlayerCard playerCard : gs.getCardMap().get(userRole)) {

			JLabel playerCardLabel = mapPlayerCardLabels.get(playerCard.getCardName());
			if (playerCardLabel != null) {
				/*System.out.println(cityCardLabel.getText());
                URL resource = GUI.class.getResource(cityCardLabel.getText());

                if(resource == null) {
                    System.err.println("NO RESOURCE");
                    return;
                }

                cityCardLabel.setIcon(new ImageIcon(new ImageIcon(resource).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
                */
				playerCardLabel.setBounds(202 + 115 * i, 585, 100, 140);
				contentPane.add(playerCardLabel);
				contentPane.setComponentZOrder(playerCardLabel, 1);
				playerCardLabel.setVisible(true);
				i++;

				//remove & add mouselisteners to each cityCardLabel after each draw
				MouseListener[] mouseListeners = playerCardLabel.getMouseListeners();
				for (MouseListener mouseListener : mouseListeners) {
					playerCardLabel.removeMouseListener(mouseListener);
				}
				playerCardLabel.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {


						System.out.println("Card clicked: " + playerCard.getCardName());
						if (moves.get("discoverCure") == true) {
							if (playerCard.getCardType().equals(CardType.CityCard)) {
								if (playerCardLabel.getY() < 585) {
									playerCardLabel.setLocation(playerCardLabel.getX(), playerCardLabel.getY() + 20);
									if (discoverCureDiscardCards.contains(playerCard)) {
										discoverCureDiscardCards.remove(playerCard);
									}
								} else if (playerCardLabel.getY() == 585) {
									playerCardLabel.setLocation(playerCardLabel.getX(), playerCardLabel.getY() - 20);
									//make sure it's a CityCard before adding
									if (playerCard.getCardType().equals(CardType.CityCard))
										discoverCureDiscardCards.add((CityCard) playerCard);


								}
								System.out.print("Selected cards to discoverCure: ");
								for (CityCard c : discoverCureDiscardCards) System.out.print(c.getCardName() + ", ");
								System.out.println();

								loadDiscoverCureButton();
							}
						} else if (moves.get("discard")) {
							//card up => discardButton show up
							//card down => discardButtion go away
							//set playerCardToDiscard (CityCard) = that card

							loadPlayerCards();

							if (playerCardLabel.getY() < 585)
							{
								playerCardLabel.setLocation(playerCardLabel.getX(), playerCardLabel.getY() + 20);
								playerCardToDiscard = null;
							}
							else if (playerCardLabel.getY() == 585)
							{
								playerCardLabel.setLocation(playerCardLabel.getX(), playerCardLabel.getY() - 20);
								playerCardToDiscard = playerCard;
							}
							System.out.println("Selected card to discard " + playerCardToDiscard.getCardName());

							System.out.println();





						} else if (moves.get("playEventCard")) {
							//make sure it's an eventCard
							if (playerCard.getCardType().equals(CardType.EventCard)) {
								//once clicked, depends which event card, display corresponding gui elements
								//for e.g if government grant is clicked => display targeted city to build station on
								//so bunch of if else (if(mapPlayerCard.get(playerCard.getCardName().equals("GovernmentGrant")...)
							}
						}

					}

					private void loadDiscoverCureButton() {

						if (discoverCureDiscardCards.size() < 2) {
							discoverCureButton.setVisible(false);
							return;
						}

						//every card in the list must have the same color(region)
						//otherwise return, i.e won't display cureButton
						Region region = discoverCureDiscardCards.get(0).getRegion();
						for (CityCard c : discoverCureDiscardCards) {
							if (!c.getRegion().equals(region)) {
								discoverCureButton.setVisible(false);
								return;
							}
						}

						System.out.println("DiscoverCure button should appear");
						discoverCureButton.setText("Cure " + region + " disease");
						discoverCureButton.setHorizontalAlignment(SwingConstants.CENTER);
						discoverCureButton.setVerticalAlignment(SwingConstants.CENTER);
						discoverCureButton.setForeground(Color.WHITE);
						discoverCureButton.setBackground(Color.BLUE);
						discoverCureButton.setBounds(566, 500, 150, 30);
						discoverCureButton.setOpaque(true);
						discoverCureButton.setVisible(true);
						contentPane.add(discoverCureButton);
						contentPane.setComponentZOrder(discoverCureButton, 0);

					}


					//-----Cursor-----
					@Override
					public void mouseEntered(MouseEvent e) {
						Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
						setCursor(cursor);
					}

					/*Mouse exit, pointer to cursor*/
					public void mouseExited(MouseEvent e) {
						Cursor cursor = Cursor.getDefaultCursor();
						setCursor(cursor);
					}


				});
				//cityCardLabel.removeMouseListener(this);

			}

		}
		
		
		/*
		JLabel yo = new JLabel("New label");
		
		yo.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/Tokyo.png")).getImage().getScaledInstance(80, 140, Image.SCALE_SMOOTH)));
		yo.setBounds(42, 567, 80, 140);
		contentPane.add(yo);*/
	}

	private void loadPlayerDiscardCards() {
		//System.out.println("CARDS IN PLAYER DISCARD PILE: " + gs.getPlayerDiscardPile().getCardsInPile());
		//System.out.println("PLAYER DISCARD PILE: " + gs.getPlayerDiscardPile());
		int i = 1;
		int k = 0;
		for (PlayerCard cityCard : gs.getPlayerDiscardPile().getCardsInPile()) {

			JLabel cityCardLabel = mapPlayerCardLabels.get(cityCard.getCardName());
			//System.out.println(cityCard.getCardName());
			if (cityCardLabel != null) {
				//cityCardLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(cityCardLabel.getText())).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
				cityCardLabel.setBounds(108, 60, 90, 120);
				contentPane.add(cityCardLabel);
				contentPane.setComponentZOrder(cityCardLabel, i);
				cityCardLabel.setVisible(true);
				k++;
				i++;

				//remove all mouseLisners from discarded cards since we don't need them anymore
				MouseListener[] mouseListeners = cityCardLabel.getMouseListeners();
				for (MouseListener mouseListener : mouseListeners) {
					cityCardLabel.removeMouseListener(mouseListener);
				}
			}

		}

	}
	//lifdc
	private void loadInfectionDiscardCards() {
		int i = 1;
		//int k = 0;
		for (InfectionCard cityCard : gs.getInfectionDiscardPile().getCards()) {
			JLabel cityCardLabel = mapInfectionCardLabels.get(cityCard.getCardName());
			if (cityCardLabel != null) {
				//cityCardLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(cityCardLabel.getText())).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
				cityCardLabel.setBounds(108, 190, 90, 120);
				contentPane.add(cityCardLabel);
				contentPane.setComponentZOrder(cityCardLabel, i);
				cityCardLabel.setVisible(true);
				//k++;
				i++;

			}

		}

	}

	private void showInfectCities() {
		//wait 3 seconds before infect (after end turn pressed)
		/*
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("2 last cards: " + gs.getInfectionDiscardPile().getCards().get(0).getCardName() + " " + gs.getInfectionDiscardPile().getCards().get(1).getCardName() );
		//show 2 cards in the middle of the screen + message : "Infect cities"
		for(int i=0;i < gs.getCurrentInfectionRate(); i++)
		{
			JLabel infectionCardLabel = mapInfectionCardLabels.get(gs.getInfectionDiscardPile().getCards().get(i).getCardName());
			infectionCardLabel.setBounds(400+i*130, 200,100,140);
			contentPane.add(infectionCardLabel);
			contentPane.setComponentZOrder(infectionCardLabel,0);
			infectionCardLabel.setVisible(true);
		}

		//set timer 3 seconds
		try {
			sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		//move them back to infectionDiscardPile


	}

	private void loadTargetsDrive() {
		for (JLabel target : targetsDrive) {
			target.setVisible(false);
		}
		targetsDrive.clear();
		for (City c : gs.getPositionMap().get(userRole).getNeighbors()) {
			//city => cityLabel
			JLabel cityLabel = mapCityLabels.get(c.getName());

			//create a target label
			JLabel target = new JLabel();
			target.setBounds(cityLabel.getX() - 5, cityLabel.getY() - 5, 40, 40);
			target.setIcon(iconTarget);

			contentPane.add(target);
			target.setVisible(false);
			//add to targetsDrive
			targetsDrive.add(target);

		}
	}

	private void loadTargetsDirectFlight() {
		//loops through player's hand, only check cityCard (if getCardType().equals(CardType.CityCard)
		//each card's name => map to city label

		for (JLabel target : targetsDirectFlight) {
			target.setVisible(false);
		}
		targetsDirectFlight.clear();
		for (PlayerCard pc : gs.getCardMap().get(userRole)) {
			if (pc.getCardType().equals(CardType.CityCard)
					&& !pc.getCardName().equals(gs.getPositionMap().get(userRole).getName().toString())) {
				JLabel cityLabel = mapCityLabels.get(CityName.valueOf(pc.getCardName()));
				//System.out.println(pc.getCardName());
				JLabel targetDirectFlight = new JLabel();
				targetDirectFlight.setBounds(cityLabel.getX() - 5, cityLabel.getY() - 5, 40, 40);
				targetDirectFlight.setIcon(iconTarget);

				contentPane.add(targetDirectFlight);
				//		contentPane.setComponentZOrder(targetDirectFlight,1);
				targetDirectFlight.setVisible(false);
				//add to targetsDirectFlight
				targetsDirectFlight.add(targetDirectFlight);


			}
		}
		// currentCity = gs.getPositionMap().get(userRole).getName();
	}

	private void loadTargetsCharterFlight() {
		for (JLabel target : targetsCharterFlight) {
			target.setVisible(false);
		}
		targetsCharterFlight.clear();
		for (JLabel cityLabel : mapCityLabels.values()) {
			if (!mapCityLabels.get(gs.getPositionMap().get(userRole).getName()).equals(cityLabel)) {
				//create a target label
				JLabel target = new JLabel();
				target.setBounds(cityLabel.getX() - 5, cityLabel.getY() - 5, 40, 40);
				target.setIcon(iconTarget);

				contentPane.add(target);
				target.setVisible(false);
				//add to targetsDrive
				targetsCharterFlight.add(target);
			}
		}
	}

	private void loadTargetsShuttleFlight() {
		/*for(JLabel target : targetsShuttleFlight){
			target.setVisible(false);
		}
		targetsShuttleFlight.clear();
		//check if player is at a city which has a research station
		//display targets on cities that have research station only
		//but not the one where the player is at => figure out which city he's at
		//we have in gs List<City> rsPosList
		List<City> rsPosList = gs.getResearchStationLocations();
		for(City c: rsPosList)
		{
			if(!c.getName().toString().equals(gs.getPositionMap().get(userRole).getName().toString()))
			{
				JLabel cityLabel = mapCityLabels.get(c.getName());

				//create a target label
				JLabel target = new JLabel();
				target.setBounds(cityLabel.getX()+5,cityLabel.getY()+5,40,40);
				target.setIcon(iconTarget);

				contentPane.add(target);
				target.setVisible(false);
				//add to targetsShuttleFlight
				targetsShuttleFlight.add(target);

			}

		}
*/
		//gs.getPositionMap().get(userRole).getName().toString())

	}

	private void loadDriveAndFlightMessage() {
		genericBox.setForeground(Color.WHITE);
		genericBox.setOpaque(false);
		genericBox.setText("Choose city to move to");
		genericBox.setBounds(600, 520, 200, 50);
		contentPane.setComponentZOrder(genericBox, 0);
		genericBox.setVisible(true);

	}

	//then showHideTargetsRS calls loadTargetRS
	private void loadTargetsRS() {
		targetsRS.forEach(rs -> rs.setVisible(false));
		targetsRS.clear();
		List<CityName> rst = new ArrayList<>(Arrays.asList(CityName.Paris, CityName.Istanbul));
		for (CityName c : rst) {
			JLabel target = new JLabel();
			target.setBounds(mapCityLabels.get(c).getX() - 5, mapCityLabels.get(c).getY() - 5, 40, 40);
			target.setIcon(iconTarget);
			contentPane.add(target);
			contentPane.setComponentZOrder(target, 1);
			target.setVisible(false);
			targetsRS.add(target);

		}

	}


	private void showHideTreatDiseaseMessage() {
		//System.out.println("Inside loadtreatMessage-Before looping diseasecubesmap");
		//current position of user
		optionRedDisease.setBounds(586, 130, 150, 36);
		contentPane.setComponentZOrder(optionRedDisease, 0);
		optionBlueDisease.setBounds(586, 210, 150, 36);
		contentPane.setComponentZOrder(optionBlueDisease, 0);
		optionYellowDisease.setBounds(586, 300, 150, 36);
		contentPane.setComponentZOrder(optionYellowDisease, 0);
		optionBlackDisease.setBounds(586, 390, 150, 36);
		contentPane.setComponentZOrder(optionBlackDisease, 0);

		//cubesTuples.forEach(p-> System.out.println("yo" + p.getKey()));
		//  System.out.println( cubesInCity + " shalalala");
		int i = 0;
		for (Pair<DiseaseType, Integer> d : this.cubesInCity = gs.getDiseaseCubesMap().get(gs.getPositionMap().get(userRole).getName())) {
			System.out.println("Check treat disease options: " + d.getKey().toString() + " " + d.getValue());
			JLabel t = new JLabel();
			if (d.getKey().equals(DiseaseType.Red)) {
				t = optionRedDisease;
				optionRedDisease.setVisible(!optionRedDisease.isVisible());
				System.out.println("Option red should appear");
			} else if (d.getKey().equals(DiseaseType.Blue)) {
				t = optionBlueDisease;
				optionBlueDisease.setVisible(!optionBlueDisease.isVisible());
				System.out.println("Option blue should appear");
			} else if (d.getKey().equals(DiseaseType.Yellow)) {
				t = optionYellowDisease;
				optionYellowDisease.setVisible(!optionYellowDisease.isVisible());
				System.out.println("Option yellow should appear");
			} else if (d.getKey().equals(DiseaseType.Black)) {
				t = optionBlackDisease;
				optionBlackDisease.setVisible(!optionBlackDisease.isVisible());
				System.out.println("Option black should appear");
			}
			t.setLocation(586, 130 + i * 55);
			t.setText("" + d.getKey());
			t.setOpaque(false);
			//t.setSize(150,50);
			//chooseCube.setBounds(586, 256, 68, 36);
			t.setForeground(Color.WHITE);
			String path = "";
			switch (d.getKey()) {
				case Red:
					path = redCubesRemPath;
					break;
				case Blue:
					path = blueCubesRemPath;
					break;
				case Yellow:
					path = yellowCubesRemPath;
					break;
				case Purple:
					path = purpleCubesRemPath;
					break;
				case Black:
					path = blackCubesRemPath;
					break;
				default:
					break;
			}

			t.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(path)).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
			contentPane.setComponentZOrder(t, 0);
			//contentPane.add(t);
			i++;

		}
		System.out.println("treat disease message clicked");
		//JLabel treatDiseaseBoxx = new JLabel();
		//treatDiseaseBox.setVisible(false);
		treatDiseaseBox.setText("");
		treatDiseaseBox.setBackground(Color.BLACK);
		treatDiseaseBox.setBounds(486, 100, 300, 400);
		treatDiseaseBox.setVisible(!treatDiseaseBox.isVisible());
		treatDiseaseBox.setOpaque(true);
		// create a line border with the specified color and width

		setShadow(treatDiseaseBox, Color.ORANGE, 3);

		contentPane.add(treatDiseaseBox);
		contentPane.setComponentZOrder(treatDiseaseBox, 1);
//        genericBox.setVisible(true);
//        genericBox.setForeground(Color.WHITE);
//        genericBox.setBackground(Color.BLACK);

//        genericBox.setOpaque(true);
	}

	private void showHideBuildRSButton() {

		buildRSButton.setText("Build station");
		buildRSButton.setHorizontalAlignment(SwingConstants.CENTER);
		buildRSButton.setVerticalAlignment(SwingConstants.CENTER);
		buildRSButton.setBounds(566, 500, 150, 30);
		buildRSButton.setForeground(Color.white);
		buildRSButton.setBackground(Color.orange);
		buildRSButton.setOpaque(true);
		buildRSButton.setFocusPainted(false);
		contentPane.add(buildRSButton);
		contentPane.setComponentZOrder(buildRSButton, 0);
		buildRSButton.setVisible(!buildRSButton.isVisible());
		System.out.println("brs click " + buildRSButton.isVisible());

	}

	private void loadBtnEndTurn() {
		if (username.equals(gs.getCurrentPlayer())) {
//			btnEndTurn.setText("END TURN");
//			btnEndTurn.setBounds(11, 530, 176, 20);
//			btnEndTurn.setBackground(Color.RED);
//			btnEndTurn.setForeground(Color.WHITE);
//
//			contentPane.add(btnEndTurn);
//			contentPane.setComponentZOrder(btnEndTurn,1);
			btnEndTurn.setEnabled(true);
			btnEndTurn.setBackground(Color.RED);
		} else {
//			btnEndTurn.setText("END TURN");
////			btnEndTurn.setBounds(11, 530, 176, 20);
////			btnEndTurn.setBackground(Color.RED);
////			btnEndTurn.setForeground(Color.WHITE);
////
////			contentPae.add(btnEndTurn);
////			contentPane.setComponentZOrder(btnEndTurn,1);
			btnEndTurn.setEnabled(false);
			btnEndTurn.setBackground(Color.DARK_GRAY);
		}

	}

	private void loadInfectNextCityButton() {
		if (gs.getInfectionsRemaining() != 0) {
			btnInfectNextCity.setEnabled(true);
			btnInfectNextCity.setBackground(Color.GREEN);
		} else {
			btnInfectNextCity.setEnabled(false);
			btnInfectNextCity.setBackground(Color.DARK_GRAY);
		}
	}

	private void loadTooManyCardsMessage()
	{
		//a box with 2 buttons: discardOptionButton and playEventOptionButton

		discardOptionButton.setVisible(true);
		playEventOptionButton.setVisible(true);
		optionDiscardButton.setVisible(true);
		//btnDriveFerry.setText(""+discardCardButton.isVisible());
	}


	private void hideTreatDiseaseOptions(){
		optionBlueDisease.setVisible(false);
		optionRedDisease.setVisible(false);
		optionBlackDisease.setVisible(false);
		optionYellowDisease.setVisible(false);
		treatDiseaseBox.setVisible(false);
	}

	private void loadGenericMessageBox(){
		if(username.equals(gs.getCurrentPlayer())) {
			genericBox.setText("YOUR TURN");

		}
		else {
			genericBox.setText(gs.getCurrentPlayer() + "'s turn.");
		}
		genericBox.setBackground(null);
		genericBox.setOpaque(false);
		genericBox.setBounds(600, 520, 200, 50);
		genericBox.setVisible(true);
	}


	/*private void loadEndTurn(){

		btnEndTurn.setBounds(1100, 600, 70, 45);
		//btnEndTurn.setBackground(Color.gray);
		//btnEndTurn.setOpaque(true);
		btnEndTurn.setForeground(Color.BLACK);
		btnEndTurn.setFont(new Font("Dialog", Font.PLAIN, 9));

	}*/

	private void loadOtherPlayersProfile()
	{
		//display other pawns
		for(RoleType r : gs.getUserMap().keySet())
		{
			JLabel j = new JLabel();
			j = mapPawnLabels.get(r);
		}
		int j = 0;
		for(Entry<RoleType, List<PlayerCard>>  entry : gs.getCardMap().entrySet())
		{
			if(!entry.getKey().equals(userRole))
			{
				//draw pawn
				int i = 0;
				for(PlayerCard cityCard : entry.getValue())
				{
					if(mapPlayerCardLabels.get(cityCard.getCardName()) != null)
					{
						//System.out.print(mapPlayerCardLabels.get(cityCard.getCardName()) + " ");
						mapPlayerCardLabels.get(cityCard.getCardName()).setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(mapPlayerCardLabels.get(cityCard.getCardName()).getText())).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
						mapPlayerCardLabels.get(cityCard.getCardName()).setBounds(302 + i*120,185 + j*140,100,140);
						contentPane.add(mapPlayerCardLabels.get(cityCard.getCardName()));
						i++;
					}

					System.out.println();

				}

			}

		}

	}


	private void resetDisplayOptions(ArrayList<ArrayList<JLabel>> lists){
			lists.forEach(list ->list.forEach(j -> j.setVisible(false)));
	}

	private void showHideOtherActionButtons(List<JButton> list, JButton jb){
		for(JButton b : list)
		{
			if(!b.equals(jb))
			{
				if(b.getForeground().equals(Color.BLACK)) {b.setForeground(Color.GRAY); jb.setBackground(new Color(66, 128, 244));}
				else {b.setForeground(Color.BLACK); jb.setBackground(new Color(173,188,204));}
			}
		}
	}
	private void resetMovesSelected(Map<String,Boolean> moves){
		for(String m : moves.keySet()) moves.put(m,false);

	}

	private void resetProfPawnSelected(Map<JLabel, Boolean> profPawnOptions, JLabel jSelected)
	{
		for(JLabel j : profPawnOptions.keySet())
		{
			if(j != jSelected) profPawnOptions.put(j,false);
		}
	}

	private RoleType getUserRole()
	{
		RoleType userRole= null;
        String value= username;
        System.out.println("userMap ");
        for(Map.Entry entry: gs.getUserMap().entrySet()){
            if(value.equals(entry.getValue())){
                userRole = (RoleType) entry.getKey();
                break; //breaking because its one to one map
            }
        }

       // System.out.println("userRole in getUserRole: " + userRole);
       // System.out.println("username in getUserRole: " + username);

        return userRole;
	}

	public JPanel getContentPane()
	{
		return this.contentPane;
	}


	public GameState getGameState()
	{
		return gs;
	}

	public void setGameState(GameState newGS)
	{
		this.gs = newGS;
		userRole = getUserRole();
        System.out.println("userRole in setGameState: " + userRole);

        currentUserCity = gs.getPositionMap().get(userRole);
        createNewEventWrapper();

	}


	public void drawAcceptDeclineMessageBox(String consentPrompt)
	{
		genericBox.setText(consentPrompt);
		genericBox.setHorizontalAlignment(SwingConstants.CENTER);
		genericBox.setVerticalAlignment(SwingConstants.CENTER);
		genericBox.setForeground(Color.WHITE);
		genericBox.setBackground(Color.BLACK);
		genericBox.setBounds(486,200,400,100);
		genericBox.setOpaque(true);

		acceptButton.setText("Accept");
		acceptButton.setHorizontalAlignment(SwingConstants.CENTER);
		acceptButton.setVerticalAlignment(SwingConstants.CENTER);
		acceptButton.setBackground(Color.GREEN);
		acceptButton.setBounds(586,300,50,30);
		acceptButton.setOpaque(true);
		//contentPane.add(acceptButton);
		contentPane.setComponentZOrder(acceptButton,0);

		declineButton.setText("Decline");
		declineButton.setHorizontalAlignment(SwingConstants.CENTER);
		declineButton.setVerticalAlignment(SwingConstants.CENTER);
		declineButton.setBackground(Color.RED);
		declineButton.setBounds(650,300,50,30);
		declineButton.setOpaque(true);
		//contentPane.add(declineButton);
		contentPane.setComponentZOrder(declineButton,0);

	}
	public void drawReceiveMessage(String message, MessageType type)
	{
		genericBox.setText(type.toString() + ": " + message);
		genericBox.setHorizontalAlignment(SwingConstants.CENTER);
		genericBox.setVerticalAlignment(SwingConstants.CENTER);
		genericBox.setForeground(Color.WHITE);
		genericBox.setBackground(new Color (31, 145, 86));

		genericBox.setBounds(486,200,400,200);
		genericBox.setOpaque(true);

		genericBox.setVisible(true);
		contentPane.setComponentZOrder(genericBox,0);

		/*acceptButton.setText("OK");
		acceptButton.setBackground(Color.GREEN);
		acceptButton.setBounds(486,300,50,50);
		acceptButton.setOpaque(true);*/

        System.out.println("draw receive message drawn.");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	public void enableDiscardCardButton()
	{
		discardCardButton.setVisible(true);
		discardCardButton.setText("Discard");
		discardCardButton.setHorizontalAlignment(SwingConstants.CENTER);
		discardCardButton.setVerticalAlignment(SwingConstants.CENTER);
		discardCardButton.setBackground(Color.ORANGE);
		discardCardButton.setBounds(486,300,50,30);
		discardCardButton.setOpaque(true);
		contentPane.setComponentZOrder(discardCardButton,0);
	}



}


