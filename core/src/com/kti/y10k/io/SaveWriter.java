package com.kti.y10k.io;

import com.kti.y10k.MainLoop;
import com.kti.y10k.utilities.Logger;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class SaveWriter {
    public static int save(String in) {
        if (Files.notExists(Paths.get("save"))) {
            File saveDir = new File("save");
            if(!saveDir.mkdir()) {
                Logger.log(Logger.LogLevel.ERROR, "Could not create save directory");
                return 0;
            }
        }

        Path file0 = Paths.get("save/"+in+".gal");
        Path file1 = Paths.get("save/recent.gal");

        try {
            List<String> c = MainLoop.instance.c.asString();

            if (Files.exists(file0)) Files.delete(file0);
            Files.write(file0, c, Charset.forName("ASCII"), StandardOpenOption.CREATE);

            if(Files.exists(file1)) Files.delete(file1);
            Files.write(file1, c, Charset.forName("ASCII"), StandardOpenOption.CREATE);

            Logger.log(Logger.LogLevel.INFO, "Saved a galaxy of size " + MainLoop.instance.c.size());
            return 1;
        } catch (Exception e) {
            Logger.log(e.getStackTrace());
            return 0;
        }
    }
}
