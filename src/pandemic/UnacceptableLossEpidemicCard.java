package pandemic;

public class UnacceptableLossEpidemicCard extends EpidemicCard{

    public UnacceptableLossEpidemicCard(GameManager gm) {
        super(CardType.VirulentStrainEpidemicCard, gm, EpidemicCardName.UnacceptableLoss);
    }

    @Override
    public void resolveEpidemic() {
        myGameManager.setEventCardsEnabled(false);
        // Increase:
        int currentInfectionRate = myGameManager.getInfectionRate();
        if (currentInfectionRate < 4) {
            myGameManager.increaseInfectionRate();
        }

        // Infect:
        CityInfectionCard bottomInfectionCard = (CityInfectionCard) myGameManager.drawLastInfectionCard();
        myGameManager.discardInfectionCard(bottomInfectionCard);
        CityName cityName = bottomInfectionCard.getCityName();
        City city = myGameManager.getCityByName(cityName);
        // FOR TESTING:
        System.out.println("Infecting " + cityName + " for epidemic...");
        myGameManager.infectCityForEpidemic(city);

        // Unacceptable Loss effect:
        if (myGameManager.getVirulentStrain() == null){
            myGameManager.setVirulentStrain();
        }
        myGameManager.resolveUnacceptableLoss();

        // Intensify:
        myGameManager.shuffleInfectionDiscardPile();
        myGameManager.combineInfectionDeckAndPile();

        myGameManager.setEventCardsEnabled(true);
    }

    public String getCardName() {
        return "Unacceptable Loss Epidemic";
    }
}
