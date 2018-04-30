package ghosts;

import java.util.List;
import java.util.regex.Pattern;

import pacman.*;

public class HuntTryhardGhost implements GhostState<TryhardGhost> {

	Game game;
	Location target;
	GhostPlayer ghostReceiver;
	
	int count = 0;
	
	//sinteltn
	private static HuntTryhardGhost instance = null;
	
	private HuntTryhardGhost(Game game) {
		this.game = game;
	}
	
	public static HuntTryhardGhost getInstance(Game game) {
		if (instance == null) {
			instance = new HuntTryhardGhost(game);
			
		}
		
		return instance;
	}
	
	@Override
	public void Enter(TryhardGhost npc) {
		System.out.println("--------------");
		System.out.println("TryhardGhost hunt");
		
		String[] names; //array de string que recebe o split
		for (int i = 0; i < game.getGhostPlayers().size(); i++) {
			names = game.getGhostPlayers().get(i).getName().split(Pattern.quote(".")); //divide o game em dois onde tem ponto
			if(names[1].compareTo("MirrorGhost") == 0) { //verifica se a segunda parte do nome é igual ao nome do ghost
				ghostReceiver = game.getGhostPlayers().get(i); //se for o ghostReceiver recebe o ghost
			}
		}
		if(ghostReceiver != null)
			MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "TryhardGhost Hunt", null); //envia a mensagem
		
	}

	@Override
	public void Execute(TryhardGhost npc) {
		State s = game.getCurrentState();
		target = s.getPacManLocation();
		
		count++;
		if (count > 50 && !npc.isTrying) {
			count = 0;
			npc.getStateMachine().setCurrentState(HuntTryhardGhost.getInstance(game));
		}
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public void Exit(TryhardGhost npc) {
		MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "TryhardGhost Scatter", null); //envia a mensagem
	}

	@Override
	public boolean onMessage(TryhardGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
