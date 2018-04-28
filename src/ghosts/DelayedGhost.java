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
		
	}
	
	public StateMachineGhost<DelayedGhost> getStateMachine(){
		return stateMachine;
		
	}	
	
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		//this.game = game;
		scatter = ScatterDelayeGhost.getInstance(game);
		hunt = HuntDelayedGhost.getInstance(game);
		//estado atual
		//System.out.println(stateMachine);
		if(stateMachine.getCurrentState() == null) {
			stateMachine.setCurrentState(scatter);
		}
		
		
		
		State state = game.getCurrentState(); //Estado atual do jogo
		//this.state = state;
		Location target = null; //local alvo
		List<Move> legalMoves = game.getLegalGhostMoves(ghostIndex); //movimentos possiveis
		Move bestMove = null; //melhor escolha
	    double minDistance = Double.POSITIVE_INFINITY; 
	    Location myLoc = state.getGhostLocations().get(ghostIndex); //minha posição
	    
		
		count++; //contator pra controlar mudança dos estados
		if(stateMachine.getCurrentState() == hunt) {
			stateMachine.update();
			target = hunt.getTarget();
			//System.out.println(target);
		}else {
			stateMachine.update();
			target = scatter.getTarget();
		}
		/*if(count > 50) {
			stateMachine.get changeState(scatter);
			stateMachine.update();
			target = scatter.getTarget();
			if(count > 100) count = 0;
		}else {
			stateMachine.changeState(hunt);
			stateMachine.update();
			target = hunt.getTarget();
		}*/
		
		for (Move m : legalMoves) {
		      Location nextLoc = Game.getNextLocation(myLoc, m); //escolhe o proximo local a partir da localização e movimentos possiveis
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
	
	//Seta o alvo como o ponto mais distante do pacman
	public void setScatterTarget(Location target) {
		
		this.target = target;
		/*double minDistance = 8.0f;
		Location target = null;
		
		List<Location> allLoc =  new ArrayList<Location>(s.getDotLocations());
		for(Location loc: allLoc) {
			double distance = Location.euclideanDistance(loc, s.getPacManLocation());
			if(distance > minDistance) {
				target = loc;
				minDistance = distance;
				System.out.println(target + " "+ s.getPacManLocation());
			}else {
				target = allLoc.get(rand.nextInt(allLoc.size()));
			}
		}
		//target = allLoc.get(rand.nextInt(allLoc.size()));
		
		return target;*/
		
	}
	
	public Location getTarget() {
		return target;
	}
	
	//Seta o alvo como a posição do pacman no estado anterior
	public Location setHuntTarget(Game game, State s) {
		Location target;
		State teste = s.getParent();
		List<State> history = s.getHistory(); //pega os estados anteriores
		Location pacmanLoc = history.get(history.size() - 2).getPacManLocation(); //pega a posição do pacman no penultimo estado
		target = pacmanLoc;
		
		return target;
		
	}

}
