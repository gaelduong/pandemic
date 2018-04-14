package pandemic.eventcards;

import pandemic.CardType;
import pandemic.GameManager;
import pandemic.PlayerCard;

public abstract class EventCard implements PlayerCard {
	
	protected transient GameManager gameManager = null;
	protected EventCardName name;
	protected CardType cardType;
	
	public EventCard(GameManager gm, EventCardName pName){
	    //gameManager = gm;
		name = pName;
	    cardType = CardType.EventCard;
	}

    public CardType getCardType() {
        return cardType;
    }

    public String getCardName() {
        return name.toString();
    }
    
//    public abstract void playEventCard();
}
