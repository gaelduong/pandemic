package pandemic.eventcards.impl;

import pandemic.GameManager;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class RapidVaccineDeploymentEventCard extends EventCard {
    public RapidVaccineDeploymentEventCard(GameManager gm){
        super(gm, EventCardName.RapidVaccineDeployment);
    }

    public void playEventCard(){}
}
