package com.kti.y10k.utilities.managers;

import com.kti.y10k.utilities.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SettingsManager {
    private static HashMap<String, String> settings;

    public static int init() {
        Logger.log(Logger.LogLevel.INFO, "Loading settings consts...");
        settings = new HashMap<>();

        try {
            Scanner reader = new Scanner(new File("settings.txt"));
            while (reader.hasNext()) {
                String line = reader.nextLine();
                if (!line.startsWith("#") && !(line.length() == 0)) {
                    StringTokenizer st = new StringTokenizer(line);
                    settings.put(st.nextToken(), st.nextToken());
                }
            }

        } catch (Exception e) {
            Logger.log(Logger.LogLevel.ERROR,"Service critical error in settings load");
            Logger.log(e.getStackTrace());

            return 0;
        }

        return 1;
    }

    public static String requestSetting(String id) {
        if (settings.get(id) == null) Logger.log(Logger.LogLevel.WARN, "Unable to find setting " + id);

        return settings.get(id);
    }
}
