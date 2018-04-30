package ghosts;

import pacman.*;
import java.util.*;
import ghosts.*;
/*
/ FleetingGhost ao entrar no estado Scatter, ele vai sortear um dos quatro cantos pra fazer com que seu movimento
/ seja mais imprevisivel
/ No estado hunt ele deve seguir o pacman normalmente
 */
public class FleetingGhost extends GhostPlayer {

	ScatterFleetingGhost scatter;
	HuntFleetingGhost hunt;
	
	Location target;
	Random rand = new Random();
	
	Move bestMove;
	
	public FleetingGhost() {
		stateMachine = new StateMachineGhost<FleetingGhost>(this);
		this.setName("FleetingGhost");
	}
	
	public StateMachineGhost<FleetingGhost> getStateMachine() {
		return stateMachine;
	}
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		scatter = ScatterFleetingGhost.getInstance(game);
		hunt = HuntFleetingGhost.getInstace(game);
		
		if (stateMachine.getCurrentState() == null) {
			stateMachine.setCurrentState(scatter);
		}
		
		State state = game.getCurrentState(); //pega estado atual do jogo
		Location target = null;
		List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex); //movimentos possiveis

		Location myLoc = state.getGhostLocations().get(ghostIndex); //pega posicao do ghost
		double minDistance = Double.POSITIVE_INFINITY;
		
		if (stateMachine.getCurrentState() == hunt) {
			stateMachine.update();
			target = hunt.getTarget();
		
		} else {
			stateMachine.update();
			target = scatter.getTarget();
		}
		
		for(Move m : legalMoves) {
			Location next = Game.getNextLocation(myLoc, m); //pega a proxima localicazao a partir da posicao do ghost e movimentos possiveis
			double distance = Location.euclideanDistance(next, target);
			
			if(distance < minDistance) {
				minDistance = distance;
				bestMove = m;
			}
		}
		
		if(bestMove == null) throw new RuntimeException("Legal moves for ghost "+ghostIndex+": " + legalMoves);
		this.setLastMove(bestMove);
		return bestMove;
	}

}
