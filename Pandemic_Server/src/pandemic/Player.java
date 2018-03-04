package pandemic;

import java.util.ArrayList;

public class Player {
	
	private int actionsTaken;
	private boolean oncePerTurnActionTaken;
	private ArrayList<PlayerCard> cardsInHand;
	
	public void setActionsTaken(int numActions){
		actionsTaken = numActions;
	}
	
	public void setOncePerTurnActionTaken(boolean b){
		oncePerTurnActionTaken = b;
	}
	
	public void addToHand(PlayerCard pc){
		cardsInHand.add(pc);
	}
}
