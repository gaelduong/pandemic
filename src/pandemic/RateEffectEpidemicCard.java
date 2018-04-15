package pandemic;

public class RateEffectEpidemicCard extends EpidemicCard{

    public RateEffectEpidemicCard(GameManager gm) {
        super(CardType.VirulentStrainEpidemicCard, gm, EpidemicCardName.RateEffect);
    }

    @Override
    public void resolveEpidemic() {
        increaseInfectIntensify();
        if (myGameManager.getVirulentStrain() == null){
            myGameManager.setVirulentStrain();
        }

        myGameManager.setRateEffectActive(true);
    }

    public String getCardName() {
        return "Rate Effect Epidemic";
    }
}
