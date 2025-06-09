package com.drawingapp.drawingapp.application.controllers;

import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.application.services.ModeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.domain.shapes.RotatableShape;
import com.drawingapp.drawingapp.domain.commands.CommandFactory;
import com.drawingapp.drawingapp.domain.commands.CommandHistory;
import com.drawingapp.drawingapp.domain.commands.Command;
import javafx.scene.canvas.GraphicsContext;

public class ShapeRotationController {
    private final ShapeManager shapeManager;
    private final ModeManager modeManager;
    private final ILogger logger;
    private final CommandFactory commandFactory;
    private final CommandHistory commandHistory;
    private GraphicsContext gc;

    public ShapeRotationController(ShapeManager shapeManager, ModeManager modeManager, ILogger logger, 
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

    public void rotateSelectedShape(double newAngle) {
        var selectedShape = shapeManager.getSelector().getSelectedShape();
        if (selectedShape instanceof RotatableShape) {
            RotatableShape rotatableShape = (RotatableShape) selectedShape;
            double oldAngle = rotatableShape.getRotationAngle();
            
            Command rotateCommand = commandFactory.createRotateCommand(rotatableShape, oldAngle, newAngle);
            commandHistory.executeCommand(rotateCommand);
            redrawCanvas();
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