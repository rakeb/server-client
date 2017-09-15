package com.client.main;

import com.serverclient.util.Util;

import java.io.*;
import java.net.Socket;

import static java.lang.System.exit;

/**
 * Created by mislam7 on 9/13/17.
 */
public class Client {
    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket;

    public void tcpConnection(String hostName, int port) {
        try {
            clientSocket = new Socket(hostName, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void submittRequest(Object userInput) {
//        this.out.println(userInput);
//    }

//    public void display() {
//        String outputFromServer;
//        System.out.println("Displaying...: ");
//
//        try {
//            while (((outputFromServer = this.in.readLine()) != null)) {
//                System.out.println("echo: " + outputFromServer);
//
//                if (outputFromServer.trim().isEmpty()) {
//                    if (!this.in.ready()) {
//                        break;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void close() {
        this.out.close();
        try {
            this.in.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Provide 4 arguments separated by space as following: HostName Port Command FileName");
            exit(1);
        }

        System.out.println("Client started...");

        System.out.println("args 1 is: " + args[0]);
        System.out.println("args 2 is: " + args[1]);
        System.out.println("args 3 is: " + args[2]);
        System.out.println("args 4 is: " + args[3]);

        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        String command = args[2];
        String fileName = args[3];

        Client client = new Client();

        // TCP Connection
        client.tcpConnection(hostName, port);

        // Generate HTTP 1.1 request
        String userInput = Util.generateHttpRequest(command, fileName, hostName);
        System.out.println(userInput);

        // Send HTTP 1.1 request
        Util.submit(userInput, client.out);

        //Send File
        if (command.equals("PUT")) {
            InputStream inputStream = client.getClass().getResourceAsStream("/test.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            Util.display(reader);

            Util.submit(reader, client.out);
        }

        //Display Server Response
        String out = Util.display(client.in);

        //Close connection
        client.close();
    }
}
