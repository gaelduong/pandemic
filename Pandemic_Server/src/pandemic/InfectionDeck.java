package pandemic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfectionDeck {
	
	private ArrayList<InfectionCard> cardsInDeck;
	
	// Constructor creates empty deck
	public InfectionDeck(){
		cardsInDeck = new ArrayList<InfectionCard>();
	}
	
	public void addCard(InfectionCard pCard){
		cardsInDeck.add(pCard);
	}
	
	public void shuffle(){
		Collections.shuffle(cardsInDeck);
	}
	
	public InfectionCard drawCard(){
		return cardsInDeck.remove(0);
	}
	
	public InfectionCard drawLastCard(){
		return cardsInDeck.remove(cardsInDeck.size()-1);
	}
	
	// Add newCards to beginning of Deck
	public void addPile(List<InfectionCard> newCards){
		cardsInDeck.addAll(0, newCards);
	}
}
