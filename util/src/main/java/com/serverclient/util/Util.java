package com.serverclient.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mislam7 on 9/14/17.
 */
public class Util {
    public static String HTTP_GET = "GET";
    public static String HTTP_PUT = "PUT";
    public static String HTTP_200_OK = "200 OK";
    //    public static String HTTP_200_OK_FILE_CREATED = "200 OK File Created";
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
                    "Cache-Control: private, max-age=0\r\n"
            ;
//                    "Content-Length: " + inputStream == null ? "25" : inputStream.available() + "\r\n" +
//                    "\r\n";

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
//        HTTP/1.1 200 OK
//        Date: Mon, 18 Sep 2017 01:16:54 GMT
//        Expires: -1
//        Cache-Control: private, max-age=0
//        Content-Type: text/html; charset=ISO-8859-1
//        Content-Encoding: gzip
//        Server: gws
//        Content-Length: 4529
//        X-XSS-Protection: 1; mode=block
//        X-Frame-Options: SAMEORIGIN
//        /r/n
//        Body


//        HTTP/1.1 404 Not Found\r\n
//                [Expert Info (Chat/Sequence): HTTP/1.1 404 Not Found\r\n]
//        Content-Type: text/html; charset=UTF-8\r\n
//        Referrer-Policy: no-referrer\r\n
//        Content-Length: 1572\r\n
//        Date: Mon, 18 Sep 2017 01:20:40 GMT\r\n
//    \r\n
//                <!DOCTYPE html>\n
//                <html lang=en>\n
//                <meta charset=utf-8>\n
//                <meta name=viewport content="initial-scale=1, minimum-scale=1, width=device-width">\n
//                <title>Error 404 (Not Found)!!1</title>\n
//                <style>\n
//                [truncated]    *{margin:0;padding:0}html,code{font:15px/22px arial,sans-serif}html{background:#fff;color:#222;padding:15px}body{margin:7% auto 0;max-width:390px;min-height:180px;padding:30px 0 15px}* > body{background:url(//www.google.com
//                </style>\n
//                <a href=//www.google.com/><span id=logo aria-label=Google></span></a>\n
//      <p><b>404.</b> <ins>That\342\200\231s an error.</ins>\n
//                    <p>The requested URL <code>/index1.html</code> was not found on this server.  <ins>That\342\200\231s all we know.</ins>\n
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

//    public static void submitRequest(Object userInput, PrintWriter out) {
//        submit(userInput, out);
//    }
//
//
//    public static void submitResponse(Object userInput, PrintWriter out) {
//        submit(userInput, out);
//    }

    public static void submit(Object userInput, PrintWriter out) {
        if (out== null) {
            System.err.println("Connection closed");
        }
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
