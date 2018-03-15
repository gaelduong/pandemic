package pandemic;

import java.io.Serializable;

public interface PlayerCard extends Serializable {
    CardType getCardType();
    String getCardName();

}
