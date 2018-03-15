package shared.request;

/**
 * Represents a condition which must be satisfied in order for the UpdateRequest to be valid.
 *
 * <b>This is only used for Consent-Requiring moves</b> because other things may have occurred while
 * the player is deciding whether or not to give consent.
 */
public class PreCondition {

    public boolean isSatisifed() {
        return false;   //TODO
    }
}
