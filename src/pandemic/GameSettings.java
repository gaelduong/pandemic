package pandemic;

public class GameSettings {

  private int numOfPlayers;
  private int numOfEpidemicCards;
  private ChallengeKind gameChallenge;

  public GameSettings(int numOfPlayers, int numOfEpidemicCards, ChallengeKind gameChallenge) {
    this.numOfPlayers = numOfPlayers;
    this.numOfEpidemicCards = numOfEpidemicCards;
    this.gameChallenge = gameChallenge;
  }

  public int getNumOfPlayers() {
    return numOfPlayers;
  }

  public int getNumOfEpidemicCards() {
    return numOfEpidemicCards;
  }

  public ChallengeKind getGameChallenge() {
    return gameChallenge;
  }
}
