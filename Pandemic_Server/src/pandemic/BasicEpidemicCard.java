package pandemic;

public class BasicEpidemicCard extends EpidemicCard {

	public BasicEpidemicCard(GameManager gm){
		super(gm);
	}
	
	public void resolveEpidemic(){
		increaseInfectIntensify();
	}
}
