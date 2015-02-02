package org.chatable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Server {

    private ArrayList<Connection> allConnections;
    private ArrayBlockingQueue<Message> messages;
    private final int PORT = 1234;
    private final int QUEUE_SIZE = 10;

    public static void main(String args[]){
        Server server = new Server();
        try {
            server.run();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void broadcast(String message, Connection sender){
        messages.add(new Message(message,sender));
    }

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

    public void run() throws Exception{
        ServerSocket ss = new ServerSocket(PORT);

        while(true){
            ss.setSoTimeout(500);
            try{
                Socket socket = ss.accept();
                Connection current = new Connection(socket, this);
                allConnections.add(current);
                current.run();
            } catch (SocketTimeoutException e){
                System.out.println("Timeout - checking broadcasts");
                processBroadcasts();
            }
        }
    }

    public Server () {
        allConnections = new ArrayList<Connection>();
        messages = new ArrayBlockingQueue<Message>(QUEUE_SIZE);
    }
}
