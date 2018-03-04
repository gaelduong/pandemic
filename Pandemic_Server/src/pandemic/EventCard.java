package pandemic;

public class EventCard implements PlayerCard{
	
	private GameManager.EventCardName name;
	
	public EventCard(GameManager.EventCardName pName){
		name = pName;
	}
}
