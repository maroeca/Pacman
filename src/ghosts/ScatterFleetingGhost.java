package ghosts;
import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

import ghosts.*;
import pacman.*;

public class ScatterFleetingGhost implements GhostState<FleetingGhost> {
	
	Game game;
	State state;
	int count = 0;
	Location target;
	

	//singleton
	private static ScatterFleetingGhost instance = null;
	
	private ScatterFleetingGhost(Game game) {
		this.game = game;
	}
	
	public static ScatterFleetingGhost getInstance(Game game) {
		if (instance == null)
			instance = new ScatterFleetingGhost(game);
			
		return instance;
	}
	
	public Location getTarget() {
		return target;
	}
	
	@Override
	public void Enter(FleetingGhost npc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Execute(FleetingGhost npc) {
		State s = game.getCurrentState(); //obtem estado atual pra ver posicoes
		Random r = new Random();
		
		switch(r.nextInt(4)) {
		
		case 0:
			target = new Location(0,0);
			break;
			
		case 1:
			target = new Location(25, 0);
			break;
			
		case 2:
			target = new Location(25, 28);
			break;
			
		case 3:
			target = new Location(0, 28);
			break;
		
		}
		
		count++;
		if (count > 50) {
			count = 0;
			npc.getStateMachine().changeState(HuntFleetingGhost.getInstace(game)); //muda o estado
		}

	}
	
//Achei que fosse necessario uma funcao pra pegar uma copia da location por ter mais informcoes
//	public Location getLocation(int x, int y, ArrayList<Location> all) {
//		for (Location l : all) {
//			if (l.getX() == x && l.getY() == y) {
//				return l;
//			}
//		}
//		return null;
//	}
	
	@Override
	public void Exit(FleetingGhost npc) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMessage(FleetingGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
