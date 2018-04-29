package ghosts;

import java.util.List;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.Move;
import pacman.State;

/*Mexer nos outro ghosts para compatibilidade*/
public class MirrorGhost extends GhostPlayer {

	Location target;
	ScatterMirrorGhost scatter;
	HuntMirrorGhost hunt;
	
	Move bestMove;
	Move checkMove;
	
	public MirrorGhost() {
		stateMachine = new StateMachineGhost<MirrorGhost>(this);
		this.setName("MirrorGhost");
	}
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		scatter = ScatterMirrorGhost.getInstance(game, ghostIndex);
		hunt = HuntMirrorGhost.getInstance(game, ghostIndex);
		
		if(stateMachine.getCurrentState() == null) {
			stateMachine.setCurrentState(scatter);
		}
		
		
		State state = game.getCurrentState(); //Estado atual do jogo
		List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex); //movimentos possiveis
		if(stateMachine.getCurrentState() == hunt) {
			stateMachine.update();
			
			switch(hunt.getMove()) {
			case RIGHT:
				checkMove = Move.LEFT;
				break;
			case LEFT:
				checkMove = Move.RIGHT;
				break;
			case UP:
				checkMove = Move.DOWN;
				break;
			case DOWN:
				checkMove = Move.UP;
				break;
			default:
				checkMove = Move.NONE;
				break;
			}
			for (Move move : legalMoves) {
				if(checkMove == move) {
					bestMove = move;
					//System.out.println("Original: "+hunt.getMove()+ " Mirror: "+ bestMove);
				}else {
					bestMove = legalMoves.get(0);
				}
			}
			
		}else {
			stateMachine.update();
			target = scatter.getTarget();
			
		    double minDistance = Double.POSITIVE_INFINITY; //variavel de comparação
		    Location myLoc = state.getGhostLocations().get(ghostIndex); //minha posição
		    
		  //foreach pelos movimentos possíveis
		  	for (Move m : legalMoves) {
		  	 Location nextLoc = Game.getNextLocation(myLoc, m); //escolhe o proximo local a partir da localização e movimentos possiveis
		  	 double distance = Location.euclideanDistance(nextLoc, target); //calcula a distancia entre o local acima e o alvo
		  	 if (distance < minDistance) { //se for a menor, seta o movimento como melhor
		  		 minDistance = distance;
		  	 	 bestMove = m;
		  		  }
		  	}
		}	
		
		this.setLastMove(bestMove);		
		return bestMove;
	}
	
	public StateMachineGhost<MirrorGhost> getStateMachine() {
		return stateMachine;
	}

}
