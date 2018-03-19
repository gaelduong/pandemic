package pandemic;

import java.io.Serializable;

public class Role implements Serializable {
    private RoleType r;
    private boolean assigned;

    public Role(RoleType r) {
        this.r = r;
        assigned = false;
    }

    public RoleType getRoleType() {
        return r;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }



}
