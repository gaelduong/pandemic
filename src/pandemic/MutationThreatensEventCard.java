package pandemic;

public class MutationThreatensEventCard extends MutationEventCard{

    public MutationThreatensEventCard(GameManager gm){
        super(MutationEventCardName.MutationThreatens, gm);
    }

    public void resolveMutationEvent(){
        gameManager.infectCitiesForMutationThreatens();
    }

    public String getCardName(){
        return "Mutation Threatens!";
    }
}
