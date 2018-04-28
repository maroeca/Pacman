package ghosts;

import pacman.Game;
import pacman.Location;

public class HuntRightGhost implements GhostState<RightGhost>{

	Game game;
	Location target;
	int timeToChangeState = 50;
	int count = 0;
	int index;
	//Begin Singleton
	   public static HuntRightGhost instance = null;

	   private HuntRightGhost(Game game, int index){
			this.game = game;
			this.index = index;
			
		}

	    public static HuntRightGhost getInstance(Game game, int index){
	        if(instance == null){
	            instance = new HuntRightGhost(game, index);
	        }
	        return instance;
	    }
	    //End Singleton
	
	@Override
	public void Enter(RightGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Execute(RightGhost npc) {
		target = game.getCurrentState().getPacManLocation();
		
		count++;
		if(count > timeToChangeState) {
			count = 0;
			npc.getStateMachine().changeState(ScatterRightGhost.getInstance(game, index));
		}
		
	}

	@Override
	public void Exit(RightGhost npc) {
		// TODO Auto-generated method stub
		
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public boolean onMessage(RightGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
