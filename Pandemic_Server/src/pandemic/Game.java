package pandemic;

public class Game {

    private GameManager gameManager;
  	private GameBoard myGameBoard;
  	private GamePhase currentPhase;
  	private GameSettings settings;
  	private Player currentPlayer;
  	private InfectionDeck myInfectionDeck;
  	private InfectionDiscardPile myInfectionDiscardPile;
  	private PlayerDeck myPlayerDeck;
  	private PlayerDiscardPile myPlayerDiscardPile;
  	private boolean resolvingEpidemic;
  	private int infectionRate;

  	public Game(Player currentPlayer, GameSettings settings, GameManager gameManager) {

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
}
