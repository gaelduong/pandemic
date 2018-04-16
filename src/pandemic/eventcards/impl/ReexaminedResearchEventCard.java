package pandemic.eventcards.impl;

import pandemic.GameManager;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class ReexaminedResearchEventCard extends EventCard {
    public ReexaminedResearchEventCard(GameManager gm){
        super(gm, EventCardName.ReexaminedResearch);
    }

    public void playEventCard(){}
}
