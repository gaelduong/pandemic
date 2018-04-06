package pandemic;

public class GovernmentGrantEventCard extends EventCard {

	public GovernmentGrantEventCard(GameManager gm){
		super(gm, EventCardName.GovernmentGrant);
	}

	// Returns 0 if successful, 1 otherwise
	// @Pre: Player has selected a targetCity to add one research station to. targetCity does not already have a research station on it.
	// ------------- TO DO: MUST HANDLE WHEN NO NEW RESEARCH STATIONS REMAINING --------------------------
	public int playEventCard(Player owner, City targetCity) {
		ResearchStation newStation = gameManager.getGame().getUnusedResearchStation();
		if (newStation != null) {
			targetCity.getCityUnits().add(newStation);
			newStation.setLocation(targetCity);

			return gameManager.discardPlayerCard(owner, this);
		}
		else return 1;
	}

}
