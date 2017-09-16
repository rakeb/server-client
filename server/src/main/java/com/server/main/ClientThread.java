package com.server.main;

import com.serverclient.util.HttpRequestParser;
import com.serverclient.util.Util;

import java.io.*;
import java.net.Socket;
import java.net.URL;

import static java.lang.System.exit;
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
            String fileName = httpRequestParser.getFileName();
            String file = httpRequestParser.getFile();

            String response;

            if (command.equals(Util.HTTP_GET)) {
                InputStream inputStream;
                inputStream = getClass().getResourceAsStream("files/" + fileName);

                if (inputStream == null) {  //no such file found on server
                    response = Util.generateHttpResponse(Util.HTTP_NOT_FOUND);
                    Util.submit(response, out);
                } else {    //file found
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    response = Util.generateHttpResponse(Util.HTTP_200_OK);
                    Util.submit(response, out); //send ok response to client
                    Util.submit(reader, out);   //send data to client
                }
            } else if (command.equals(Util.HTTP_PUT)) {
//                URL filePathUrl = this.getClass().getResource("files");
//                String filePath = filePathUrl.getPath() + "/" + fileName;

                response = Util.generateHttpResponse(Util.HTTP_200_OK_FILE_CREATED);
                Util.submit(response, out); //send ok response to client
//                fileName.split()
                PrintWriter writer = new PrintWriter("test.html", "UTF-8");
                writer.println("test file created");
                writer.close();

                bufferedReader = new BufferedReader(new FileReader(new File("test.html")));
                System.out.println(bufferedReader.readLine());;
//                Util.createFile(fileName, file);

            } else {
                System.err.println("Command not supported: " + command);
                exit(1);
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            } catch (Exception e) {

            }
        }
    }
}
