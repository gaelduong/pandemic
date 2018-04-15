package pandemic;

import java.io.Serializable;

public interface MovingCard extends Serializable, Card {
    CityName getCityName();
}
