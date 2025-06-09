package com.drawingapp.drawingapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import com.drawingapp.drawingapp.infrastructure.persistence.DrawingRepository;
import com.drawingapp.drawingapp.infrastructure.persistence.JdbcDrawingRepository;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.infrastructure.logging.DatabaseLogger;
import com.drawingapp.drawingapp.infrastructure.logging.FileLogger;
import com.drawingapp.drawingapp.infrastructure.logging.ConsoleLogger;
import com.drawingapp.drawingapp.infrastructure.logging.CompositeLogger;

public class HelloApplication extends Application {
    String dbUrl = "jdbc:mysql://localhost:3306/drawingapp_logs?useSSL=false&serverTimezone=UTC";
    String dbUser = "root"; 
    String dbPass = "";  
    private ILogger logger;

    public ILogger getLogger() {
        return logger;
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        // Initialize loggers
        String logFile = "drawing_app.log";
        ILogger fileLogger = new FileLogger(logFile);
        ILogger dbLogger = new DatabaseLogger(dbUrl, dbUser, dbPass, logFile);
        ILogger consoleLogger = new ConsoleLogger();
        this.logger = new CompositeLogger(fileLogger, dbLogger, consoleLogger);
        
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        // scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        stage.setTitle("Drawing App");
        stage.setScene(scene);
        HelloController controller = fxmlLoader.getController();
        
        // Pass the logger to the controller
        controller.setLogger(logger);
        
        // Initialize database connection and repository
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Initialize repository with database credentials
            DrawingRepository repository = new JdbcDrawingRepository(dbUrl, dbUser, dbPass);
            controller.setDrawingRepository(repository);
            logger.log("Successfully connected to database");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
        
        controller.setStage(stage);
        controller.postInjectInit();
        stage.show();
    }

    @Override
    public void stop() {
        if (logger instanceof CompositeLogger) {
            ((CompositeLogger) logger).close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
