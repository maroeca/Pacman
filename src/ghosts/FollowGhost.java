package ghosts;

import java.awt.Color;
import java.util.List;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.Move;
import pacman.State;

public class FollowGhost extends GhostPlayer{

	Location target;
	
	HuntFollowGhost hunt;
	ScatterFollowGhost scatter;
	
	public FollowGhost() {
		stateMachine = new StateMachineGhost<FollowGhost>(this);
		this.setName("FollowGhost");
	}
	
	public StateMachineGhost<FollowGhost> getStateMachine(){
		return stateMachine;
		
	}	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		scatter = ScatterFollowGhost.getInstance(game, ghostIndex);
		hunt = HuntFollowGhost.getInstance(game, ghostIndex);		
		
		if(stateMachine.getCurrentState() == null) {
			stateMachine.setCurrentState(scatter);
		}
		
		if(stateMachine.getCurrentState() == hunt) {
			stateMachine.update();
			target = hunt.getTarget();
		}else {
			stateMachine.update();
			target = scatter.getTarget();
		}
		
		State state = game.getCurrentState(); //Estado atual do jogo
		List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex); //movimentos possiveis
		Move bestMove = null; //melhor escolha
	    double minDistance = Double.POSITIVE_INFINITY; //variavel de comparacao
	    Location myLoc = state.getGhostLocations().get(ghostIndex); //minha posicao
	    
	  //foreach pelos movimentos possiveis
	  	for (Move m : legalMoves) {
	  	 Location nextLoc = Game.getNextLocation(myLoc, m); //escolhe o proximo local a partir da localizacao e movimentos possiveis
	  	 double distance = Location.euclideanDistance(nextLoc, target); //calcula a distancia entre o local acima e o alvo
	  	 if (distance < minDistance) { //se for a menor, seta o movimento como melhor
	  		 minDistance = distance;
	  	 	 bestMove = m;
	  		  }
	  	}    
	  	 if (bestMove==null) throw new RuntimeException("Legal moves for ghost "+ghostIndex+": " + legalMoves);
		this.setLastMove(bestMove);
		return bestMove;
	}
	
}
