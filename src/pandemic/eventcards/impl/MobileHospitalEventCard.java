package pandemic.eventcards.impl;

import pandemic.GameManager;
import pandemic.Player;
import pandemic.eventcards.EventCard;
import pandemic.eventcards.EventCardName;

public class MobileHospitalEventCard extends EventCard {

    public MobileHospitalEventCard(GameManager gm){
        super(gm, EventCardName.MobileHospital);
    }

    // Returns 0 if successful, 1 if failed
    // Pre: eventCardsEnabled == true, the currentPlayer has selected to play this card, endTurn has not yet begun
    public int playEventCard(){
        Player currentPlayer = gameManager.getCurrentPlayer();
        gameManager.setMobileHospitalActive(true);
        return gameManager.discardPlayerCard(currentPlayer, this);
    }
}
