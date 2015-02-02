package org.chatable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by jackgerrits on 2/02/15.
 */
public class Connection {

    private String ip;
    private int port;
    private Socket socket;
    private static final Logger logger = LogManager.getLogger(Connection.class);

    public Connection(String ip, int port) {
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
}
