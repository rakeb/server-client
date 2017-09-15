package com.server.main;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by mislam7 on 9/14/17.
 */
public class Server {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
        }

        System.out.println("Server started at Port: " + args[0]);

        File file = new File("server/src/main/resources/index.html");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (listening) {
                new ServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
