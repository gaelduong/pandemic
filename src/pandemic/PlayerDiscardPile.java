package pandemic;

import shared.request.CardTarget;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerDiscardPile implements Serializable, CardTarget {


	private ArrayList<PlayerCard> cardsInPile;

	public PlayerDiscardPile () {
	    cardsInPile = new ArrayList<PlayerCard>();
    }
	
	// Add card to top of pile
	public void addCard(PlayerCard pc){
	    cardsInPile.add(0, pc);
	}

    public void acceptCard(Object cardObj) {
        PlayerCard card  = (PlayerCard) cardObj;
        addCard(card);
    }

    public int getPileSize() {
        return cardsInPile.size();
    }

    public ArrayList<PlayerCard> getCardsInPile(){
	    return cardsInPile;
    }

    public void printPile() {
        System.out.println("PRINTING OUT PLAYER DISCARD PILE(size: " + getPileSize()+ ")......");
        cardsInPile.forEach(card -> System.out.println("Card: " +
                "    card type : " + card.getCardType() +
                ", card name: " + card.getCardName()));
        System.out.println("PLAYER DISCARD PILE PRINTED.");
    }
}
