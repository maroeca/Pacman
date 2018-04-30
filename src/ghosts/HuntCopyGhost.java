package ghosts;

import java.util.List;

import pacman.*;

public class HuntCopyGhost implements GhostState<CopyGhost> {
	
	Game game;
	int ghostIndex;
	int count = 0;
	
	Move bestMove;
	
	//Singleton
	private static HuntCopyGhost instance = null;
	
	private HuntCopyGhost(Game game, int index) {
		this.game = game;
		this.ghostIndex = index;
	}
	
	public static HuntCopyGhost getInstance(Game game, int index) {
		if (instance == null)
			instance = new HuntCopyGhost(game, index);
		return instance;
	}
	
	@Override
	public void Enter(CopyGhost npc) {
		System.out.println("Copy hunt");
		
	}

	@Override
	public void Execute(CopyGhost npc) {
		Location pacman;
		State s = game.getCurrentState();
		List<State> hist = s.getHistory(); //estados anteriores
		
		if (hist.size() >= 2) {
			pacman = hist.get(hist.size() - 2).getPacManLocation();
			bestMove = calculatePacmanMove(pacman, s.getPacManLocation());
		} 
		
		count++;
		if (count > 50) {
			count  = 0;
			npc.getStateMachine().setCurrentState(ScatterCopyGhost.getInstance(game, ghostIndex));
		}
		
	}
	
	public Move getMove() {
		return bestMove;
	}

	private Move calculatePacmanMove(Location l1, Location l2) {
		if (l1.getX() > l2.getX()) {
			return Move.LEFT;
			
		} else if (l1.getX() < l2.getX()) {
			return Move.RIGHT;
			
		} else {
			
			if (l1.getY() > l2.getY() ) {
				return Move.DOWN;
			} else {
				return Move.UP;
			}
		}
	}
	@Override
	public void Exit(CopyGhost npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	
	public boolean onMessage(CopyGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
