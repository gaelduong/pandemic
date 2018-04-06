package pandemic;

public class ResilientPopulationEventCard extends EventCard {
	
	public ResilientPopulationEventCard(GameManager gm){
		super(gm, EventCardName.ResilientPopulation);
	}

	public int playEventCard(Player owner, InfectionCard c) {
		// Remove any 1 card in the Infection Discard Pile from the Game.
		// This card can be played between the infect and intensify steps of an epidemic
		// Returns 0 if successful, 1 if failed
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
