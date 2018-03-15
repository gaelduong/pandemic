package shared.request;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an action that the client would like to perform.
 * If the action is accepted by the server, the server will update its internal GameState accordingly.
 * Then it will propagate the new GameState to all clients.
 */
public class UpdateRequest implements Serializable {

    private final List<PostCondition> postConditions;
    private PreCondition preCondition = null;

    /**
     * A sequence of actions that will be performed in order
     * @param actions nonempty list
     */
    public UpdateRequest(PostCondition... actions) {
        this.postConditions = Arrays.asList(actions);
    }

    /**
     * A sequence of actions that will be performed in order if and only if its pre condition is satisfied.
     *
     * @param preCondition condition which must be satisfied before execution
     * @param actions nonempty list
     */
    public UpdateRequest(PreCondition preCondition, PostCondition... actions) {
        this(actions);
        this.preCondition = preCondition;
    }

    public boolean isRequestValid() {
        return preCondition == null || preCondition.isSatisifed();
    }

    public void executeRequest() {
        postConditions.forEach(this::executeAction);
    }

    private void executeAction(PostCondition action) {
        final List arguments = action.getArguments();
        switch (action.getActionType()) {
            case MOVE_CARD:

                break;

            case MOVE_PLAYER_POS:

                break;

            case DISCOVER_CURE:

                break;

            case TREAT_DISEASE:

                break;
        }
    }
}
