package pandemic;

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
		myGameManager.setResolvingEpidemic(true);
		// Increase:
		int currentInfectionRate = myGameManager.getInfectionRate();
		if (currentInfectionRate < 4) {
			myGameManager.increaseInfectionRate();
		}
		
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
		
		myGameManager.setResolvingEpidemic(false);
	}
	
	public abstract void resolveEpidemic();
}
