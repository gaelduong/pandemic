package pandemic;

public class ShareKnowledgeAction extends ConsentRequiringAction{

	public ShareKnowledgeAction(GameManager gm, Player participant, CityCard card){
		super(gm, participant, card);
	}
	
	// return 0 if successful, 1 if failed
	public int playAction(){
		Player cPlayer = gameManager.getCurrentPlayer();
		if (cPlayer.isInHand(associatedCard)){
			// currentPlayer wants to give card to participant
			cPlayer.discardCard(associatedCard);
			affectedPlayer.addToHand(associatedCard);
			gameManager.checkHandSize(affectedPlayer);
			return 1;
		}
		else if (affectedPlayer.isInHand(associatedCard)){
			// currentPlayer wants to take card from participant
			affectedPlayer.discardCard(associatedCard);
			cPlayer.addToHand(associatedCard);
			gameManager.checkHandSize(cPlayer);
			return 1;
		}
		return 0;
	}
}
