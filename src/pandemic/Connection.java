package pandemic;

import java.io.Serializable;

public class Connection implements Serializable {

    private City end1;
    private City end2;
    private ConnectionStatus status;

    public Connection(City c1, City c2) {
        end1 = c1;
        end2 = c2;
        status = ConnectionStatus.NoOutbreaks;
    }

    public City getEnd1() {
        return end1;
    }

    public City getEnd2() {
        return end2;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setConnectionStatus(DiseaseType d) {
        if (d != null) {
            switch (d) {
                case Blue:
                    status = ConnectionStatus.BlueDiseaseOutbreak;
                    break;
                case Black:
                    status = ConnectionStatus.BlackDiseaseOutbreak;
                    break;
                case Red:
                    status = ConnectionStatus.RedDiseaseOutbreak;
                    break;
                case Yellow:
                    status = ConnectionStatus.YellowDiseaseOutbreak;
                    break;
                default:
                    break;
            }
        } else {
            status = ConnectionStatus.NoOutbreaks;
        }
    }

}
