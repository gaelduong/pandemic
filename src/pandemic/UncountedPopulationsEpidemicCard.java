package pandemic;

public class UncountedPopulationsEpidemicCard extends EpidemicCard{

    public UncountedPopulationsEpidemicCard(GameManager gm) {
        super(CardType.VirulentStrainEpidemicCard, gm, EpidemicCardName.UncountedPopulations);
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

        // Uncounted Population effect:
        if (myGameManager.getVirulentStrain() == null){
            myGameManager.setVirulentStrain();
        }
        myGameManager.infectUncountedPopulations();

        // Intensify:
        myGameManager.shuffleInfectionDiscardPile();
        myGameManager.combineInfectionDeckAndPile();

        myGameManager.setEventCardsEnabled(true);
    }
}
