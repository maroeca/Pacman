package ghosts;

import java.util.List;

import pacman.Game;
import pacman.Location;
import pacman.State;

public class HuntDelayedGhost implements GhostState<DelayedGhost>{

	Game game;	
	Location target;
	int count = 0;
	
	//Begin Singleton
   public static HuntDelayedGhost instance = null;

   private HuntDelayedGhost(Game game){
		this.game = game;
	}

    public static HuntDelayedGhost getInstance(Game game){
        if(instance == null){
            instance = new HuntDelayedGhost(game);
        }
        return instance;
    }
    //End Singleton
	
	@Override
	public void Enter(DelayedGhost npc) {
		System.out.println("Entrando no Hunt");
		
	}

	@Override
	public void Execute(DelayedGhost npc) {
		// TODO Auto-generated method stub
		Location pacmanLoc;
		State s = game.getCurrentState();
		List<State> history = s.getHistory(); //pega os estados anteriores
		if(history.size() >= 2) {
			pacmanLoc = history.get(history.size() - 2).getPacManLocation(); //pega a posição do pacman no penultimo estado
			
		}else {
			pacmanLoc = s.getPacManLocation();
		}
		
		target = pacmanLoc;
		//System.out.println(count);
		count++;
		if(count > 50) {
			count = 0;
			npc.getStateMachine().changeState(ScatterDelayeGhost.getInstance(game));
		}
	}

	@Override
	public void Exit(DelayedGhost npc) {
		System.out.println("Saindo do Hunt");
		
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public boolean onMessage(DelayedGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
