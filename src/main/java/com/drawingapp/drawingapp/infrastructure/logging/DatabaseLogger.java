package com.drawingapp.drawingapp.infrastructure.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseLogger implements ILogger {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPass;
    private final String logFile;
    private final PrintWriter fileWriter;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DatabaseLogger(String dbUrl, String dbUser, String dbPass, String logFile) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.logFile = logFile;
        
        try {
            this.fileWriter = new PrintWriter(new FileWriter(logFile, true));
            createLogTable();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize logger: " + e.getMessage(), e);
        }
    }

    private void createLogTable() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
            String sql = "CREATE TABLE IF NOT EXISTS app_logs (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "timestamp DATETIME," +
                "level VARCHAR(10)," +
                "message TEXT" +
                ")";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.execute();
            }
        } catch (SQLException e) {
            System.err.println("Failed to create log table: " + e.getMessage());
        }
    }

    @Override
    public void log(String message) {
        logToDatabase("INFO", message);
        logToFile("INFO", message);
    }

    @Override
    public void error(String message) {
        logToDatabase("ERROR", message);
        logToFile("ERROR", message);
    }

    @Override
    public void warn(String message) {
        logToDatabase("WARN", message);
        logToFile("WARN", message);
    }

    private void logToDatabase(String level, String message) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
            String sql = "INSERT INTO app_logs (timestamp, level, message) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, LocalDateTime.now().format(formatter));
                pstmt.setString(2, level);
                pstmt.setString(3, message);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Failed to log to database: " + e.getMessage());
        }
    }

    private void logToFile(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        fileWriter.println(logMessage);
        fileWriter.flush();
    }

    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
