package ghosts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.State;

public class ScatterDelayeGhost implements GhostState<DelayedGhost>{
	Game game;
	State state;
	int count = 0;
	Location target;
	
	//Begin Singleton
   public static ScatterDelayeGhost instance = null;

   private ScatterDelayeGhost(Game game){
		this.game = game;
	}

    public static ScatterDelayeGhost getInstance(Game game){
        if(instance == null){
            instance = new ScatterDelayeGhost(game);
        }
        return instance;
    }
    //End Singleton
    /*Scatter(Game game){
		this.game = game;
		//this.state = state;
	}*/
    
	
	
	@Override
	public void Enter(DelayedGhost npc) {
		System.out.println("Entrando no Scatter");		
	}

	@Override
	public void Execute(DelayedGhost npc) {
		double minDistance = 8.0f;
		//Location target = null;
		State s = game.getCurrentState();
		//target = s.getPacManLocation();
		//System.out.println(count);
		
		Random rand = new Random();
		
		List<Location> allLoc =  new ArrayList<Location>(s.getDotLocations());
		for(Location loc: allLoc) {
			double distance = Location.euclideanDistance(loc, s.getPacManLocation());
			if(distance > minDistance) {
				target = loc;
				minDistance = distance;
				//System.out.println(target + " "+ s.getPacManLocation());
			}else {
				target = allLoc.get(rand.nextInt(allLoc.size()));
			}
		}	
		count++;
		if(count > 50) {
			count = 0;
			npc.getStateMachine().changeState(HuntDelayedGhost.getInstance(game));
		}
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public void Exit(DelayedGhost npc) {
		System.out.println("Mudando de estado");
		
	}

	@Override
	public boolean onMessage(DelayedGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
