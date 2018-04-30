package ghosts;
import ghosts.*;
import pacman.*;

public class HuntBlindGhost implements GhostState<BlindGhost> {

	Game game;
	State state;
	
	int ghostIndex;
	int count = 0;
	Location target;
	static Location pacman;
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(BlindGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
