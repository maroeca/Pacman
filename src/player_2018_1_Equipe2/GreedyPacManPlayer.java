package player_2018_1_Equipe2;

import java.util.Collection;

import java.util.List;

import pacman.Game;
import pacman.Location;
import pacman.Move;
import pacman.PacManPlayer;
import pacman.State;
import pacman.StateEvaluator;
import player.DFSPacManPlayer;
import util.Counter;

public class GreedyPacManPlayer extends DFSPacManPlayer {

	private Move lastMove = null; //vai ser usado para evitar o retorno
	private Move bestMove = null;
	
	
	@Override
	public Move chooseMove(Game game) {
		//estado atual
		State s = game.getCurrentState(); 
		
		//Movimentos possiveis do pacman
		List<Move> legalMoves = game.getLegalPacManMoves();
		
		double lastScore = Double.NEGATIVE_INFINITY;
		
		//Counter retorna uma especie de hashtable, atribui um double a um move(nesse caso)
		//Counter<Move> scores = new Counter<Move>();
		
		//verifica as ações possiveis(nodes)
		for(Move move: legalMoves) {
			//pega o proximo estado a partir do movimento atual
			State nextState = Game.getNextState(s, move);			
			
			//calcula o score a aplicando as heuristicas
			double stateScore = evaluateState(nextState);			
			
			//verificação pra n voltar o movimento(ta igual do simplepacman, melhor mudar)
			double cantTurnBack = (lastMove == move.getOpposite() ? -10.0 : 0.0);
			System.out.println(stateScore + cantTurnBack + " "+ move);

			//retorna o movimento com melhor score
			if((stateScore + cantTurnBack) > lastScore) {
				lastScore = stateScore + cantTurnBack;
				bestMove = move;
			}
			
		}	
		lastMove = bestMove;
		return bestMove;
		
	}
	
	//HEURISTICAS
	@Override
	public double evaluateState(State state) {
		//Verifica se o proximo estado vai fazer o pacman perder
		if (Game.isLosing(state))
		      return -1000.0; 
		//Verifica se no proximo estado ganharia o jogo
		if (Game.isWinning(state))
		      return 10.0; 
		
		double score = 0.0;
		
		//loc do pacman no proximo state
		Location pacManLoc = state.getPacManLocation();
		Location closestDot = getClosest(pacManLoc, state.getDotLocations());
		
		//Distancia do ponto mais proximo do pacman para o fantasma mais perto do ponto
		score += Location.manhattanDistanceToClosest(closestDot, state.getGhostLocations());
		
		Location closestGhost = getClosest(pacManLoc, state.getGhostLocations());

		//distancia media do fantasma mais perto do pacman para os outros fantasmas
		score -= getMedDistance(closestGhost, state.getGhostLocations());
		
		//Distancia do pacman para o fantasma mais perto
		score += Location.manhattanDistanceToClosest(pacManLoc, state.getGhostLocations()) *1.2;
		
		//distancia do pacman para o ponto mais perto
		score -= Location.manhattanDistance(pacManLoc, closestDot);
		
		//Quantidade de pontos em jogo
		score -= state.getDotLocations().size();
		
		return score;
		
	}
	
	//calcula a distancia media de um local para uma lista de locais
	private double getMedDistance(Location source, Collection<Location> targets) {
		double totalDistance = 0.0;
		for(Location loc : targets) {
			totalDistance += Location.manhattanDistance(source, loc);
		}
		
		return totalDistance/targets.size();
	}
	
	//calcula a location mais proxima do pacman
	Location getClosest(Location pacman, Collection<Location> targets) {
		//Location pacman = s.getPacManLocation();
		Location closest = null;
		double minDistance = Double.POSITIVE_INFINITY;
		for(Location c : targets) {
			if(minDistance > Location.manhattanDistance(pacman, c)) {
				closest = c;
			}
		}
		
		return closest;
	}
	
}
