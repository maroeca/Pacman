package ghosts;

import java.util.List;
import java.util.regex.Pattern;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.State;

public class HuntDelayedGhost implements GhostState<DelayedGhost>{

	Game game;	
	Location target;
	int count = 0;
	GhostPlayer ghostReceiver;
	
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
			MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "DelayedGhost Hunt", null); //envia a mensagem
		
	}

	@Override
	public void Execute(DelayedGhost npc) {
		// TODO Auto-generated method stub
		Location pacmanLoc;
		State s = game.getCurrentState();
		List<State> history = s.getHistory(); //pega os estados anteriores
		if(history.size() >= 2) {
			pacmanLoc = history.get(history.size() - 2).getPacManLocation(); //pega a posição do pacman no penultimo estado
			
		}else { //caso seja o começo do jogo, pega a posição atual do pacboy
			pacmanLoc = s.getPacManLocation();
		}
		
		target = pacmanLoc;
		count++;
		if(count > 50) {
			count = 0;
			npc.getStateMachine().changeState(ScatterDelayedGhost.getInstance(game));
		}
	}

	@Override
	public void Exit(DelayedGhost npc) {
		//Envia mensagem para o Mirror avisando que saiu do Hunt
		if(ghostReceiver != null)
			MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "DelayedGhost Scatter", null); //envia a mensagem
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
