package pandemic;

abstract class ConsentRequiringAction {
	
	protected GameManager gameManager;
	protected Player affectedPlayer;
	protected PlayerCard associatedCard;
	
	public ConsentRequiringAction(GameManager gm, Player player, PlayerCard card){
		gameManager = gm;
		affectedPlayer = player;
		associatedCard = card;
	}
	
	public Player getAffectedPlayer(){
		return affectedPlayer;
	}
	
	public PlayerCard getAssociatedCard(){
		return associatedCard;
	}
	
	// returns 0 if successful, 1 if failed
	public abstract int playAction();
}
