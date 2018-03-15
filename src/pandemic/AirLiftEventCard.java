package pandemic;

public class AirLiftEventCard extends EventCard {

	public AirLiftEventCard(GameManager gm){
		super(gm, EventCardName.AirLift);
	}
	
	@Override
	public void playEventCard() {
		// TODO 
		// Move any 1 pawn to any city. Get permission before moving another player's pawn
		
		if (gameManager.getCurrentPlayerTurnStatus().equals(CurrentPlayerTurnStatus.PlayerDiscardingCards)){
			Player p = gameManager.getPlayerDiscardingCards();
			gameManager.checkHandSize(p);
		}
		
	}

}
