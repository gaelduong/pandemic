package pandemic;

public class ResilientPopulationEventCard extends EventCard {
	
	public ResilientPopulationEventCard(GameManager gm){
		super(gm, EventCardName.ResilientPopulation);
	}

	// Returns 0 if successful, 1 if failed
	// @Pre: Player has viewed cards in Infection Discard Pile and selected InfectionCard c to remove from the game. c is not a Mutation card.
	public int playEventCard(Player owner, InfectionCard c) {
		InfectionDiscardPile dp = gameManager.getGame().getInfectionDiscardPile();
		if (dp.containsCard(c)){
			dp.removeCard(c);

			return gameManager.discardPlayerCard(owner, this);
		}
		else {
			return 1;
		}
	}
}
