package com.drawingapp.drawingapp.application.services;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.domain.shapes.RotatableShape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Objects;

public class ShapeOperationManager {
    private final List<Shape> shapes;
    private final ILogger logger;

    public ShapeOperationManager(List<Shape> shapes, ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        if (shapes == null) {
            throw new IllegalArgumentException("Shapes list cannot be null");
        }
        this.shapes = shapes;
        this.logger = logger;
    }

    public void resizeShape(Shape shape, double width, double height) {
        if (shape == null) {
            logger.log("Warning: Attempted to resize null shape");
            return;
        }
        if (!shapes.contains(shape)) {
            logger.log("Warning: Shape not found in manager");
            return;
        }
        if (width <= 0 || height <= 0) {
            logger.log("Warning: Invalid dimensions for resize: " + width + "x" + height);
            return;
        }
        shape.resize(width, height);
        logger.log("Resized shape: " + shape.getClass().getSimpleName());
    }

    public void moveShape(Shape shape, double deltaX, double deltaY) {
        if (shape == null) {
            logger.log("Warning: Attempted to move null shape");
            return;
        }
        if (!shapes.contains(shape)) {
            logger.log("Warning: Shape not found in manager");
            return;
        }
        shape.move(deltaX, deltaY);
        logger.log("Moved shape: " + shape.getClass().getSimpleName());
    }

    public void rotateShape(Shape shape, double angle) {
        if (shape == null) {
            logger.log("Warning: Attempted to rotate null shape");
            return;
        }
        if (!shapes.contains(shape)) {
            logger.log("Warning: Shape not found in manager");
            return;
        }
        if (!(shape instanceof RotatableShape)) {
            logger.log("Warning: Shape does not support rotation");
            return;
        }
        ((RotatableShape) shape).setRotationAngle(angle);
        logger.log("Rotated shape: " + shape.getClass().getSimpleName());
    }

    public void changeColor(Shape shape, Color color) {
        if (shape == null) {
            logger.log("Warning: Attempted to change color of null shape");
            return;
        }
        if (!shapes.contains(shape)) {
            logger.log("Warning: Shape not found in manager");
            return;
        }
        if (color == null) {
            logger.log("Warning: Invalid color value");
            return;
        }
        shape.setColor(color);
        logger.log("Changed color of shape: " + shape.getClass().getSimpleName());
    }
} 