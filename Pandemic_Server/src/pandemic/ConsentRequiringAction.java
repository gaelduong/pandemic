package pandemic;

abstract class ConsentRequiringAction {
	
	protected GameManager gameManager;
	protected Player affectedPlayer;
	protected PlayerCard associatedCard;
	
	public ConsentRequiringAction(Player player, PlayerCard card){
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
