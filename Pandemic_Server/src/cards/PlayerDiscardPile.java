package cards;

import java.util.ArrayList;

public class PlayerDiscardPile {

	private ArrayList<PlayerCard> cardsInPile;
	
	// Add card to top of pile
	public void addCard(PlayerCard pc){
		cardsInPile.add(0, pc);
	}
}
