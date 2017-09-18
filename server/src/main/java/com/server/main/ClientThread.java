package com.server.main;

import com.serverclient.util.HttpRequestParser;
import com.serverclient.util.Util;

import java.io.*;
import java.net.Socket;
import java.net.URL;

import static java.lang.System.exit;
import static java.lang.System.in;
import static java.lang.System.setOut;

/**
 * Created by mislam7 on 9/14/17.
 */
public class ClientThread extends Thread {
    private Socket socket = null;

    public ClientThread(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run() {
        BufferedReader bufferedReader = null;

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("server reading request: ");

            //read request, should not be display()
            String request = Util.readRequest(in);

            if (request.trim().isEmpty()) {
                System.err.println("Client sent an empty request.");
                return;
            }

            System.out.println("Client request: " + request);

            HttpRequestParser httpRequestParser = new HttpRequestParser(request);
            String command = httpRequestParser.getCommand();
            String[] fileNames = httpRequestParser.getFileName().split("/");
            String fileName = fileNames[1];

            String sFile = httpRequestParser.getFile();

            String response = null;

            if (command.equals(Util.HTTP_GET)) {
                InputStream inputStream;
                try {
                    inputStream = new FileInputStream(new File(fileName));
                } catch (IOException e) {
                    inputStream = null;
                }

                if (inputStream == null) {  //no such file found on server
                    response = Util.generateHttpResponse(Util.HTTP_NOT_FOUND, command, inputStream);
                    Util.submit(response, out); //send response to client
                } else {    //file found
                    response = Util.generateHttpResponse(Util.HTTP_200_OK, command, inputStream);
                    Util.submit(response, out); //send response to client
                    inputStream.close();
                }
            } else if (command.equals(Util.HTTP_PUT)) {
                response = Util.generateHttpResponse(Util.HTTP_200_OK, command, null);
                Util.submit(response, out); //send ok response to client

                File file = new File(fileName);
                Util.createFile(file.getPath(), sFile);

            } else {
                System.err.println("Command not supported: " + command);
                exit(1);
            }

            System.out.println("response: " + response);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
