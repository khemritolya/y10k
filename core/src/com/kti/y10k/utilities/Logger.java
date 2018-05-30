package com.kti.y10k.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Logger {
    public enum LogLevel {
        INFO("INFO"), DEBUG("DEBUG"),
        WARN("WARN"), ERROR("ERROR");

        public final String name;

        LogLevel(String name) {
            this.name = name;
        }
    }

    private static ArrayList<String> log;
    private static Calendar calendar;
    private static String startTime;
    private static BufferedWriter out;

    static {
        log = new ArrayList<>();
        calendar = Calendar.getInstance();
        startTime = new SimpleDateFormat("HH-mm-ss-SS").format(calendar.getTime());
        try {
            out = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 256);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Thread currThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log(LogLevel.INFO, "Dumping Logs...");
            dump();
            try {
                currThread.join();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }));
    }

    public static void log(LogLevel level, String msg) {
        String logMessage = String.format("[%s]\t %s", level.name, msg);
        log.add(logMessage);
        try {
            out.write(logMessage + "\n");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(StackTraceElement[] err) {
        for (StackTraceElement e:err) {
            Logger.log(LogLevel.ERROR, e.toString());
        }
    }

    private static void dump() {
        if (Files.notExists(Paths.get("logs"))) {
            File logsDir = new File("logs");
            if(!logsDir.mkdir()) {
                System.err.println("Critical error cannot dump logs");
            }
        }

        Path file = Paths.get("logs/y10k-" + startTime +".log");
        Path recent = Paths.get("logs/y10k-recent.log");

        try {
            if (Files.exists(recent)) Files.delete(recent);
            Files.write(recent, log, Charset.forName("ASCII"), StandardOpenOption.CREATE);
            Files.write(file, log, Charset.forName("ASCII"), StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
