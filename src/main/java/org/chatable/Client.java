package org.chatable;

import java.util.Scanner;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Client {

    private static Connection connection;

    public Client(String ip, int port){
        this.connection = new Connection(ip,port);
        this.connection.run();
    }

    public static void main(String args[]){
        String ip = "127.0.0.1";
        int port = 1234;

        Client client = new Client(ip,port);
        Scanner kb = new Scanner(System.in);
        while(true){
            connection.send(kb.nextLine());
        }
    }
}
