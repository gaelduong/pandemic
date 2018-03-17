package pandemic;

public class CityCard implements PlayerCard{

	private CardType type;
	private CityName name;
	private Region region;
	private CardType cardType;
	
	public CityCard(CityName pName,  Region pRegion){
		type = CardType.CityCard;
		name = pName;
		region = pRegion;
		cardType = CardType.CityCard;
	}
	
	public CityName getCityName(){
	    return name;
	}

	public Region getRegion(){
	    return region;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardName() {
        return name.toString();
    }
}
