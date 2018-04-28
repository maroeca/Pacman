package ghosts;

import pacman.GhostPlayer;

public class MessageDispatcher {

	//Begin Singleton
    private static MessageDispatcher instance = null;

    private MessageDispatcher() {}

    public static MessageDispatcher getInstance() {
        if (instance == null) {
            instance = new MessageDispatcher();
        }
        return instance;
    }
    //End Singleton

    public void dispatchMessage(GhostPlayer sender, GhostPlayer receiver, String msg, Object extra) {
        //Cria a mensagem
        Message message = new Message(sender, receiver, msg, extra);

        //Entrega a mensagem
        deliverMessage(receiver, message);
    }

    public void deliverMessage(GhostPlayer receiver, Message message) {
        if (!receiver.handleMessage(message)) {
            System.out.println("Cannot handle. Error");
        }
    }
}
