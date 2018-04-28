package ghosts;

import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.Move;
import pacman.State;

public class RightGhost extends GhostPlayer {

	ScatterRightGhost scatter; //pra economizar dedo
	HuntRightGhost hunt;
	
	public RightGhost(){
		stateMachine = new StateMachineGhost<RightGhost>(this);
		this.setName("RightGhost"); //não esqueça de setar os nomes para poder usar as mensagens
	}
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		State state = game.getCurrentState(); //estado atual
		scatter = ScatterRightGhost.getInstance(game, ghostIndex);
		hunt = HuntRightGhost.getInstance(game, ghostIndex);
		
		if(stateMachine.getCurrentState() == null) { //se nenhum estado estiver setado, seta o scatter
			stateMachine.setCurrentState(scatter);
		}
		
		//lista com os movimentos possíveis para o ghost
		List<Move>legalMoves = game.getLegalGhostMoves(ghostIndex);
		Move bestMove = legalMoves.get(0); //inicia o bestmove com o primeiro movimento valido apenas para não dar erro caso algo n de certo rs	
		
		//verificação dos targets
		if(stateMachine.getCurrentState() == scatter) {
			stateMachine.update(); //chama o update sempre, da erro sem ele 
			bestMove = ScatterRightGhost.getInstance(game, ghostIndex).getMove();
		}else {
			stateMachine.update();
			Location target = hunt.getTarget(); //recebe o target calculado no estado hunt
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

}
