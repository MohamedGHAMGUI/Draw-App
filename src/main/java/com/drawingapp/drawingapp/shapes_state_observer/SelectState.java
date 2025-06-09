package com.drawingapp.drawingapp.shapes_state_observer;

import java.util.List;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;

public class SelectState implements State {
    private Shape selectedShape;
    private final ShapeManager shapeManager;
    private final ILogger logger;

    public SelectState(List<Shape> shapes, ShapeManager shapeManager, ILogger logger) {
        this.shapeManager = shapeManager;
        this.logger = logger;
    }

    @Override
    public void onMousePressed(double x, double y) {
        boolean found = false;
        for (Shape shape : shapeManager.getShapes()) {
            if (shape.contains(x, y)) {
                setSelectedShape(shape);
                logger.log("Selected shape at (" + x + ", " + y + ")");
                found = true;
            } else {
                shape.setSelected(false);
            }
        }
        if (!found) {
            setSelectedShape(null);
        }
    }

    @Override
    public void onMouseDragged(double x, double y) {
        // Optionally implement for drag-to-select
    }

    @Override
    public void onMouseReleased(double x, double y) {}

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

    public void clearSelection() {
        setSelectedShape(null);
    }
}
