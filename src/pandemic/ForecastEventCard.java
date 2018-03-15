package pandemic;

public class ForecastEventCard extends EventCard{

	public ForecastEventCard(GameManager gm){
		super(gm, EventCardName.Forecast);
	}
	
	@Override
	public void playEventCard() {
		// TODO 
		// Draw, look at, and rearrange the top 6 cards of the Infection Deck.
		// Put them back on top.
		
		
		if (gameManager.getCurrentPlayerTurnStatus().equals(CurrentPlayerTurnStatus.PlayerDiscardingCards)){
			Player p = gameManager.getPlayerDiscardingCards();
			gameManager.checkHandSize(p);
		}
	}

}
