package ghosts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.*;

public class ScatterCopyGhost implements GhostState<CopyGhost> {
	
	Game game;
	int ghostIndex;
	int count = 0;
	Location target;
	
	//singleton
	private static ScatterCopyGhost instance = null;
	private ScatterCopyGhost(Game game, int index) {
		this.game = game;
		this.ghostIndex = index;
	}
	
	public static ScatterCopyGhost getInstance(Game game, int index) {
		if (instance == null)
			instance = new ScatterCopyGhost(game, index);
		
		return instance;
	}
	
	@Override
	public void Enter(CopyGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Execute(CopyGhost npc) {
		target = new Location(25, 0); //no modo scatter volta para o canto dele
		
		count++;
		if (count > 50) {
			count = 0;
			npc.getStateMachine().changeState(HuntCopyGhost.getInstance(game, ghostIndex));
		}
	} 

	public Location getTarget() {
		return target;
	}
	
	@Override
	public void Exit(CopyGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(CopyGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
