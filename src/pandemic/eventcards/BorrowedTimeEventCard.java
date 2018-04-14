package pandemic.eventcards;

import pandemic.GameManager;
import pandemic.Player;

public class BorrowedTimeEventCard extends EventCard {

    public BorrowedTimeEventCard(GameManager gm){
        super(gm, EventCardName.BorrowedTime);
    }

    // Returns 0 if successful, 1 if failed
    // Pre: eventCardsEnabled == true, the currentPlayer has selected to play this card, endTurn has not yet begun
    public int playEventCard(){
        Player currentPlayer = gameManager.getCurrentPlayer();
        currentPlayer.setActionsTaken(currentPlayer.getActionsTaken() - 2);
        return gameManager.discardPlayerCard(currentPlayer, this);
    }
}
