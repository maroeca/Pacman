package ghosts;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.Move;

public class FollowGhost extends GhostPlayer{

	Location target;
	
	public FollowGhost() {
		stateMachine = new StateMachineGhost<FollowGhost>(this);
	}
	@Override
	public Move chooseMove(Game game, int ghostIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
