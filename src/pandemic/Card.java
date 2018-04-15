package pandemic;

import java.io.Serializable;

public interface Card extends Serializable {
    CardType getCardType();
    String getCardName();
}
