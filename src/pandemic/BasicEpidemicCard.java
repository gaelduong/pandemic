package pandemic;

public class BasicEpidemicCard extends EpidemicCard implements PlayerCard {

    private CardType cardType;

	public BasicEpidemicCard(GameManager gm){
	    super(gm);
	    cardType = CardType.BasicEpidemicCard;
	}
	
	public void resolveEpidemic(){
	    increaseInfectIntensify();
	}

    public CardType getCardType() {
        return cardType;
    }

    public String getCardName() {
        return cardType.toString();
    }
}
