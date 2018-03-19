package pandemic.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.ClientCommands;
import client.PandemicClient;
import javafx.util.Pair;
import pandemic.CardType;
import pandemic.ChallengeKind;
import pandemic.City;
import pandemic.CityName;
import pandemic.DiseaseType;
//import pandemic.GameManager;
import pandemic.InfectionCard;
//import pandemic.Player;
import pandemic.PlayerCard;
import pandemic.RoleType;
import pandemic.Unit;
import pandemic.UnitType;
//import pandemic.User;
import server.PandemicServer;
import server.ServerCommands;
import shared.GameState;
import shared.MessageType;
import shared.TravelType;
import shared.request.PostCondition;
import shared.request.UpdateRequest;

import static java.lang.Thread.sleep;

/**
 * Purpose: Create GUI components + event listeners
 *
 **/

/**TO DO: 
 * - Add city names
 * - Draw lines
 * - Fix targetsDrive
 * - Add cards to player's hand from playerDeck
 * - Discard cards
 * - Show infection cards => Infect citiesLabels
 * - Add/remove cubes
 * 
 * 
 * -....
 **/
//only click on ok city-directflight
public class GUI extends JFrame
{
	
	
	private GameState gs;
	private String username;
	private RoleType userRole;
	private PandemicClient client;
	private City currentUserCity;
	private List<Pair<DiseaseType,Integer>> cubesInCity;

	private boolean hasNeverBeenDrawnBefore = true;
	private int controller = 0;


	//private boolean[] moves = {driveFerrySelected,directFlightSelected,treatDiseaseSelected,shareKnowledgeSelected};
	private Map<String,Boolean> moves = new HashMap<String,Boolean>()
	{{
		put("drive",false);
		put("directFlight",false);
		put("treatDisease",false);
		put("shareKnowledge",false);
	}};
	private CityName cityNameSelected= null;

	
	
	/*private CityName Atlanta = CityName.Atlanta;
	private CityName Chicago = CityName.Chicago;
	private CityName Paris = CityName.Paris;
	private CityName Tokyo = CityName.Tokyo;
	private CityName Bangkok = CityName.Bangkok;
	private CityName LosAngeles = CityName.LosAngeles;
	private CityName London = CityName.London;
	private DiseaseType red = DiseaseType.Red;
	private DiseaseType blue = DiseaseType.Blue;
	private DiseaseType purple = DiseaseType.Purple;
	private DiseaseType black = DiseaseType.Black;
	private DiseaseType yellow = DiseaseType.Yellow;
	


	
	
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
	
	private JLabel p1Pawn;
	private JLabel p2Pawn;


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
		LondonCityLabel,StPetersburgCityLabel,WashingtonCityLabel,NewYorkCityLabel,SanFranciscoCityLabel,ChicagoCityLabel,AtlantaCityLabel,MontrealCityLabel,MadridCityLabel,ParisCityLabel,MilanCityLabel,EssenCityLabel,
		SantiagoCityLabel,JohannesburgCityLabel,LimaCityLabel,SaoPauloCityLabel,LosAngelesCityLabel,MexicoCityCityLabel,MiamiCityLabel,BogotaCityLabel,BuenosAiresCityLabel,LagosCityLabel,KinshasaCityLabel,KhartoumCityLabel,
		ChennaiCityLabel,CairoCityLabel,DelhiCityLabel,BaghdadCityLabel,MumbaiCityLabel,KolkataCityLabel,TehranCityLabel,RiyadhCityLabel,KarachiCityLabel,IstanbulCityLabel,AlgiersCityLabel,MoscowCityLabel,
		TokyoCityLabel,JakataCityLabel,SeoulCityLabel,ManilaCityLabel,OsakaCityLabel,BeijingCityLabel,HongKongCityLabel,TapeiCityLabel,ShanghaiCityLabel,BangkokCityLabel,HoChiMinhCityCityLabel,SydneyCityLabel
		));	
	
	
	/*Event Cards*/
	private JLabel AirLiftCardLabel = new JLabel("/pandemic/resources/PlayerCards/AirLiftEventCard.png");
	private JLabel OneQuietNightCardLabel = new JLabel("/pandemic/resources/PlayerCards/OneQuietNightEventCard.png");
	private JLabel ResilientPopulationCardLabel = new JLabel("/pandemic/resources/PlayerCards/ResilientPopulationEventCard.png");
	private JLabel GovernmentGrantCardLabel = new JLabel("/pandemic/resources/PlayerCards/GovernmentGrantEventCardLabel.png");
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
	private JPanel contentPane = new JPanel();
	
	private ArrayList<JLabel> targetsDrive = new ArrayList<JLabel>();
	private ArrayList<JLabel> targetsDirectFlight = new ArrayList<JLabel>();
	/*TreatdiseaseOptions container*/
	private JLabel genericBox;
	private JLabel optionRedDisease = new JLabel();
	private JLabel optionBlueDisease = new JLabel();
	private JLabel optionYellowDisease = new JLabel();
	private JLabel optionBlackDisease = new JLabel();
	private JButton btnEndTurn = new JButton("END TURN");
	//private ArrayList<JLabel> optionsTreatDisease = new ArrayList<JLabel>(Arrays.asList(genericBox));
	private ArrayList<ArrayList<JLabel>> displayOptions = new ArrayList<ArrayList<JLabel>>
	(Arrays.asList(targetsDrive, targetsDirectFlight));
	/*Pawn dimensions*/
	private final int[] pawnDims = {30,40}; //{width,height}
	
	/*city size (width)*/
	private final int citySize = 30;
	
	/*cityCardsLabels */
	private ArrayList<JLabel> cityCardsLabels = new ArrayList<JLabel>();
	private ArrayList<String> cityCardImagePaths = new ArrayList<String>();
	
	
	/*PlayerDeck*/
	private JLabel playerDeck = new JLabel(" Player Deck");;
	
	/*InfectionDeck*/
	private JLabel infectionDeck = new JLabel(" Infection Deck");; 
	
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

	/*Action buttons*/
	private JButton btnDriveFerry = new JButton("Drive");;
	private JButton btnDirectFlight = new JButton("<html>Direct <br> Flight</html>");;
	private JButton btnCharterFlight = new JButton("<html>Charter<br> Flight</html>");
	private JButton btnShuttleFlight = new JButton("<html>Shuttle <br> Flight</html>");;
	private JButton btnBuildResearch = new JButton("<html>Build<br>research</html>");
	private JButton btnTreatDisease = new JButton("<html>Treat <br> Disease</html>");
	private JButton btnShareKnowledge = new JButton("<html>Share<br> Knowledge</html>");
	private JButton btnDiscoverCure = new JButton("<html>Discover <br> a Cure</html>");
	
