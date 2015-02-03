package org.chatable;

import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Connection {

    public enum Type {
        SERVER, CLIENT
    }

    private Type type;
    private String ip;
    private int port;
    private Socket socket;
    private InputListener listener;
    private Server server;
    private PrintWriter output;
    private BufferedReader input;
    private static final Logger logger = LogManager.getLogger(Connection.class);

    public Connection(String ip, int port) {
        type = Type.CLIENT;
        this.ip = ip;
        this.port = port;

        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip,port),4000);
        } catch (UnknownHostException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        } catch (Exception e) {
            logger.error(e.toString());
        }

        listener = new InputListener();
        new Thread(new Thread(listener)).start();
    }

    public Connection(Socket socket, Server server) {
        type = Type.SERVER;

        try{
            this.socket = socket;
            this.server = server;
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            ip = socket.getRemoteSocketAddress().toString();
            port = socket.getPort();
        } catch (UnknownHostException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        } catch (Exception e) {
            logger.error(e.toString());
        }

        listener = new InputListener();
        new Thread(new Thread(listener)).start();
    }

    public boolean send(String input){
        if(!isConnected()){
            return false;
        }

        output.println(input);

        if(output.checkError()){
            output.flush();
            return false;
        }
        return true;
    }

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
            return null;
        }

        return message;
    }

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

    public Type getType(){
        return type;
    }

    public Server getServer(){
        return server;
    }

    //Possibly bad code... Just used by input listener to get ref to 'parent'
    public Connection getRef() {
        return this;
    }

    class InputListener implements Runnable {
        private boolean running;

        public InputListener() {
            running = true;
        }

        public void stop(){
            running = false;
        }

        public void run() {
            if(getType() == Type.SERVER){
                while(running) {
                    String message = read();
                    if(message!=null){
                        getServer().broadcast(message, getRef());
                        System.out.println("RECEIVED: " + message);
                    }
                }
            } else if (getType() == Type.CLIENT) {
                while(running) {
                    String message = read();
                    if (message != null) {
                        System.out.println("Received: " + message);
                    }
                }
            }
        }
    }
}