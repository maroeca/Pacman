package player_2018_1_Equipe2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import pacman.Move;

//Classe que recebe os nodes(Moves) possiveis do pacman para 
//o uso na busca de profundidade limitada

public class Graph {
	//Lista com os nao pais
	List<Node> mainNodes = new ArrayList<Node>();
	
	//adiciona os primeiros movimentos quando criado
	Graph(List<Node> pacManMoves){
		mainNodes = pacManMoves;
	}
	
	
	public void addNode(Move mainNode, List<Move> nodes) {
		
	}
}