	/*Actions remaining*/
	private JLabel actionsRemaining = new JLabel("Actions remaining: 4");
	
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
	
	/*Target icon*/
	private final String targetIconPath = "/pandemic/resources/target.png";
	
	private Icon iconTarget = new ImageIcon(new ImageIcon(GUI.class.getResource(targetIconPath))
			.getImage().getScaledInstance(40, 40,  Image.SCALE_SMOOTH));
	
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
	
	//================================================================================================
	//================================================================================================
	//====================================MAP=========================================================
	//================================================================================================
	//================================================================================================
	/*RoleType <---> PawnLabel*/
	private Map<RoleType,JLabel> mapPawnLabels = new HashMap<RoleType,JLabel>()
	{{
		put(RoleType.Medic,pinkPawn);
		put(RoleType.Scientist,greenPawn);
		put(RoleType.Researcher,yellowPawn);
		put(RoleType.ContingencyPlanner,orangePawn);
		put(RoleType.Dispatcher,purplePawn);
		put(RoleType.OperationsExpert,bluePawn);
		put(RoleType.QuarantineSpecialist,whitePawn);
	}};
	
	
	/*CityName <---> CityLabel*/
	private Map<CityName, JLabel> mapCityLabels = new HashMap<CityName,JLabel>()
	{{
		
		put(CityName.SanFrancisco,SanFranciscoCityLabel);
		put(CityName.Chicago,ChicagoCityLabel);
		put(CityName.Montreal,MontrealCityLabel);
		put(CityName.NewYork,NewYorkCityLabel);//
		put(CityName.Atlanta,AtlantaCityLabel);
		put(CityName.StPetersburg,StPetersburgCityLabel);//
		put(CityName.London,LondonCityLabel);//
		put(CityName.Essen,EssenCityLabel);
		put(CityName.Washington,WashingtonCityLabel);//
		put(CityName.Madrid,MadridCityLabel);
		put(CityName.Paris,ParisCityLabel);
		put(CityName.Milan,MilanCityLabel);
		
		put(CityName.LosAngeles,LosAngelesCityLabel);
		put(CityName.MexicoCity,MexicoCityCityLabel);
		put(CityName.Miami,MiamiCityLabel);
		put(CityName.Bogota,BogotaCityLabel);
		put(CityName.Lima,LimaCityLabel);
		put(CityName.Santiago,SantiagoCityLabel);
		put(CityName.BuenosAires,BuenosAiresCityLabel);
		put(CityName.SaoPaulo,SaoPauloCityLabel);
		put(CityName.Lagos,LagosCityLabel);
		put(CityName.Kinshasa,KinshasaCityLabel);
		put(CityName.Johannesburg,JohannesburgCityLabel);
		put(CityName.Khartoum,KhartoumCityLabel);
		
		put(CityName.Algiers,AlgiersCityLabel);
		put(CityName.Istanbul,IstanbulCityLabel);
		put(CityName.Moscow,MoscowCityLabel);
		put(CityName.Cairo,CairoCityLabel);
		put(CityName.Baghdad,BaghdadCityLabel);
		put(CityName.Tehran,TehranCityLabel);
		put(CityName.Riyadh,RiyadhCityLabel);
		put(CityName.Karachi,KarachiCityLabel);
		put(CityName.Delhi,DelhiCityLabel);
		put(CityName.Mumbai,MumbaiCityLabel);
		put(CityName.Kolkata,KolkataCityLabel);
		put(CityName.Chennai,ChennaiCityLabel);
		
		put(CityName.Jakarta,JakataCityLabel);//
		put(CityName.Bangkok,BangkokCityLabel);//
		put(CityName.HongKong,HongKongCityLabel);//
		put(CityName.Shanghai,ShanghaiCityLabel);//
		put(CityName.Beijing,BeijingCityLabel);//
		put(CityName.Seoul,SeoulCityLabel);//
		put(CityName.Tokyo,TokyoCityLabel);//
		put(CityName.Osaka,OsakaCityLabel);//
		put(CityName.Taipei,TapeiCityLabel);//
		put(CityName.Manila,ManilaCityLabel);//
		put(CityName.HoChiMinhCity,HoChiMinhCityCityLabel);//
		put(CityName.Sydney,SydneyCityLabel);//
	}};
	
	
	/*CityCard <---> CityCardLabel
	private Map<CityName, JLabel> mapCityCardLabels = new HashMap<CityName,JLabel>()
	{{
		
		put(CityName.SanFrancisco,SanFranciscoCardLabel);
		put(Chicago,ChicagoCardLabel);
		put(CityName.Montreal,MontrealCardLabel);
		put(CityName.NewYork,NewYorkCardLabel);//
		put(Atlanta,AtlantaCardLabel);
		put(CityName.Washington,WashingtonCardLabel);//
		put(CityName.London,LondonCardLabel);//
		put(CityName.Essen,EssenCardLabel);
		put(CityName.StPetersburg,StPetersburgCardLabel);//
		put(CityName.Madrid,MadridCardLabel);
		put(CityName.Paris,ParisCardLabel);
		put(CityName.Milan,MilanCardLabel);
		
		put(LosAngeles,LosAngelesCardLabel);
		put(CityName.MexicoCity,MexicoCityCardLabel);
		put(CityName.Miami,MiamiCardLabel);
		put(CityName.Bogota,BogotaCardLabel);
		put(CityName.Lima,LimaCardLabel);
		put(CityName.Santiago,SantiagoCardLabel);
		put(CityName.BuenosAires,BuenosAiresCardLabel);
		put(CityName.SaoPaulo,SaoPauloCardLabel);
		put(CityName.Lagos,LagosCardLabel);
		put(CityName.Kinshasa,KinshasaCardLabel);
		put(CityName.Johannesburg,JohannesburgCardLabel);
		put(CityName.Khartoum,KhartoumCardLabel);
		
		put(CityName.Algiers,AlgiersCardLabel);
		put(CityName.Istanbul,IstanbulCardLabel);
		put(CityName.Moscow,MoscowCardLabel);
		put(CityName.Cairo,CairoCardLabel);
		put(CityName.Baghdad,BaghdadCardLabel);
		put(CityName.Tehran,TehranCardLabel);
		put(CityName.Riyadh,RiyadhCardLabel);
		put(CityName.Karachi,KarachiCardLabel);
		put(CityName.Delhi,DelhiCardLabel);
		put(CityName.Mumbai,MumbaiCardLabel);
		put(CityName.Kolkata,KolkataCardLabel);
		put(CityName.Chennai,ChennaiCardLabel);
		
		put(CityName.Jakarta,JakartaCardLabel);//
		put(Bangkok,BangkokCardLabel);//
		put(CityName.HongKong,HongKongCardLabel);//
		put(CityName.Shanghai,ShanghaiCardLabel);//
		put(CityName.Beijing,BeijingCardLabel);//
		put(CityName.Seoul,SeoulCardLabel);//
		put(Tokyo,TokyoCardLabel);//
		put(CityName.Osaka,OsakaCardLabel);//
		put(CityName.Taipei,TaipeiCardLabel);//
		put(CityName.Manila,ManilaCardLabel);//
		put(CityName.HoChiMinhCity,HoChiMinhCityCardLabel);//
		put(CityName.Sydney,SydneyCardLabel);//
	}};
	*/

