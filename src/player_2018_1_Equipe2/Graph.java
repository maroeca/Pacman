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
	Node startNode;
//	Node currentNode;

	int limitDepth = 2;
	//ArrayList<Node> states;

	Graph (Game game) {
		startNode = new Node(game.getCurrentState(), 0);
		//currentNode = startNode;

		//states = new ArrayList<Node>();
		//states.add(startState);
		getAllPossibleStates(startNode, 0);
	}

	private void getAllPossibleStates(Node node, int depth) {
		if (depth >= limitDepth) { //Interrompe a funcao recursiva caso exceda o limite
			return;
		}


		List<Move> moves = Game.getLegalPacManMoves(node.state);	//Pega todos os movimentos possiveis do pacman no estado

		for(Move m: moves) {
			State s = Game.projectPacManLocation(node.state, m, 1).get(0);	//Pega o primeiro estado do movimento realizado
			addNode(s, node, depth);	//cria um nó novo e adiciona coomo filho do nó recebido como argumento
		}

		for(Node child: node.children) {
			getAllPossibleStates(child, depth++);	//recursividade, vai repetir os passos acima com todos os filhos
		}
	}

	private void addNode(State newState, Node parent, int depth) {
		Node newNode = new Node(newState, depth);
		parent.addChild(newNode);
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
