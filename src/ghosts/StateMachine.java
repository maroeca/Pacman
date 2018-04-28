package com.mylla;

public class StateMachine<Ghost> {
    private Ghost ghostType;
    private GhostState<Ghost> currentState;
    private GhostState<Ghost> previousState;
    private GhostState<Ghost> globalState;

    public StateMachine(Ghost ghost) {
        this.ghostType = ghost;
        currentState = null;
        previousState = null;
        globalState = null;
    }

    public void setCurrentState(GhostState<Ghost> s) {
        currentState = s;
    }

    public void setGlobalState(GhostState<Ghost> s) {
        globalState = s;
    }

    public void setPreviousState(GhostState<Ghost> s) {
        previousState = s;
    }

    public void update() {
        if (globalState != null) {
            globalState.execute(ghostType);
        }

        if (currentState != null) {
            currentState.execute(ghostType);
        }
    }

    public void changeState(GhostState<Ghost> newS) {
        previousState = currentState;
        currentState.exit(ghostType);

        currentState = newS;
        currentState.enter(ghostType);
    }

    public void revertToPreviousState() {
        changeState(previousState);
    }

    public GhostState<Ghost> getCurrentState() {
        return currentState;
    }

    public GhostState<Ghost> getGlobalState() {
        return globalState;
    }

    public GhostState<Ghost> getPreviousState() {
        return previousState;
    }

    public boolean handleMessage(Message msg) {
        if (currentState != null && currentState.onMessage(ghostType, msg)) {
            return true;
        }

        if (globalState != null && globalState.onMessage(ghostType, msg)) {
            return true;
        }

        return false;
    }
}
