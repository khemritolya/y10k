package com.kti.y10k.io;

import com.kti.y10k.MainLoop;
import com.kti.y10k.universe.Star;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.WindowManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SaveLoader {
    public static int load() {
        if (Files.notExists(Paths.get("save"))) {
            Logger.log(Logger.LogLevel.ERROR, "Could not find save directory");
            return 0;
        }

        Integer id = WindowManager.newTextInput("Galaxy name?", "MilkyWay", 0.45f, 0.45f);

        while (!WindowManager.hasOutput(id)) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                Logger.log(e.getStackTrace());
            }
        }

        String name = WindowManager.requestOutput(id);
        Path file = Paths.get("save/"+name+".gal");

        return loadStars(file);
    }

    public static int load(String name) {
        if (Files.notExists(Paths.get("save"))) {
            Logger.log(Logger.LogLevel.ERROR, "Could not find save directory");
            return 0;
        }

        Path file = Paths.get("save/"+name+".gal");

        return loadStars(file);
    }

    private static int loadStars(Path file) {
        try {
            if (!Files.exists(file)) {
                Logger.log(Logger.LogLevel.ERROR, "File " + file.toString() + " not found!");
                throw new IOException("File " + file.toString() + " not found!");
            }

            Scanner s = new Scanner(file.toFile());
            List<Star> stars = new ArrayList<>();

            while (s.hasNextLine()) {
                StringTokenizer st = new StringTokenizer(s.nextLine());
                if (st.nextToken().endsWith("Star")) {
                    float x = Float.parseFloat(st.nextToken());
                    float y = Float.parseFloat(st.nextToken());
                    float z = Float.parseFloat(st.nextToken());
                    float sizeMod = Float.parseFloat(st.nextToken());

                    if (Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z) || Float.isNaN(sizeMod)) {
                        Logger.log(Logger.LogLevel.WARN, "Could not load a star, posn was nan!");
                    } else {
                        Star star = new Star(x,y,z,sizeMod);
                        stars.add(star);
                    }
                } else {
                    Logger.log(Logger.LogLevel.WARN, "Unknown Prefix in Save File!");
                }
            }

            MainLoop.instance.c.set(stars);

            return 1;
        } catch (Exception e) {
            Logger.log(e.getStackTrace());
            return 0;
        }
    }
}
