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
    opens com.drawingapp.drawingapp.domain.commands to com.google.gson;
    opens com.drawingapp.drawingapp.domain.shapes to com.google.gson;
    opens com.drawingapp.drawingapp.domain.graph to com.google.gson;
    opens com.drawingapp.drawingapp.domain.algorithms to com.google.gson;

    exports com.drawingapp.drawingapp;
    exports com.drawingapp.drawingapp.domain.commands;
    exports com.drawingapp.drawingapp.domain.shapes;
    exports com.drawingapp.drawingapp.domain.graph;
    exports com.drawingapp.drawingapp.domain.algorithms;
    exports com.drawingapp.drawingapp.application.services;
    exports com.drawingapp.drawingapp.application.controllers;
    exports com.drawingapp.drawingapp.application.controllers.interfaces;
    exports com.drawingapp.drawingapp.infrastructure.persistence;
    exports com.drawingapp.drawingapp.infrastructure.logging;
    exports com.drawingapp.drawingapp.infrastructure.serialization;
    exports com.drawingapp.drawingapp.infrastructure.visualization;
}