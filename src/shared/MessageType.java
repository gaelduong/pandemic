package shared;

import java.io.Serializable;

public enum MessageType implements Serializable {
    INFORMATION,
    WARNING,
    ERROR,
    GAME_WON,
    GAME_LOST;
}
