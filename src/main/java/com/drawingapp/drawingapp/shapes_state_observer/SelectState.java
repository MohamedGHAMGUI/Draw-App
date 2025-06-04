package com.drawingapp.drawingapp.shapes_state_observer;

import java.util.List;

import com.drawingapp.drawingapp.shapes_factory.Shape;


public class SelectState implements State {
    private List<Shape> shapes;
    private Shape selectedShape;

    public SelectState(List<Shape> shapes) {
        this.shapes = shapes;
    }

    @Override
    public void onMousePressed(double x, double y) {
        for (Shape shape : shapes) {
            if (shape.contains(x, y)) {
                shape.setSelected(true);
                selectedShape = shape;
            } else {
                shape.setSelected(false);
            }
        }
    }

    @Override
    public void onMouseDragged(double x, double y) {
        // Optionally implement for drag-to-select
    }

    @Override
    public void onMouseReleased(double x, double y) {}
}
