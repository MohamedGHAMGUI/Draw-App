package com.drawingapp.drawingapp.shapes_state_observer;

import java.util.List;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.logging.LoggerManager;


public class SelectState implements State {
    private List<Shape> shapes;
    private Shape selectedShape;
    private ShapeManager shapeManager;

    public SelectState(List<Shape> shapes, ShapeManager shapeManager) {
        this.shapes = shapes;
        this.shapeManager = shapeManager;
    }

    @Override
    public void onMousePressed(double x, double y) {
        boolean found = false;
        for (Shape shape : shapes) {
            if (shape.contains(x, y)) {
                shape.setSelected(true);
                selectedShape = shape;
                shapeManager.setSelectedShape(shape);
                LoggerManager.getInstance().log("Selected shape at (" + x + ", " + y + ")");
                found = true;
            } else {
                shape.setSelected(false);
            }
        }
        if (!found) {
            shapeManager.setSelectedShape(null);
        }
    }

    @Override
    public void onMouseDragged(double x, double y) {
        // Optionally implement for drag-to-select
    }

    @Override
    public void onMouseReleased(double x, double y) {}
}
