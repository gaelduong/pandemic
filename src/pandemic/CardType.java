package pandemic;

import java.io.Serializable;

public enum CardType implements Serializable {

    CityCard, CityInfectionCard, EventCard, BasicEpidemicCard, VirulentStrainEpidemicCard,
    MutationCard, MutationEventCard,
    MovingCard
}
