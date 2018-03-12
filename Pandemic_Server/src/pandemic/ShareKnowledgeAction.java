package pandemic;

public class ShareKnowledgeAction extends ConsentRequiringAction{

	public ShareKnowledgeAction(Player participant, CityCard card){
		super(participant, card);
	}
	
	// return 0 is successful, 1 if failed
	public int playAction(){
		Player cPlayer = gameManager.getCurrentPlayer();
		if (cPlayer.isInHand(associatedCard)){
			// currentPlayer wants to give card to participant
			cPlayer.discardCard(associatedCard);
			affectedPlayer.addToHand(associatedCard);
			return 1;
		}
		else if (affectedPlayer.isInHand(associatedCard)){
			// currentPlayer wants to take card from participant
			affectedPlayer.discardCard(associatedCard);
			cPlayer.addToHand(associatedCard);
			return 1;
		}
		return 0;
	}
}
