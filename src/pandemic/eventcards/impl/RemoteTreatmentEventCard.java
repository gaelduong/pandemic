package pandemic.eventcards.impl;

import pandemic.GameManager;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class RemoteTreatmentEventCard extends EventCard {
    public RemoteTreatmentEventCard(GameManager gm){
        super(gm, EventCardName.RemoteTreatment);
    }

    public void playEventCard(){}

}
