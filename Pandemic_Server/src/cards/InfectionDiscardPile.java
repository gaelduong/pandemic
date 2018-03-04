package cards;

import java.util.ArrayList;

public class InfectionDiscardPile {

	private ArrayList<InfectionCard> cardsInPile;
	
	// Add card to top of pile
	public void addCard(InfectionCard ic){
		cardsInPile.add(0,ic);
	}
}
