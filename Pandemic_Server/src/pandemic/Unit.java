package pandemic;

public abstract class Unit {
    private City location;
    private UnitType u;

    public City getLocation() {
        return location;
    }

    public UnitType getUnitType() {
        return u;
    }

    public void setLocation(City c) {
        location = c;
    }

    public void setUnitType(UnitType u) {
        this.u = u;
    }



}
