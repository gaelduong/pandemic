package shared.request;

import java.io.Serializable;

/**
 * Represents a condition which must be satisfied in order for the UpdateRequest to be valid.
 *
 * <b>This is only used for Consent-Requiring moves</b> because other things may have occurred while
 * the player is deciding whether or not to give consent.
 */
public class PreCondition implements Serializable {


    /**
     * We don't need to use a precondition since the UpdateRequests cannot be performed on an inconsistent gamestate.
     *
     * Consider the following example:
     * A requests share knowledge to B. and before B accepts, C does something to change the gamestate (maybe an event card).
     * Then B accepts. and sent an update request on an inconsistent game state.
     *
     * This cannot occur, because the GUI would refresh the gamestate when C alters it, removing B's option to share knowledge.
     */
    public boolean isSatisifed() {
        return true;
    }
}
