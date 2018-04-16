package pandemic;

import java.util.Objects;

public class CityCard implements PlayerCard, MovingCard{

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CityCard cityCard = (CityCard) o;
		return name == cityCard.name &&
				cardType == cityCard.cardType;
	}

	@Override
	public int hashCode() {

		return Objects.hash(name, cardType);
	}
}
