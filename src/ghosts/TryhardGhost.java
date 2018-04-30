package ghosts;

import java.util.List;

import pacman.*;

/*
 * No modo hunt vai pro pacman
 * No modo scatter ele vai pro dot mais proximo do pacman
 */

public class TryhardGhost extends GhostPlayer {
	
	ScatterTryhardGhost scatter;
	HuntTryhardGhost hunt;
	
	boolean isTrying = false; //variavel pra quando tiver na cola do pacman
	
	public TryhardGhost() {
		this.stateMachine = new StateMachineGhost<TryhardGhost>(this);
		this.setName("TryhardGhost");
	}
	
	public StateMachineGhost<TryhardGhost> getStateMachine() {
		return stateMachine;
	}
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		scatter = ScatterTryhardGhost.getInstance(game);
		hunt = HuntTryhardGhost.getInstance(game);
		
		Location target;
		Move bestMove = null;
		
		if (stateMachine.getCurrentState() == null) {
			stateMachine.setCurrentState(scatter);
		}
		
		stateMachine.update();
		if (stateMachine.getCurrentState() == hunt) {
			target = hunt.getTarget();
				
		} else {
			target = scatter.getTarget();
		}
		
		State s = game.getCurrentState();
		Location mine = s.getGhostLocations().get(ghostIndex);
		double minDist = Double.POSITIVE_INFINITY;
		
		List<Move> moves = game.getLegalGhostMoves(ghostIndex);
		
		for (Move m : moves) {
			Location next = Game.getNextLocation(mine, m);
			double d = Location.euclideanDistance(next, target);
			
			if (d < minDist) {
				minDist = d;
				bestMove = m;
			}
		}
		if (minDist <= 5.0)
			isTrying = true;
		else 
			isTrying = false;
		System.out.println("distancia da raba: " + minDist);
	    if (bestMove == null) throw new RuntimeException("Legal moves for ghost "+ghostIndex+": " + moves);
	    return bestMove;
	}

}
