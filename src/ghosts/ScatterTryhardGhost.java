package ghosts;
import java.util.List;

import pacman.*;

public class ScatterTryhardGhost implements GhostState<TryhardGhost> {

	Game game;
	Location target;
	
	int count = 0;
	
	//sinigleton
	private static ScatterTryhardGhost instance = null;
	
	private ScatterTryhardGhost(Game game) {
		this.game = game;
	}
	
	public static ScatterTryhardGhost getInstance(Game game) {
		if (instance == null) {
			instance = new ScatterTryhardGhost(game);
		}
		return instance;
	}
	@Override
	public void Enter(TryhardGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Execute(TryhardGhost npc) {
State s = game.getCurrentState(); 
		
		List<Location> dots = s.getDotLocations().list();
		Location pacman = s.getPacManLocation();
		
		double minDist = Double.POSITIVE_INFINITY;
		
		for(Location d : dots) {
			double dist = Location.manhattanDistance(d, pacman);
			if (dist < minDist) {
				minDist = dist;
				target = d;
			}
		}
		
		count++;
		if (count > 50) {
			count = 0;
			npc.getStateMachine().setCurrentState(ScatterTryhardGhost.getInstance(game));
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
