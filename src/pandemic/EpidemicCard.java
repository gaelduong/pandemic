package pandemic;

import java.util.Objects;

abstract class EpidemicCard implements PlayerCard{

	protected CardType type;
	protected GameManager myGameManager;
	protected EpidemicCardName name;
	
	public EpidemicCard(CardType t, GameManager gm, EpidemicCardName n){
	    type = t;
		myGameManager = gm;
		name = n;
	}

	public CardType getCardType(){
		return type;
	}

	public String getCardName(){
		return name.toString();
	}
	
	protected void increaseInfectIntensify(){
		myGameManager.setEventCardsEnabled(false);
		// Increase:
		System.out.println("INFECTION RATE BEFORE EPIDEMIC INCREASE: " + myGameManager.getInfectionRate());
		myGameManager.increaseInfectionRate();
		System.out.println("INFECTION RATE AFTER EPIDEMIC INCREASE: " + myGameManager.getInfectionRate());
		// Infect:
		// --> MUST CHANGE THIS TO HANDLE MUTATION CARDS <--
		CityInfectionCard bottomInfectionCard = (CityInfectionCard) myGameManager.drawLastInfectionCard();
		myGameManager.discardInfectionCard(bottomInfectionCard);
		CityName cityName = bottomInfectionCard.getCityName();
		City city = myGameManager.getCityByName(cityName);

		// FOR TESTING:
		System.out.println("Infecting " + cityName + " for epidemic...");


		myGameManager.infectCityForEpidemic(city);
		
		// Intensify:
		myGameManager.shuffleInfectionDiscardPile();
		myGameManager.combineInfectionDeckAndPile();
		
		myGameManager.setEventCardsEnabled(true);
	}
	
	public abstract void resolveEpidemic();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EpidemicCard that = (EpidemicCard) o;
		return type == that.type &&
				name == that.name;
	}

	@Override
	public int hashCode() {

		return Objects.hash(type, name);
	}
}
