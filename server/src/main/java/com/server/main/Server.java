package com.server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

/**
 * Created by mislam7 on 9/18/17.
 */
public class Server {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: Server <port number>");
            System.exit(1);
        }

        System.out.println("Server started at Port: " + args[0]);

        int portNumber = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);

            ServerThread serverThread = new ServerThread(); //starting server as a seperate thread

            serverThread.setListening(true);

            serverThread.setServerSocket(serverSocket);

            serverThread.start();

            Scanner scanner = new Scanner(System.in);

            String line = scanner.nextLine();

            if (line.equals("shutdown")) {
                serverThread.setListening(false);
                serverThread.interrupt();

                if (serverSocket !=null) {
                    serverSocket.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
