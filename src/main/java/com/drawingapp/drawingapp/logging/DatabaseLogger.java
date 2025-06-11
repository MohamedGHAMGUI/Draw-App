package com.drawingapp.drawingapp.logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseLogger implements LoggerStrategy {
    private Connection conn;

    public DatabaseLogger(String dbUrl, String user, String pass) throws SQLException {
        this.conn = DatabaseConnection.getInstance(dbUrl, user, pass).getConnection();
    }

    @Override
    public void log(String message) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO logs (message) VALUES (?)")) {
            ps.setString(1, message);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
