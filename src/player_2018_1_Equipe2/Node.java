package player_2018_1_Equipe2;

import java.util.ArrayList;
import java.util.List;

import pacman.Move;

//Cada Node é um Move

public class Node {
	Move mainNode;
	List<Move>children = new ArrayList<Move>();
	
	Node(Move mainNode){
		this.mainNode = mainNode;
	}
	
	public void addChild(List<Move> pacManMoves) {
		for (Move move : pacManMoves) {
			children.add(move);
		}
		//talvez assim seja melhor
		//children = pacManMoves;
	}
}
