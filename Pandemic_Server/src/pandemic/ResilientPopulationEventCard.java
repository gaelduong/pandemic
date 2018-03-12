package pandemic;

public class ResilientPopulationEventCard extends EventCard {
	
	public ResilientPopulationEventCard(){
		super(EventCardName.ResilientPopulation);
	}

	@Override
	public void playEventCard() {
		// TODO 
		// Remove any 1 card in the Infection Discard Pile from the Game.
		// This card can be played between the infect and intensify steps of an epidemic
		
		if (gameManager.getCurrentPlayerTurnStatus().equals(CurrentPlayerTurnStatus.PlayerDiscardingCards)){
			Player p = gameManager.getPlayerDiscardingCards();
			gameManager.checkHandSize(p);
		}
	}

}
