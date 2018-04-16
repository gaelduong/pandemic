package pandemic;

import java.util.Objects;

public abstract class MutationEventCard implements PlayerCard{

    protected CardType type = CardType.MutationEventCard;
    protected MutationEventCardName name;
    protected GameManager gameManager;

    public MutationEventCard(MutationEventCardName pName, GameManager gm){
        name = pName;
        gameManager = gm;
    }

    public CardType getCardType(){
        return type;
    }

    public String getCardName(){
        return name.toString();
    }

    abstract void resolveMutationEvent();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutationEventCard that = (MutationEventCard) o;
        return type == that.type &&
                name == that.name;
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, name);
    }
}
