package com.serverclient.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by mislam7 on 9/14/17.
 */
public class Util {
    public static String HTTP_GET = "GET";
    public static String HTTP_PUT = "PUT";
    public static String HTTP_200_OK = "200 OK";
    public static String HTTP_NOT_FOUND = "404 Not Found";

    public static String display(BufferedReader in) {
        String output;
        String results = "";
        System.out.println("Displaying: ");
        try {
            while (((output = in.readLine()) != null)) {
                System.out.println("echo: " + output);
                results += output;
                if (output.trim().isEmpty()) {
                    if (!in.ready()) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static String generateHttpRequest(String command, String fileName, String hostName) {
        String request;
        if (!command.equals("GET") && !command.equals("PUT")) {
            throw new RuntimeException("HTTP Command not supported: " + command);
        }

        request = command + " /" + fileName + " HTTP/1.1\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
                "Host: " + hostName + "\n" +
//                "Accept: */*\n" +
                "Accept-Language: en-us\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: Keep-Alive" +
                "\n\r";

        return request;
    }

    public static String generateHttpResponse(String status) {
        String response;

        response = "HTTP/1.1 " + status + "\n\r";


        if (status.equals(HTTP_NOT_FOUND)) {
            String body = " 404 Not Found \n\r";
            response += body;
        }

        return response;
    }

    public static void submit(Object userInput, PrintWriter out) {
        if (userInput instanceof BufferedReader) {
            out.println(display((BufferedReader) userInput));
        } else {
            out.println(userInput);
        }

    }
}
