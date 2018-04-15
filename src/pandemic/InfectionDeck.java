package pandemic;

import shared.PlayerCardSimple;
import shared.request.CardSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfectionDeck implements CardSource {

	
	private ArrayList<InfectionCard> cardsInDeck;
	
	// Constructor creates empty deck
	public InfectionDeck(){
	    cardsInDeck = new ArrayList<InfectionCard>();
	}
	
	public void addCard(InfectionCard pCard){
	    cardsInDeck.add(pCard);
	}
	
	public void shuffleDeck(){
	    Collections.shuffle(cardsInDeck);
	}

	public int getDeckSize() {
	    return cardsInDeck.size();
    }
	
	public InfectionCard drawCard(){
        return !cardsInDeck.isEmpty() ? cardsInDeck.remove(0) : null;
	}
	
	public InfectionCard drawLastCard(){
        return !cardsInDeck.isEmpty() ? cardsInDeck.remove(cardsInDeck.size()-1) : null;
	}
	
	// Add newCards to beginning of Deck
	public void addPile(List<InfectionCard> newCards){
	    cardsInDeck.addAll(0, newCards);
	}

    public void printDeck() {
        System.out.println("PRINTING OUT INFECTION DECK(size: " + getDeckSize()+ ")......");
        cardsInDeck.forEach(card -> System.out.println("Card: " +
                "    card type : " + card.getCardType() +
                ", card name: " + card.getCardName()));
        System.out.println("INFECTION DECK PRINTED.");
    }

    public InfectionCard getCard(PlayerCardSimple card) {
        CardType pcsCardType;
        if(card.getCardType() == CardType.MovingCard) {
            pcsCardType = CardType.CityInfectionCard;
        } else {
            pcsCardType = card.getCardType();
        }

        return cardsInDeck.stream().filter(c -> c.getCardName().equals(card.getCardName()) &&
                c.getCardType() == pcsCardType)
                .findAny().orElse(null);
    }
}
