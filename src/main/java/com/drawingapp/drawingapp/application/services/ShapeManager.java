package com.drawingapp.drawingapp.application.services;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShapeManager {
    private final List<Shape> shapes;
    private final ShapeSelector selector;
    private final ILogger logger;
    private final ShapeStorageManager storageManager;
    private final ShapeOperationManager operationManager;

    public ShapeManager(ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        this.shapes = new ArrayList<>();
        this.logger = logger;
        this.selector = new ShapeSelector(shapes, logger);
        this.storageManager = new ShapeStorageManager(shapes, logger);
        this.operationManager = new ShapeOperationManager(shapes, logger);
    }

    public void addShape(Shape shape) {
        if (shape == null) {
            logger.log("Warning: Attempted to add null shape");
            return;
        }
        storageManager.addShape(shape);
    }

    public void addShape(Shape shape, int index) {
        if (shape == null) {
            logger.log("Warning: Attempted to add null shape");
            return;
        }
        if (index < 0) {
            logger.log("Warning: Invalid index " + index + ", adding shape at end");
            storageManager.addShape(shape);
            return;
        }
        storageManager.addShape(shape, index);
    }

    public void removeShape(Shape shape) {
        if (shape == null) {
            logger.log("Warning: Attempted to remove null shape");
            return;
        }
        storageManager.removeShape(shape);
    }

    public List<Shape> getShapes() {
        return new ArrayList<>(shapes); // Return defensive copy
    }

    public ShapeSelector getSelector() {
        return selector;
    }

    public void clear() {
        storageManager.clear();
    }

    public ShapeOperationManager getOperationManager() {
        return operationManager;
    }
} 