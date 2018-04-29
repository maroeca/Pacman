package ghosts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.Move;
import pacman.State;

/*Tem o target como pontos aleatórios até receber uma mensagem e mudar de estado*/

public class ScatterMirrorGhost implements GhostState<MirrorGhost>{

	Game game;
	int ghostIndex;
	int count = 0;
	int timeToChangeTarget = 10;
	
	Location target;
	GhostPlayer mirrored;
	
	boolean canChangeState = false;
	
	Random rand;
	String[] names;

	//Begin Singleton
	   public static ScatterMirrorGhost instance = null;

	   private ScatterMirrorGhost(Game game, int index){
			this.game = game;
			ghostIndex = index;
		}

	    public static ScatterMirrorGhost getInstance(Game game, int index){
	        if(instance == null){
	            instance = new ScatterMirrorGhost(game, index);
	        }
	        return instance;
	    }
	    //End Singleton
	
	@Override
	public void Enter(MirrorGhost npc) {
		System.out.println("Entrou no Scatter");
		rand = new Random();
		List<GhostPlayer>ghosts = game.getGhostPlayers();
		List<GhostPlayer>availableGhosts = new ArrayList<GhostPlayer>();
		for (int i = 0; i < ghosts.size(); i++) {
			if(ghosts.get(i).getName().compareTo(npc.getName()) == 0) continue;
			
			availableGhosts.add(ghosts.get(i));
		}
		
		mirrored = availableGhosts.get(rand.nextInt(availableGhosts.size()));
		System.out.println(mirrored);
		names = mirrored.getName().split(Pattern.quote(".")); //recebe o nome do fantasma sem o index
		System.out.println(names[1]);		
	}
	

	@Override
	public void Execute(MirrorGhost npc) {
		State s = game.getCurrentState();
		List<Location> dotLocations = new ArrayList<Location>(game.getAllLocationsCopy());
		//List<Move>legalMoves = game.getLegalGhostMoves(ghostIndex);
		target = dotLocations.get(rand.nextInt(dotLocations.size()));		
		
		if(canChangeState) {
			canChangeState = false;
			npc.getStateMachine().changeState(HuntMirrorGhost.getInstance(game, ghostIndex));
		}
		
	}
	
	public Location getTarget() {
		return target;
	}
	
	public GhostPlayer getMirrored() {
		if(mirrored == null) {
			System.out.println("Ta null");
		}
		return mirrored;
	}

	@Override
	public void Exit(MirrorGhost npc) {
		System.out.println("Saiu");		
	}

	@Override
	public boolean onMessage(MirrorGhost npc, Message msg) {
		if(msg.getMessage().compareTo(names[1]+ " Hunt") == 0) { //teste com as mensagens
			canChangeState = true;
			System.out.println("Scatter mirror Mensagem de "+ names[1]);
			return true;
		}
		return false;
	}

}
