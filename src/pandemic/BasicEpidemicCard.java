package pandemic;

public class BasicEpidemicCard extends EpidemicCard implements PlayerCard {

	public BasicEpidemicCard(GameManager gm){
	    super(CardType.BasicEpidemicCard, gm, EpidemicCardName.BasicEpidemicCard);
	}
	
	public void resolveEpidemic(){
	    increaseInfectIntensify();
	}

    public CardType getCardType() {
        return type;
    }

    public String getCardName() {
        return "Epidemic";
    }
}
