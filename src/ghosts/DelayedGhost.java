package ghosts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.Move;
import pacman.State;

public class DelayedGhost extends GhostPlayer{
	
	ScatterDelayedGhost scatter;
	HuntDelayedGhost hunt;
	
	Location target = new Location(0,0);
	
	Random rand = new Random();
	
	Move bestMove = null; //melhor escolha
	
	public DelayedGhost() {
		stateMachine = new StateMachineGhost<DelayedGhost>(this);
		this.setName("DelayedGhost");
	}
	
	public StateMachineGhost<DelayedGhost> getStateMachine(){
		return stateMachine;
		
	}	
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		scatter = ScatterDelayedGhost.getInstance(game); //pra facilitar a escrita
		hunt = HuntDelayedGhost.getInstance(game); //idem
		
		if(stateMachine.getCurrentState() == null) { //se nenhum estado estiver setado, seta o scatter
			stateMachine.setCurrentState(scatter);
		}		
		
		State state = game.getCurrentState(); //Estado atual do jogo
		Location target = null; //local alvo
		List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex); //movimentos possiveis
		
	    double minDistance = Double.POSITIVE_INFINITY; //variavel de comparação
	    Location myLoc = state.getGhostLocations().get(ghostIndex); //minha posição
	    
	    //Verifica em qual estado tá para definir o target
		if(stateMachine.getCurrentState() == hunt) {
			stateMachine.update();
			target = hunt.getTarget();
			//System.out.println(target);
		}else {
			stateMachine.update();
			target = scatter.getTarget();
		}
		
		//foreach pelos movimentos possíveis
		for (Move m : legalMoves) {
		      Location nextLoc = Game.getNextLocation(myLoc, m); //escolhe o proximo local a partir da localização e movimentos possiveis
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
