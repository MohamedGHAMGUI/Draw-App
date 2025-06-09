package com.drawingapp.drawingapp.application.services;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.shapes_observer.ShapeSubject;
import java.util.List;
import java.util.Objects;

public class ShapeSelector {
    private final ShapeSubject subject;
    private final ILogger logger;
    private String selectedShapeType;
    private Shape selectedShape;
    private final List<Shape> shapes;

    public ShapeSelector(List<Shape> shapes, ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        if (shapes == null) {
            throw new IllegalArgumentException("Shapes list cannot be null");
        }
        this.shapes = shapes;
        this.subject = new ShapeSubject();
        this.logger = logger;
        this.selectedShapeType = "rectangle"; // default shape
    }

    public void selectShape(String shapeType) {
        if (shapeType == null || shapeType.trim().isEmpty()) {
            logger.log("Warning: Attempted to select null or empty shape type");
            return;
        }
        this.selectedShapeType = shapeType;
        logger.log("Selected shape type: " + shapeType);
        subject.notifyShapeSelected(shapeType);
    }

    public void selectShapeAt(double x, double y) {
        if (x < 0 || y < 0) {
            logger.log("Warning: Invalid coordinates (" + x + ", " + y + ")");
            return;
        }
        boolean found = false;
        for (Shape shape : shapes) {
            if (shape != null && shape.contains(x, y)) {
                setSelectedShape(shape);
                logger.log("Selected shape at (" + x + ", " + y + ")");
                found = true;
            } else if (shape != null) {
                shape.setSelected(false);
            }
        }
        if (!found) {
            setSelectedShape(null);
        }
    }

    public void setSelectedShape(Shape shape) {
        if (selectedShape != null) {
            selectedShape.setSelected(false);
        }
        selectedShape = shape;
        if (selectedShape != null) {
            selectedShape.setSelected(true);
        }
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public String getSelectedShapeType() {
        return selectedShapeType;
    }

    public ShapeSubject getSubject() {
        return subject;
    }

    public void clearSelection() {
        setSelectedShape(null);
    }
} 