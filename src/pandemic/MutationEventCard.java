package pandemic;

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
}
