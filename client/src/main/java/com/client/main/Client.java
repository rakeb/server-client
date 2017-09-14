package com.client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.SocketHandler;

/**
 * Created by mislam7 on 9/13/17.
 */
public class Client {

    public void tcpConnection() {

    }

    public void generateHttpRequest () {

    }

    public void display() {

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

        try {
            Socket clientSocket = new Socket(hostName, port);

            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));

            String userInput =  "GET /index.html HTTP/1.1\n" +
                    "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
                    "Host: www.google.com\n" +
                    "Accept-Language: en-us\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Connection: Keep-Alive" +
                    "\n\r";
            out.println(userInput);

            String outputFromServer;
            while ((outputFromServer = in.readLine()) != null) {
                System.out.println("echo: " + outputFromServer);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
