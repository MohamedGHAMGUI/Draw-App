package com.drawingapp.drawingapp.application.services;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import java.util.List;
import java.util.Objects;

public class ShapeStorageManager {
    private final List<Shape> shapes;
    private final ILogger logger;

    public ShapeStorageManager(List<Shape> shapes, ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        if (shapes == null) {
            throw new IllegalArgumentException("Shapes list cannot be null");
        }
        this.shapes = shapes;
        this.logger = logger;
    }

    public void addShape(Shape shape) {
        if (shape == null) {
            logger.log("Warning: Attempted to add null shape");
            return;
        }
        shapes.add(shape);
        logger.log("Added shape: " + shape.getClass().getSimpleName());
    }

    public void addShape(Shape shape, int index) {
        if (shape == null) {
            logger.log("Warning: Attempted to add null shape");
            return;
        }
        if (index < 0) {
            logger.log("Warning: Invalid index " + index + ", adding shape at end");
            shapes.add(shape);
            return;
        }
        if (index > shapes.size()) {
            logger.log("Warning: Index " + index + " out of bounds, adding shape at end");
            shapes.add(shape);
            return;
        }
        shapes.add(index, shape);
        logger.log("Added shape at index " + index + ": " + shape.getClass().getSimpleName());
    }

    public void removeShape(Shape shape) {
        if (shape == null) {
            logger.log("Warning: Attempted to remove null shape");
            return;
        }
        if (shapes.remove(shape)) {
            logger.log("Removed shape: " + shape.getClass().getSimpleName());
        } else {
            logger.log("Warning: Shape not found for removal");
        }
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void clear() {
        shapes.clear();
        logger.log("Cleared all shapes");
    }
} 