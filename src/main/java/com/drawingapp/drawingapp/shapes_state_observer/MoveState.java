package com.drawingapp.drawingapp.shapes_state_observer;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.logging.LoggerManager;

public class MoveState implements State {
    private Shape selectedShape;
    private double lastX, lastY;
    private boolean isDragging = false;

    public void setSelectedShape(Shape shape) {
        this.selectedShape = shape;
    }

    @Override
    public void onMousePressed(double x, double y) {
        if (selectedShape != null && selectedShape.contains(x, y)) {
            lastX = x;
            lastY = y;
            isDragging = true;
            LoggerManager.getInstance().log("Started moving shape at (" + x + ", " + y + ")");
        }
    }

    @Override
    public void onMouseDragged(double x, double y) {
        if (selectedShape != null && isDragging) {
            double dx = x - lastX;
            double dy = y - lastY;
            selectedShape.move(dx, dy);
            LoggerManager.getInstance().log("Moved shape by (" + dx + ", " + dy + ")");
            lastX = x;
            lastY = y;
        }
    }

    @Override
    public void onMouseReleased(double x, double y) {
        isDragging = false;
    }
}
