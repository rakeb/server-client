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

            if (out == null || in == null) {
                System.err.println("Connection closed");
                closeAll();
                exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeAll();
            exit(1);
        }
    }

    private void closeAll() {
        if (this.out !=null)
            this.out.close();
        try {
            if (this.in !=null)
                this.in.close();
            if (this.clientSocket !=null)
                this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            closeAll();
            exit(1);
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Usage: Client <host name> <port number> <command:GET/PUT> <file name>");
            exit(1);
        }

        System.out.println("Client started...");

        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        String command = args[2];
        String fileName = args[3];

        Client client = new Client();

        // TCP Connection
        client.tcpConnection(hostName, port);

        InputStream inputStream = null;
        if (command.equals("PUT")) {
            try {
                inputStream = new FileInputStream(new File(fileName));  //get file
            } catch (IOException e) {
                inputStream = null;
            }

            if (inputStream == null) {  //no such file found
                System.err.println("File not found: " + fileName);
                client.closeAll();
                exit(1);
            }
        }

        // Generate HTTP 1.1 request
        String userInput = Util.generateHttpRequest(command, fileName, hostName, inputStream);
        try {
            if (inputStream != null)
                inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            client.closeAll();
            exit(1);
        }
        System.out.println("Request: " + userInput);

        // Send HTTP 1.1 request
        Util.submit(userInput, client.out);

        //Display Server Response
        Util.display(client.in);

        //Close connection
        client.closeAll();
    }
}
