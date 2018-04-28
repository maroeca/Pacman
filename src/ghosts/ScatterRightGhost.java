package ghosts;

import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.Move;

public class ScatterRightGhost implements GhostState<RightGhost>{

	Game game;
	int ghostIndex;
	Move bestMove;
	int timeToChangeState = 30;
	int count = 0;
	//Begin Singleton
	   public static ScatterRightGhost instance = null;

	   private ScatterRightGhost(Game game, int index){
			this.game = game;
			ghostIndex = index;
		}

	    public static ScatterRightGhost getInstance(Game game, int index){
	        if(instance == null){
	            instance = new ScatterRightGhost(game, index);
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
		List<Move>legalMoves = game.getLegalGhostMoves(ghostIndex);
		Random rand = new Random();
		for(Move m: legalMoves) {
			if(m == Move.RIGHT) {
				bestMove = m;
			}else {
				bestMove = legalMoves.get(rand.nextInt(legalMoves.size()));
			}
		}
		count++;
		if(count > timeToChangeState) {
			count = 0;
			npc.getStateMachine().changeState(HuntRightGhost.getInstance(game, ghostIndex));
		}
		
	}

	@Override
	public void Exit(RightGhost npc) {
		// TODO Auto-generated method stub
		
	}
	
	public Move getMove() {
		return bestMove;
	}

	@Override
	public boolean onMessage(RightGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
