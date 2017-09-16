package com.serverclient.util;

/**
 * Created by mislam7 on 9/15/17.
 */
public class HttpRequestParser {
    private String request;
    private String fileName;
    private String file;
    private String command;
    private boolean fileStarted;

    public HttpRequestParser(String request) {
        this.request = request;
        String[] parsed = request.split("\n");

        String[] header = parsed[0].split(" ");
        command = header[0];
        fileName = header[1];

        //Setting the file
        fileStarted = false;
        file = "";
        int i = 0;
        for (String element :
                parsed) {
            i++;
            if (!fileStarted && element.equals("")) {
                fileStarted = true;
                continue;
            }
            if (fileStarted) {
                if (element.equals("")) {
                    element = "\n";
                }
                file += element;
            }
        }
    }

    public String getRequest() {
        return request;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFile() {
        return file;
    }

    public String getCommand() {
        return command;
    }

    public boolean isFileStarted() {
        return fileStarted;
    }
}
