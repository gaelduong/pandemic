package shared;

import shared.request.UpdateRequest;

/**
 * Created by Russell on 3/19/2018.
 */
public class ConsentRequestBundle {
    private final String sourcePlayer, targetPlayer;
    private final UpdateRequest ur;

    public ConsentRequestBundle(String sourcePlayer, String targetPlayer, UpdateRequest ur) {
        this.sourcePlayer = sourcePlayer;
        this.targetPlayer = targetPlayer;
        this.ur = ur;
    }

    public String getSourcePlayer() {
        return sourcePlayer;
    }

    public String getTargetPlayer() {
        return targetPlayer;
    }

    public UpdateRequest getUr() {
        return ur;
    }
}
