package ghosts;
/*Mesmo esquema do farmerbob */
public class StateMachineGhost <NonPlayableCharacter>{
	
	private NonPlayableCharacter myOwner;
	//Define o estado atual
    private GhostState<NonPlayableCharacter> estadoAtual;

    //Define o estado anterior
    private GhostState<NonPlayableCharacter> estadoAnterior;

    //Define o estado global
    private GhostState<NonPlayableCharacter> estadoGlobal;

    public StateMachineGhost(NonPlayableCharacter owner){
        myOwner = owner;
        estadoAtual = null;
        estadoAnterior = null;
        estadoGlobal = null;
    }

    //Utilize estes metodos para inicializar a state machine

    public void setCurrentState(GhostState<NonPlayableCharacter> s){
        estadoAtual = s;
        estadoAtual.Enter(myOwner);
    }

    public void setGlobalState(GhostState<NonPlayableCharacter> s){
        estadoGlobal = s;
    }

    public void setPreviousState(GhostState<NonPlayableCharacter> s){
        estadoAnterior = s;
    }

    //Invoca este metedo pra atualizar a FSM
    public void update(){
        //Se existir um estado global, invoca o seu metodo Execute, caso contrario não faz nada
        if(estadoGlobal != null){
            estadoGlobal.Execute(myOwner);
        }

        if(estadoAtual != null){
            estadoAtual.Execute(myOwner);
        }

    }

    //Faz a troca de estados
    public void changeState(GhostState<NonPlayableCharacter> novoEstado){
        //Armazena o estado anterior
        estadoAnterior = estadoAtual;

        //Invoca o método de saida do estado atual
        estadoAtual.Exit(myOwner);

        //Faz a troca de estados
        estadoAtual = novoEstado;

        //Invoca o método de Entrada do novo estado
        estadoAtual.Enter(myOwner);
    }

    //Muda para o estado anterior
    public void revertToPreviousState(){
        changeState(estadoAnterior);
    }

    public GhostState<NonPlayableCharacter> getCurrentState(){
        return estadoAtual;
    }

    public GhostState<NonPlayableCharacter> getGlobalState() {
        return estadoGlobal;
    }

    public GhostState<NonPlayableCharacter> getPreviousState() {
        return estadoAnterior;
    }

    public boolean handleMessage(Message msg) {
        //Verifica se o estado atual é capaz de lidar com a mensagem recebida
        if (estadoAtual != null && estadoAtual.onMessage(myOwner, msg)) {
            return true;
        }
        //Se  o estado atual nao sabe lidar com a mensagem, verificamos se existe um estado global e se ele sabe lidar
        if (estadoGlobal != null && estadoGlobal.onMessage(myOwner, msg)) {
            return true;
        }

        //Se nao retorna falso
        return false;
    }

}
