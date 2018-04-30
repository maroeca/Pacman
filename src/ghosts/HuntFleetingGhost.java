package ghosts;
import ghosts.*;
import pacman.*;

public class HuntFleetingGhost implements GhostState<FleetingGhost> {
	
	Game game;
	Location target;
	
	int count = 0;
	double minDist = Double.POSITIVE_INFINITY;
	
	//singelton
	private static HuntFleetingGhost instance = null;
	
	private HuntFleetingGhost(Game game) {}
	
	public static HuntFleetingGhost getInstace(Game game) {
		if (instance == null)
			instance = new HuntFleetingGhost(game);
		return instance;
	}
	
	@Override
	public void Enter(FleetingGhost npc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Execute(FleetingGhost npc) {
		State s = game.getCurrentState();
		
		target = s.getPacManLocation();
		count++;
		
		if (count > 50) {
			count = 0;
			npc.getStateMachine().changeState(ScatterFleetingGhost.getInstance(game));
		}
	}
	
	public Location getTarget() {
		return target;
	}

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
