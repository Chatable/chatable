package org.chatable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Connection {

    private String ip;
    private int port;
    private Socket socket;
    private InputListener listener;
    private Server server;
    private PrintWriter output;
    private BufferedReader input;
    private static final Logger logger = LogManager.getLogger(Connection.class);
    //private String name;

    /**
     * Constructor for connections from server to client
     * @param socket Connected socket to client
     * @param server Reference to server object that is managing the connection
     */
    public Connection(Socket socket, Server server) {

        if(!socket.isConnected()){
            System.out.println("ERROR: Constructor requires connected socket");
            System.exit(0);
        }

        try{
            this.socket = socket;
            this.server = server;
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            ip = socket.getRemoteSocketAddress().toString();
            port = socket.getPort();
        } catch (Exception e) {
            logger.error(e.toString());
        }

        //Handling websocket connections, currently not fussy. Will accept any websocket connection. Can change later to be stricter.
        String message = read();
        if (message!=null){
            String key = "";

            //Detect http request header
            if (message.contains("GET / HTTP/1.1")){
                //Goes through each http field
                while(!message.isEmpty()) {
                    if(message.contains(":")) {
                        //Searches for the key and extracts it. Can be extended to gather other pieces of information from the header.
                        String field = message.substring(0,message.indexOf(':'));
                        switch(field){
                            case "Sec-WebSocket-Key":
                                key = message.substring(message.indexOf(':')+1).trim();
                        }
                    }
                    message = read();
                }

                //send handshake response to client, with required accept key
                send("HTTP/1.1 101 Switching Protocols\r\n" +
                        "Upgrade: websocket\r\n" +
                        "Connection: Upgrade\r\n" +
                        "Sec-WebSocket-Accept: " + genResKey(key) +
                        "\r\n");
            }
        }


        listener = new InputListener();
        new Thread(new Thread(listener)).start();
    }

    static String genResKey(String key){
        //append GUID to given key
        String concat = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //hash the concatentated key and then return as base64 encoded string
        return Base64.getEncoder().encodeToString(sha1.digest(concat.getBytes()));
    }

    /**
     * Sends message over the connected socket
     * @param input Message to be sent
     * @return true if send succeeds, false if send fails
     */
    public boolean send(String input){
        if(!isConnected()){
            return false;
        }

        output.println(input);
        //output.println("name: " + input);

        if(output.checkError()){
            output.flush();
            return false;
        }
        return true;
    }

    /**
     * Reads from socket, times out after 1 second
     * @return Returns message if read successfully, or null if failed/timed out
     */
    public String read() {
        if(!isConnected()){
            return null;
        }

        String message = null;
        try {
            socket.setSoTimeout(1000);
            message = input.readLine();
        } catch (SocketTimeoutException e) {
            //Standard read time out. Not bad at all.
        } catch (IOException e) {
            logger.error(e.toString());
        }

        return message;
    }

    /**
     * Flushes and closes all connections and stops the listening thread
     * @throws IOException
     */
    public void close() throws IOException{
        input.close();
        output.flush();
        output.close();
        socket.close();
        listener.stop();
        System.out.println("Connection closed.");
    }

    public boolean isConnected(){
        return(socket.isConnected());
    }

    public String getIP(){
        return ip;
    }

    public int getPort(){
        return port;
    }

    public Server getServer(){
        return server;
    }

    //Possibly bad code... Just used by input listener to get ref to 'parent'
    public Connection getRef() {
        return this;
    }

    /**
     * Separate thread for listening to incoming messages over the connection.
     */
    class InputListener implements Runnable {
        private boolean running;

        public InputListener() {
            running = true;
        }

        public void stop(){
            running = false;
        }

        public void run() {
            while(running) {
                String message = read();
                if (message != null) {
                    getServer().broadcast(message, getRef());
                    System.out.println("RECEIVED: " + message);
                    //System.out.println("RECEIVED - " + name + ": " + message);
                }
            }
        }
    }
}


