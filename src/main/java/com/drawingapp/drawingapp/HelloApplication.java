package com.drawingapp.drawingapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.logging.*;

public class HelloApplication extends Application {
    String dbUrl = "jdbc:mysql://localhost:3306/drawingapp_logs?useSSL=false&serverTimezone=UTC";
    String dbUser = "root"; 
    String dbPass = "";  
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
// Wider to fit sidebar + canvas
        stage.setTitle("Drawing App");
        stage.setScene(scene);
        HelloController controller = fxmlLoader.getController();
        controller.setShapeManager(new ShapeManager());
        controller.setModeManager(new ModeManager());
        controller.postInjectInit();
        LoggerManager.getInstance().setStrategy(new ConsoleLogger());
        LoggerManager.getInstance().log("===> ConsoleLogger  entry");
        
        

        // For file logging:
   
        
        
        // For database logging:
         

        try {
            LoggerManager.getInstance().setStrategy(
                new DatabaseLogger(dbUrl, dbUser, dbPass)
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LoggerManager.getInstance().setStrategy(new FileLogger("log.txt"));
        LoggerManager.getInstance().log("===> FileLogger test entry");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
