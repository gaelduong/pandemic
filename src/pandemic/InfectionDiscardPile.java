package pandemic;

import shared.request.CardTarget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfectionDiscardPile implements Serializable, CardTarget {


	private ArrayList<InfectionCard> cardsInPile;
	
	public InfectionDiscardPile(){
	    cardsInPile = new ArrayList<InfectionCard>();
	}
	
	// Add card to top of pile
	public void addCard(InfectionCard ic){
	    cardsInPile.add(0,ic);
	}

	public void addCards(List<CityInfectionCard> list) {
	    cardsInPile.addAll(0, list);
    }

    public void acceptCard(Card card) {
        addCard((InfectionCard) card);
    }

	public void shuffle(){
	    Collections.shuffle(cardsInPile);
	}
	
	public ArrayList<InfectionCard> getCards(){
	    return cardsInPile;
	}

	public int getPileSize() {
	    return cardsInPile.size();
    }
	
	// Once InfectionDiscardPile has been added to InfectionDeck, it can no longer hold a reference
	// to the ArrayList that was added to the Deck.
	public void clearPile(){
	    //cardsInPile = new ArrayList<InfectionCard>();
        cardsInPile.clear();
	}

    public void printPile() {
        System.out.println("PRINTING OUT INFECTION DISCARD PILE(size: " + getPileSize()+ ")......");
        cardsInPile.forEach(card -> System.out.println("Card: " +
                "    card type : " + card.getCardType() +
                ", card name: " + card.getCardName()));
        System.out.println("INFECTION DISCARD PILE PRINTED.");
    }


	public boolean containsCard(InfectionCard c) {
		return cardsInPile.contains(c);
	}

	public void removeCard(InfectionCard c) {
		cardsInPile.remove(c);
	}
}
