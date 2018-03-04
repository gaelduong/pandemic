package pandemic;

public class Game {
	
	private InfectionDeck myInfectionDeck;
	private InfectionDiscardPile myInfectionDiscardPile;
	private PlayerDeck myPlayerDeck;
	private PlayerDiscardPile myPlayerDiscardPile;
	private boolean resolvingEpidemic;
	private int infectionRate;
	private GameBoard myGameBoard;
	private GamePhase currentPhase;
	private Player currentPlayer;
	
	enum GamePhase{
		ReadyToJoin, SetupRoleSelection, TurnActions, TurnPlayerCards, TurnInfection, Completed
	};
	
	
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
	
	public City getCityByName(GameManager.CityName cn){
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
}
