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


    }
}
