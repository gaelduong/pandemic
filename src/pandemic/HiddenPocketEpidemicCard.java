package pandemic;

public class HiddenPocketEpidemicCard extends EpidemicCard{

    public HiddenPocketEpidemicCard(GameManager gm) {
        super(CardType.VirulentStrainEpidemicCard, gm, EpidemicCardName.HiddenPocket);
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
        // --> MUST CHANGE THIS TO HANDLE MUTATION CARDS <--
        CityInfectionCard bottomInfectionCard = (CityInfectionCard) myGameManager.drawLastInfectionCard();
        myGameManager.discardInfectionCard(bottomInfectionCard);
        CityName cityName = bottomInfectionCard.getCityName();
        City city = myGameManager.getCityByName(cityName);
        // FOR TESTING:
        System.out.println("Infecting " + cityName + " for epidemic...");
        myGameManager.infectCityForEpidemic(city);

        // Hidden Pocket effect:
        if (myGameManager.getVirulentStrain() == null){
            myGameManager.setVirulentStrain();
        }
        if (myGameManager.isVirulentStrainEradicated() && myGameManager.infectionDiscardPileContainsVirulentStrain()){
            myGameManager.setVirulentStrainIsEradicated(false);
            myGameManager.infectVirulentStrainCitiesInInfectionDiscardPile();
        }

        // Intensify:
        myGameManager.shuffleInfectionDiscardPile();
        myGameManager.combineInfectionDeckAndPile();

        myGameManager.setEventCardsEnabled(true);
    }
}
