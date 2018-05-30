package com.kti.y10k.utilities.managers;

import com.badlogic.gdx.graphics.Texture;
import com.kti.y10k.utilities.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AssetManager {
    private static HashMap<String, Texture> texmap;

    public static int init() {
        Logger.log(Logger.LogLevel.INFO, "Loading assets...");
        texmap = new HashMap<>();

        try {
            Scanner reader = new Scanner(new File("assets/assetlist.txt"));
            while (reader.hasNext()) {
                String line = reader.nextLine();
                if (!line.startsWith("#") && !(line.length() == 0)) {
                    StringTokenizer st = new StringTokenizer(line);
                    texmap.put(st.nextToken(), new Texture("assets/" + st.nextToken()));
                }
            }

        } catch (IOException e) {
            Logger.log(Logger.LogLevel.ERROR,"Service critical error in assetload");
            Logger.log(e.getStackTrace());

            return 0;
        }

        return 1;
    }

    public static void dispose() {
        for (String s:texmap.keySet()) {
            texmap.get(s).dispose();
        }
    }

    public static Texture requestTexture(String id) {
        if (texmap.get(id) == null) {
            Logger.log(Logger.LogLevel.WARN, "Unable to find asset " + id);
            return requestTexture("not-found");
        }

        return texmap.get(id);
    }
}
