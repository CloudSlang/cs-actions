package io.cloudslang.content.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Logger {

    private static final SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
    private static final String LOG_FORMAT = "[%s][#%d][%s:%d]: %s";
    private static final Path LOG_FILE_PATH = Paths.get("C:\\tmp\\powershell_script_action_" + UUID.randomUUID().toString() + ".log");

    private final Class clazz;

    static {
        try {
            if (!Files.exists(LOG_FILE_PATH)) {
                Files.createDirectories(LOG_FILE_PATH.getParent());
                Files.createFile(LOG_FILE_PATH);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Logger(Class clazz) {
        this.clazz = clazz;
    }


    public static Logger getLogger(Class clazz) {
        return new Logger(clazz);
    }


    public void log(String message) {
        String timeStamp = TIME_STAMP_FORMAT.format(new Date());
        long threadId = Thread.currentThread().getId();
        String className = clazz.getSimpleName();
        int lineOfCode = Thread.currentThread().getStackTrace()[2].getLineNumber();

        String str = String.format(LOG_FORMAT, timeStamp, threadId, className, lineOfCode, message);
        try (FileWriter fw = new FileWriter(LOG_FILE_PATH.toString(), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
