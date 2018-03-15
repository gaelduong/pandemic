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

        
        System.out.println("TESTING SHAREKNOWLEDGE...");
        User user1Test = new User("jbh12", "123456", "127.0.0.1");
        Player player1Test = new Player(user1Test);
        player1Test.setGameManager(gameManager);
        User user2Test = new User("skdnasn", "123456", "127.0.0.1");
        Player player2Test = new Player(user2Test);
        player2Test.setGameManager(gameManager);
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
        
    }
}
