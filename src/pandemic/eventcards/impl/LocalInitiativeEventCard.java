package pandemic.eventcards.impl;

import pandemic.City;
import pandemic.GameManager;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class LocalInitiativeEventCard extends EventCard {

    public LocalInitiativeEventCard (GameManager gm){
        super(gm, EventCardName.LocalInitiative);
    }

    /**
     * Places two quarantine markers on the two cities
     */
    public int playEventCard(City city1, City city2) {
        gameManager.placeQuaratineMarker(city1, null);
        return gameManager.placeQuaratineMarker(city2, null);
    }
}
