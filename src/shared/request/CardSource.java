package shared.request;

import pandemic.Card;
import pandemic.PlayerCard;
import shared.PlayerCardSimple;

import java.io.Serializable;

/**
 * Represents a source from where a card will be taken from
 */
public interface CardSource extends CardSourceTarget, Serializable {

    Card getCard(PlayerCardSimple card);

}
