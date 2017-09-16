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

        //Get File
        InputStream inputStream = null;
        if (command.equals("PUT")) {
            inputStream = client.getClass().getResourceAsStream("/files/" + fileName);

            if (inputStream == null) {  //no such file found
                System.err.println("File not found: " + fileName);
                exit(1);
            }
        }

        // Generate HTTP 1.1 request
        String userInput = Util.generateHttpRequest(command, fileName, hostName, inputStream);
        System.out.println("Request: " + userInput);

        // Send HTTP 1.1 request
        Util.submit(userInput, client.out);

        //Display Server Response
        Util.display(client.in);

        //Close connection
        client.close();
    }
}
