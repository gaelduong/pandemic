package shared;

import pandemic.*;

public class CreateTestGameState {

    public static void main(String args[]){

        System.out.println("Creating host player for user 'jbh12'...");
        User userTest1 = new User("jbh12", "123456", "127.0.0.1");
        Player playerTest = new Player(userTest1);
        System.out.println("...hostPlayer created.");

        System.out.println("Creating GameBoard and initializing game....");
        GameManager gameManager = new GameManager(playerTest, 3, 6, ChallengeKind.OriginalBaseGame);

        System.out.println("Players joining game...");
        User userTest2 = new User("laskdf", "123456", "127.0.0.2");
        gameManager.joinGame(userTest2);
        User userTest3 = new User("aksjfdkdsjn", "123456", "127.0.0.3");
        gameManager.joinGame(userTest3);

        GameState gameStateTest = gameManager.getGame().generateCondensedGameState();
    }
}
