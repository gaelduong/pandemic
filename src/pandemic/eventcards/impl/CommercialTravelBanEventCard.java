package pandemic.eventcards.impl;

import pandemic.GameManager;
import pandemic.Player;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class CommercialTravelBanEventCard extends EventCard {

    public CommercialTravelBanEventCard(GameManager gm){
        super(gm, EventCardName.CommercialTravelBan);
    }

    // Returns 0 if successful, 1 if failed
    // Pre: eventCardsEnabled == true
    public int playEventCard(Player owner){
        gameManager.setCommercialTravelBanActive(true);
        gameManager.setCommercialTravelBanPlayedBy(owner);
        return gameManager.discardPlayerCard(owner, this);
    }
}
