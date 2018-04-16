package pandemic;

import shared.request.CardTarget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameCardRemover implements CardTarget, Serializable {

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
