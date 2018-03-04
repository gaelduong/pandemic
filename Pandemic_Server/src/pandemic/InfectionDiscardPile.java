package pandemic;

import java.util.ArrayList;
import java.util.Collections;

public class InfectionDiscardPile {

	private ArrayList<InfectionCard> cardsInPile;
	
	public InfectionDiscardPile(){
		cardsInPile = new ArrayList<InfectionCard>();
	}
	
	// Add card to top of pile
	public void addCard(InfectionCard ic){
		cardsInPile.add(0,ic);
	}
	
	public void shuffle(){
		Collections.shuffle(cardsInPile);
	}
	
	public ArrayList<InfectionCard> getCards(){
		return cardsInPile;
	}
	
	// Once InfectionDiscardPile has been added to InfectionDeck, it can no longer hold a reference
	// to the ArrayList that was added to the Deck.
	public void clearPile(){
		cardsInPile = new ArrayList<InfectionCard>();
	}
}
