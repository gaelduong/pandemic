package pandemic;

import java.util.ArrayList;

public class Player {


	private int actionsTaken;
	private boolean oncePerTurnActionTaken;
	private ArrayList<PlayerCard> cardsInHand;
	private User user;
	private Pawn pawn;

	public Player(User user) {
	    this.user = user;
	    cardsInHand = new ArrayList<PlayerCard>();
    }

    public String getPlayerUserName() {
	    return user.getUserName();
    }

	public void setActionsTaken(int numActions){
	    actionsTaken = numActions;
	}
	
	public void setOncePerTurnActionTaken(boolean b){
	    oncePerTurnActionTaken = b;
	}

    public Pawn getPawn() {
	    return pawn;
    }

    public void setPawn(Pawn pawn) {
	    this.pawn = pawn;
    }

    public void addToHand(PlayerCard pc){
	    cardsInHand.add(pc);
	}

	public boolean isInHand(PlayerCard pc) {
	    return cardsInHand.stream().filter(card -> card == pc).findAny().orElse(null) != null ? true : false;
    }

	public int getActionsTaken() {
	    return actionsTaken;
    }

    public void incrementActionTaken() {
	    actionsTaken++;
    }


}
