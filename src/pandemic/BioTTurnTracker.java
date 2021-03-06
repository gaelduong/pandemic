package pandemic;

import java.util.HashMap;
import java.util.Map;

public class BioTTurnTracker {

    private int type1ActionCounter; // hidden actions counter
    private int type2ActionCounter; // twice per turn action counter
    private int type3ActionCounter; // drive/ferry action counter
    private int totalActionCounter;

    private boolean type1ActionsCompleted;
    private boolean type2ActionsCompleted;
    private boolean type3ActionsCompleted;
    private boolean totalActionsCompleted;

    private boolean bioTSpotted;
    private boolean bioTCaptured;
    private boolean bioTChallengeActive;

    private Map<BioTTurnStats, Boolean> bioTStatMap;

    private Player player;

    public BioTTurnTracker(Player player) {
        type1ActionCounter = 0;
        type2ActionCounter = 0;
        type3ActionCounter = 0;
        totalActionCounter = 0;

        type1ActionsCompleted = false;
        type2ActionsCompleted = false;
        type3ActionsCompleted = false;
        totalActionsCompleted = false;

        bioTSpotted = false;
        bioTCaptured = false;

        this.player = player;

        bioTStatMap = new HashMap<BioTTurnStats, Boolean>() {
            {
                put(BioTTurnStats.ChallengeActive, true);
                put(BioTTurnStats.BioTSpotted, bioTSpotted);
                put(BioTTurnStats.BioTCaptured, bioTCaptured);
                put(BioTTurnStats.Type1ActionsCompleted, type1ActionsCompleted);
                put(BioTTurnStats.Type2ActionsCompleted, type2ActionsCompleted);
                put(BioTTurnStats.Type3ActionsCompleted, type3ActionsCompleted);
                put(BioTTurnStats.TotalActionCompleted, totalActionsCompleted);
            }
        };
    }

    public void incrementType1ActionCounter() {
        type1ActionCounter++;
        totalActionCounter++;
        player.incrementActionTaken();

        if(type1ActionCounter == 1)
            type1ActionsCompleted = true;

        if(totalActionCounter == 3)
            totalActionsCompleted = true;

        bioTStatMap.put(BioTTurnStats.Type1ActionsCompleted, type1ActionsCompleted);
        bioTStatMap.put(BioTTurnStats.TotalActionCompleted, totalActionsCompleted);
    }

    public void incrementType2ActionCounter() {
        type2ActionCounter++;
        totalActionCounter++;
        player.incrementActionTaken();

        if(type2ActionCounter == 2)
            type2ActionsCompleted = true;

        if(totalActionCounter == 3)
            totalActionsCompleted = true;

        bioTStatMap.put(BioTTurnStats.Type2ActionsCompleted, type2ActionsCompleted);
        bioTStatMap.put(BioTTurnStats.TotalActionCompleted, totalActionsCompleted);
    }

    public void incrementType3ActionCounter() {
        type3ActionCounter++;
        totalActionCounter++;
        player.incrementActionTaken();

        if(type3ActionCounter == 3)
            type3ActionsCompleted = true;

        if(totalActionCounter == 3)
            totalActionsCompleted = true;

        bioTStatMap.put(BioTTurnStats.Type3ActionsCompleted, type3ActionsCompleted);
        bioTStatMap.put(BioTTurnStats.TotalActionCompleted, totalActionsCompleted);
    }

    public boolean isType1ActionsCompleted() {

        return type1ActionsCompleted || isTotalActionsCompleted();
    }

    public boolean isType2ActionsCompleted() {

        return type2ActionsCompleted || isTotalActionsCompleted();
    }

    public boolean isType3ActionsCompleted() {

        return type3ActionsCompleted || isTotalActionsCompleted();
    }

    public boolean isTotalActionsCompleted() {

        return totalActionsCompleted;
    }

    public void resetTurnTracker() {
        type1ActionCounter = 0;
        type2ActionCounter = 0;
        type3ActionCounter = 0;
        totalActionCounter = 0;

        type1ActionsCompleted = false;
        type2ActionsCompleted = false;
        type3ActionsCompleted = false;
        totalActionsCompleted = false;

        player.setActionsTaken(0);

        bioTStatMap.put(BioTTurnStats.Type1ActionsCompleted, type1ActionsCompleted);
        bioTStatMap.put(BioTTurnStats.Type2ActionsCompleted, type2ActionsCompleted);
        bioTStatMap.put(BioTTurnStats.Type3ActionsCompleted, type3ActionsCompleted);
        bioTStatMap.put(BioTTurnStats.TotalActionCompleted, totalActionsCompleted);
    }

    public void setSpotted(boolean spotted) {

        bioTSpotted = spotted;
        bioTStatMap.put(BioTTurnStats.BioTCaptured, bioTCaptured);
    }

    public void setCaptured(boolean captured) {

        bioTCaptured = true;
        bioTStatMap.put(BioTTurnStats.BioTCaptured, bioTCaptured);
    }

    public boolean isSpotted() {

        return bioTSpotted;
    }

    public boolean isCaptured() {

        return bioTCaptured;
    }

    public Map<BioTTurnStats, Boolean> getBioTStatMap() {
        return bioTStatMap;
    }
}
