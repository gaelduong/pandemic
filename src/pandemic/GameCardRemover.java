package pandemic;

import shared.request.CardTarget;

import java.util.ArrayList;
import java.util.List;

public class GameCardRemover implements CardTarget {

    private final List<Card> cardsRemovedFromGame;
    private final Game game;

    public GameCardRemover(Game game) {
        this.game = game;
        this.cardsRemovedFromGame = new ArrayList<>();
    }

    @Override
    public void acceptCard(Card card) {
        cardsRemovedFromGame.add(card);
        //TODO omer call corresponding back-end game method to remove that card from the game
    }
}
