package ghosts;
import java.util.regex.Pattern;

import ghosts.*;
import pacman.*;

public class HuntBlindGhost implements GhostState<BlindGhost> {

	Game game;
	State state;
	
	int ghostIndex;
	int count = 0;
	Location target;
	static Location pacman;
	
	GhostPlayer ghostReceiver;
	
	double minDistance = Double.POSITIVE_INFINITY;
	
	//sinlgeton
	private static HuntBlindGhost instance = null;
	
	private HuntBlindGhost(Game game, int ghostIndex) {
		this.game = game;
		this.ghostIndex = ghostIndex;
	}
	
	public static HuntBlindGhost getInstance(Game game, int ghostIndex) {
		if (instance == null)
			instance = new HuntBlindGhost(game, ghostIndex);
		return instance;
	}
	
	@Override
	public void Enter(BlindGhost npc) {
		State s = game.getCurrentState();
		pacman = s.getPacManLocation();
		
		System.out.println("Target encontrado: " + pacman.getX() + ", " + pacman.getY());
	
		String[] names; //array de string que recebe o split
		for (int i = 0; i < game.getGhostPlayers().size(); i++) {
			names = game.getGhostPlayers().get(i).getName().split(Pattern.quote(".")); //divide o game em dois onde tem ponto
			if(names[1].compareTo("MirrorGhost") == 0) { //verifica se a segunda parte do nome é igual ao nome do ghost
				ghostReceiver = game.getGhostPlayers().get(i); //se for o ghostReceiver recebe o ghost
			}
		}
		if(ghostReceiver != null)
			MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "BlindGhost Hunt", null); //envia a mensagem
	}

	@Override
	public void Execute(BlindGhost npc) {
		State s = game.getCurrentState();
		target = pacman;
		Location my = s.getGhostLocations().get(ghostIndex);
		minDistance = Location.euclideanDistance(my, pacman);
		
		for(Location l : s.getDotLocations()) {
			double distance = Location.euclideanDistance(l, pacman);
			if (distance < minDistance) {
				target = l;
			
			} else {
				target = pacman;
			}
		}
		
		count++;
		if (count > 50) {
			count = 0;
			npc.getStateMachine().changeState(ScatterBlindGhost.getInstance(game, ghostIndex));
		}
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public void Exit(BlindGhost npc) {
		if(ghostReceiver != null)
			MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "BlindGhost Scatter", null); //envia a mensagem
		
	}

	@Override
	public boolean onMessage(BlindGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
