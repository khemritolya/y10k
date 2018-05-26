package com.kti.y10k.io;

import com.badlogic.gdx.math.Vector2;
import com.kti.y10k.MainLoop;
import com.kti.y10k.universe.Galaxy;
import com.kti.y10k.universe.Sector;
import com.kti.y10k.universe.Star;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.GalaxyConstManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SaveLoader {
    public static int load(String name) {
        if (Files.notExists(Paths.get("save"))) {
            Logger.log(Logger.LogLevel.ERROR, "Could not find save directory");
            return 0;
        }

        Path file = Paths.get("save/"+name+".gal");

        try {
            if (!Files.exists(file)) {
                Logger.log(Logger.LogLevel.ERROR, "File " + file.toString() + " not found!");
                throw new IOException("File " + file.toString() + " not found!");
            }

            float sq3 = (float) Math.sqrt(3);

            Scanner s = new Scanner(file.toFile());
            List<Star> stars = new ArrayList<>();
            List<Sector> sectors = new ArrayList<>();

            float galacticRadius = GalaxyConstManager.requestConstant("galaxy_radius");
            float conv_fac = GalaxyConstManager.requestConstant("sector_to_galrad");
            float rat = galacticRadius / conv_fac;

            while (s.hasNextLine()) {
                StringTokenizer st = new StringTokenizer(s.nextLine());
                String f = st.nextToken();
                if (f.endsWith("Star")) {
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
                } else if (f.endsWith("Sector")) {
                    String n = st.nextToken() + " " + st.nextToken();
                    float x = Float.parseFloat(st.nextToken());
                    float z = Float.parseFloat(st.nextToken());

                    float[] vertices = {
                            x - 0.5f * rat, z + sq3 / 2  * rat,
                            x -  rat, z,
                            x - 0.5f * rat, z - sq3 / 2 * rat,
                            x + 0.5f * rat, z - sq3 / 2 * rat,
                            x + rat, z,
                            x + 0.5f * rat, z + sq3 / 2  * rat
                    };

                    sectors.add(new Sector(n, x, z, vertices));
                } else {
                    Logger.log(Logger.LogLevel.WARN, "Unknown Prefix in Save File!");
                }
            }

            Logger.log(Logger.LogLevel.DEBUG, "Loaded a galaxy of size " + stars.size());

            MainLoop.instance.c.set(stars, sectors);

            return 1;
        } catch (Exception e) {
            Logger.log(e.getStackTrace());
            return 0;
        }
    }
}
