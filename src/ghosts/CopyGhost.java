package ghosts;

import pacman.*;

import java.util.List;

import ghosts.*;

/*
 * CopyGhost vai copiar os movimentos do pacman
 */
public class CopyGhost extends GhostPlayer {

	ScatterCopyGhost scatter;
	HuntCopyGhost hunt;
	Game game;
	State state;
	
	public CopyGhost() {
		stateMachine = new StateMachineGhost<CopyGhost>(this);
	}
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		scatter = ScatterCopyGhost.getInstance(game, ghostIndex);
		hunt = HuntCopyGhost.getInstance(game, ghostIndex);
		
		if (stateMachine.getCurrentState() == null)
			stateMachine.setCurrentState(scatter);
		
		List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex);
		if (stateMachine.getCurrentState() == hunt) {
			
			Move checkMove;
			Move bestMove = null;
			stateMachine.update();
			checkMove = hunt.getMove();
			
			for(Move m : legalMoves) { //verifica se o movimento do estado � possivel
				if (m == checkMove) {
					bestMove = m;
				}
			}
			
			if (bestMove == null) { //i.e. o movimento do estado nao � possivel, portanto ele vai retornar o primeiro movimento possivel
				System.out.println("nao deu pra copiar o pacman");
				return legalMoves.get(0);
			} else {
				System.out.println("Copy ghost: "+ bestMove);
				return bestMove;
			}
		
		} else {
			stateMachine.update();
			
			State s = game.getCurrentState();
			Location target = scatter.getTarget();
			Location mine = s.getGhostLocations().get(ghostIndex);
			double minDist = Double.POSITIVE_INFINITY;
			Move best = null;
			
			for(Move m : legalMoves) {
				Location next = Game.getNextLocation(mine, m);
				double distance = Location.euclideanDistance(next, target);
				
				if (distance < minDist) {
					minDist = distance;
					best = m;
				}
			}
			if (best == null) throw new RuntimeException("Legal moves for ghost "+ghostIndex+": " + legalMoves);
			return best;		
		}
	
	}
	
	public StateMachineGhost<CopyGhost> getStateMachine() {
		return stateMachine;
	}

}
