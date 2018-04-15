package pandemic;

public class GovernmentInterferenceEpidemicCard extends EpidemicCard{

    public GovernmentInterferenceEpidemicCard(GameManager gm) {
        super(CardType.VirulentStrainEpidemicCard, gm, EpidemicCardName.GovernmentInterference);
    }

    @Override
    public void resolveEpidemic() {
        increaseInfectIntensify();
        if (myGameManager.getVirulentStrain() == null){
            myGameManager.setVirulentStrain();
        }

        myGameManager.setGovernmentInterferenceActive(true);
    }

    public String getCardName() {
        return "Government Interference Epidemic";
    }
}
