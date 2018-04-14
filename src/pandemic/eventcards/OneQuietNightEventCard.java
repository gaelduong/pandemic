package pandemic.eventcards;

import pandemic.GameManager;
import pandemic.Player;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class OneQuietNightEventCard extends EventCard {

	public OneQuietNightEventCard(GameManager gm){
		super(gm, EventCardName.OneQuietNight);
	}

	// Returns 0 if successful, 1 if failed
	// Pre: eventCardEnabled == true
	public int playEventCard(Player owner) {
		// Skip the next infect cities step (at the end of a turn)
		gameManager.setOneQuietNight(true);
		return gameManager.discardPlayerCard(owner, this);
	}

}
