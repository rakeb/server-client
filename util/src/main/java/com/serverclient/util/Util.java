package com.serverclient.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by mislam7 on 9/14/17.
 */
public class Util {
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
}
