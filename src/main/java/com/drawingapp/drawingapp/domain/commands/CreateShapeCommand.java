package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;

public class CreateShapeCommand implements Command {
    private final Shape shape;
    private final ShapeManager shapeManager;
    private final ILogger logger;

    public CreateShapeCommand(ShapeManager shapeManager, Shape shape, ILogger logger) {
        this.shape = shape;
        this.shapeManager = shapeManager;
        this.logger = logger;
    }

    @Override
    public void execute() {
        shapeManager.addShape(shape);
        logger.log("Created shape: " + shape.getClass().getSimpleName());
    }

    @Override
    public void undo() {
        shapeManager.removeShape(shape);
        logger.log("Undid shape creation: " + shape.getClass().getSimpleName());
    }

    @Override
    public void redo() {
        shapeManager.addShape(shape);
        logger.log("Redid shape creation: " + shape.getClass().getSimpleName());
    }
} 