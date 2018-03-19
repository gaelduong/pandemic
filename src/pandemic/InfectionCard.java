package pandemic;

import java.io.Serializable;

public interface InfectionCard extends Serializable {
    CardType getCardType();
    String getCardName();
}
