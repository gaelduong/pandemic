package pandemic.eventcards;

import pandemic.CardType;
import pandemic.GameManager;
import pandemic.PlayerCard;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EventCard eventCard = (EventCard) o;
		return name == eventCard.name &&
				cardType == eventCard.cardType;
	}

	@Override
	public int hashCode() {

		return Objects.hash(name, cardType);
	}
}
