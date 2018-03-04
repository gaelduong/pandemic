package cards;

public class EpidemicCard implements PlayerCard{

	private GameManager myGameManager;
	
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
		GameManager.CityName cityName = bottomInfectionCard.getCityName();
		
	}
}
