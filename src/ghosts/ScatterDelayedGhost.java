package ghosts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.Game;
import pacman.GhostPlayer;
import pacman.Location;
import pacman.State;

public class ScatterDelayedGhost implements GhostState<DelayedGhost>{
	Game game; //referencia para o jogo
	State state;
	int count = 0; //count do "tempo" para mudar os estados
	Location target; //target a ser passado para o ghost
	
	//Begin Singleton
   public static ScatterDelayedGhost instance = null;

   private ScatterDelayedGhost(Game game){
		this.game = game;
	}

    public static ScatterDelayedGhost getInstance(Game game){
        if(instance == null){
            instance = new ScatterDelayedGhost(game);
        }
        return instance;
    }
    //End Singleton	
	
	@Override
	public void Enter(DelayedGhost npc) {
	}

	@Override
	public void Execute(DelayedGhost npc) {
		double minDistance = Double.NEGATIVE_INFINITY;
		State s = game.getCurrentState(); //pega o estado atual para ter acesso as posicoes
		
		Random rand = new Random();
		
		List<Location> allLoc =  new ArrayList<Location>(s.getDotLocations()); //uma list com todas as localizacoes dos pontos
		//foreach pela lista das posicoes dos pontos
		for(Location loc: allLoc) {
			double distance = Location.euclideanDistance(loc, s.getPacManLocation()); //calcula a distancia entre cada ponto e a posicao do pacboy
			if(distance > minDistance) { //escolhe o ponto mais distante do pacboy
				target = loc;
				minDistance = distance;
				//System.out.println(target + " "+ s.getPacManLocation());
			}else { //senao pega um ponto random
				target = allLoc.get(rand.nextInt(allLoc.size()));
			}
		}	
		count++; //incrementa o tempo
		if(count > 50) {
			count = 0;
			npc.getStateMachine().changeState(HuntDelayedGhost.getInstance(game)); //muda o estado
		}
	}
	
	public Location getTarget() {
		return target;
	}

	@Override
	public void Exit(DelayedGhost npc) {
		
	}

	@Override
	public boolean onMessage(DelayedGhost npc, Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
