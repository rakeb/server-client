package com.serverclient.util;

import java.io.*;

/**
 * Created by mislam7 on 9/14/17.
 */
public class Util {
    public static String HTTP_GET = "GET";
    public static String HTTP_PUT = "PUT";
    public static String HTTP_200_OK = "200 OK";
    public static String HTTP_200_OK_FILE_CREATED = "200 OK File Created";
    public static String HTTP_NOT_FOUND = "404 Not Found";

    /**
     * Display whatever in has
     *
     * @param in The {@link BufferedReader} which will be displayed
     */
    public static void display(BufferedReader in) {
        socketReader(in, true);
    }

    public static String readRequest(BufferedReader in) {
        return socketReader(in, false);
    }

    public static String socketReader(BufferedReader in, boolean isPrint) {
        String output;
        String results = "";
        try {
            while (((output = in.readLine()) != null)) {
                if (isPrint) {
                    System.out.println("echo: " + output);
                }
                results += output + "\n";
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

    public static String generateHttpRequest(String command, String fileName, String hostName, InputStream inputStream) {
        String request = "";
        if (command.equals("GET")) {
            request = command + " /" + fileName + " HTTP/1.1\r\n" +
                    "cache-control: no-cache\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Host: " + hostName + "\r\n" +
                    "Accept: */*\r\n" +
                    "Accept-Language: en-us\r\n" +
                    "Connection: Keep-Alive\r\n" +
                    "\n\r";
        } else if (command.equals("PUT")) {
            try {
                request = command + " /" + fileName + " HTTP/1.1\r\n" +
                        "cache-control: no-cache\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Host: " + hostName + "\r\n" +
                        "Accept: */*\r\n" +
                        "Accept-Language: en-us\r\n" +
                        "content-length: " + inputStream.available() + "\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\n\r";

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    request += line + "\n";
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            request += "\r\n";
        } else {
            throw new RuntimeException("HTTP Command not supported: " + command);
        }
        return request;
    }

    public static String generateHttpResponse(String status) {
        String response;

        response = "HTTP/1.1 " + status + "\n\n\r";


        if (status.equals(HTTP_NOT_FOUND)) {
            String body = " 404 Not Found \n\n\r";
            response += body;
        }

        return response;
    }

//    public static void submitRequest(Object userInput, PrintWriter out) {
//        submit(userInput, out);
//    }
//
//
//    public static void submitResponse(Object userInput, PrintWriter out) {
//        submit(userInput, out);
//    }

    public static void submit(Object userInput, PrintWriter out) {
        if (userInput instanceof BufferedReader) {
            out.println(socketReader((BufferedReader) userInput, false));
        } else {
            out.println(userInput);
        }
    }

//    public static void createFile(String filePath, BufferedReader in) {
//        try {
//            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
//            String output;
//
//            while ((output = in.readLine()) != null) {
//                writer.println(output);
//                if (output.trim().isEmpty() && !in.ready()) {
//                    break;
//                }
//            }
//
//            writer.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void createFile(String filePath, String file) {
        try {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            String[] lines = file.split("\n");

            for (String line :
                    lines) {
                writer.println(line);
            }
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
