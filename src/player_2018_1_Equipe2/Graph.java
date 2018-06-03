package player_2018_1_Equipe2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.Move;
import pacman.State;

//Classe que recebe os nodes(Moves) possiveis do pacman para 
//o uso na busca de profundidade limitada

public class Graph {
	private Node startNode;
	private ArrayList<Node> nodes;

	public int limitDepth = 4;


	Graph (Game game) {
		startNode = new Node(game.getCurrentState(), Move.NONE, 0);

		nodes = new ArrayList<Node>();
		nodes.add(startNode);
		getAllPossibleStates(startNode, 1);
	}

	private void getAllPossibleStates(Node node, int depth) {
		if (depth >= limitDepth) { //Interrompe a funcao recursiva caso exceda o limite
			return;
		}

		List<Move> moves = Game.getLegalPacManMoves(node.getState());	//Pega todos os movimentos possiveis do pacman no estado

		if (moves.isEmpty()) {
			return;
		}

		for(Move m: moves) {
			//System.out.println(Game.projectPacManLocation(node.getState(), m, 1));
			State s = Game.projectPacManLocation(node.getState(), m, 1).get(0);	//Pega o primeiro estado do movimento realizado
			addNode(s, node, m, depth);	//cria um nó novo e adiciona como filho do nó recebido como argumento
		}

		for(Node child: node.getChildren()) {
			getAllPossibleStates(child, depth + 1);	//recursividade, vai repetir os passos acima com todos os filhos
		}
	}

	private void addNode(State newState, Node parent, Move move, int depth) {
		Node newNode = new Node(newState, move, depth);
		parent.addChild(newNode);

		if (parent.getDepth() != 0)
			newNode.addParent(parent); //apenas pra facilitar a busca do nó pai, pois o nó inicial (0) é o estado atual do jogo de qualquer maneira. Entao nao sera utilizado mesmo

		nodes.add(newNode);
	}

	public ArrayList<Node> getNodesOnDepth(int depth) { //busca todos os nos na profundidade desejada
		try {
			ArrayList<Node> depthNodes = new ArrayList<Node>();

			for(Node n: this.nodes) {
				if (n.getDepth() == depth) {
					depthNodes.add(n);
				}
			}

			return depthNodes;

		} catch (Exception e) {
			if (depth > limitDepth) {
				System.err.println("Depth too deep");
			}
			return null;
		}
	}

	public Node getParentNode(Node childNode) {
		Node parent = childNode.getParentNode();
		Node aux = parent;

		if(parent == null)
			return childNode;

		while(parent != null) {
			aux = parent;
			parent = aux.getParentNode();
		}

		return aux;
	}


//	//Lista com os nao pais
//	List<Node> mainNodes = new ArrayList<Node>();
//
//	//adiciona os primeiros movimentos quando criado
//	Graph(List<Node> pacManMoves){
//		mainNodes = pacManMoves;
//	}
//
//
//	public void addNode(Move mainNode, List<Move> nodes) {
//
//	}
}
