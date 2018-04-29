package ghosts;

import java.util.regex.Pattern;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Move;

public class HuntMirrorGhost implements GhostState<MirrorGhost>{

	Game game;
	int ghostIndex;
	
	GhostPlayer mirrored;
	Move bestMove;
	
	String[] names;
	boolean canChangeState = false;
	
	//Begin Singleton
	   public static HuntMirrorGhost instance = null;

	   private HuntMirrorGhost(Game game, int index){
			this.game = game;
			ghostIndex = index;
		}

	    public static HuntMirrorGhost getInstance(Game game, int index){
	        if(instance == null){
	            instance = new HuntMirrorGhost(game, index);
	        }
	        return instance;
	    }
	    //End Singleton
	
	@Override
	public void Enter(MirrorGhost npc) {
		mirrored = ScatterMirrorGhost.getInstance(game, ghostIndex).getMirrored();
		names = mirrored.getName().split(Pattern.quote("."));
		System.out.println("Mirror hunt");
	}
	
	public Move getMove() {
		return bestMove;
	}

	@Override
	public void Execute(MirrorGhost npc) {
		if(mirrored != null) {
			bestMove = mirrored.getLastMove();
		}else {
			npc.getStateMachine().changeState(ScatterMirrorGhost.getInstance(game, ghostIndex));
		}
		
		if(canChangeState) {
			canChangeState = false;
			npc.getStateMachine().changeState(ScatterMirrorGhost.getInstance(game, ghostIndex));
		}
		
	}

	@Override
	public void Exit(MirrorGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(MirrorGhost npc, Message msg) {
		if(msg.getMessage().compareTo(names[1] +" Scatter") == 0) {
			canChangeState = true;
			System.out.println("Hunt mirror Mensagem de "+ names[1]);
			return true;
		}
		return false;
	}

}
