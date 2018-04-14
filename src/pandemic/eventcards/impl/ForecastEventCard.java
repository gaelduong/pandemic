package pandemic.eventcards.impl;

import pandemic.GameManager;
import pandemic.InfectionCard;
import pandemic.InfectionDeck;
import pandemic.Player;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

import java.util.List;

public class ForecastEventCard extends EventCard {

	public ForecastEventCard(GameManager gm){
		super(gm, EventCardName.Forecast);
	}

	// Returns 0 if successful, 1 if failed
	// Pre: eventCardPlayable == true, Player has viewed top 6 cards of Infection Deck. rearrangedCards is the order that the Player has chosen for these cards.
	public int playEventCard(Player owner, List<InfectionCard> rearrangedCards) {
		InfectionDeck iDeck = gameManager.getGame().getInfectionDeck();
		for (int i = 0; i < 6; i++){
			iDeck.drawCard();
		}
		iDeck.addPile(rearrangedCards);
		return gameManager.discardPlayerCard(owner, this);
	}

}
