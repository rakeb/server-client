package com.server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by mislam7 on 9/14/17.
 */
public class ServerThread extends Thread {
    private Socket socket = null;

    public ServerThread(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
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

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
