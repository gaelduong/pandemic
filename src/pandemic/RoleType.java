package pandemic;

import java.io.Serializable;

public enum RoleType implements Serializable {

    ContingencyPlanner, Dispatcher, Medic, OperationsExpert, QuarantineSpecialist,
    Researcher, Scientist, Colonel,
    ContainmentSpecialist, Generalist, Archivist, Epidemiologist, FieldOperative, Troubleshooter, BioTerrorist,
}
