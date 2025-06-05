package com.drawingapp.drawingapp.logging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection(String dbUrl, String user, String pass) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, user, pass);
    }

    public static DatabaseConnection getInstance(String dbUrl, String user, String pass) throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection(dbUrl, user, pass);
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
