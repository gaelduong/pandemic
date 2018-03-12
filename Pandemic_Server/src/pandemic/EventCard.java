package pandemic;

public abstract class EventCard implements PlayerCard{
	
	protected GameManager gameManager;
	protected EventCardName name;
	protected CardType cardType;
	
	public EventCard(EventCardName pName){
	    name = pName;
	    cardType = CardType.EventCard;
	}

    public CardType getCardType() {
        return cardType;
    }

    public String getCardName() {
        return name.toString();
    }
    
    public abstract void playEventCard();
}
