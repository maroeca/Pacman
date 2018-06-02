package player_2018_1_Equipe2;

import java.util.Collection;

import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.Location;
import pacman.Move;
import pacman.State;
import player.DFSPacManPlayer;

public class GreedyPacManPlayer extends DFSPacManPlayer {

	private Move lastMove = null; //vai ser usado para evitar o retorno
	private Move bestMove = null;
	private Random random = new Random();
    private int lives;
    Location target = null;

	@Override
	public Move chooseMove(Game game) {
		//estado atual
		State s = game.getCurrentState();
		lives = game.getLives();

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
			double stateScore = evaluateState(nextState, s);

			//verificação pra n voltar o movimento(ta igual do simplepacman, melhor mudar)
			double cantTurnBack = (lastMove == move.getOpposite() ? -100.0 : 0.0);
			//System.out.println(stateScore + cantTurnBack + " "+ move);

			//retorna o movimento com melhor score
			if((stateScore + cantTurnBack) > lastScore) {
				lastScore = stateScore + cantTurnBack;
				//System.out.println(lastScore);
				bestMove = move;
			}else //caso o score seja igual randomiza o move entre os possiveis
				if((stateScore + cantTurnBack) == lastScore) {
			        //Move opt = legalMoves.get(random.nextInt(legalMoves.size()));
					bestMove = lastMove; //(opt == move.getOpposite() ? lastMove : opt);
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
	 * 8- Direcao e localizacao dos ghosts em relacao ao pacman
	 * 9- Multiplicador de risco de acordo com quantas vidas tem o pacman
	 */

	public double evaluateState(State state, State oldState) {
		//Verifica se o proximo estado vai fazer o pacman perder
		if (Game.isLosing(state))
			return -1000.0;
		//Verifica se no proximo estado ganharia o jogo
		if (Game.isWinning(state))
			return 0.0;

		double score = 0.0;
		int riscMultiplier;

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
		    score += 500 * riscMultiplier;
		    System.out.println("Foi");
        }


		//distancia media do fantasma mais perto do pacman para os outros fantasmas
		score -= getMedDistance(closestGhost, state.getGhostLocations());
		//System.out.println("medDistance " + getMedDistance(closestGhost, state.getGhostLocations()));
		//System.out.println("score: " + score);

		//Distancia do pacman para o fantasma mais perto
		score += Location.manhattanDistanceToClosest(pacManLoc, state.getGhostLocations()) *1.3;

		//distancia do pacman para o ponto mais perto
		score -= Location.manhattanDistance(pacManLoc, closestDot);

		//Quantidade de pontos em jogo
		score -= state.getDotLocations().size();

		//Direçao dos fantasmas em relaçao ao pacman
		try {
			score += getMedDirectionGhostsToPacman(pacManLoc, state.getPreviousGhostMoves(), state.getGhostLocations());
		} catch(Exception e) {
			System.err.println("Histórico vazio dos movimentos do ghost. Primeira movimenentacao necessaria.");
		}

		
        //System.out.println("Score: " + score);
		return score;

	}

	private double getDistanceMedDotGhost(List<Location> locations, double medDistance, Location pacman, State state) {
		Location bestDot = locations.get(0);
		double bestDist = Double.POSITIVE_INFINITY; //menor diferença entre a distancia media do ponto, i.e. o ponto "mais na media"

		for (Location l: locations) {
			double dist = Location.manhattanDistance(l, pacman);
			if ((Math.abs(dist - medDistance)) < bestDist) {
				bestDist = dist;
				bestDot = l;
			}
		}

		Location closestGhost = getClosest(bestDot, state.getGhostLocations());

		return Location.manhattanDistance(bestDot, closestGhost);

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

}
