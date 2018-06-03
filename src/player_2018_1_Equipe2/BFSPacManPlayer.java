package player_2018_1_Equipe2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import pacman.Game;
import pacman.Location;
import pacman.Move;
import pacman.State;
import player.DFSPacManPlayer;

public class BFSPacManPlayer extends DFSPacManPlayer{

	private Move lastMove = null; //vai ser usado para evitar o retorno
	private Move bestMove = null;
	private Random random = new Random();



	@Override
	public Move chooseMove(Game game) {
		
		this.bestMove = breadthFirstSearch(game.getCurrentState(), game);
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
		
		//Distancia media entre o ponto mais perto do pacman e os outros pontos
		score -= getMedDistance(closestDot, state.getDotLocations());
		
		//Distancia do ponto mais proximo do pacman para o fantasma mais perto do ponto
		score += Location.manhattanDistanceToClosest(closestDot, state.getGhostLocations());
		
		Location closestGhost = getClosest(pacManLoc, state.getGhostLocations());

		//distancia media do fantasma mais perto do pacman para os outros fantasmas
		score -= getMedDistance(closestGhost, state.getGhostLocations());
		
		//Distancia do pacman para o fantasma mais perto
		score += Location.manhattanDistanceToClosest(pacManLoc, state.getGhostLocations()) *1.5;
		
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
				minDistance = Location.manhattanDistance(pacman, c);
			}
		}
		
		return closest;
	}
	
	public Move breadthFirstSearch(State state, Game game) {
		Node bestNode = new Node(state,0);
		Double bestValue = Double.NEGATIVE_INFINITY;

		Graph graph = new Graph(game); //Cria um grafo com o estado atual do jogo

		for (int d = 0; d < graph.limitDepth; d++) { //Vai pegar um array com todos os nós de uma profundidade (0 a 2)
			ArrayList<Node> nodes = graph.getNodesOnDepth(d); //Array que vai receber os nós da profundidade d

			for (Node n: nodes) {	//Percorre todos os nós da profundidade
				double aux = evaluateState(n.getState()); //e aplica heuritica no state

				if (aux >= bestValue) { //se o estado tiver uma heuristica melhor que o armazenado, troca
					aux = bestValue;
					bestNode = n;
				}
			}
		}


		Node nextNode = graph.getParentNode(bestNode);
		System.out.println(nextNode);
		Move move = Game.getLegalPacManMoves(nextNode.getState()).get(0);

		return move;
	}
}
