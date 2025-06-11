package com.drawingapp.drawingapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.JdbcDrawingRepository;
import com.drawingapp.drawingapp.logging.*;
import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_graph.GraphNode;
import com.drawingapp.drawingapp.shapes_graph.GraphEdge;

public class HelloApplication extends Application {
    String dbUrl = "jdbc:mysql://localhost:3306/drawingapp_logs?useSSL=false&serverTimezone=UTC";
    String dbUser = "root"; 
    String dbPass = "";  
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);
        stage.setTitle("Projet JAVA - Application de Dessin");
        stage.setScene(scene);
        HelloController controller = fxmlLoader.getController();
        controller.setShapeManager(new ShapeManager());
        controller.setModeManager(new ModeManager());
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/images/icon.png")));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            DrawingRepository repository = new JdbcDrawingRepository(connection);
            controller.setDrawingRepository(repository);
            System.out.println("Successfully connected to database");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();

            controller.setDrawingRepository(new DrawingRepository() {
                @Override
                public void saveDrawing(String name, List<Shape> shapes, List<GraphNode> nodes, List<GraphEdge> edges) {
                    System.err.println("Cannot save drawing - database connection failed");
                }

                @Override
                public DrawingData loadDrawing(String name) {
                    System.err.println("Cannot load drawing - database connection failed");
                    return null;
                }

                @Override
                public List<String> listSavedDrawings() {
                    System.err.println("Cannot list drawings - database connection failed");
                    return new ArrayList<>();
                }

                @Override
                public void deleteDrawing(String name) {
                    System.err.println("Cannot delete drawing - database connection failed");
                }
            });
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            // Create a default repository that does nothing
            controller.setDrawingRepository(new DrawingRepository() {
                @Override
                public void saveDrawing(String name, List<Shape> shapes, List<GraphNode> nodes, List<GraphEdge> edges) {
                    System.err.println("Cannot save drawing - MySQL driver not found");
                }

                @Override
                public DrawingData loadDrawing(String name) {
                    System.err.println("Cannot load drawing - MySQL driver not found");
                    return null;
                }

                @Override
                public List<String> listSavedDrawings() {
                    System.err.println("Cannot list drawings - MySQL driver not found");
                    return new ArrayList<>();
                }

                @Override
                public void deleteDrawing(String name) {
                    System.err.println("Cannot delete drawing - MySQL driver not found");
                }
            });
        }
        
        controller.postInjectInit();
        
        // Initialize composite logger
        CompositeLogger compositeLogger = new CompositeLogger();
        compositeLogger.addLogger(new ConsoleLogger());
        
        // Add database logger if available
        try {
            compositeLogger.addLogger(new DatabaseLogger(dbUrl, dbUser, dbPass));
        } catch (SQLException e) {
            System.err.println("Logger database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Add file logger
        compositeLogger.addLogger(new FileLogger("log.txt"));
        
        // Set the composite logger as the strategy
        LoggerManager.getInstance().setStrategy(compositeLogger);
        LoggerManager.getInstance().log("Application started - logging to console, file, and database (if available)");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
