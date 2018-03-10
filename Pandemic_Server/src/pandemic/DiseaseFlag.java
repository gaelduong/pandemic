package pandemic;

public class DiseaseFlag extends Unit {
    private DiseaseType diseaseType;
    private boolean used;

    public DiseaseFlag(DiseaseType diseaseType) {
        this.diseaseType = diseaseType;
        used = false;
        setLocation(null);
        setUnitType(UnitType.DiseaseFlag);
    }

    public DiseaseType getDiseaseType() {
        return diseaseType;
    }

    public void setUsed(boolean b) {
        used = b;
    }

    public boolean isUsed() {
        return used;
    }
}
