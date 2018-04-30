package ghosts;

import pacman.*;

import java.util.List;
import java.util.Random;

import ghosts.*;

/*
 * Blind ghost vai sempre perseguir o pacman porem so vai ser informado a posicao dele 
 * no momento que ele entra no estado hunt
 */
public class BlindGhost extends GhostPlayer {
	
	HuntBlindGhost hunt;
	ScatterBlindGhost scatter;
	
	Location target;
	Random rand = new Random();
	Move best;
	
	public BlindGhost() {
		stateMachine = new StateMachineGhost<BlindGhost>(this);
		this.setName("BlindGhost");
	}

	public StateMachineGhost<BlindGhost> getStateMachine() {
		return stateMachine;
	}
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		scatter = ScatterBlindGhost.getInstance(game, ghostIndex);
		hunt = HuntBlindGhost.getInstance(game, ghostIndex);
		
		if (stateMachine.getCurrentState() == null)
			stateMachine.setCurrentState(scatter);
		
		State s = game.getCurrentState();
		Location mine = s.getGhostLocations().get(ghostIndex);
		List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex);
		double minDistance = Double.POSITIVE_INFINITY;
		
		if (stateMachine.getCurrentState() == hunt) {
			stateMachine.update();
			target = hunt.getTarget();
			
		} else {
			stateMachine.update();
			target = scatter.getTarget();
		}
		
		for (Move m : legalMoves) {
			Location next = Game.getNextLocation(mine, m);
			double distance = Location.euclideanDistance(next, target);
			
			if (distance < minDistance) {
				minDistance = distance;
				best = m;
			}
		}
		
		if (best == null) throw new RuntimeException("Legal moves for ghost "+ghostIndex+": " + legalMoves);
		this.setLastMove(best);
		return best;
	}

}

