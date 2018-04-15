package pandemic;

public class MutationIntensifiesEventCard extends MutationEventCard{

    public MutationIntensifiesEventCard(GameManager gm){
        super(MutationEventCardName.MutationIntensifies, gm);
    }

    public void resolveMutationEvent(){
        //gameManager.infectCitiesForMutationThreatens();
    }
}
