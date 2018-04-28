package ghosts;

import pacman.GhostPlayer;

public class Message {
	private GhostPlayer sender;
    private GhostPlayer receiver;
    private String msg;
    private Object extraInfo;

    public Message(GhostPlayer msgSender, GhostPlayer msgReceiver, String message, Object extraInfo) {
        this.sender = msgSender;
        this.receiver = msgReceiver;
        this.msg = message;
        this.extraInfo = extraInfo;

    }

    public String getMessage() {
        return this.msg;
    }
}
