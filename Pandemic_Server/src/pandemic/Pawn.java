package pandemic;

public class Pawn extends Unit {
    private Role r;
    private Player p;
    private boolean assigned;

    public Pawn(Role r) {
        this.r = r;
        assigned = false;
        setLocation(null);
        setUnitType(UnitType.Pawn);
    }

    public Player getPlayer() {
        return p;
    }

    public void setPlayer(Player p) {
        this.p = p;
    }

    public Role getRole() {
        return r;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
