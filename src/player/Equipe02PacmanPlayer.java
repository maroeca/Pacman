package player;

import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.Move;
import pacman.PacManPlayer;

public class Equipe02PacmanPlayer implements PacManPlayer{

	int count = 0;
	public Move lastmove = null;
	@Override
	public Move chooseMove(Game game) {
		//Obtem a lista de movimentos validos e retorna o primeiro movimento valido
		List<Move>legalMoves = game.getLegalPacManMoves();
		Random rand = new Random();
		count += game.getMoveTime() / 100;
		int r = rand.nextInt(legalMoves.size());
		if(count >= 10) {
			count = 0;
			lastmove = legalMoves.get(r);
			return legalMoves.get(r);
		}
		return lastmove;
		
	}

}
