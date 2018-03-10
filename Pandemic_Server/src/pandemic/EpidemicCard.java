package pandemic;

abstract class EpidemicCard {

	protected GameManager myGameManager;
	
	public EpidemicCard(GameManager gm){
	    myGameManager = gm;
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
		myGameManager.infectCityForEpidemic(city);
		
		// Intensify:
		myGameManager.shuffleInfectionDiscardPile();
		myGameManager.combineInfectionDeckAndPile();
		
		myGameManager.setResolvingEpidemic(false);
	}
	
	public abstract void resolveEpidemic();
}
