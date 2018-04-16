package pandemic.eventcards.impl;

import pandemic.GameManager;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class NewAssignmentEventCard extends EventCard {
    public NewAssignmentEventCard(GameManager gm){
        super(gm, EventCardName.NewAssignment);
    }

    public void playEventCard(){}
}
