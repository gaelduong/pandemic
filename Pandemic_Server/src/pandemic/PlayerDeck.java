package pandemic;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeck {


	private ArrayList<PlayerCard> cardsInDeck;
	
	// Constructor creates and empty deck
	public PlayerDeck(){
		cardsInDeck = new ArrayList<PlayerCard>();
	}
	
	public void addCard(PlayerCard pCard){
		cardsInDeck.add(pCard);
	}
	
	public int getDeckSize(){
		return cardsInDeck.size();
	}
	
	// IDK what this method is for
	public void addAll(List<PlayerCard> pCards){}
	
	public void clear(){
		cardsInDeck.clear();
	}
	
	// GameManager must check that PlayerDeck is not empty before calling drawCard()
	public PlayerCard drawCard(){
		return cardsInDeck.remove(0);
	}
}
