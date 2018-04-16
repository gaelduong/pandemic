package pandemic;

import java.io.Serializable;

public enum GamePhase implements Serializable {
    ReadyToJoin, SetupRoleSelection, TurnActions, TurnPlayerCards, TurnInfection, Completed
}
