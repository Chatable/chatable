package org.chatable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Connection implements Runnable {

    public enum Type {
        SERVER, CLIENT
    }

    Type type;
    private String ip;
    private int port;
    private Socket socket;
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
    }

    public Connection(Socket socket) {

        try{
            this.socket = socket;
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

    public String read(){
        if(!isConnected()){
            return null;
        }

        String message = null;
        try {
            socket.setSoTimeout(1000);
            message = input.readLine();
        } catch (IOException e) {
            logger.error(e.toString());
            return null;
        }

        return message;
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

    public void run() {
        if(type==Type.SERVER){
            while(true){
                String message = this.read();
                server.broadcast(message, this);
            }
        } else if ( type==Type.CLIENT) {
            String message = this.read();
            System.out.println("Received: " + message);
        }
    }
}