package ghosts;

import java.util.regex.Pattern;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;

public class HuntRightGhost implements GhostState<RightGhost>{

	Game game;
	Location target;
	int timeToChangeState = 50;
	int count = 0;
	int index;
	
	GhostPlayer ghostReceiver; //ghost que vai receber as mensagens
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
		String[] names; //array de string que recebe o split
		for (int i = 0; i < game.getGhostPlayers().size(); i++) {
			names = game.getGhostPlayers().get(i).getName().split(Pattern.quote(".")); //divide o game em dois onde tem ponto
			if(names[1].compareTo("MirrorGhost") == 0) { //verifica se a segunda parte do nome é igual ao nome do ghost
				ghostReceiver = game.getGhostPlayers().get(i); //se for o ghostReceiver recebe o ghost
			}
		}
		if(ghostReceiver != null)
			MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "RightGhost Hunt", null); //envia a mensagem		
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
		//Envia mensagem para o Mirror avisando que saiu do Hunt
		if(ghostReceiver != null)
			MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "RightGhost Scatter", null); //envia a mensagem		
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public boolean onMessage(RightGhost npc, Message msg) {
		if(msg.getMessage().compareTo("Hunt") == 0) {
			System.out.println("Right ghost received: "+msg.getMessage() + " from Hunt Delayed");
			return true;
		}
		return false;
	}

}
