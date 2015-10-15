package com.AndyK;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.Scanner;


public class Main {

    public static int port;
    public static String datadir;
    public static String fileext;
    public static boolean configcreated = false;
    public static boolean foldercreated = false;
    public static String slash = "\\";

    public static ScriptEngineManager mgr = new ScriptEngineManager();
    public static ScriptEngine engine = mgr.getEngineByName("JavaScript");

    public static void main(String[] args) {


        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println("OS = " + System.getProperty("os.name"));

        if (!System.getProperty("os.name").startsWith("Windows")) {

            slash = "/";

        }

        File file4 = new File(System.getProperty("user.dir") + "/config.txt");
        if (!file4.exists()) {
            Config.createcfg();
        }
        if ((Config.getvalue("port") != null)) {
            port = Integer.parseInt(Config.getvalue("port"));
        } else {
            port = 80;
        }
        datadir = Config.getvalue("datadir");
        fileext = Config.getvalue("fileext");

        try {

            File file2 = new File(System.getProperty("user.dir") + slash + datadir);
            File file3 = new File(System.getProperty("user.dir") + slash + datadir + slash + "index." + fileext);
            if (!file2.exists()) {
                if (file2.mkdir()) {
                    file3.createNewFile();
                    FileWriter fw = new FileWriter(file3);
                    BufferedWriter out = new BufferedWriter(fw);

                    out.write("<h1>It works!</h1><br><p>access html documents at: (DataDir). Have an UUID: (UUID).</p>");
                    out.flush();
                    out.close();
                    System.out.println("data directory created!");
                    foldercreated = true;
                } else {
                    System.out.println("failed to create directory!");
                }
            }

            if (foldercreated && configcreated) {
                System.out.println("First launch detected");
            }


            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new MyHandler());

            System.out.println("Initialisation looks good so far");

            System.out.println("");

            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception io) {
            System.out.println(io);
        }
    }


    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) {
            try {
                String file = t.getRequestURI().toString();
                if (file.endsWith("/")) {
                    file = file + "index." + fileext;
                }
                System.out.println("new request: " + file + " | " + t.getRemoteAddress());
                int errorcode = 200;

                file = file.replace("/", slash);
                file = System.getProperty("user.dir") + slash + datadir + slash + file;


                File filetype = new File(file);

                String response = null;


                System.out.println(filetype);


                if (filetype.exists() && !filetype.isDirectory()) {
                    try {
                        if (new Scanner(filetype).useDelimiter("\\A").hasNext()) {
                            response = new Scanner(filetype).useDelimiter("\\A").next();
                        } else {
                            response = String.valueOf(new Scanner(filetype).useDelimiter("\\A").toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    response = Utility.prep(response);
                    System.out.println("response sent to: " + t.getRemoteAddress());
                } else {
                    response = "<h1>Error 404: file not found</h1>";
                    System.out.println("Error 404");
                    errorcode = 404;
                }
                t.sendResponseHeaders(errorcode, response.length());
                OutputStream os = t.getResponseBody();
                System.out.println("");
                new Scanner(filetype).close();
                os.write(response.getBytes());
                os.flush();
                os.close();
            } catch (IOException io) {
                System.out.println(io);
            }
        }
    }
}

