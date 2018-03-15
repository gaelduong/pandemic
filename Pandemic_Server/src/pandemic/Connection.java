package pandemic;

public class Connection {

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
}
