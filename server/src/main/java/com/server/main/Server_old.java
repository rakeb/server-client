package com.server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by mislam7 on 9/13/17.
 */
public class Server_old {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }


        System.out.println("Server_old is running...");
        System.out.println("args 1 is: " + args[0]);

        int portNumber = Integer.parseInt(args[0]);

        try {
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;

            System.out.println("server reading request...");
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                if (inputLine.contains("Keep-Alive")) {
                    break;
                }
            }

            // Initiate conversation with client
            System.out.println("server sending response...");
            outputLine = "server responding...";
            out.println(outputLine);

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
