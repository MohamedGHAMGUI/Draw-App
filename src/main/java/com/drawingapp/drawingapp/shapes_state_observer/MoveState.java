package com.drawingapp.drawingapp.shapes_state_observer;

import com.drawingapp.drawingapp.shapes_factory.Shape;

public class MoveState implements State {
    private Shape selectedShape;
    private double lastX, lastY;

    public void setSelectedShape(Shape shape) {
        this.selectedShape = shape;
    }

    @Override
    public void onMousePressed(double x, double y) {
        if (selectedShape != null && selectedShape.contains(x, y)) {
            lastX = x; lastY = y;
        }
    }

    @Override
    public void onMouseDragged(double x, double y) {
        if (selectedShape != null) {
            double dx = x - lastX;
            double dy = y - lastY;
            selectedShape.move(dx, dy);
            lastX = x; lastY = y;
        }
    }

    @Override
    public void onMouseReleased(double x, double y) {}
}
