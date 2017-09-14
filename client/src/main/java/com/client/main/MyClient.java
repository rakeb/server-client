package com.client.main;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mislam7 on 9/13/17.
 */
public class MyClient {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Provide 4 arguments separated by space as following: HostName Port Command FileName");
            System.exit(1);
        }

        System.out.println("Starting MyClient...");

        System.out.println("args 1 is: " + args[0]);
        System.out.println("args 2 is: " + args[1]);
        System.out.println("args 3 is: " + args[2]);
        System.out.println("args 4 is: " + args[3]);

        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        String command = args[2];
        String fileName = args[3];

        try {
            URL url = new URL("http", hostName, port, "/" + fileName);
//            URL url = new URL("https://www.amazon.com/");
//            URL url = new URL("https://www.google.com:80/index.html");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(command);
//            connection.setRequestProperty();

            System.out.println("Status code: " + connection.getResponseCode());

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed: HTTP error code: " +
                        connection.getResponseCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output;

            System.out.println("Server Response: ");
            while ((output = bufferedReader.readLine()) != null) {
                System.out.println(output);
            }

            bufferedReader.close();
            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
