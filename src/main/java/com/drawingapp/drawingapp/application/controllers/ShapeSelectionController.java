package com.drawingapp.drawingapp.application.controllers;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.application.services.ModeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.commands.CommandFactory;
import com.drawingapp.drawingapp.domain.commands.CommandHistory;
import com.drawingapp.drawingapp.domain.commands.Command;
import javafx.scene.canvas.GraphicsContext;

public class ShapeSelectionController {
    private final ShapeManager shapeManager;
    private final ModeManager modeManager;
    private final ILogger logger;
    private final CommandFactory commandFactory;
    private final CommandHistory commandHistory;
    private GraphicsContext gc;
    private Shape selectedShape;
    private double initialX, initialY;

    public ShapeSelectionController(ShapeManager shapeManager, ModeManager modeManager, ILogger logger,
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
        shapeManager.getSelector().selectShapeAt(x, y);
        selectedShape = shapeManager.getSelector().getSelectedShape();
        if (selectedShape != null) {
            initialX = selectedShape.getX();
            initialY = selectedShape.getY();
        }
        logger.log("Selected shape at (" + x + ", " + y + ")");
    }

    public void handleMouseDragged(double x, double y) {
        if (selectedShape != null) {
            double dx = x - selectedShape.getX();
            double dy = y - selectedShape.getY();
            selectedShape.move(dx, dy);
            redrawCanvas();
            logger.log("Moved shape to (" + x + ", " + y + ")");
        }
    }

    public void handleMouseReleased(double x, double y) {
        if (selectedShape != null) {
            double dx = x - initialX;
            double dy = y - initialY;
            Command moveCommand = commandFactory.moveShapeCommand(selectedShape, dx, dy);
            commandHistory.executeCommand(moveCommand);
            selectedShape = null;
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