	private Map<String, JLabel> mapPlayerCardLabels = new HashMap<String,JLabel>()
	{{
		
		put("SanFrancisco",SanFranciscoCardLabel);
		put("Chicago",ChicagoCardLabel);
		put("Montreal",MontrealCardLabel);
		put("NewYork",NewYorkCardLabel);//
		put("Atlanta",AtlantaCardLabel);
		put("Washington",WashingtonCardLabel);//
		put("London",LondonCardLabel);//
		put("Essen",EssenCardLabel);
		put("StPetersburg",StPetersburgCardLabel);//
		put("Madrid",MadridCardLabel);
		put("Paris",ParisCardLabel);
		put("Milan",MilanCardLabel);
		
		put("LosAngeles",LosAngelesCardLabel);
		put("MexicoCity",MexicoCityCardLabel);
		put("Miami",MiamiCardLabel);
		put("Bogota",BogotaCardLabel);
		put("Lima",LimaCardLabel);
		put("Santiago",SantiagoCardLabel);
		put("BuenosAires",BuenosAiresCardLabel);
		put("SaoPaulo",SaoPauloCardLabel);
		put("Lagos",LagosCardLabel);
		put("Kinshasa",KinshasaCardLabel);
		put("Johannesburg",JohannesburgCardLabel);
		put("Khartoum",KhartoumCardLabel);
		
		put("Algiers",AlgiersCardLabel);
		put("Istanbul",IstanbulCardLabel);
		put("Moscow",MoscowCardLabel);
		put("Cairo",CairoCardLabel);
		put("Baghdad",BaghdadCardLabel);
		put("Tehran",TehranCardLabel);
		put("Riyadh",RiyadhCardLabel);
		put("Karachi",KarachiCardLabel);
		put("Delhi",DelhiCardLabel);
		put("Mumbai",MumbaiCardLabel);
		put("Kolkata",KolkataCardLabel);
		put("Chennai",ChennaiCardLabel);
		
		put("Jakarta",JakartaCardLabel);//
		put("Bangkok",BangkokCardLabel);//
		put("HongKong",HongKongCardLabel);//
		put("Shanghai",ShanghaiCardLabel);//
		put("Beijing",BeijingCardLabel);//
		put("Seoul",SeoulCardLabel);//
		put("Tokyo",TokyoCardLabel);//
		put("Osaka",OsakaCardLabel);//
		put("Taipei",TaipeiCardLabel);//
		put("Manila",ManilaCardLabel);//
		put("HoChiMinhCity",HoChiMinhCityCardLabel);//
		put("Sydney",SydneyCardLabel);//
		
		put("AirLift",AirLiftCardLabel);
		put("Forecast",ForecastCardLabel);
		put("OneQuietNight",OneQuietNightCardLabel);
		put("GovernmentGrant",GovernmentGrantCardLabel);
		put("ResilientPopulation",ResilientPopulationCardLabel);
		put("BasicEpidemicCard",BasicEpidemicCardLabel);
		
	
	}};
	
	private Map<String, JLabel> mapInfectionCardLabels = new HashMap<String,JLabel>()
	{{
		put("SanFrancisco",SanFranciscoInfectionLabel);
		put("Chicago",ChicagoInfectionLabel);
		put("Montreal",MontrealInfectionLabel);
		put("NewYork",NewYorkInfectionLabel);//
		put("Atlanta",AtlantaInfectionLabel);
		put("Washington",WashingtonInfectionLabel);//
		put("London",LondonInfectionLabel);//
		put("Essen",EssenInfectionLabel);
		put("StPetersburg",StPetersburgInfectionLabel);//
		put("Madrid",MadridInfectionLabel);
		put("Paris",ParisInfectionLabel);
		put("Milan",MilanInfectionLabel);
		
		put("LosAngeles",LosAngelesInfectionLabel);
		put("MexicoCity",MexicoCityInfectionLabel);
		put("Miami",MiamiInfectionLabel);
		put("Bogota",BogotaInfectionLabel);
		put("Lima",LimaInfectionLabel);
		put("Santiago",SantiagoInfectionLabel);
		put("BuenosAires",BuenosAiresInfectionLabel);
		put("SaoPaulo",SaoPauloInfectionLabel);
		put("Lagos",LagosInfectionLabel);
		put("Kinshasa",KinshasaInfectionLabel);
		put("Johannesburg",JohannesburgInfectionLabel);
		put("Khartoum",KhartoumInfectionLabel);
		
		put("Algiers",AlgiersInfectionLabel);
		put("Istanbul",IstanbulInfectionLabel);
		put("Moscow",MoscowInfectionLabel);
		put("Cairo",CairoInfectionLabel);
		put("Baghdad",BaghdadInfectionLabel);
		put("Tehran",TehranInfectionLabel);
		put("Riyadh",RiyadhInfectionLabel);
		put("Karachi",KarachiInfectionLabel);
		put("Delhi",DelhiInfectionLabel);
		put("Mumbai",MumbaiInfectionLabel);
		put("Kolkata",KolkataInfectionLabel);
		put("Chennai",ChennaiInfectionLabel);
		
		put("Jakarta",JakartaInfectionLabel);//
		put("Bangkok",BangkokInfectionLabel);//
		put("HongKong",HongKongInfectionLabel);//
		put("Shanghai",ShanghaiInfectionLabel);//
		put("Beijing",BeijingInfectionLabel);//
		put("Seoul",SeoulInfectionLabel);//
		put("Tokyo",TokyoInfectionLabel);//
		put("Osaka",OsakaInfectionLabel);//
		put("Taipei",TaipeiInfectionLabel);//
		put("Manila",ManilaInfectionLabel);//
		put("HoChiMinhCity",HoChiMinhCityInfectionLabel);//
		put("Sydney",SydneyInfectionLabel);//
	}};

