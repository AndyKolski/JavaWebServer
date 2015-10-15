package com.AndyK;

import java.io.*;
import java.util.Properties;

/**
 * Created by Andy on 10/2/2015.
 */
public class Config {
    public static String getvalue(String valtoget) {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.txt");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            return prop.getProperty(valtoget);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void createcfg() {

        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("config.txt");

            // set the properties value
            prop.setProperty("port", String.valueOf(80));
            prop.setProperty("datadir", "html");
            prop.setProperty("fileext", "html");

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                System.out.println("config file generated");
                Main.configcreated = true;
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
