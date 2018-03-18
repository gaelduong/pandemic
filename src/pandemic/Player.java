package pandemic;

import java.util.ArrayList;

public class Player {

	private int actionsTaken;
	private boolean oncePerTurnActionTaken;
	private ArrayList<PlayerCard> cardsInHand;
	private User user;
	private Pawn pawn;
	private Role role;

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

    public int getHandSize(){
	    return cardsInHand.size();
    }
    
    public void addToHand(PlayerCard pc){
	    cardsInHand.add(pc);
	}

	public boolean isInHand(PlayerCard pc) {
	    return cardsInHand.stream().anyMatch(pc::equals);
    }
	
	// Note: Does not add card too any discard pile
	public boolean discardCard(PlayerCard card){
		return cardsInHand.remove(card);
	}

	public int getActionsTaken() {
	    return actionsTaken;
    }

    public void incrementActionTaken() {
	    actionsTaken++;
    }

    public RoleType getRoleType(){
    	return role.getRoleType();
    }

    public void setRole(Role role) {
	    this.role = role;
    }

	public ArrayList<PlayerCard> getCardsInHand() {
		return cardsInHand;
	}
}
