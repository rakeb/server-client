package com.serverclient.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.System.exit;

/**
 * Created by mislam7 on 9/14/17.
 */
public class Util {
    public static String HTTP_GET = "GET";
    public static String HTTP_PUT = "PUT";
    public static String HTTP_200_OK = "200 OK";
    public static String HTTP_NOT_FOUND = "404 Not Found";

    /**
     * Display whatever in buffer has
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
                    System.out.println(output);
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
                    "\r\n";
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
                        "\r\n";

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

    public static String generateHttpResponse(String status, String command, InputStream inputStream) {
        String response = "";
        try {
            response = "HTTP/1.1 " + status + "\r\n" +
                    "Date: " + getDateHeader() + "\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Cache-Control: private, max-age=0\r\n";

            if (status.equals(HTTP_NOT_FOUND)) {
                response += "Content-Length: 23\r\n" +
                        "\r\n";
                String body = "Error 404 (Not Found)!!\r\n"; //length 23
                response += body;
            } else {
                if (command.equals(HTTP_GET)) {
                    response += "Content-Length: " + (inputStream.available() + 1) + "\r\n" +
                            "\r\n";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response += line + "\r\n";
                    }
                    reader.close();
                } else {
                    response += "Content-Length: 13\r\n" +
                            "\r\n";
                    String body = "File Created!\r\n"; //length 13
                    response += body;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        response += "\r\n";
        return response;
    }

    public static String getDateHeader() {
        SimpleDateFormat format;
        String ret;

        format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("EST"));
        ret = "Date: " + format.format(new Date()) + " EST";

        return ret;
    }

    public static void submit(Object userInput, PrintWriter out) {
        if (userInput instanceof BufferedReader) {
            out.println(socketReader((BufferedReader) userInput, false));
        } else {
            out.println(userInput);
        }
    }

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
