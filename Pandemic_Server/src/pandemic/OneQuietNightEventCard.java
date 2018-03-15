package pandemic;

public class OneQuietNightEventCard extends EventCard {

	public OneQuietNightEventCard(GameManager gm){
		super(gm, EventCardName.OneQuietNight);
	}
	
	@Override
	public void playEventCard() {
		// Skip the next infect cities step (at the end of a turn)
		gameManager.setOneQuietNight(true);
		
		if (gameManager.getCurrentPlayerTurnStatus().equals(CurrentPlayerTurnStatus.PlayerDiscardingCards)){
			Player p = gameManager.getPlayerDiscardingCards();
			gameManager.checkHandSize(p);
		}
	}

}
