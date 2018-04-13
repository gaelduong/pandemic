package shared;

import java.io.Serializable;

public enum MessageType implements Serializable {
    INFORMATION,
    GAME_WON,
    GAME_LOST,
    DISCARD_CARD
}
