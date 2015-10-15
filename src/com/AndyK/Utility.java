package com.AndyK;


import javax.script.ScriptException;
import java.util.UUID;

/**
 * Created by Andy on 10/3/2015.
 */
public class Utility {

    public static String temp;

    public static String prep(String in) {
        //outputs
        in = in.replace("(UUID)", UUID.randomUUID().toString());
        in = in.replace("(Port)", Integer.toString(Main.port));
        in = in.replace("(DataDir)", System.getProperty("user.dir") + Main.slash + Main.datadir);
        in = in.replace("(ServerOS)", System.getProperty("os.name"));

        while (in.contains("(Script)") && in.contains("(/Script)")) {
            System.out.println("Processing script...");
            temp = in.substring(in.indexOf("(Script)") + 8);
            temp = temp.substring(0, temp.indexOf("(/Script)"));

            try {
                in = in.replace("(Script)" + temp + "(/Script)", Main.engine.eval(temp).toString());
            } catch (ScriptException e) {
                in = in.replace("(Script)" + temp + "(/Script)", "Eval:" + e.getMessage());
                System.err.println(e);
            }

        }


        return in;
    }
}
