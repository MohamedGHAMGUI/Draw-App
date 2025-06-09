package com.drawingapp.drawingapp.application.controllers;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.domain.shapes.ShapeFactory;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.application.services.ModeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.commands.CommandFactory;
import com.drawingapp.drawingapp.domain.commands.CommandHistory;

import com.drawingapp.drawingapp.domain.commands.Command;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeDrawingController {
    private final ShapeManager shapeManager;
    private final ModeManager modeManager;
    private final ILogger logger;
    private final CommandFactory commandFactory;
    private final CommandHistory commandHistory;
    private final ShapeFactory shapeFactory;
    private GraphicsContext gc;

    public ShapeDrawingController(ShapeManager shapeManager, ModeManager modeManager, ILogger logger,
                                CommandFactory commandFactory, CommandHistory commandHistory,
                                ShapeFactory shapeFactory) {
        this.shapeManager = shapeManager;
        this.modeManager = modeManager;
        this.logger = logger;
        this.commandFactory = commandFactory;
        this.commandHistory = commandHistory;
        this.shapeFactory = shapeFactory;
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public void handleMousePressed(double x, double y, String selectedShape, Color currentColor) {
        if (selectedShape != null && !selectedShape.isEmpty()) {
            Shape shape;
            switch (selectedShape) {
                case "star":
                    // Star starts at click position like other shapes
                    shape = shapeFactory.createShape(selectedShape, x, y, 100, 100, currentColor);
                    break;
                case "circle":
                    // Circle is centered at click position
                    shape = shapeFactory.createShape(selectedShape, x, y, 80, 80, currentColor);
                    break;
                case "line":
                    // Line starts at click position
                    shape = shapeFactory.createShape(selectedShape, x, y, 200, 60, currentColor);
                    break;
                case "rectangle":
                    // Rectangle starts at click position
                    shape = shapeFactory.createShape(selectedShape, x, y, 100, 60, currentColor);
                    break;
                default:
                    // Other shapes start at click position
                    shape = shapeFactory.createShape(selectedShape, x, y, 200, 60, currentColor);
                    break;
            }
            
            if (shape != null) {
                Command createCommand = commandFactory.createShapeCommand(shape);
                commandHistory.executeCommand(createCommand);
                redrawCanvas();
                logger.log("Created " + selectedShape + " at (" + x + ", " + y + ")");
            }
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