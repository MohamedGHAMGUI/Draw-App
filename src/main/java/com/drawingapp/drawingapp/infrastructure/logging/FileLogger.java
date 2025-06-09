package com.drawingapp.drawingapp.infrastructure.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements ILogger {
    private final String logFile;
    private final DateTimeFormatter formatter;

    public FileLogger(String logFile) {
        this.logFile = logFile;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void log(String message) {
        writeLog("INFO", message);
    }

    @Override
    public void error(String message) {
        writeLog("ERROR", message);
    }

    @Override
    public void warn(String message) {
        writeLog("WARN", message);
    }

    private void writeLog(String level, String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.println(String.format("[%s] [%s] %s", timestamp, level, message));
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}
    
    

