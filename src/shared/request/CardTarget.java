package shared.request;

import pandemic.PlayerCard;

import java.io.Serializable;

/**
 * Represents the destination where the player card will be added to
 */
public interface CardTarget extends Serializable {

    void acceptCard(PlayerCard card);

}
