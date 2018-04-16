package pandemic;

import java.util.Objects;

public class CityInfectionCard implements InfectionCard, MovingCard {

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CityInfectionCard that = (CityInfectionCard) o;
		return name == that.name &&
				cardType == that.cardType;
	}

	@Override
	public int hashCode() {

		return Objects.hash(name, cardType);
	}
}
