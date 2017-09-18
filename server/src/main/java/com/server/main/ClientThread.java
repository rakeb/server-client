package com.server.main;

import com.serverclient.util.HttpRequestParser;
import com.serverclient.util.Util;

import java.io.*;
import java.net.Socket;

import static java.lang.System.exit;

/**
 * Created by mislam7 on 9/14/17.
 */
public class ClientThread extends Thread {
    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;

    public ClientThread(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (out == null || in == null) {
                System.err.println("Connection closed");
                closeAll();
                exit(1);
            }

            System.out.println("Server reading request: ");

            //read request, should not be display()
            String request = Util.readRequest(in);

            if (request.trim().isEmpty()) {
                System.err.println("Client sent an empty request.");
                closeAll();
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
                    inputStream = new FileInputStream(new File(fileName));  //read the file sending back to client
                } catch (IOException e) {
                    inputStream = null;
                }

                if (inputStream == null) {  //no such file found on server
                    response = Util.generateHttpResponse(Util.HTTP_NOT_FOUND, command, inputStream);    //404 file not found
                    Util.submit(response, out); //send response to client
                } else {    //file found
                    response = Util.generateHttpResponse(Util.HTTP_200_OK, command, inputStream);   //200 OK
                    Util.submit(response, out); //send response to client
                    inputStream.close();
                }
            } else if (command.equals(Util.HTTP_PUT)) {
                response = Util.generateHttpResponse(Util.HTTP_200_OK, command, null);  //200 OK
                Util.submit(response, out); //send ok response to client

                File file = new File(fileName);
                Util.createFile(file.getPath(), sFile); //save file

            } else {
                System.err.println("Command not supported: " + command);
                closeAll();
                exit(1);
            }

            System.out.println("response: " + response);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            closeAll();
            exit(1);
        }
    }

    private void closeAll() {
        if (this.out != null)
            this.out.close();
        try {
            if (this.in != null)
                this.in.close();
            if (this.socket != null)
                this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
