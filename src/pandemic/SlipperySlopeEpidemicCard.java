package pandemic;

public class SlipperySlopeEpidemicCard extends EpidemicCard{

    public SlipperySlopeEpidemicCard(GameManager gm) {
        super(CardType.VirulentStrainEpidemicCard, gm, EpidemicCardName.SlipperySlope);
    }

    @Override
    public void resolveEpidemic(){
        increaseInfectIntensify();
        if (myGameManager.getVirulentStrain() == null){
            myGameManager.setVirulentStrain();
        }

        myGameManager.setSlipperySlopeActive(true);
    }

    public String getCardName() {
        return "Slippery Slope Epidemic";
    }
}
