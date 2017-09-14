package com.client.main;

import com.serverclient.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

    public String generateHttpRequest(String command, String fileName, String hostName) {
        String request;

//        GET /hello.htm HTTP/1.1
//        User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)
//        Host: www.tutorialspoint.com
//        Accept-Language: en-us
//        Accept-Encoding: gzip, deflate
//        Connection: Keep-Alive
//
//        PUT /hello.htm HTTP/1.1
//        User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)
//        Host: www.tutorialspoint.com
//        Accept-Language: en-us
//        Connection: Keep-Alive
//        Content-type: text/html
//        Content-Length: 182

        if (!command.equals("GET") && !command.equals("PUT")) {
            throw new RuntimeException("HTTP Command not supported: " + command);
        }

//        request = command + " /" + fileName + " HTTP/1.1\n";
        request = command + " /" + fileName + " HTTP/1.1\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
                "Host: " + hostName + "\n" +
                "Accept-Language: en-us\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: Keep-Alive" +
                "\n\r";

        return request;
    }

    private void submittRequest(String userInput) {
        this.out.println(userInput);
    }

    public void display() {
        String outputFromServer;
        System.out.println("Displaying...: ");

        try {
            while (((outputFromServer = this.in.readLine()) != null)) {
                System.out.println("echo: " + outputFromServer);

                if (outputFromServer.trim().isEmpty()) {
                    if (!this.in.ready()) {
                        break;
                    }
                }
            }
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
            System.exit(1);
        }

        System.out.println("Starting MyClient...");

//        System.out.println("args 1 is: " + args[0]);
//        System.out.println("args 2 is: " + args[1]);
//        System.out.println("args 3 is: " + args[2]);
//        System.out.println("args 4 is: " + args[3]);

        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        String command = args[2];
        String fileName = args[3];

        Client client = new Client();

        client.tcpConnection(hostName, port);

        String userInput = client.generateHttpRequest(command, fileName, hostName);
        System.out.println(userInput);

        client.submittRequest(userInput);
//        client.display();
        String out = Util.display(client.in);
        System.out.println(out.length());
        client.close();
    }
}
