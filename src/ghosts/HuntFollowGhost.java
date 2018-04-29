package ghosts;

import java.util.regex.Pattern;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.State;

public class HuntFollowGhost implements GhostState<FollowGhost>{

	Game game;
	int ghostIndex;
	Location target;
	
	int count = 0;
	int timeToChangeState = 50;
	GhostPlayer ghostReceiver;
	
	double minDistance = Double.POSITIVE_INFINITY;
	
	//Begin Singleton
	   public static HuntFollowGhost instance = null;

	   private HuntFollowGhost(Game game, int index){
			this.game = game;
			ghostIndex = index;
		}

	    public static HuntFollowGhost getInstance(Game game, int index){
	        if(instance == null){
	            instance = new HuntFollowGhost(game, index);
	        }
	        return instance;
	    }
	    //End Singleton
	
	@Override
	public void Enter(FollowGhost npc) {
		String[] names; //array de string que recebe o split
		for (int i = 0; i < game.getGhostPlayers().size(); i++) {
			names = game.getGhostPlayers().get(i).getName().split(Pattern.quote(".")); //divide o game em dois onde tem ponto
			if(names[1].compareTo("MirrorGhost") == 0) { //verifica se a segunda parte do nome é igual ao nome do ghost
				ghostReceiver = game.getGhostPlayers().get(i); //se for o ghostReceiver recebe o ghost
			}
		}
		if(ghostReceiver != null)
			MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "FollowGhost Hunt", null); //envia a mensagem
		
	}

	@Override
	public void Execute(FollowGhost npc) {
		State s = game.getCurrentState();
		
		Location myLoc = s.getGhostLocations().get(ghostIndex);
		minDistance = Location.euclideanDistance(myLoc, s.getPacManLocation());
		
		for (Location loc : s.getGhostLocations()) {
			double distance = Location.euclideanDistance(loc, s.getPacManLocation());
			if(distance < minDistance) {
				target = loc;
			}else {
				target = s.getPacManLocation();
			}
		}
		
		count++;
		if(count > timeToChangeState) {
			count = 0;
			npc.getStateMachine().changeState(ScatterFollowGhost.getInstance(game, ghostIndex));
		}
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public void Exit(FollowGhost npc) {
		//Envia mensagem para o Mirror avisando que saiu do Hunt
		MessageDispatcher.getInstance().dispatchMessage(npc, ghostReceiver, "FollowGhost Scatter", null); //envia a mensagem		
	}

	@Override
	public boolean onMessage(FollowGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
