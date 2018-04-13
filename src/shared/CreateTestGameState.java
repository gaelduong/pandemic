package shared;

import pandemic.*;

public class CreateTestGameState {

    public static void main(String args[]){

        System.out.println("Creating host player for user 'jbh12'...");
        User userTest1 = new User("jbh12", "123456", "127.0.0.1");
        Player playerTest = new Player(userTest1);
        System.out.println("...hostPlayer created.");

        System.out.println("Creating GameBoard and initializing game....");
        GameManager gameManager = new GameManager( 2, 6, ChallengeKind.OriginalBaseGame);

        System.out.println("Players joining game...");
        User userTest2 = new User("laskdf", "123456", "127.0.0.2");
        gameManager.joinGame(userTest2);

        System.out.println("------ACTIVE PLAYER LIST:");
        gameManager.getActivePlayers().forEach(p -> System.out.println("    Player username:" + p.getPlayerUserName()));
        System.out.println("-------------------------");

        User userTest3 = new User("aksjfdkdsjn", "123456", "127.0.0.3");
        gameManager.joinGame(userTest3);

        System.out.println("------ACTIVE PLAYER LIST:");
        gameManager.getActivePlayers().forEach(p -> System.out.println("    Player username:" + p.getPlayerUserName()));
        System.out.println("-------------------------");

        System.out.println("All Players joined. Creating game....");

        gameManager.createNewGame();
        System.out.println("Game created.");

        System.out.println("------ACTIVE PLAYER LIST:");
        gameManager.getActivePlayers().forEach(p -> System.out.println("    Player username:" + p.getPlayerUserName() +
                                                ", role: " + p.getRoleType()));
        System.out.println("-------------------------");

        // Forcing outbreak in Bangkok and Los Angeles:
        City bangkok = gameManager.getCityByName(CityName.Bangkok);
        City losAngeles = gameManager.getCityByName(CityName.LosAngeles);
//        gameManager.infectNextCity(bangkok);
//        gameManager.infectNextCity(bangkok);
//        gameManager.infectNextCity(bangkok);
//        gameManager.infectNextCity(bangkok);
//        gameManager.infectNextCity(losAngeles);
//        gameManager.infectNextCity(losAngeles);
//        gameManager.infectNextCity(losAngeles);
//        gameManager.infectNextCity(losAngeles);
//        City chennai = gameManager.getCityByName(CityName.Chennai);
//        gameManager.infectNextCity(chennai);


        GameState gameStateTest = gameManager.getGame().generateCondensedGameState();

    }
}
