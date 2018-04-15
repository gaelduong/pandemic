package pandemic;

public class MutationCard implements InfectionCard{
    private CardType type = CardType.MutationCard;
    private String name = "MutationCard";
    private transient GameManager gameManager;

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
}
