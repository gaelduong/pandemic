package shared.request;

import pandemic.Card;
import pandemic.PlayerCard;

import java.io.Serializable;

/**
 * Represents the destination where the player card will be added to
 */
public interface CardTarget extends CardSourceTarget, Serializable {

    void acceptCard(Card card);

}
