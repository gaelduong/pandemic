package pandemic;

public class MutationSpreadsEventCard extends MutationEventCard{

    public MutationSpreadsEventCard(GameManager gm){
        super(MutationEventCardName.MutationSpreads, gm);
    }

    public void resolveMutationEvent(){
        gameManager.infectCitiesForMutationSpreads();
    }
}
