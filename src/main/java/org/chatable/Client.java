package org.chatable;

import java.io.IOException;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Client {

    private Connection connection;

    public Client(String ip, int port){
        this.connection = new Connection(ip,port);
//        try{
//            wait(500);
//        } catch (Exception e) {
//            System.out.println("Interrupted");
//        }
//        connection.send("name:"+name);
    }

    public void send(String message){
        connection.send(message);
    }

    public void close() throws IOException{
        connection.close();
    }

}
