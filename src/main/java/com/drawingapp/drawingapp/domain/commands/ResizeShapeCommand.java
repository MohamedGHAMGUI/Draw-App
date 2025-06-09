package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import java.util.Objects;

public class ResizeShapeCommand implements Command {
    private final Shape shape;
    private final double oldWidth;
    private final double oldHeight;
    private final double newWidth;
    private final double newHeight;
    private final ILogger logger;

    public ResizeShapeCommand(Shape shape, double oldWidth, double oldHeight, double newWidth, double newHeight, ILogger logger) {
        if (shape == null) {
            throw new IllegalArgumentException("Shape cannot be null");
        }
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        if (oldWidth <= 0 || oldHeight <= 0 || newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("Invalid dimensions for resize");
        }
        this.shape = shape;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.logger = logger;
    }

    @Override
    public void execute() {
        shape.resize(newWidth, newHeight);
        logger.log("Resized shape: " + shape.getClass().getSimpleName());
    }

    @Override
    public void undo() {
        shape.resize(oldWidth, oldHeight);
        logger.log("Undid shape resize: " + shape.getClass().getSimpleName());
    }

    @Override
    public void redo() {
        shape.resize(newWidth, newHeight);
        logger.log("Redid shape resize: " + shape.getClass().getSimpleName());
    }
} 