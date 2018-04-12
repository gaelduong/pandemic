package pandemic;

import java.util.stream.Collectors;

public class AirLiftEventCard extends EventCard {

	public AirLiftEventCard(GameManager gm){
		super(gm, EventCardName.AirLift);
	}

	// Returns 0 if successful, 1 if failed
	// Pre: eventCardsEnabled == true, Player who owns this card has prompted owner of pawnToMove and received permission to move it to destination
	public int playEventCard(Player owner, Pawn pawnToMove, City destination) {
		Role playerRole = pawnToMove.getRole();
		City currentCity = pawnToMove.getLocation();

		currentCity.getCityUnits().remove(pawnToMove);
		destination.getCityUnits().add(pawnToMove);
		pawnToMove.setLocation(destination);

		if (playerRole.getRoleType() == RoleType.Medic) {
			gameManager.medicEnterCity(destination);
		}
		// -------UNCOMMENT ONCE CONTAINMENT SPECIALIST IS ADDED ---------------
//                if (currentPlayer.getRoleType().equals(RoleType.ContainmentSpecialist)){
//                    containmentSpecialistEnterCity(destination);
//                }

		return gameManager.discardPlayerCard(owner, this);
	}
}
