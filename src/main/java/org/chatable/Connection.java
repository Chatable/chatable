package org.chatable;

import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Connection {

    private String ip;
    private int port;
    private Socket socket;
    private static final Logger logger = LogManager.getLogger();

    public Connection(String ip, int port) {
        this.ip = ip;
        this.port = port;

        try{


        } catch (UnknownHostException e) {

        } catch (IOException e) {

        } catch (Exception e) {

        }

    }
}
