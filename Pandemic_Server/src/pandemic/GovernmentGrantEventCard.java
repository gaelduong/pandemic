package pandemic;

public class GovernmentGrantEventCard extends EventCard {

	public GovernmentGrantEventCard(){
		super(EventCardName.GovernmentGrant);
	}
	
	@Override
	public void playEventCard() {
		// TODO 
		// Add 1 research station to any city (No City Card needed).
		
		if (gameManager.getCurrentPlayerTurnStatus().equals(CurrentPlayerTurnStatus.PlayerDiscardingCards)){
			Player p = gameManager.getPlayerDiscardingCards();
			gameManager.checkHandSize(p);
		}
	}

}
