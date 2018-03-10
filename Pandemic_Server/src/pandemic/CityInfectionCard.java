package pandemic;

public class CityInfectionCard implements InfectionCard{

	private CityName name;
	private Region region;
	private CardType cardType;
	
	public CityInfectionCard(CityName pName, Region pRegion){
		name = pName;
		region = pRegion;
		cardType = CardType.CityInfectionCard;
	}
	
	public CityName getCityName(){
	    return name;
	}

    public CardType getCardType() {
        return cardType;
    }

    public String getCardName() {
        return name.toString();
    }

    public Region getRegion() {
	    return region;
    }
}
