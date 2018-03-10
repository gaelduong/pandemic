package pandemic;

public class EventCard implements PlayerCard{
	
	private EventCardName name;
	private CardType cardType;
	
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
}
