package player_2018_1_Equipe2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.Location;
import pacman.Move;
import pacman.State;
import player.DFSPacManPlayer;

public class BFSPacManPlayer extends DFSPacManPlayer{

	//private Move lastMove = null; //vai ser usado para evitar o retorno
	private Move bestMove = null;
	private Random random = new Random();
	private int lives;

	private ArrayList<Move> lastMoves = new ArrayList<Move>();
	private Node lastNode;
	private double lastValue;


	@Override
	public Move chooseMove(Game game) {
		lives = game.getLives();
		this.bestMove = breadthFirstSearch(game.getCurrentState(), game);
		return bestMove;
	}

	//HEURISTICAS

	public double evaluateState(State state, State oldState) {
		double score = 0.0;
		int riscMultiplier;

		//Verifica se o proximo estado perderia o jogo
		if (Game.isLosing(state))
			return -1000.0;
		//Verifica se no proximo estado ganharia o jogo
		if (Game.isWinning(state))
			return 0.0;


		if(lives > 1){
			riscMultiplier = 2;
		}else{
			riscMultiplier = 1;
		}

		//loc do pacman no proximo state
		Location pacManLoc = state.getPacManLocation();
		Location closestDot = getClosest(pacManLoc, state.getDotLocations());

		//Distancia media entre o ponto mais perto do pacman e os outros pontos
		score -= getMedDistance(closestDot, state.getDotLocations());

		//Distancia do ponto mais proximo do pacman para o fantasma mais perto do ponto
		score += Location.manhattanDistanceToClosest(closestDot, state.getGhostLocations());

		Location closestGhost = getClosest(pacManLoc, state.getGhostLocations());

		//preferencia para pegar pontos
		if(state.getDotLocations().size() < oldState.getDotLocations().size()){
			score += 1000 * riscMultiplier;
		}

		//distancia media do fantasma mais perto do pacman para os outros fantasmas
		score -= getMedDistance(closestGhost, state.getGhostLocations());
		//System.out.println("medDistance " + getMedDistance(closestGhost, state.getGhostLocations()));
		//System.out.println("score: " + score);

		//Distancia do pacman para o fantasma mais perto
		score += Location.manhattanDistanceToClosest(pacManLoc, state.getGhostLocations()) * 1.23;

		//distancia do pacman para o ponto mais perto
		score -= Location.manhattanDistance(pacManLoc, closestDot);

		//Quantidade de pontos em jogo
		score -= state.getDotLocations().size();

		score -= Location.euclideanDistance(pacManLoc, closestDot) - Location.euclideanDistance(pacManLoc, closestGhost);

		//Direçao dos fantasmas em relaçao ao pacman
		try {
			score += getMedDirectionGhostsToPacman(pacManLoc, state.getPreviousGhostMoves(), state.getGhostLocations());
		} catch(Exception e) {
			System.err.println("Histórico vazio dos movimentos do ghost. Primeira movimenentacao necessaria.");
		}


		return score;

	}

	private double getMedDirectionGhostsToPacman(Location source, List<Move> moves, List <Location> targets) {
		double localScore = 0.0;

		for(int i = 0; i < moves.size(); i++) {
			if (isMovingTorwardsPacmanHorizontally(targets.get(i), source, moves.get(i))) {
				//TODO: balancear esse valor
				//update: acho que ta balanceado
				localScore -= 1.5;
			} else {
				localScore += 1.5;
			}

		}

		return localScore;
	}

	private boolean isMovingTorwardsPacmanHorizontally(Location ghost, Location pacman, Move ghostMove) {
		if (ghost.getX() < pacman.getX()) { //está a esquerda do pacman
			if (ghostMove == Move.RIGHT)
				return true;
			else
				return isMovingTorwardsPacmanVertically(ghost, pacman, ghostMove);

		} else if (ghost.getX() > pacman.getX()) { //está à direita do pacman
			if (ghostMove == Move.LEFT)
				return true;
			else
				return isMovingTorwardsPacmanVertically(ghost, pacman, ghostMove);

		} else {
			return isMovingTorwardsPacmanVertically(ghost, pacman, ghostMove);

		}
	}

	private boolean isMovingTorwardsPacmanVertically(Location ghost, Location pacman, Move ghostMove) {
		if (ghost.getY() < pacman.getY()) { //está acima do pacman
			if (ghostMove == Move.DOWN)
				return true;

		} else if (ghost.getY() > pacman.getY()) { //está abaixo do pacman
			if (ghostMove == Move.UP)
				return true;
		}

		return false;
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
		Node bestNode = new Node(state, Move.NONE,0);
		Double bestValue = Double.NEGATIVE_INFINITY;

		Graph graph = new Graph(game); //Cria um grafo com o estado atual do jogo

		for (int d = 1; d < graph.limitDepth; d++) { //Vai pegar um array com todos os nós de uma profundidade (0 a 2)
			ArrayList<Node> nodes = graph.getNodesOnDepth(d); //Array que vai receber os nós da profundidade d

			for (Node n: nodes) {	//Percorre todos os nós da profundidade
				double aux = evaluateState(n.getState(), graph.getStartNode().getState()); //e aplica heuritica no state

				if (aux > bestValue && isNotOnLoop(graph.getParentNode(n).getMove())) { //se o estado tiver uma heuristica melhor que o armazenado, troca
					bestValue = aux;
					bestNode = n;
				}
			}
		}


		Node nextNode = graph.getParentNode(bestNode); //pega o node pai do bestNode, pois é o proximo estado do estado atual
		lastNode = nextNode;
		Move move = nextNode.getMove(); //pega o movimento do nextNode
		System.out.println(bestValue + " " + move);
		addLastMovement(move);
		return move; //retorno o move
	}


	private void addLastMovement(Move newMove) {
		if (lastMoves.size() >= 2) { //remove primeiro elemento
			lastMoves.remove(0);
		}
		lastMoves.add(newMove);
	}

	private boolean isNotOnLoop(Move move) {

		if (lastMoves.size() < 2) {
			return true;
		} else {
			if ((lastMoves.get(0) == move || lastMoves.get(0) == move.getOpposite()) && (lastMoves.get(1) == move.getOpposite() || lastMoves.get(1) == move.getOpposite()))
				return false;

			return true;
		}

	}
}
