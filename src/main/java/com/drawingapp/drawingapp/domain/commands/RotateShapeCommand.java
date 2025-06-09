package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.shapes.RotatableShape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import java.util.Objects;

public class RotateShapeCommand implements Command {
    private final RotatableShape shape;
    private final double oldAngle;
    private final double newAngle;
    private final ILogger logger;

    public RotateShapeCommand(RotatableShape shape, double oldAngle, double newAngle, ILogger logger) {
        if (shape == null) {
            throw new IllegalArgumentException("Shape cannot be null");
        }
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        this.shape = shape;
        this.oldAngle = oldAngle;
        this.newAngle = newAngle;
        this.logger = logger;
    }

    @Override
    public void execute() {
        shape.setRotationAngle(newAngle);
        logger.log("Rotated shape: " + shape.getClass().getSimpleName());
    }

    @Override
    public void undo() {
        shape.setRotationAngle(oldAngle);
        logger.log("Undid shape rotation: " + shape.getClass().getSimpleName());
    }

    @Override
    public void redo() {
        shape.setRotationAngle(newAngle);
        logger.log("Redid shape rotation: " + shape.getClass().getSimpleName());
    }
} 