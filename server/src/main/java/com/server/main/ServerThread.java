package com.server.main;

import com.serverclient.util.Util;

import java.io.*;
import java.net.Socket;

import static java.lang.System.exit;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String outputLine;

            System.out.println("server reading request...");
//            while ((inputLine = in.readLine()) != null) {
//                System.out.println(inputLine);
//                if (inputLine.contains("Keep-Alive")) {
//                    break;
//                }
//            }

            String request = Util.display(in);
            if (request.trim().isEmpty()) {
                exit(1);
            }
            System.out.println(request);

            String[] parsed = request.split(" ");
            String command = parsed[0];
            String fileName = parsed[1];

            String response;

            if (command.equals(Util.HTTP_GET)) {
                InputStream inputStream;
                inputStream = getClass().getResourceAsStream(fileName);

                if (inputStream == null) {
                    response = Util.generateHttpResponse(Util.HTTP_NOT_FOUND);
                    Util.submit(response, out);
                } else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    response = Util.generateHttpResponse(Util.HTTP_200_OK);
                    Util.submit(response, out);
                    Util.submit(reader, out);
                }
            } else if (command.equals(Util.HTTP_PUT)) {

            } else {
                System.err.println("Command not supported: " + command);
                exit(1);
            }


            // Initiate conversation with client
//            System.out.println("server sending response...");
//            outputLine = "server responding...";
//            out.println(outputLine);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
