package player_2018_1_Equipe2;

import java.util.Collection;

import java.util.List;
import java.util.Random;

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
	private Random random = new Random();


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
			//System.out.println(stateScore + cantTurnBack + " "+ move);

			//retorna o movimento com melhor score
			if((stateScore + cantTurnBack) > lastScore) {
				lastScore = stateScore + cantTurnBack;
				//System.out.println(lastScore);
				bestMove = move;
			}else //caso o score seja igual randomiza o move entre os possiveis
				if((stateScore + cantTurnBack) == lastScore) {
					bestMove = lastMove; //legalMoves.get(random.nextInt(legalMoves.size()));
					//System.out.println("Randomizou");
				}

		}
		lastMove = bestMove;
		return bestMove;

	}

	/*HEURISTICAS
	 * 1- Estado final é bom (win) ou ruim (game over)
	 * 2- Distancia do pacman para os fantasmas
	 * 3- Quantidades de pontos em jogo
	 * 4- Distancia do ponto mais perto do pacman em relaçao aos fantasmas (Maycow)
	 * 5- Distancia media entre os fantasmas (Mylla)
	 * 6- Distanca do pacman para o ponto mais próximo
	 * 7- Distancia média entre o ponto mais perto do pacman em realção aos outros pontos
	 * 8-
	 */
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
		//System.out.println("medDistance " + getMedDistance(closestGhost, state.getGhostLocations()));
		//System.out.println("score: " + score);

		//Distancia do pacman para o fantasma mais perto
		score += Location.manhattanDistanceToClosest(pacManLoc, state.getGhostLocations()) *1.5;

		//distancia do pacman para o ponto mais perto
		score -= Location.manhattanDistance(pacManLoc, closestDot);

		//Quantidade de pontos em jogo
		score -= state.getDotLocations().size();

		try {
			//Direçao dos fantasmas em relaçao ao pacman
			score += getMedDirectionGhostsToPacman(pacManLoc, state.getPreviousGhostMoves(), state.getGhostLocations());
		} catch(Exception e) {
			System.err.println("Histórico vazio dos movimentos do ghost. Primeira movimenentacao necessaria.");
		}


		return score;

	}

	private double getMedDirectionGhostsToPacman(Location source, List<Move> moves, List <Location> targets) {
		double localScore = 0.0;

		for(int i = 0; i < moves.size(); i++) {
			if (isMovingTorwardsPacmanHorizontaly(targets.get(i), source, moves.get(i))) {
				//TODO: balancear esse valor
				//update: acho que ta balanceado
				localScore -= 2.0;
			} else {
				localScore += 2.0;
			}

		}

		return localScore;
	}

	private boolean isMovingTorwardsPacmanHorizontaly(Location ghost, Location pacman, Move ghostMove) {
		if (ghost.getX() < pacman.getX()) { //está a esquerda do pacman
			if (ghostMove == Move.RIGHT)
				return true;
			else
				return isMovingTorwardsPacmanVerticaly(ghost, pacman, ghostMove);

		} else if (ghost.getX() > pacman.getX()) { //está à direita do pacman
			if (ghostMove == Move.LEFT)
				return true;
			else
				return isMovingTorwardsPacmanVerticaly(ghost, pacman, ghostMove);

		} else {
			return isMovingTorwardsPacmanVerticaly(ghost, pacman, ghostMove);

		}
	}

	private boolean isMovingTorwardsPacmanVerticaly(Location ghost, Location pacman, Move ghostMove) {
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

}
