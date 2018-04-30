package ghosts;

import ghosts.*;
import pacman.*;

public class ScatterBlindGhost implements GhostState<BlindGhost> {

	Game game;
	State state;
	int ghostIndex;
	int count = 0;
	Location target;
	
	//singleton
	private static ScatterBlindGhost instance = null;
	
	private ScatterBlindGhost(Game game, int index) {
		this.game = game;
		this.ghostIndex = index;
	}
	
	public static ScatterBlindGhost getInstance(Game game, int index) {
		if (instance == null) 
			instance = new ScatterBlindGhost(game, index);
		
		return instance;
	}
	@Override
	public void Enter(BlindGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Execute(BlindGhost npc) {
		state = game.getCurrentState();
		target = new Location(0,0); //no modo scatter vai sempre pro canto esquerdo superior
		
		count++;
		if (count > 30) {
			count = 0;
			npc.getStateMachine().setCurrentState(HuntBlindGhost.getInstance(game, ghostIndex));
		}
		
	}

	@Override
	public void Exit(BlindGhost npc) {
		// TODO Auto-generated method stub
		
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public boolean onMessage(BlindGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}

