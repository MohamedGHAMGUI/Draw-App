package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import java.util.Objects;

public class MoveShapeCommand implements Command {
    private final Shape shape;
    private final double dx;
    private final double dy;
    private final ILogger logger;

    public MoveShapeCommand(Shape shape, double dx, double dy, ILogger logger) {
        if (shape == null) {
            throw new IllegalArgumentException("Shape cannot be null");
        }
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        this.shape = shape;
        this.dx = dx;
        this.dy = dy;
        this.logger = logger;
    }

    @Override
    public void execute() {
        shape.move(dx, dy);
        logger.log("Moved shape: " + shape.getClass().getSimpleName());
    }

    @Override
    public void undo() {
        shape.move(-dx, -dy);
        logger.log("Undid shape move: " + shape.getClass().getSimpleName());
    }

    @Override
    public void redo() {
        shape.move(dx, dy);
        logger.log("Redid shape move: " + shape.getClass().getSimpleName());
    }
} 