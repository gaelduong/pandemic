package pandemic;

import shared.request.CardSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlayerDeck implements CardSource {


	private ArrayList<PlayerCard> cardsInDeck;
	
	// Constructor creates and empty deck
	public PlayerDeck(){
	    cardsInDeck = new ArrayList<PlayerCard>();
	}
	
	public void addCard(PlayerCard pCard){
	    cardsInDeck.add(pCard);
	}

    public PlayerCard getCard(PlayerCard card) {
        return cardsInDeck.stream().filter(c -> c.getCardName().equals(card.getCardName()) &&
                c.getCardType() == card.getCardType())
                .findAny().orElse(null);
    }
	
	public int getDeckSize(){
	    return cardsInDeck.size();
	}
	
	// IDK what this method is for
	public void addAll(List<PlayerCard> pCards){}
	
	public void clear(){
	    cardsInDeck.clear();
	}

	// returns null if deck is empty
	public PlayerCard drawCard(){
	    return !cardsInDeck.isEmpty() ? cardsInDeck.remove(0) : null;
	}

	public void shuffleDeck() {
        Collections.shuffle(cardsInDeck);
    }

    //must be done after all players have joined game and have been dealt cards according to numOfPlayers
    public void insertAndShuffleEpidemicCards(List<PlayerCard> epidemicCards) {
	    shuffleDeck();
	    List<List<PlayerCard>> choppedDeck = chopDeckForEpidemicCardInsertion( (int) Math.ceil(cardsInDeck.size() / (double) epidemicCards.size()));
	    ArrayList<PlayerCard> newCardDeck = new ArrayList<PlayerCard>();
	    for(List<PlayerCard> sublist : choppedDeck) {
	        if(epidemicCards.isEmpty())
	            break;
	        sublist.add(epidemicCards.remove(0));
            Collections.shuffle(sublist);
            newCardDeck.addAll(sublist);
        }
        cardsInDeck.clear();
	    cardsInDeck.addAll(newCardDeck);
    }

    private List<List<PlayerCard>> chopDeckForEpidemicCardInsertion(int numOfCards) {
        System.out.println("NUMBER OF CARDS FOR CHOPPING: " + numOfCards);
	    List<List<PlayerCard>> result = new ArrayList<List<PlayerCard>>();
	    int sizeOfDeck = getDeckSize();

	    for(int i = 0; i < sizeOfDeck; i+=numOfCards) {
	        // separating cardsInDeck into numOfEpidemicCards separate lists of almost equal size
            // (except perhaps the last list)
	        result.add(new ArrayList<PlayerCard>(cardsInDeck.subList(i, Math.min(sizeOfDeck, i + numOfCards))));
        }
        return result;
    }

    public void printDeck() {
        System.out.println("PRINTING OUT PLAYER DECK (size: " + getDeckSize()+ ")......");
        cardsInDeck.forEach(card -> System.out.println("Card: " +
                                                       "    card type : " + card.getCardType() +
                                                       ", card name: " + card.getCardName()));
        System.out.println("PLAYER DECK PRINTED.");
    }

    //FOR TESTING ////////////////////////////////////////////////
    public PlayerCard getCard(String cardName) {
	    return cardsInDeck.stream().filter(card -> card.getCardName().equals(cardName)).findAny().orElse(null);
    }
}
