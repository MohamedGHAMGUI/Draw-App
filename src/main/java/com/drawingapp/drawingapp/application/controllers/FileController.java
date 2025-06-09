package com.drawingapp.drawingapp.application.controllers;

import com.drawingapp.drawingapp.application.controllers.interfaces.IFileController;
import com.drawingapp.drawingapp.infrastructure.persistence.DrawingRepository;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.application.services.GraphManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.commands.CommandFactory;
import com.drawingapp.drawingapp.domain.commands.CommandHistory;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;
import java.util.Optional;
import java.util.List;

public class FileController implements IFileController {
    private final DrawingRepository repository;
    private final ShapeManager shapeManager;
    private final GraphManager graphManager;
    private final ILogger logger;
    private final CommandFactory commandFactory;
    private final CommandHistory commandHistory;
    private Stage stage;

    public FileController(DrawingRepository repository, ShapeManager shapeManager, 
                         GraphManager graphManager, ILogger logger,
                         CommandFactory commandFactory, CommandHistory commandHistory) {
        this.repository = repository;
        this.shapeManager = shapeManager;
        this.graphManager = graphManager;
        this.logger = logger;
        this.commandFactory = commandFactory;
        this.commandHistory = commandHistory;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void saveDrawing() {
        if (stage == null) {
            logger.log("Error: Stage not set for file dialog");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Drawing");
        dialog.setHeaderText("Enter a name for your drawing");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                repository.saveDrawing(
                    name,
                    shapeManager.getShapes(),
                    graphManager.getNodes(),
                    graphManager.getEdges()
                );
                logger.log("Drawing saved successfully as: " + name);
            } catch (Exception e) {
                logger.log("Error saving drawing: " + e.getMessage());
            }
        });
    }

    @Override
    public void loadDrawing() {
        if (stage == null) {
            logger.log("Error: Stage not set for file dialog");
            return;
        }

        List<String> savedDrawings = repository.listSavedDrawings();
        if (savedDrawings.isEmpty()) {
            logger.log("No saved drawings found");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(savedDrawings.get(0), savedDrawings);
        dialog.setTitle("Load Drawing");
        dialog.setHeaderText("Select a drawing to load");
        dialog.setContentText("Drawing:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                var drawingData = repository.loadDrawing(name);
                if (drawingData != null) {
                    // Clear current drawing
                    shapeManager.clear();
                    graphManager.clear();
                    commandHistory.clear();

                    // Load shapes
                    if (drawingData.shapes() != null) {
                        for (var shape : drawingData.shapes()) {
                            if (shape != null) {
                                shapeManager.addShape(shape);
                                logger.log("Loaded shape: " + shape.getClass().getSimpleName());
                            } else {
                                logger.warn("Warning: Attempted to add null shape");
                            }
                        }
                    }

                    // Load nodes and edges
                    if (drawingData.nodes() != null) {
                        for (var node : drawingData.nodes()) {
                            if (node != null) {
                                graphManager.addNode(node);
                                logger.log("Loaded node: " + node.getLabel());
                            }
                        }
                    }
                    if (drawingData.edges() != null) {
                        for (var edge : drawingData.edges()) {
                            if (edge != null) {
                                graphManager.addEdge(edge);
                                logger.log("Loaded edge: " + edge.getLabel());
                            }
                        }
                    }

                    logger.log("Drawing loaded successfully: " + name);
                }
            } catch (Exception e) {
                logger.error("Error loading drawing: " + e.getMessage());
            }
        });
    }
} 