	//========================================================================
	//========================================================================
	//========================================================================
	//========================================================================
	/*Constructor does 2 things: (1) setting up GUI components (2) create event listener for each component*/
	public GUI(String username, PandemicServer server, GameState gameStateTest) {
		
		 /*System.out.println("Creating host player for user 'jbh12'...");
	        User userTest1 = new User("jbh12", "123456", "127.0.0.1");
	        Player playerTest = new Player(userTest1);
	        System.out.println("...hostPlayer created.");

	        System.out.println("Creating GameBoard and initializing game....");
	        GameManager gameManager = new GameManager(playerTest, 3, 6, ChallengeKind.OriginalBaseGame);

	        System.out.println("Players joining game...");
	        User userTest2 = new User("laskdf", "123456", "127.0.0.2");
	        gameManager.joinGame(userTest2);

	        System.out.println("------ACTIVE PLAYER LIST:");
	        gameManager.getActivePlayers().forEach(p -> System.out.println("    Player username:" + p.getPlayerUserName()));
	        System.out.println("-------------------------");

	        User userTest3 = new User("aksjfdkdsjn", "123456", "127.0.0.3");
	        gameManager.joinGame(userTest3);

	        System.out.println("------ACTIVE PLAYER LIST:");
	        gameManager.getActivePlayers().forEach(p -> System.out.println("    Player username:" + p.getPlayerUserName()));
	        System.out.println("-------------------------");

	        System.out.println("All Players joined. Creating game....");

	        gameManager.createNewGame();
	        System.out.println("Game created.");

	        System.out.println("------ACTIVE PLAYER LIST:");
	        gameManager.getActivePlayers().forEach(p -> System.out.println("    Player username:" + p.getPlayerUserName() +
	                                                ", role: " + p.getRoleType()));
	        System.out.println("-------------------------");

	        // Forcing outbreak in Bangkok and Los Angeles:
	        City bangkok = gameManager.getCityByName(CityName.Bangkok);
	        City losAngeles = gameManager.getCityByName(CityName.LosAngeles);
	        City atlanta = gameManager.getCityByName(CityName.Atlanta);
	        
	        gameManager.infectNextCity(atlanta);
	        gameManager.infectNextCity(bangkok);
	        gameManager.infectNextCity(bangkok);
	        gameManager.infectNextCity(bangkok);
	        gameManager.infectNextCity(bangkok);
	        gameManager.infectNextCity(losAngeles);
	        gameManager.infectNextCity(losAngeles);
	        gameManager.infectNextCity(losAngeles);
	        gameManager.infectNextCity(losAngeles);
	        City chennai = gameManager.getCityByName(CityName.Chennai);
	        gameManager.infectNextCity(chennai);*/


        
		this.gs = gameStateTest;
		this.username = username;
		this.userRole = getUserRole();
        System.out.println("userRole in constructor server: " + userRole);


        /*This method is responsible for creating all the event listeners*/



        /*try {
            server.setGame(gameManager.getGame());
        } catch (NullPointerException e) {
            System.out.println(gameManager.getGame());
        }
        System.out.println(server);
        System.out.println(gameManager.getGame());
        System.out.println(gameManager.getGame().generateCondensedGameState());*/


		}

    public GUI(String username, PandemicClient client) {
	    this.username = username;
	    //this.userRole = getUserRole();
        System.out.println("userRole in constructor client: " + userRole);
	    this.client = client;


        /*This method is responsible for creating all the event listeners*/


    }

	//public void receiveNewGameState(GameState gs) {}
	public String getUsername() {
		return username;
	}
		
	public void draw()
	{
		/*This method is responsible for setting up GUI components*/
		initComponents();
		/*This method is responsible for creating all the event listeners*/

	}

	public void createNewEventWrapper() {
	    if(controller == 0) {
	        controller++;
            System.out.println("print CONTROLLER");
            initComponents();
	        createEvents();
        }
    }

