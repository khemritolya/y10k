package com.kti.y10k.utilities.managers;

import com.kti.y10k.utilities.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GalaxyConstManager {
    private static HashMap<String, Float> vals;

    public static int init() {
        Logger.log(Logger.LogLevel.INFO, "Loading galaxy consts...");
        vals = new HashMap<>();

        try {
            Scanner reader = new Scanner(new File("assets/predefs/galconsts.txt"));
            while (reader.hasNext()) {
                String line = reader.nextLine();
                if (!line.startsWith("#") && !(line.length() == 0)) {
                    StringTokenizer st = new StringTokenizer(line);
                    vals.put(st.nextToken(), Float.parseFloat(st.nextToken()));
                }
            }

        } catch (Exception e) {
            Logger.log(Logger.LogLevel.ERROR,"Service critical error in assetload");
            Logger.log(e.getStackTrace());

            return 0;
        }

        return 1;
    }

    public static float requestConstant(String id) {
        if (vals.get(id) == null) Logger.log(Logger.LogLevel.WARN, "Unable to find constant " + id);

        return vals.get(id);
    }
}
