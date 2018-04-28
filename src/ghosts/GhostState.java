package ghosts;

public interface GhostState <GhostPlayer>{
	
	public void Enter(GhostPlayer npc);

    public void Execute(GhostPlayer npc);

    public void Exit(GhostPlayer npc);
    
    public boolean onMessage(GhostPlayer npc, Message msg);

}
