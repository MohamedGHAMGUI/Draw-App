package com.drawingapp.drawingapp.application.controllers;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.application.services.ModeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.commands.CommandFactory;
import com.drawingapp.drawingapp.domain.commands.CommandHistory;
import com.drawingapp.drawingapp.domain.commands.Command;
import javafx.scene.canvas.GraphicsContext;

public class ShapeResizeController {
    private final ShapeManager shapeManager;
    private final ModeManager modeManager;
    private final ILogger logger;
    private final CommandFactory commandFactory;
    private final CommandHistory commandHistory;
    private GraphicsContext gc;
    private double initialWidth, initialHeight;

    public ShapeResizeController(ShapeManager shapeManager, ModeManager modeManager, ILogger logger,
                               CommandFactory commandFactory, CommandHistory commandHistory) {
        this.shapeManager = shapeManager;
        this.modeManager = modeManager;
        this.logger = logger;
        this.commandFactory = commandFactory;
        this.commandHistory = commandHistory;
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public void handleMousePressed(double x, double y) {
        Shape selected = shapeManager.getSelector().getSelectedShape();
        if (selected != null) {
            initialWidth = selected.getWidth();
            initialHeight = selected.getHeight();
            logger.log("Started resizing shape: " + selected.getClass().getSimpleName() + 
                      " from size " + initialWidth + "x" + initialHeight);
        }
    }

    public void handleMouseDragged(double x, double y) {
        Shape selected = shapeManager.getSelector().getSelectedShape();
        if (selected != null) {
            double newWidth = Math.max(10, x - selected.getX());
            double newHeight = Math.max(10, y - selected.getY());
            
            Command resizeCommand = commandFactory.createResizeCommand(selected, initialWidth, initialHeight, newWidth, newHeight);
            commandHistory.executeCommand(resizeCommand);
            
            logger.log("Resizing shape to: " + newWidth + "x" + newHeight);
            redrawCanvas();
        }
    }

    public void handleMouseReleased(double x, double y) {
        Shape selected = shapeManager.getSelector().getSelectedShape();
        if (selected != null) {
            logger.log("Finished resizing shape to: " + selected.getWidth() + "x" + selected.getHeight());
        }
    }

    private void redrawCanvas() {
        if (gc != null) {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            for (var shape : shapeManager.getShapes()) {
                shape.draw(gc);
            }
        }
    }
} 