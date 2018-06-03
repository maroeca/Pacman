package player_2018_1_Equipe2;

import java.util.ArrayList;
import java.util.List;
import pacman.*;
import pacman.State;

//Cada Node e um Move

public class Node {
	private Node parentNode;
	private int depth;
	private State state;
	private List<Node> children = new ArrayList<Node>();
	
	Node(State state, int depth){
		this.state = state;
		this.depth = depth;
	}

	public void addChild(Node c) {
		children.add(c);
		if (this.depth != 0)
			c.addParent(this);
	}

	public List<Node> getChildren()	{
		return children;
	}

	private void addParent(Node p) {
		this.parentNode = p;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public State getState() {
		return state;
	}

	public int getDepth() {
		return depth;
	}

//	public void addChild(List<Move> pacManMoves) {
//		for (Move move : pacManMoves) {
//			children.add(move);
//		}
//		//talvez assim seja melhor
//		//children = pacManMoves;
//	}
}
