package org.chatable;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by jackgerrits on 4/02/15.
 */
public class Driver {
    public static void main(String args[]) throws IOException{
        if(args.length!=3){
            System.out.println("usage java Driver ip port");
            System.exit(0);
        }

        String ip = args[1];
        int port = Integer.parseInt(args[2]);

        Client client = new Client(ip, port);
        Scanner kb = new Scanner(System.in);
        while (true) {
            String input = kb.nextLine();
            if(input.toLowerCase().equals("quit")||input.toLowerCase().equals("quit")){
                client.close();
                System.exit(0);

            } else {
                client.send(input);
            }
        }
    }
}
