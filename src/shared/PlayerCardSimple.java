package shared;

import pandemic.CardType;

import java.io.Serializable;

public class PlayerCardSimple implements Serializable {
    private static final long serialVersionUID = 5839939014215188098L;

    private final CardType cardType;
    private final String cardName;

    public PlayerCardSimple(CardType cardType, String cardName) {
        this.cardType = cardType;
        this.cardName = cardName;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardName() {
        return cardName;
    }
}