	private void initComponents() 
	{

		/*------Set up JFrame and contentPane-----*/
		setTitle("Pandemic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 900);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*----------Set up 8 buttons----------*/

		if(username.equals(gs.getCurrentPlayer()) && gs.getCurrentPlayerActionsRemaining() > 0) {
			//Drive Ferry
			//btnDriveFerry.setIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/icon.png")));
			btnDriveFerry.setBounds(11, 370, 90, 40);
			btnDriveFerry.setFont(new Font("Dialog", Font.PLAIN, 12));
			contentPane.add(btnDriveFerry);

			//Direct Flight
			//btnDirectFlight.setIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/icon.png")));
			btnDirectFlight.setBounds(97, 370, 90, 40);
			btnDirectFlight.setFont(new Font("Dialog", Font.PLAIN, 12));
			contentPane.add(btnDirectFlight);

			//Charter Flight
			btnCharterFlight.setBounds(11, 407, 90, 40);
			btnCharterFlight.setFont(new Font("Dialog", Font.PLAIN, 12));
			contentPane.add(btnCharterFlight);

			//Shuttle Flight
			btnShuttleFlight.setBounds(97, 407, 90, 40);
			btnShuttleFlight.setFont(new Font("Dialog", Font.PLAIN, 12));
			contentPane.add(btnShuttleFlight);

			//Treat Disease
			btnTreatDisease.setBounds(97, 445, 90, 40);
			btnTreatDisease.setFont(new Font("Dialog", Font.PLAIN, 12));
			contentPane.add(btnTreatDisease);

			//Discover Cure
			btnDiscoverCure.setBounds(97, 482, 90, 40);
			btnDiscoverCure.setFont(new Font("Dialog", Font.PLAIN, 12));
			contentPane.add(btnDiscoverCure);

			//Build Research
			btnBuildResearch.setBounds(11, 445, 90, 40);
			btnBuildResearch.setFont(new Font("Dialog", Font.PLAIN, 12));
			contentPane.add(btnBuildResearch);

			//Share Knowledge
			btnShareKnowledge.setBounds(11, 482, 90, 40);
			btnShareKnowledge.setFont(new Font("Dialog", Font.PLAIN, 12));
			contentPane.add(btnShareKnowledge);
		}
		/*-------Set up Actions remaining-------*/
		
		actionsRemaining.setBounds(19, 348, 131, 16);
		actionsRemaining.setForeground(Color.WHITE);
		contentPane.add(actionsRemaining);
	
		/*----------Set up playerDeck----------*/
		playerDeck.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(playerDeckPicPath))
				.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH)));
		playerDeck.setBounds(7, 56, 100, 130);
		contentPane.add(playerDeck);

		/*----------Set up infectionDeck----------*/
		infectionDeck.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(infectionDeckPicPath))
				.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH)));
		infectionDeck.setBounds(7, 185, 100, 130);
		contentPane.add(infectionDeck);

		/*----------Set up playerDiscard----------*/
		playerDiscard.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(playerDiscardPicPath))
				.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH)));
		playerDiscard.setBounds(106, 56, 100, 130);
		contentPane.add(playerDiscard);
		
		/*----------Set up infectionDiscard----------*/
		infectionDiscard.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(infectionDiscardPicPath))
				.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH)));
		infectionDiscard.setBounds(106, 185, 100, 130);
		contentPane.add(infectionDiscard);
		
		/*----------Set up top bar ----------*/
		/* (cubes remaining,instruction,outbreak count, infection rate)*/
		
		//top bar panel container

		topBar.setBackground(Color.BLACK);
		topBar.setBounds(214, 0, 980, 30);
		contentPane.add(topBar);
		topBar.setLayout(null);
		//+gameManager().getDiseaseSupplyByDiseaseType(DiseaseType.Red).size()
		//red cubes remaining
		redRemaining = new JLabel(""+gs.getRemainingDiseaseCubesMap().get(DiseaseType.Red));
		redRemaining.setBackground(Color.BLACK);
		redRemaining.setForeground(Color.WHITE);
		redRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(redCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		redRemaining.setBounds(286, 6, 48, 16);
		topBar.add(redRemaining);
		redRemaining.setOpaque(true);
		
		//blue cubes remaining
		blueRemaining = new JLabel(""+gs.getRemainingDiseaseCubesMap().get(DiseaseType.Blue));
		blueRemaining.setBackground(Color.BLACK);
		blueRemaining.setForeground(Color.WHITE);
		blueRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(blueCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		blueRemaining.setBounds(341, 6, 48, 16);
		topBar.add(blueRemaining);
		blueRemaining.setOpaque(true);

		//yellow cubes remaining
		yellowRemaining = new JLabel(""+gs.getRemainingDiseaseCubesMap().get(DiseaseType.Yellow));
		yellowRemaining.setBackground(Color.BLACK);
		yellowRemaining.setForeground(Color.WHITE);
		yellowRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(yellowCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		yellowRemaining.setBounds(396, 6, 48, 16);
		topBar.add(yellowRemaining);
		yellowRemaining.setOpaque(true);
		
		//purple cubes remaining
		blackRemaining = new JLabel(""+gs.getRemainingDiseaseCubesMap().get(DiseaseType.Black));
		blackRemaining.setBackground(Color.BLACK);
		blackRemaining.setForeground(Color.WHITE);
		blackRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(blackCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		blackRemaining.setBounds(451, 6, 48, 16);
		topBar.add(blackRemaining);
		blackRemaining.setOpaque(true);
		
		//instruction
		instruction = new JLabel("");
		instruction.setBackground(Color.BLACK);
		instruction.setForeground(Color.WHITE);
		instruction.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(instructionIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		instruction.setBounds(616, 6, 48, 16);
		topBar.add(instruction);
		instruction.setOpaque(true);
		
		//infection rate
		infectionRate = new JLabel(""+gs.getCurrentInfectionRate());
		infectionRate.setBackground(Color.BLACK);
		infectionRate.setForeground(Color.WHITE);
		infectionRate.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(infectionRateIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		infectionRate.setBounds(506, 6, 48, 16);
		topBar.add(infectionRate);
		infectionRate.setOpaque(true);
	
		//outbreak meter count
		outbreakMeterCount = new JLabel(""+gs.getCurrentOutbreakMeter());
		outbreakMeterCount.setBackground(Color.BLACK);
		outbreakMeterCount.setForeground(Color.WHITE);
		outbreakMeterCount.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(outbreakMeterCountIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		outbreakMeterCount.setBounds(561, 6, 48, 16);
		topBar.add(outbreakMeterCount);
		outbreakMeterCount.setOpaque(true);
		

		/*---popups/messages/optionDisplay..---*/
		acceptButton = new JLabel();
		declineButton = new JLabel();
		genericBox = new JLabel();
		discardCardButton = new JLabel();

		contentPane.add(btnEndTurn);
		contentPane.add(optionRedDisease);
		contentPane.add(optionYellowDisease);
		contentPane.add(optionBlueDisease);
		contentPane.add(optionBlackDisease);


		contentPane.add(discardCardButton);
		contentPane.add(genericBox);
		contentPane.add(acceptButton);
		contentPane.add(declineButton);



		loadPlayerCards();
		loadPlayerDiscardCards();
		loadInfectionDiscardCards();
		//loadOtherPlayersProfile();


		/*----------Set up pawns ----------*/
		loadPawns();
		/*----------Set up controlPawn------*/
		loadControlPawn();

		loadBtnEndTurn();
		loadActionsRemaining();
		loadGenericMessageBox();

		/*----------Set up citiesLabels ----------*/
		loadCities();

	

		cardsContainer.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/cardsContainer.png")).getImage().getScaledInstance(809, 191, Image.SCALE_SMOOTH)));
		cardsContainer.setBounds(270, 560, 809, 191);
		contentPane.add(cardsContainer);
		
		if(gs.getCurrentPlayerActionsRemaining() == 0)
				loadBtnEndTurn();
		loadTargetsDrive();
		loadDriveAndFlightMessage();
		loadTargetsDirectFlight();
		loadGenericMessageBox();
		//loadTreatDiseaseMessage();
		//loadShareKnowledgeMessage();
		
		/*----------Set up board map ----------*/
		loadMapLines();
		loadMap();
		
	
	}

	private void createEvents() 
	{
		/*Create event listeners for each city*/
		for(JLabel city: citiesLabels)
		{
            System.out.println("city (in createEvents): "+ city.getText() +  " " + citiesLabels.size());
			/*Mouse entered, cursor change to pointer*/
			city.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR); 
			     setCursor(cursor);
			}
			/*Mouse exit, pointer to cursor*/
			public void mouseExited(MouseEvent e)
			{
				Cursor cursor = Cursor.getDefaultCursor();
			     setCursor(cursor);
			}
			
			/*When click on city*/
			public void mouseReleased(MouseEvent e)
			{

		        JLabel value= city;
		        for(Map.Entry entry: mapCityLabels.entrySet()){
		            if(value.equals(entry.getValue())){
		                cityNameSelected = (CityName) entry.getKey();
		                break; //breaking because its one to one map
		            }
		        }
		        System.out.println("City Clicked: " + cityNameSelected);
                System.out.println("mouse event: " + e.getClickCount());

		        if(moves.get("drive") &&
		        		currentUserCity.getNeighbors().stream().anyMatch(city -> city.getName().equals(cityNameSelected)) ){
		        System.out.println("yey" + moves.get("drive"));

		        client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
		          new UpdateRequest(
		                  new PostCondition(PostCondition.ACTION.MOVE_PLAYER_POS, username, cityNameSelected.toString(), TravelType.DRIVE_FERRY)));
		        }
		        else if(moves.get("directFlight")){
		            client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(),
			         new UpdateRequest(new PostCondition(PostCondition.ACTION.MOVE_PLAYER_POS, username, cityNameSelected.toString(), TravelType.DIRECT_FLIGHT)));

		        }

				//mapPawnLabels.get(userRole).setLocation(city.getX(),city.getY()-20);
			}
		});
		}

		acceptButton.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				//client.sendMesageToServer(ServerCommands.ANSWER_CONSENT_PROMPT.name(), true);
				acceptButton.setVisible(false);
			}
		});

		declineButton.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				declineButton.setVisible(false);
				//client.sendMesageToServer(ServerCommands.ANSWER_CONSENT_PROMPT.name(), false);
			}
		});
		
		discardCardButton.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				discardCardButton.setVisible(false);
			}
		});

		optionRedDisease.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				hideTreatDiseaseOptions();
			client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.TREAT_DISEASE, username, DiseaseType.Red)));
			}
		});

		optionBlueDisease.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				hideTreatDiseaseOptions();
				client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.TREAT_DISEASE, username, DiseaseType.Blue)));
			}
		});


		optionYellowDisease.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				hideTreatDiseaseOptions();
				client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.TREAT_DISEASE, username, DiseaseType.Yellow)));
			}
		});

		optionBlackDisease.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				hideTreatDiseaseOptions();
				client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.TREAT_DISEASE, username, DiseaseType.Black)));
			}
		});

		btnEndTurn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				btnEndTurn.setVisible(false);
				client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.END_TURN)));
			}
		});

		/*-------Events for 8 buttons-------*/
		//Drive Ferry button
		btnDriveFerry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetMovesSelected(moves);
				for(Boolean b: moves.values()) System.out.println(b);
				moves.put("drive",true);
				resetDisplayOptions(displayOptions);
				loadDriveAndFlightMessage();
				hideTreatDiseaseOptions();
				displayTargetsDrive();
				System.out.println(citiesLabels.size());
				}

			private void displayTargetsDrive() {targetsDrive.forEach(t -> t.setVisible(true));}
		});
		
		//Direct Flight button
		btnDirectFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetMovesSelected(moves);
				moves.put("directFlight",true);
				resetDisplayOptions(displayOptions);
				loadDriveAndFlightMessage();
				hideTreatDiseaseOptions();
				displayTargetsDirectFlight();
			}
			private void displayTargetsDirectFlight() {targetsDirectFlight.forEach(t -> t.setVisible(true));}
		});
		
		//Charter Flight button
		btnCharterFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			}
		});
		
		//Shuttle Flight button
		btnShuttleFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			}
		});
		
		//Build Research button
		btnBuildResearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			}
		});
		
		//TreatDisease button
		btnTreatDisease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetMovesSelected(moves);
				resetDisplayOptions(displayOptions);
				loadTreatDiseaseMessage();
			}
		});
		
		//Discover a cure button
		btnDiscoverCure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		//Share Knowledge button
		btnShareKnowledge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			}
		});

		btnEndTurn.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				client.sendMessageToServer(ServerCommands.SEND_UPDATE_REQUEST.name(), new UpdateRequest(new PostCondition(PostCondition.ACTION.END_TURN)));
			}
		});
	}

	/*Helper method*/
	private int[] getCityPosition(String labelText)
	{
		String X = "", Y = ""; 
		int i = 0;
		for(i=2; i < labelText.length();i++){
			if(labelText.charAt(i) == ',') {i++;break;}
			X += labelText.charAt(i);
		}
		for(int j=i; j < labelText.length();j++) Y += labelText.charAt(j);
		return new int[]{Integer.parseInt(X),Integer.parseInt(Y)};
	}

	private void loadCities()
	{
		Icon blueCityIcon = new ImageIcon((new ImageIcon(GUI.class.getResource(blueCityIconPath)))
				.getImage().getScaledInstance(citySize, citySize,  Image.SCALE_SMOOTH));
		Icon redCityIcon = new ImageIcon((new ImageIcon(GUI.class.getResource(redCityIconPath)))
				.getImage().getScaledInstance(citySize, citySize,  Image.SCALE_SMOOTH));
		Icon yellowCityIcon = new ImageIcon((new ImageIcon(GUI.class.getResource(yellowCityIconPath)))
				.getImage().getScaledInstance(citySize, citySize,  Image.SCALE_SMOOTH));
		Icon whiteCityIcon = new ImageIcon((new ImageIcon(GUI.class.getResource(whiteCityIconPath)))
				.getImage().getScaledInstance(citySize, citySize,  Image.SCALE_SMOOTH));
		for(JLabel city : citiesLabels)
		{
			int x = getCityPosition(city.getText())[0];
			int y= getCityPosition(city.getText())[1];
			city.setBounds(x,y,citySize,citySize);
			switch(city.getText().charAt(0))
			{
				case 'b': city.setIcon(blueCityIcon); break;
				case 'y': city.setIcon(yellowCityIcon); break;
				case 'r': city.setIcon(redCityIcon); break;
				case 'w': city.setIcon(whiteCityIcon); break;
				default:break;
			}
			contentPane.add(city);
			
			//add city names
			CityName key= null;
	        JLabel value= city;
	        for(Map.Entry entry: mapCityLabels.entrySet()){
	            if(value.equals(entry.getValue())){
	                key = (CityName) entry.getKey();
	                break; //breaking because its one to one map
	            }
	        }
			JLabel cityNameLabel = new JLabel(key.toString());
			cityNameLabel.setFont(new Font("Lao MN", Font.PLAIN, 10));
			cityNameLabel.setForeground(Color.WHITE);
			cityNameLabel.setBounds(city.getX()-key.toString().length()+5, city.getY()+23, 90, 16);
			contentPane.add(cityNameLabel);
			
			//add cubes
			int[][] cubePos = {{city.getX()-2,city.getY()-5},{city.getX()-2,city.getY()+5},
					{city.getX()-9,city.getY()-5},{city.getX()-9,city.getY()+5},{city.getX()-16,city.getY()}};
			if(gs.getDiseaseCubesMap().get(key) != null){

				List<javafx.util.Pair<DiseaseType, Integer>> cubesTuples = gs.getDiseaseCubesMap().get(key);
				//System.out.println(cubesTuples);
				for(int i = 0; i < cubesTuples.size();i++){
					
					JLabel cubeLabel = new JLabel(""+cubesTuples.get(i).getValue());
					cubeLabel.setFont(new Font("Lao MN", Font.PLAIN, 12));
					switch(cubesTuples.get(i).getKey())
					{
						case Red: cubeLabel.setForeground(Color.RED); break;
						case Blue: cubeLabel.setForeground(new Color(30, 144, 255)); break;
						case Yellow: cubeLabel.setForeground(Color.YELLOW); break;
						case Purple: cubeLabel.setForeground(Color.MAGENTA); break;
						case Black: cubeLabel.setForeground(Color.WHITE); break;
						default:break;
					}
					
					cubeLabel.setBounds(cubePos[i][0], cubePos[i][1], 90, 16);
					contentPane.add(cubeLabel);
					
				}
			}
		}
	}

	private void loadPawns()
	{
		
		/*Load blue pawn*/
		
		//System.out.println(gs.getPositionMap().get(medic));
	
		//load all pawnLabels from positionMap - loop through positionMap
		
		for (Entry<RoleType, City> entry : gs.getPositionMap().entrySet())
		{
			
			RoleType pawnRole = entry.getKey();
			City atCity = gs.getPositionMap().get(pawnRole);
			JLabel cityLabel = mapCityLabels.get(atCity.getName());
			JLabel pawnLabel = mapPawnLabels.get(pawnRole);
		    //System.out.println(entry.getKey() + "/" + entry.getValue());
			
			List<Unit> cPawns = atCity.getCityUnits().stream().filter(unit -> unit.getUnitType() == UnitType.Pawn)
	                .collect(Collectors.toList());
			int numOfPawns = cPawns.size();
			
		    int pawnX =  getCityPosition(cityLabel.getText())[0];
		    int pawnY =  getCityPosition(cityLabel.getText())[1] - 23;
		    
		    pawnLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(pawnLabel.getText())).getImage().getScaledInstance(pawnDims[0], pawnDims[1], Image.SCALE_SMOOTH)));
			pawnLabel.setBounds(pawnX, pawnY, pawnDims[0], pawnDims[1]);
			contentPane.add(pawnLabel);
			
		}

	}
	
	//draw pawn controlled by user (so it's based on the username, which determines the RoleType)
	private void loadControlPawn()
	{
		
		//Display
        System.out.println("username  in lcp: " + username);
		JLabel userRoleLabel = new JLabel("Role: " + userRole.toString());
		//userRoleLabel.setFont(new Font("Lao MN", Font.PLAIN, 12));
		userRoleLabel.setForeground(Color.WHITE);
		userRoleLabel.setBounds(74-userRole.toString().length(), 555, 190, 16);
		contentPane.add(userRoleLabel);
		
		
		controlPawn = new JLabel();
		String controlPawnIconPath = mapPawnLabels.get(userRole).getText();
		//System.out.println(controlPawnIconPath);
		controlPawn.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(controlPawnIconPath)).getImage().getScaledInstance(80, 106, Image.SCALE_SMOOTH)));
		controlPawn.setBounds(74, 595, 80, 106);
		contentPane.add(controlPawn);
		
	}

    private void loadActionsRemaining(){
        if(username.equals(gs.getCurrentPlayer())){
            actionsRemaining.setText("Actions remaining: " + gs.getCurrentPlayerActionsRemaining());
        }
        else {
            actionsRemaining.setText("Actions remaining: 0");
        }
    }
	
	private void loadPlayerCards()
	{
		RoleType userRole= null;
        String value= username;
        for(Map.Entry entry: gs.getUserMap().entrySet()){
            if(value.equals(entry.getValue())){
                userRole = (RoleType) entry.getKey();
                break; //breaking because its one to one map
            }
        }
        
		int i = 1;
        System.out.println("gs : " + gs);
        System.out.println("gs.getCardMap: " +gs.getCardMap());
        System.out.println("gs.getUserMap: " +gs.getUserMap());
        //System.out.println("gs.getCardMap.get(userRole): " +gs.getCardMap().get(userRole));
        System.out.println("CARD MAP: ");

        for (Map.Entry<RoleType, List<PlayerCard>> entry : gs.getCardMap().entrySet())
        {
            System.out.println(entry.getKey() + "/");
            System.out.println("Cards:");
            entry.getValue().forEach(c -> System.out.println("          "+ c.getCardName() + " " + c.getCardType()));
        }

        System.out.println("username: " +username);
        System.out.println("userRole: " + userRole);
		for(PlayerCard cityCard : gs.getCardMap().get(userRole))
		{
			
			JLabel cityCardLabel = mapPlayerCardLabels.get(cityCard.getCardName());
			if(cityCardLabel != null)
			{
				System.out.println(cityCardLabel.getText());
				cityCardLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(cityCardLabel.getText())).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
				cityCardLabel.setBounds(202+115*i,585,100,140);
				contentPane.add(cityCardLabel);
				i++;
			}
			
		}
		
		
		/*
		JLabel yo = new JLabel("New label");
		
		yo.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/Tokyo.png")).getImage().getScaledInstance(80, 140, Image.SCALE_SMOOTH)));
		yo.setBounds(42, 567, 80, 140);
		contentPane.add(yo);*/
	}
	
	private void loadPlayerDiscardCards()
	{
		System.out.println("CARDS IN PLAYER DISCARD PILE: " + gs.getPlayerDiscardPile().getCardsInPile());
		System.out.println("PLAYER DISCARD PILE: " + gs.getPlayerDiscardPile());
		for(PlayerCard cityCard : gs.getPlayerDiscardPile().getCardsInPile())
		{
			
			JLabel cityCardLabel = mapPlayerCardLabels.get(cityCard.getCardName());
			if(cityCardLabel != null)
			{
				cityCardLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(cityCardLabel.getText())).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
				cityCardLabel.setBounds(106, 56, 100, 130);
				contentPane.add(cityCardLabel);
			}
			
		}
		
	}
	
	private void loadInfectionDiscardCards()
	{
		int i = 0;
		for(InfectionCard cityCard : gs.getInfectionDiscardPile().getCards())
		{
			JLabel cityCardLabel = mapInfectionCardLabels.get(cityCard.getCardName());
			if(cityCardLabel != null)
			{
				cityCardLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(cityCardLabel.getText())).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH)));
				cityCardLabel.setBounds(102+i*5,185,100,140);
				contentPane.add(cityCardLabel);
				i++;
			}
			
		}
		
	}

	private void loadMapLines(){
		JLabel mapLines = new JLabel();
		mapLines.setBounds(210, -14, 980, 650);
		mapLines.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(boardMapLinesPath))
				.getImage().getScaledInstance(980, 560,  Image.SCALE_SMOOTH)));
		contentPane.add(mapLines);
	}

	private void loadMap()
	{
		JLabel map = new JLabel();
		map.setBounds(214, 0, 980, 650);
		map.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(boardMapPath))
				.getImage().getScaledInstance(980, 650,  Image.SCALE_SMOOTH)));
		contentPane.add(map);
		map.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println(MouseInfo.getPointerInfo().getLocation());
			}});
		
		
		
	}
	
	private void loadTargetsDrive()
	{

		for(City c : currentUserCity.getNeighbors())
		{
			//city => cityLabel
			JLabel cityLabel = mapCityLabels.get(c.getName());
			
			//create a target label
			JLabel target = new JLabel();
			target.setBounds(cityLabel.getX()-5,cityLabel.getY()-5,40,40);
			target.setIcon(iconTarget);
			target.setVisible(false);
			contentPane.add(target);
			//add to targetsDrive
			targetsDrive.add(target);
			
		}
	}
	
	private void loadTargetsDirectFlight()
	{
		//loops through player's hand, only check cityCard (if getCardType().equals(CardType.CityCard)
		//each card's name => map to city label

		for(PlayerCard pc : gs.getCardMap().get(userRole))
		{
			if(pc.getCardType().equals(CardType.CityCard) 
				&& !pc.getCardName().equals(currentUserCity.getName().toString()))
			{
				JLabel cityLabel = mapCityLabels.get(CityName.valueOf(pc.getCardName()));
				//System.out.println(pc.getCardName());
				JLabel targetDirectFlight = new JLabel();
				targetDirectFlight.setBounds(cityLabel.getX()-5,cityLabel.getY()-5,40,40);
				targetDirectFlight.setIcon(iconTarget);
				targetDirectFlight.setVisible(false);
				contentPane.add(targetDirectFlight);
				//add to targetsDirectFlight
				targetsDirectFlight.add(targetDirectFlight);
				
				
			}
		}
		// currentCity = gs.getPositionMap().get(userRole).getName();
	}
	
	private void loadDriveAndFlightMessage()
	{
		genericBox.setForeground(Color.WHITE);
		genericBox.setOpaque(false);
		genericBox.setText("Choose city to move to");
		genericBox.setBounds(600,520,200,50);
		genericBox.setVisible(true);
	}
	
	private void loadTreatDiseaseMessage() {
        //current position of user
        optionRedDisease.setBounds(586, 130, 68, 36);
        optionBlueDisease.setBounds(586, 210, 68, 36);
        optionYellowDisease.setBounds(586, 300, 68, 36);
        optionBlackDisease.setBounds(586, 390, 68, 36);

        //cubesTuples.forEach(p-> System.out.println("yo" + p.getKey()));
        for (Pair<DiseaseType, Integer> d : cubesInCity) {
            JLabel t = new JLabel();
            if (d.getKey().equals(DiseaseType.Red)) {
                t = optionRedDisease;
                optionRedDisease.setVisible(true);
            } else if (d.getKey().equals(DiseaseType.Blue)) {
                t = optionBlueDisease;
                optionBlueDisease.setVisible(true);
            } else if (d.getKey().equals(DiseaseType.Yellow)) {
                t = optionYellowDisease;
                optionYellowDisease.setVisible(true);
            } else if (d.getKey().equals(DiseaseType.Black)) {
                t = optionBlackDisease;
                optionBlackDisease.setVisible(true);
            }

            t.setText("" + d.getKey());
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

            //contentPane.add(t);


        }
        genericBox.setText("");
        genericBox.setVisible(true);
        genericBox.setForeground(Color.WHITE);
        genericBox.setBackground(Color.BLACK);
        genericBox.setBounds(486, 100, 300, 400);
        genericBox.setOpaque(true);
    }

	private void loadBtnEndTurn(){
		if(username.equals(gs.getCurrentPlayer()) && gs.getCurrentPlayerActionsRemaining() == 0) {
			btnEndTurn.setText("END TURN");
			btnEndTurn.setBounds(11, 530, 176, 20);
			btnEndTurn.setBackground(Color.RED);
			btnEndTurn.setForeground(Color.WHITE);
			btnEndTurn.setVisible(true);
			contentPane.add(btnEndTurn);
		}
		else {
			btnEndTurn.setText("END TURN");
			btnEndTurn.setBounds(11, 530, 176, 20);
			btnEndTurn.setBackground(Color.RED);
			btnEndTurn.setForeground(Color.WHITE);
			btnEndTurn.setVisible(false);
			contentPane.add(btnEndTurn);
		}
	}

	private void hideTreatDiseaseOptions(){
		optionBlueDisease.setVisible(false);
		optionRedDisease.setVisible(false);
		optionRedDisease.setVisible(false);
		optionRedDisease.setVisible(false);
		genericBox.setVisible(false);
	}

	private void loadGenericMessageBox(){
		if(username.equals(gs.getCurrentPlayer())) {
			genericBox.setForeground(Color.WHITE);
			genericBox.setOpaque(false);
			genericBox.setText("YOUR TURN");
			genericBox.setBounds(600, 520, 200, 50);
			genericBox.setVisible(true);
		}
		else {
			genericBox.setForeground(Color.WHITE);
			genericBox.setOpaque(false);
			genericBox.setText(gs.getCurrentPlayer() + "'s turn.");
			genericBox.setBounds(600,520,200,50);
			genericBox.setVisible(true);
		}
	}

	private void loadShareKnowledgeMessage()
	{
		
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
	private void resetMovesSelected(Map<String,Boolean> moves){
		for(String m : moves.keySet()) moves.put(m,false);

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

        System.out.println("userRole in getUserRole: " + userRole);
        System.out.println("username in getUserRole: " + username);

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
		genericBox.setForeground(Color.WHITE);
		genericBox.setBackground(Color.BLACK);
		genericBox.setBounds(486,200,400,100);
		genericBox.setOpaque(true);
		
		acceptButton.setText("Accept");
		acceptButton.setBackground(Color.GREEN);
		acceptButton.setBounds(486,300,50,50);
		acceptButton.setOpaque(true);

		declineButton.setText("Decline");
		declineButton.setBackground(Color.RED);
		declineButton.setBounds(550,300,50,50);
		declineButton.setOpaque(true);
		
	}
	public void drawReceiveMessage(String message, MessageType type)
	{
		genericBox.setText(type.toString() + ": " + message);
		genericBox.setForeground(Color.WHITE);
		genericBox.setBackground(Color.BLACK);
		genericBox.setBounds(486,200,400,200);
		genericBox.setOpaque(true);

		genericBox.setVisible(true);

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
		discardCardButton.setBackground(Color.ORANGE);
		discardCardButton.setBounds(486,300,50,50);
		discardCardButton.setOpaque(true);
	}
	
}


