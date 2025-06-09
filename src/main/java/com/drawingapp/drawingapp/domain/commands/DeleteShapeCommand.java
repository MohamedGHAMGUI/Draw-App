package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;

public class DeleteShapeCommand implements Command {
    private final Shape shape;
    private final ShapeManager shapeManager;
    private final ILogger logger;

    public DeleteShapeCommand(ShapeManager shapeManager, Shape shape, ILogger logger) {
        this.shape = shape;
        this.shapeManager = shapeManager;
        this.logger = logger;
    }

    @Override
    public void execute() {
        shapeManager.removeShape(shape);
        logger.log("Deleted shape: " + shape.getClass().getSimpleName());
    }

    @Override
    public void undo() {
        shapeManager.addShape(shape);
        logger.log("Undid shape deletion: " + shape.getClass().getSimpleName());
    }

    @Override
    public void redo() {
        shapeManager.removeShape(shape);
        logger.log("Redid shape deletion: " + shape.getClass().getSimpleName());
    }
} 