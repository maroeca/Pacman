package player_2018_1_Equipe2;

import java.util.ArrayList;
import java.util.List;

import pacman.Move;
import pacman.State;

//Cada Node e um Move

public class Node {
	Node parentNode;
	int depth;
	State state;
	List<Node> children = new ArrayList<Node>();
	
	Node(State state, int depth){
		this.state = state;
		this.depth = 0;
	}

	public void addChild(Node c) {
		children.add(c);
		c.addParent(this);
	}

	public List<Node> getChildrenStates()	{
		return children;
	}

	private void addParent(Node p) {
		this.parentNode = p;
	}

//	public void addChild(List<Move> pacManMoves) {
//		for (Move move : pacManMoves) {
//			children.add(move);
//		}
//		//talvez assim seja melhor
//		//children = pacManMoves;
//	}
}
