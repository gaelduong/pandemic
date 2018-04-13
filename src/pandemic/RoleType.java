package pandemic;

import java.io.Serializable;

public enum RoleType implements Serializable {

    ContingencyPlanner, Dispatcher, Medic, OperationsExpert, QuarantineSpecialist,
    Researcher, Scientist,
    ContainmentSpecialist, Generalist, Archivist, Epidemiologist
}
