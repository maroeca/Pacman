package ghosts;

import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.Move;
import pacman.State;

public class RightGhost extends GhostPlayer {

	ScatterRightGhost scatter;
	HuntRightGhost hunt;
	
	public RightGhost(){
		stateMachine = new StateMachineGhost<RightGhost>(this);
	}
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		State state = game.getCurrentState();
		scatter = ScatterRightGhost.getInstance(game, ghostIndex);
		hunt = HuntRightGhost.getInstance(game, ghostIndex);
		if(stateMachine.getCurrentState() == null) {
			stateMachine.setCurrentState(scatter);
		}
		Random rand = new Random();
		List<Move>legalMoves = game.getLegalGhostMoves(ghostIndex);
		Move bestMove = legalMoves.get(0);
		
		
		
		if(stateMachine.getCurrentState() == scatter) {
			stateMachine.update();
			bestMove = ScatterRightGhost.getInstance(game, ghostIndex).getMove();
		}else {
			stateMachine.update();
			Location target = hunt.getTarget();
			double minDistance = Double.POSITIVE_INFINITY; 
		    Location myLoc = state.getGhostLocations().get(ghostIndex); //minha posição 
		    for (Move m : legalMoves) {
			      Location nextLoc = Game.getNextLocation(myLoc, m); //escolhe o proximo local a partir da localização e movimentos possiveis
			      double distance = Location.euclideanDistance(nextLoc, target); //calcula a distancia entre o local acima e o alvo
			      if (distance < minDistance) { //se for a menor, seta o movimento como melhor
			        minDistance = distance;
			        bestMove = m;
			      }
			    }    
		}
		
		return bestMove;
	}
	
	public StateMachineGhost<RightGhost> getStateMachine(){
		return stateMachine;
		
	}	
	/*private Location Scatter(Game game, State s) {
		Location target;
		
		return null;
		
	}*/

}
