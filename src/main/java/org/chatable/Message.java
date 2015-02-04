package org.chatable;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Message {
    private String message;
    private Connection sender;

    /**
     * Holds extra information about the message
     * @param message Message content
     * @param sender Connection that sent the message
     */
    public Message(String message, Connection sender){
        this.message = message;
        this.sender = sender;
    }

    public String getMessage(){
        return message;
    }

    public Connection getSender(){
        return sender;
    }
}
