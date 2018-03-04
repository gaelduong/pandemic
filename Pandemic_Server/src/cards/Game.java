package cards;

public class Game {
	
	private InfectionDeck myInfectionDeck;
	private InfectionDiscardPile myInfectionDiscardPile;
	private boolean resolvingEpidemic;
	private int infectionRate;
	private GameBoard myGameBoard;
	
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
}
