package pandemic.views;
import javafx.util.Pair;
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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Purpose: Create GUI components + event listeners
 * NOTES: All GUI components which might be used in other classes (e.g pawns,cards) will be declared as private fields 
 * (For now, I'll make everything as fields, will figure out later which ones don't need to be a field)
 * For clarity's sake, I declared and initialized them at the same time, in one line
 * (Could also initialize them in constructor but let's keep it this way for now)
 **/

/**TO DO: 
 * - Add city names
 * - Draw lines
 * - Fix targets
 * - Add cards to player's hand from playerDeck
 * - Discard cards
 * - Show infection cards => Infect citiesLabels
 * - Add/remove cubes
 * 
 * 
 * -....
 **/
public class GUI extends JFrame 
{
	
	
	private dummyGS gs;
	private String username;
	private RoleType medic = RoleType.Medic;
	private RoleType scientist = RoleType.Scientist;
	private RoleType dispatcher = RoleType.Dispatcher;
	private RoleType researcher = RoleType.Researcher;
	private CityName Atlanta = CityName.Atlanta;
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
	private final String greenPawnIconPath = "/pandemic/resources/Pawns/greenPawn.png";
	private final String bluePawnIconPath = "/pandemic/resources/Pawns/bluePawn.png";
	private final String orangePawnIconPath = "/pandemic/resources/Pawns/orangePawn.png";
	private final String purplePawnIconPath = "/pandemic/resources/Pawns/purplePawn.png";
	
	
	/*Pawns*/
	private JLabel orangePawn = new JLabel(orangePawnIconPath);
	private JLabel greenPawn = new JLabel(greenPawnIconPath);
	private JLabel bluePawn = new JLabel(bluePawnIconPath);
	private JLabel purplePawn = new JLabel(purplePawnIconPath);
	private JLabel yellowPawn = new JLabel();
	private JLabel whitePawn = new JLabel();
	private JLabel pinkPawn = new JLabel();
	
	
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
	private JLabel MexicoCityCityLabel = new JLabel("y,352,334");
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
	private JLabel AirliftCardLabel = new JLabel("/pandemic/resources/PlayerCards/AirliftEventCard.png");
	private JLabel OneQuietNightCardLabel = new JLabel("/pandemic/resources/PlayerCards/OneQuietNightEventCard.png");
	private JLabel ResilientPopulationCardLabel = new JLabel("/pandemic/resources/PlayerCards/ResilientPopulationEventCard.png");
	private JLabel GovernmentRantCardLabel = new JLabel("/pandemic/resources/PlayerCards/GovernmentRantEventCardLabel.png");
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

	/*Content pane which contains all GUI components*/
	private JPanel contentPane;
	  
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
	private JLabel purpleRemaining;
	
	/*Player cards*/
	
	/*Infection rate indicator*/
	private JLabel infectionRate;
	
	/*outbreak meter count indicator*/
	private JLabel outbreakMeterCount;
	
	/*Instruction*/
	private JLabel instruction;
	
	/*ICON/IMAGE PATHS (FINAL FIELDS)*/
	//Including: MAP, pawn icons, city icons, card pics
	
	/*Board Map*/
	private final String boardMapPath = "/pandemic/resources/Map/pandemic9.jpeg";
	
	
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
	private final String purpleCubesRemPath = "/pandemic/resources/TopBar/purpleRem.png";
	
	/*Target icon*/
	private final String targetIconPath = "/pandemic/resources/target.png";
	
	/*Player Deck Picture*/
	private final String playerDeckPicPath = "/pandemic/resources/Deck_Discard/playerDeck.png";

	/*Infection Deck Picture*/
	private final String infectionDeckPicPath = "/pandemic/resources/Deck_Discard/infectionDeck.png";
	
	/*Player Discard Picture*/
	private final String playerDiscardPicPath = "/pandemic/resources/Deck_Discard/playerDisCard.png";
			
	/*Infection Discard Picture*/
	private final String infectionDiscardPicPath = "/pandemic/resources/Deck_Discard/infectionDisCard.png";
			
	/////////Ignore these (for testing)////////////
	public boolean driveSelected;
	private boolean directFlightSelected;
	private JLabel message;
	private JLabel target = new JLabel();
	private JLabel target2 = new JLabel();
	private JLabel target5 = new JLabel();
	private JLabel target3 = new JLabel();
	private JLabel target4 = new JLabel();
	private JLabel lblCard3;
	
	//================================================================================================
	//================================================================================================
	//====================================MAP=========================================================
	//================================================================================================
	//================================================================================================
	/*RoleType <---> PawnLabel*/
	private Map<RoleType,JLabel> mapPawnLabels = new HashMap<RoleType,JLabel>()
	{{
		put(RoleType.Medic,bluePawn);
		put(RoleType.Scientist,greenPawn);
		put(RoleType.Researcher,yellowPawn);
		put(RoleType.ContingencyPlanner,orangePawn);
		put(RoleType.Dispatcher,purplePawn);
		put(RoleType.OperationsExpert,pinkPawn);
		put(RoleType.QuarantineSpecialist,whitePawn);
	}};
	
	
	/*CityName <---> CityLabel*/
	private Map<CityName, JLabel> mapCityLabels = new HashMap<CityName,JLabel>()
	{{
		
		put(CityName.SanFrancisco,SanFranciscoCityLabel);
		put(Chicago,ChicagoCityLabel);
		put(CityName.Montreal,MontrealCityLabel);
		put(CityName.NewYork,NewYorkCityLabel);//
		put(Atlanta,AtlantaCityLabel);
		put(CityName.StPetersburg,StPetersburgCityLabel);//
		put(CityName.London,LondonCityLabel);//
		put(CityName.Essen,EssenCityLabel);
		put(CityName.Washington,WashingtonCityLabel);//
		put(CityName.Madrid,MadridCityLabel);
		put(Paris,ParisCityLabel);
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
	
	
	/*CityCard <---> CityCardLabel*/
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
		
		put("Airlift",AirliftCardLabel);
		put("Forecast",ForecastCardLabel);
		put("OneQuietNight",OneQuietNightCardLabel);
		put("GovernmentRant",GovernmentRantCardLabel);
		put("ResilientPopulation",ResilientPopulationCardLabel);
		put("BasicEpidemicCard",BasicEpidemicCardLabel);
		
	
	}};
	

	/*
	 * startTurn
	 * After Play is pressed: 
	 * 1) Draw pawn's hand (each hand for everyone) => a pawn is associated to playerID
	 * 		blue - {Paris, Montreal, New York}
	 * 		same for green,orange, purple
	 * 2) 
	 * 3) 
	 * 
	 * ----
	 * 1) 48 cityCardLabels 
	 * Ultimately, all 48 cards are on the screen, we're just moving them around
	 * JLabel TorontoLabel = new JLabel("Toronto");
	 * m = Map<String, ArrayList<String> > = Map<color,hand> => {"blue",{"Toronto","Paris"}}
	 * 
	 * 
	 * JLabel currentPawn = pawnMap.get(gt.getCurrentPawn());
	 * currentPawn.setLocation(m.getX(currentPawn.get(), m.getY(
	 * => need drawPlayerHand method 
	 * { loops through hands, 
	 * 
	 * 
	 * driveFerry
	 * In order to draw new GUI after driveFerry is executed, I need to know:
	 * current pawn: blue or green or orange
	 * pawn's position, i.e city
	 * new GUI = pawn position
	 * 
	 * directFlight
	 * In order to draw new GUI after directFlight is executed, I need to know:
	 * pawn's position, i.e city
	 * which city card is discarded
	 * new GUI
	 * 
	 */
	
	//========================================================================
	//========================================================================
	//========================================================================
	//========================================================================
	/*Constructor does 2 things: (1) setting up GUI components (2) create event listener for each component*/
	public GUI(String username) {this.username = username;}
	
	//public void receiveNewGameState(GameState gs) {}
	
		
	public void draw()
	{
		/*This method is responsible for setting up GUI components*/
		initComponents();
		/*This method is responsible for creating all the event listeners*/
		createEvents();
	}

	private void initComponents() 
	{
	
		//an instance of dummyGS
		//userMap
		Map<RoleType,String> userMap = new HashMap<RoleType,String>()
		{{
			put(medic, "Gael");
			put(scientist,"Russel");
		}};
		
		//cardMap
		List<String> cardList1 = new ArrayList<String>() {{
		    add("Airlift");
		    add("Paris");
		    add("Atlanta");
		}};
		List<String> cardList2 = new ArrayList<String>() {{
		    
		    add("Paris");
		    add("Atlanta");
		}};
		Map<RoleType,List<String>> cardMap = new HashMap<RoleType,List<String>>()
		{{
			put(medic, cardList1);
			put(scientist, cardList2);
		}};
		
		//positionMap
		
		Map<RoleType,CityName> positionMap = new HashMap<RoleType,CityName>()
		{{
			put(medic, Atlanta);
			put(scientist,Chicago);
			put(dispatcher,Paris);
		}};
		
		
		//diseaseCubesMap
		
		List<Pair<DiseaseType,Integer>> cubesList1 = new ArrayList<Pair<DiseaseType,Integer>>() {{
		    add(new Pair(red,1));
		    add(new Pair(blue,2));
		    add(new Pair(purple,2));
		    add(new Pair(yellow,2));
		    add(new Pair(black,2));
		   
		}};
		List<Pair<DiseaseType,Integer>> cubesList2 = new ArrayList<Pair<DiseaseType,Integer>>() {{
		    add(new Pair(red,1));
		    add(new Pair(blue,2));
		}};
		List<Pair<DiseaseType,Integer>> cubesList3 = new ArrayList<Pair<DiseaseType,Integer>>() {{
		    add(new Pair(purple,1));
		    add(new Pair(blue,2));
		}};
		Map<CityName,List<Pair<DiseaseType,Integer>>> diseaseCubesMap = new HashMap<CityName,List<Pair<DiseaseType,Integer>>>()
		{{
			put(Bangkok, cubesList1);
			put(LosAngeles, cubesList2);
			put(Tokyo,cubesList3);
			put(Atlanta,cubesList1);
			put(Chicago,cubesList2);
			
		}};
		
		List<CityName> infectionDiscardDeck= new ArrayList<CityName>() {{
		    add(Tokyo);
		    add(Paris);
		}};
		List<CityName> playerDiscardDeck= new ArrayList<CityName>() {{
		    add(Tokyo);
		    add(Paris);
		}};
		
	
		
		this.gs = new dummyGS(userMap,cardMap,positionMap,diseaseCubesMap, infectionDiscardDeck,playerDiscardDeck);
		
		//System.out.println(RoleType.Medic);
		//System.out.println("yO " + gs.getPositionMap());
		
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
		
		/*-------Set up Actions remaining-------*/
		
		actionsRemaining.setBounds(19, 348, 131, 16);
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
		JPanel topBar = new JPanel();
		topBar.setBackground(Color.BLACK);
		topBar.setBounds(214, 0, 980, 30);
		contentPane.add(topBar);
		topBar.setLayout(null);
		
		//red cubes remaining
		redRemaining = new JLabel("14");
		redRemaining.setBackground(Color.BLACK);
		redRemaining.setForeground(Color.WHITE);
		redRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(redCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		redRemaining.setBounds(286, 6, 48, 16);
		topBar.add(redRemaining);
		redRemaining.setOpaque(true);
		
		//blue cubes remaining
		blueRemaining = new JLabel("19");
		blueRemaining.setBackground(Color.BLACK);
		blueRemaining.setForeground(Color.WHITE);
		blueRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(blueCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		blueRemaining.setBounds(341, 6, 48, 16);
		topBar.add(blueRemaining);
		blueRemaining.setOpaque(true);

		//yellow cubes remaining
		yellowRemaining = new JLabel("16");
		yellowRemaining.setBackground(Color.BLACK);
		yellowRemaining.setForeground(Color.WHITE);
		yellowRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(yellowCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		yellowRemaining.setBounds(396, 6, 48, 16);
		topBar.add(yellowRemaining);
		yellowRemaining.setOpaque(true);
		
		//purple cubes remaining
		purpleRemaining = new JLabel("23");
		purpleRemaining.setBackground(Color.BLACK);
		purpleRemaining.setForeground(Color.WHITE);
		purpleRemaining.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(purpleCubesRemPath)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		purpleRemaining.setBounds(451, 6, 48, 16);
		topBar.add(purpleRemaining);
		purpleRemaining.setOpaque(true);
		
		//instruction
		instruction = new JLabel("");
		instruction.setBackground(Color.BLACK);
		instruction.setForeground(Color.WHITE);
		instruction.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(instructionIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		instruction.setBounds(616, 6, 48, 16);
		topBar.add(instruction);
		instruction.setOpaque(true);
		
		//infection rate
		infectionRate = new JLabel("2");
		infectionRate.setBackground(Color.BLACK);
		infectionRate.setForeground(Color.WHITE);
		infectionRate.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(infectionRateIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		infectionRate.setBounds(506, 6, 48, 16);
		topBar.add(infectionRate);
		infectionRate.setOpaque(true);
	
		//outbreak meter count
		outbreakMeterCount = new JLabel("0");
		outbreakMeterCount.setBackground(Color.BLACK);
		outbreakMeterCount.setForeground(Color.WHITE);
		outbreakMeterCount.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(outbreakMeterCountIconPath)).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		outbreakMeterCount.setBounds(561, 6, 48, 16);
		topBar.add(outbreakMeterCount);
		outbreakMeterCount.setOpaque(true);
		
		/*----------Set up pawns ----------*/
		loadPawns();
		
		/*----------Set up citiesLabels ----------*/
		loadCities();
		
		loadPlayerCards();
		
		Icon iconTarget = new ImageIcon(new ImageIcon(GUI.class.getResource(targetIconPath))
				.getImage().getScaledInstance(40, 40,  Image.SCALE_SMOOTH));
		
		
		target.setBounds(SydneyCityLabel.getX()-5,SydneyCityLabel.getY()-5,40,40);
		target.setIcon(iconTarget);
		
		
		target2.setBounds(HongKongCityLabel.getX()-5,HongKongCityLabel.getY()-15,40,40);
		target2.setIcon(iconTarget);
		
		
		target3.setBounds(TapeiCityLabel.getX()-5,TapeiCityLabel.getY()-25,40,40);
		target3.setIcon(iconTarget);
		

		target4.setBounds(ShanghaiCityLabel.getX()-5,ShanghaiCityLabel.getY()-35,40,40);
		target4.setIcon(iconTarget);
		
		
		target5.setBounds(BangkokCityLabel.getX()-5,BangkokCityLabel.getY()-45,40,40);
		target5.setIcon(iconTarget);
	
		
		
		
		contentPane.add(target5);
		contentPane.add(target);
		contentPane.add(target4);
		contentPane.add(target3);
		contentPane.add(target2);
		
		/*Ignore this - For testing/Hardcode this part
		Icon iconTarget = new ImageIcon(new ImageIcon(GUI.class.getResource(targetIconPath))
				.getImage().getScaledInstance(40, 40,  Image.SCALE_SMOOTH));
		target.setVisible(false);
		System.out.println(ChicagoCityLabel.getX());
		target.setBounds(ChicagoCityLabel.getX()-5,ChicagoCityLabel.getY()-5,40,40);
		target.setIcon(iconTarget);
		contentPane.add(target);
		
		target2.setVisible(false);
		target2.setBounds(WashingtonCityLabel.getX()-5,WashingtonCityLabel.getY()-5,40,40);
		target2.setIcon(iconTarget);
		contentPane.add(target2);
	
		target3.setVisible(false);
		target3.setBounds(JakataCityLabel.getX()-5,JakataCityLabel.getY()-5,40,40);
		target3.setIcon(iconTarget);
		contentPane.add(target3);
		
		target4.setVisible(false);
		target4.setBounds(blueCity2.getX()-5,blueCity2.getY()-5,40,40);
		target4.setIcon(iconTarget);
		contentPane.add(target4);
		
		target5.setVisible(false);
		target5.setBounds(BuenosAiresCityLabel.getX()-5,BuenosAiresCityLabel.getY()-5,40,40);
		target5.setIcon(iconTarget);
		contentPane.add(target5);
		
		JLabel lblCard = new JLabel("");
		lblCard.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/Miami.png")).getImage().getScaledInstance(90, 120, Image.SCALE_SMOOTH)));
		lblCard.setBounds(535, 613, 90, 120);
		
		JLabel lblCard2 = new JLabel("");
		lblCard2.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/Tokyo.png")).getImage().getScaledInstance(90, 120, Image.SCALE_SMOOTH)));
		lblCard2.setBounds(647, 613, 90, 120);
		
		lblCard3 = new JLabel("");
		lblCard3.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/Moscow.png")).getImage().getScaledInstance(90, 120, Image.SCALE_SMOOTH)));
		lblCard3.setBounds(759, 613, 90, 120);
	
		contentPane.add(lblCard);
		contentPane.add(lblCard2);
		contentPane.add(lblCard3);
		*/
		
		cardsContainer.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource("/pandemic/resources/cardsContainer.png")).getImage().getScaledInstance(809, 191, Image.SCALE_SMOOTH)));
		cardsContainer.setBounds(270, 560, 809, 191);
		contentPane.add(cardsContainer);
		
		/*----------Set up board map ----------*/
		loadMap();
		
		//Ignore this-these are for window builder
		/*createBlueCities();
		createdWhiteCities();
		createRedCities();
		createYellowCities();*/
	}

	private void createEvents() 
	{
		/*Create event listeners for each city*/
		for(JLabel city: citiesLabels)
		{
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
				bluePawn.setLocation(city.getX(),city.getY()-20);
				/*if(driveSelected)
				{
					target.setVisible(false);
					target2.setVisible(false);
				}
				if(directFlightSelected)
				{
					target3.setVisible(false);
					target4.setVisible(false);
					target5.setVisible(false);
					lblCard3.setLocation(playerDiscard.getX()+5,playerDiscard.getY()+5);
				}*/
			}
		});
		}
		
		/*-------Events for 8 buttons-------*/
		//Drive Ferry button
		btnDriveFerry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				bluePawn.setLocation(bluePawn.getX()+20,bluePawn.getY()+20);
				
				driveSelected = true;
				directFlightSelected=false;
				
				target.setVisible(true);
				target2.setVisible(true);
				target3.setVisible(false);
				target4.setVisible(false);
				target5.setVisible(false);
			}
		});
		
		//Direct Flight button
		btnDirectFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				directFlightSelected = true;
				driveSelected = false;
				target3.setVisible(true);
				target4.setVisible(true);
				target5.setVisible(true);
				target.setVisible(false);
				target2.setVisible(false);
			}
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

				List<Pair<DiseaseType,Integer>> cubesTuples = gs.getDiseaseCubesMap().get(key);
				System.out.println(cubesTuples);
				for(int i = 0; i < cubesTuples.size();i++){
					
					JLabel cubeLabel = new JLabel(""+cubesTuples.get(i).getValue());
					cubeLabel.setFont(new Font("Lao MN", Font.PLAIN, 12));
					switch(cubesTuples.get(i).getKey())
					{
						case Red: cubeLabel.setForeground(Color.RED); break;
						case Blue: cubeLabel.setForeground(new Color(30, 144, 255)); break;
						case Yellow: cubeLabel.setForeground(Color.YELLOW); break;
						case Purple: cubeLabel.setForeground(Color.MAGENTA); break;
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
		
		for (Entry<RoleType, CityName> entry : gs.getPositionMap().entrySet())
		{
			
			RoleType pawnRole = entry.getKey();
			CityName atCity = gs.getPositionMap().get(pawnRole);
			JLabel cityLabel = mapCityLabels.get(atCity);
			JLabel pawnLabel = mapPawnLabels.get(pawnRole);
		    //System.out.println(entry.getKey() + "/" + entry.getValue());
			
		    int pawnX =  getCityPosition(cityLabel.getText())[0];
		    int pawnY =  getCityPosition(cityLabel.getText())[1] - 23;
		    
		    pawnLabel.setIcon(new ImageIcon(new ImageIcon(GUI.class.getResource(pawnLabel.getText())).getImage().getScaledInstance(pawnDims[0], pawnDims[1], Image.SCALE_SMOOTH)));
			pawnLabel.setBounds(pawnX, pawnY, pawnDims[0], pawnDims[1]);
			contentPane.add(pawnLabel);
			
		}
		
		
	}
	
	private void loadPlayerCards()
	{
		//String username = 
		
		RoleType key= null;
        String value= username;
        for(Map.Entry entry: gs.getUserMap().entrySet()){
            if(value.equals(entry.getValue())){
                key = (RoleType) entry.getKey();
                break; //breaking because its one to one map
            }
        }
        System.out.println(key);
		int i = 1;
		for(String cityCard : gs.getCardMap().get(key))
		{
			
			JLabel cityCardLabel = mapPlayerCardLabels.get(cityCard);
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
	
	
}
