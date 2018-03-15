package pandemic;

public class Disease {

    private DiseaseType diseaseType;
    private boolean cured;
    private boolean eradicated;

    public Disease(DiseaseType diseaseType) {
        this.diseaseType = diseaseType;
        cured = false;
        eradicated = false;
    }

    public DiseaseType getDiseaseType() {
        return diseaseType;
    }

    public boolean isCured() {
        return cured;
    }

    public void setCured(boolean cured) {
        this.cured = cured;
    }

    public boolean isEradicated() {
        return eradicated;
    }

    public void setEradicated(boolean eradicated) {
        this.eradicated = eradicated;
    }
}
