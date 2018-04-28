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
	
	ScatterDelayeGhost scatter;
	HuntDelayedGhost hunt;
	/*Game game;
	State state;*/
	
	Location target = new Location(0,0);
	
	private Move lastMove = null;
	
	Random rand = new Random();
	
	int count = 0;
	
	public DelayedGhost() {
		stateMachine = new StateMachineGhost<DelayedGhost>(this);
		this.setName("DelayedGhost");
	}
	
	public StateMachineGhost<DelayedGhost> getStateMachine(){
		return stateMachine;
		
	}	
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		scatter = ScatterDelayeGhost.getInstance(game); //pra facilitar a escrita
		hunt = HuntDelayedGhost.getInstance(game); //idem
		
		if(stateMachine.getCurrentState() == null) { //se nenhum estado estiver setado, seta o scatter
			stateMachine.setCurrentState(scatter);
		}		
		
		State state = game.getCurrentState(); //Estado atual do jogo
		Location target = null; //local alvo
		List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex); //movimentos possiveis
		Move bestMove = null; //melhor escolha
	    double minDistance = Double.POSITIVE_INFINITY; //variavel de compara��o
	    Location myLoc = state.getGhostLocations().get(ghostIndex); //minha posi��o
	    
	    //Verifica em qual estado t� para definir o target
		if(stateMachine.getCurrentState() == hunt) {
			stateMachine.update();
			target = hunt.getTarget();
			//System.out.println(target);
		}else {
			stateMachine.update();
			target = scatter.getTarget();
		}
		
		//foreach pelos movimentos poss�veis
		for (Move m : legalMoves) {
		      Location nextLoc = Game.getNextLocation(myLoc, m); //escolhe o proximo local a partir da localiza��o e movimentos possiveis
		      double distance = Location.euclideanDistance(nextLoc, target); //calcula a distancia entre o local acima e o alvo
		      if (distance < minDistance) { //se for a menor, seta o movimento como melhor
		        minDistance = distance;
		        bestMove = m;
		      }
		    }    
		    if (bestMove==null) throw new RuntimeException("Legal moves for ghost "+ghostIndex+": " + legalMoves);
		    lastMove = bestMove; //salva o ultimo movimento
		    return bestMove;
	}
	
	/*//Seta o alvo como o ponto mais distante do pacman
	public void setScatterTarget(Location target) {
		
		this.target = target;
		
	}
	
	public Location getTarget() {
		return target;
	}
	
	//Seta o alvo como a posi��o do pacman no estado anterior
	public Location setHuntTarget(Game game, State s) {
		Location target;
		State teste = s.getParent();
		List<State> history = s.getHistory(); //pega os estados anteriores
		Location pacmanLoc = history.get(history.size() - 2).getPacManLocation(); //pega a posi��o do pacman no penultimo estado
		target = pacmanLoc;
		
		return target;
		
	}*/

}
