package ghosts;
import java.util.regex.Pattern;

import ghosts.*;
import pacman.*;

public class HuntFleetingGhost implements GhostState<FleetingGhost> {
	
	Game game;
	Location target;
	
	int count = 0;
	double minDist = Double.POSITIVE_INFINITY;
	GhostPlayer ghostReceiver;
	
	//singelton
	private static HuntFleetingGhost instance = null;
	
	private HuntFleetingGhost(Game game) {
		this.game = game;
	}
	
	public static HuntFleetingGhost getInstace(Game game) {
		if (instance == null)
			instance = new HuntFleetingGhost(game);
		return instance;
	}
	
	@Override
	public void Enter(FleetingGhost npc) {
		//Envia a mensagem para o MirrorGhost avisando que entrou no modo hunt
				//Compara os nomes dos ghosts para enviar para os ghosts certos
				String[] names; //array de string que recebe o split
				for (int i = 0; i < game.getGhostPlayers().size(); i++) {
					names = game.getGhostPlayers().get(i).getName().split(Pattern.quote(".")); //divide o game em dois onde tem ponto
					if(names[1].compareTo("MirrorGhost") == 0) { //verifica se a segunda parte do nome é igual ao nome do ghost
						ghostReceiver = game.getGhostPlayers().get(i); //se for o ghostReceiver recebe o ghost
					}
				}
				if(ghostReceiver != null)
					MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "FleetingGhost Hunt", null); //envia a mensagem
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
		MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "FleetingGhost Scatter", null); //envia a mensagem

	}

	@Override
	public boolean onMessage(FleetingGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
