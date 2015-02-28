package org.chatable;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Server {

    private ArrayList<Connection> allConnections;
    private ArrayBlockingQueue<Message> messages;
    private int port;
    private final int QUEUE_SIZE = 50;

    public static void main(String args[]){
        Server server = new Server(11101);
        try {
            server.run();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a message to the queue for sending messages which the server will process inbetween socket accepts.
     * @param message
     * @param sender
     */
    public void broadcast(String message, Connection sender){
        messages.add(new Message(message,sender));
    }

    /**
     * Sends all messages queued for broacasting to all connections, except the one that sent the message.
     * @throws InterruptedException
     */
    public void processBroadcasts() throws InterruptedException {
        while(!messages.isEmpty()){
            Message current = messages.take();
            for(Connection con : allConnections) {
                if(con != current.getSender()){
                    con.send(current.getMessage());
                }
            }
        }
    }

    /**
     * Infinite loop for server. Accepts new connections and serves messages that are broadcasted by current connections.
     * @throws Exception Proper exception handling has not yet been implemented.
     */
    public void run() throws Exception {
        ServerSocket ss = new ServerSocket(port);

        //TODO handle ping/pong
        while(true){
            ss.setSoTimeout(500);
            try{
                Socket socket = ss.accept();
                Connection current = new Connection(socket, this);
                allConnections.add(current);
            } catch (SocketTimeoutException e){
                System.out.println("Timeout - checking broadcasts");
                processBroadcasts();
            }
        }
    }

    public Server (int port) {
        this.port = port;
        allConnections = new ArrayList<Connection>();
        messages = new ArrayBlockingQueue<Message>(QUEUE_SIZE);
    }
}
