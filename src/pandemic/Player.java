package pandemic;

import shared.PlayerCardSimple;
import shared.request.CardSource;
import shared.request.CardTarget;

import java.util.ArrayList;

public class Player implements CardTarget, CardSource {

	private int actionsTaken;
	private boolean oncePerTurnActionTaken;
	private ArrayList<Card> cardsInHand;
	private User user;
	private Pawn pawn;
	private Role role;

	private BioTTurnTracker bioTTurnTracker;

	public Player(User user) {
	    this.user = user;
	    cardsInHand = new ArrayList<Card>();
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
    
    public void addToHand(Card pc){
	    cardsInHand.add(pc);
	}

	@Override
    public void acceptCard(Card card) {
        addToHand(card);
    }

    @Override
    public Card getCard(PlayerCardSimple card) {

	    CardType pcsCardType;
	    if(card.getCardType() == CardType.MovingCard) {
	        if(isBioTerrorist())
	            pcsCardType = CardType.CityInfectionCard;
	        else
	            pcsCardType = CardType.CityCard;
        } else {
	        pcsCardType = card.getCardType();
        }
	    return cardsInHand.stream().filter(c -> c.getCardName().equals(card.getCardName()) &&
                                    c.getCardType() == pcsCardType)
                            .findAny().orElse(null);
    }



	public boolean isInHand(PlayerCard pc) {
	    return cardsInHand.stream().anyMatch(pc::equals);
    }

    public boolean isInHandMovingCard(MovingCard mc) {
        return cardsInHand.stream().anyMatch(mc::equals);
    }
	
	// Note: Does not add card too any discard pile
	public boolean discardCard(Card card){
	    return cardsInHand.remove(card);
	}

	public ArrayList<CityInfectionCard> discardAllCards() {
	    if(!isBioTerrorist()) {
            return null;
        } else {
	        ArrayList<CityInfectionCard> cardsToDiscard = new ArrayList<CityInfectionCard>();
	        cardsInHand.forEach(card -> cardsToDiscard.add((CityInfectionCard) card));
	        cardsInHand.clear();
	        return cardsToDiscard;
        }

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

	    if(isBioTerrorist()) {
	        bioTTurnTracker = new BioTTurnTracker(this);
        }

    }

    public boolean isBioTerrorist() {
	    return this.role.getRoleType() == RoleType.BioTerrorist;
    }

    public BioTTurnTracker getBioTTurnTracker() {
        return bioTTurnTracker;
    }

    public ArrayList<PlayerCard> getCardsInHand() {
	    if(isBioTerrorist()) {
	        return null;
        } else {
            return (ArrayList<PlayerCard>) (ArrayList<?>) cardsInHand;
        }
	}

	public ArrayList<Card> getCardsInHandBioT() {
	    if(isBioTerrorist()) {
	        return cardsInHand;
        } else {
	        return null;
        }
    }
}
