package ghosts;

import java.util.List;

import pacman.*;

public class HuntTryhardGhost implements GhostState<TryhardGhost> {

	Game game;
	Location target;
	
	int count = 0;
	
	//sinteltn
	private static HuntTryhardGhost instance = null;
	
	private HuntTryhardGhost(Game game) {
		this.game = game;
	}
	
	public static HuntTryhardGhost getInstance(Game game) {
		if (instance == null) {
			instance = new HuntTryhardGhost(game);
			
		}
		
		return instance;
	}
	
	@Override
	public void Enter(TryhardGhost npc) {
		System.out.println("--------------");
		System.out.println("TryhardGhost hunt");
		
	}

	@Override
	public void Execute(TryhardGhost npc) {
		State s = game.getCurrentState();
		target = s.getPacManLocation();
		
		count++;
		if (count > 50 && !npc.isTrying) {
			count = 0;
			npc.getStateMachine().setCurrentState(HuntTryhardGhost.getInstance(game));
		}
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public void Exit(TryhardGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(TryhardGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
