package pandemic;

public class BackendServerTester {
    public static void main(String[] args) {
        System.out.println("TESTING BACKEND SERVER....");

        System.out.println("Creating host player for user 'jbh12'...");
        User userTest = new User("jbh12", "123456", "127.0.0.1");
        Player playerTest = new Player(userTest);
        System.out.println("...hostPlayer created.");

        System.out.println("Creating GameBoard and initializing game....");
        GameManager gameManager = new GameManager(playerTest, 3, 6, ChallengeKind.OriginalBaseGame);
        gameManager.createNewGame();

        
        /*System.out.println("TESTING SHAREKNOWLEDGE...");
        User user1Test = new User("jbh12", "123456", "127.0.0.1");
        //Player player1Test = new Player(user1Test);
        gameManager.joinGame(user1Test);

        User user2Test = new User("skdnasn", "123456", "127.0.0.1");
        //Player player2Test = new Player(user2Test);
        CityCard c = new CityCard(CityName.Atlanta, Region.Blue);
        player2Test.addToHand(c);
        gameManager.setCurrentPlayer(player1Test);
        
        gameManager.playShareKnowledgeRequest(player2Test, c);
        System.out.println("ShareKnowledge Request created");
        
     //   gameManager.replyConsentRequest(false);
        gameManager.replyConsentRequest(true);
        System.out.println("Player1 holds Card c: " + player1Test.isInHand(c));
        System.out.println("Player2 holds Card c: " + player2Test.isInHand(c));
        
        System.out.println("TESTING SHAREKNOWLEDGE COMPLETE");
*/

//        System.out.println("TESTING END TURN...");
//        gameManager.endTurn();
//        System.out.println("TESTING END TURN COMPLETE");


//        System.out.println("TESTING INFECT THEN OUTBREAK...");
        City madrid = gameManager.getCityByName(CityName.Madrid);
        City london = gameManager.getCityByName(CityName.London);
        City washington = gameManager.getCityByName(CityName.Washington);
        City newyork = gameManager.getCityByName(CityName.NewYork);
        City paris = gameManager.getCityByName(CityName.Paris);
        City essen = gameManager.getCityByName(CityName.Essen);
        City montreal = gameManager.getCityByName(CityName.Montreal);
        City milan = gameManager.getCityByName(CityName.Milan);
        gameManager.infectNextCity(washington);
        gameManager.infectNextCity(madrid);
        gameManager.infectNextCity(madrid);
        gameManager.infectNextCity(madrid);
        gameManager.infectNextCity(london);
        gameManager.infectNextCity(london);
        gameManager.infectNextCity(london);
        gameManager.infectNextCity(newyork);
        gameManager.infectNextCity(newyork);
        gameManager.infectNextCity(newyork);
        gameManager.infectNextCity(washington);
        gameManager.infectNextCity(washington);
        gameManager.infectNextCity(washington);
        gameManager.infectNextCity(paris);
        gameManager.infectNextCity(paris);
        gameManager.infectNextCity(paris);
        gameManager.infectNextCity(essen);
        gameManager.infectNextCity(essen);
        gameManager.infectNextCity(essen);
        gameManager.infectNextCity(montreal);
        gameManager.infectNextCity(montreal);
        gameManager.infectNextCity(montreal);
        gameManager.infectNextCity(milan);
        gameManager.infectNextCity(milan);
        gameManager.infectNextCity(milan);





//        System.out.println("TESTING Game::increaseInfectionRate...");
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        gameManager.getGame().increaseInfectionRate();
//        System.out.println("infection rate increased = " + gameManager.getGame().getInfectionRate());
//        System.out.println("TESTING Game::increaseInfectionRate complete.");



    }
}
