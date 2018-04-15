package pandemic;

public class ChronicEffectEpidemicCard extends EpidemicCard{

    public ChronicEffectEpidemicCard(GameManager gm) {
        super(CardType.VirulentStrainEpidemicCard, gm, EpidemicCardName.ChronicEffect);
    }

    @Override
    public void resolveEpidemic() {
        increaseInfectIntensify();
        if (myGameManager.getVirulentStrain() == null){
            myGameManager.setVirulentStrain();
        }

        myGameManager.setChronicEffectActive(true);
    }

    public String getCardName() {
        return "Chronic Effect Epidemic";
    }
}
