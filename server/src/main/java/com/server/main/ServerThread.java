package com.server.main;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by mislam7 on 9/18/17.
 */
public class ServerThread extends Thread {
    private boolean listening;
    private ServerSocket serverSocket;

    public ServerThread() {
        super();
    }

    public void run() {
        try {
            while (listening) {
                new ClientThread(serverSocket.accept()).start();    //Creating thread for all client
            }
        } catch (IOException e) {
            System.err.println("Server shutdown on port " + serverSocket.getLocalPort());
            System.exit(-1);
        }
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
}
