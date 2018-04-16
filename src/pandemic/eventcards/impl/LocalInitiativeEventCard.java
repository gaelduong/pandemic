package pandemic.eventcards.impl;

import pandemic.City;
import pandemic.GameManager;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class LocalInitiativeEventCard extends EventCard {

    public LocalInitiativeEventCard (GameManager gm){
        super(gm, EventCardName.LocalInitiative);
    }

    public int playEventCard(City city) {
        return gameManager.placeQuaratineMarker(city, null);
    }
}
