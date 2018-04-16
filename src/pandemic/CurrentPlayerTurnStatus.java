package pandemic;

import java.io.Serializable;

public enum CurrentPlayerTurnStatus implements Serializable {
	PlayingActions, WaitingForReply, PlayerDiscardingCards
}
