package pandemic;

public class ComplexMolecularStructureEpidemicCard extends EpidemicCard{

    public ComplexMolecularStructureEpidemicCard(GameManager gm) {
        super(CardType.VirulentStrainEpidemicCard, gm, EpidemicCardName.ComplexMolecularStructure);
    }

    @Override
    public void resolveEpidemic() {
        increaseInfectIntensify();
        if (myGameManager.getVirulentStrain() == null){
            myGameManager.setVirulentStrain();
        }

        myGameManager.setComplexMolecularStructureActive(true);
    }
}
