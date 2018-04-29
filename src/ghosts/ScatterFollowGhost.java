package ghosts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.State;

public class ScatterFollowGhost implements GhostState<FollowGhost>{

	Game game;
	int ghostIndex;
	Location target;
	
	int count = 0;
	int timeToChangeState = 50;
	
	double minDistance;
	
	//Begin Singleton
	   public static ScatterFollowGhost instance = null;

	   private ScatterFollowGhost(Game game, int index){
			this.game = game;
			ghostIndex = index;
		}

	    public static ScatterFollowGhost getInstance(Game game, int index){
	        if(instance == null){
	            instance = new ScatterFollowGhost(game, index);
	        }
	        return instance;
	    }
	    //End Singleton
	
	
	@Override
	public void Enter(FollowGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Execute(FollowGhost npc) {
		State s = game.getCurrentState();
		Random rand = new Random();
		
		minDistance = Double.NEGATIVE_INFINITY;//Location.euclideanDistance(myLoc, s.getPacManLocation());
		
		for (Location loc : s.getGhostLocations()) {
			double distance = Location.euclideanDistance(loc, s.getPacManLocation());
			if(distance > minDistance) {
				target = loc;
			}else {
				target = s.getGhostLocations().get(rand.nextInt(s.getGhostLocations().size()));
			}
		}
		
		count++;
		if(count > timeToChangeState) {
			count = 0;
			npc.getStateMachine().changeState(HuntFollowGhost.getInstance(game, ghostIndex));
		}
		
	}
	
	Location getTarget() {
		return target;
	}

	@Override
	public void Exit(FollowGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(FollowGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
