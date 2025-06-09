module com.drawingapp.drawingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign;
    requires org.kordamp.bootstrapfx.core;

    opens com.drawingapp.drawingapp to javafx.fxml;
    opens com.drawingapp.drawingapp.services to com.google.gson;
    exports com.drawingapp.drawingapp;
    exports com.drawingapp.drawingapp.services;
}