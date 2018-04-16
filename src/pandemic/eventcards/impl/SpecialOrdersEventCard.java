package pandemic.eventcards.impl;

import pandemic.GameManager;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class SpecialOrdersEventCard extends EventCard {
    public SpecialOrdersEventCard(GameManager gm){
        super(gm, EventCardName.SpecialOrders);
    }

    public void playEventCard(){}
}
