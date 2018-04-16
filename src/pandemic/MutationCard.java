package pandemic;

import java.util.Objects;

public class MutationCard implements InfectionCard{
    private CardType type = CardType.MutationCard;
    private String name = "MutationCard";
    private GameManager gameManager;

    public MutationCard(GameManager gm){
        gameManager = gm;
    }

    public CardType getCardType(){
        return type;
    }

    public String getCardName(){
        return name;
    }

    public void resolveMutation(){
        gameManager.infectCityForMutationCard();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutationCard that = (MutationCard) o;
        return type == that.type &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, name);
    }
}
