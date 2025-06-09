package com.drawingapp.drawingapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.JdbcDrawingRepository;
import com.drawingapp.drawingapp.logging.*;

public class HelloApplication extends Application {
    String dbUrl = "jdbc:mysql://localhost:3306/drawingapp_logs?useSSL=false&serverTimezone=UTC";
    String dbUser = "root"; 
    String dbPass = "";  
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        // scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        stage.setTitle("Drawing App");
        stage.setScene(scene);
        HelloController controller = fxmlLoader.getController();
        controller.setShapeManager(new ShapeManager());
        controller.setModeManager(new ModeManager());
        
        // Initialize database connection and repository
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to database with credentials
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            DrawingRepository repository = new JdbcDrawingRepository(connection);
            controller.setDrawingRepository(repository);
            System.out.println("Successfully connected to database");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
        
        controller.postInjectInit();
        LoggerManager.getInstance().setStrategy(new ConsoleLogger());
        LoggerManager.getInstance().log("===> ConsoleLogger entry");
        
        // For database logging:
        try {
            LoggerManager.getInstance().setStrategy(
                new DatabaseLogger(dbUrl, dbUser, dbPass)
            );
        } catch (SQLException e) {
            System.err.println("Logger database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        // For file logging:
        // LoggerManager.getInstance().setStrategy(new FileLogger("log.txt"));
        // LoggerManager.getInstance().log("===> FileLogger test entry");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
