package com.drawingapp.drawingapp.shapes_state_observer;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.application.services.ShapeManager;

public class MoveState implements State {
    private ShapeManager shapeManager;
    private final ILogger logger;
    private double lastX, lastY;
    private boolean isDragging = false;

    public MoveState(ShapeManager shapeManager, ILogger logger) {
        this.shapeManager = shapeManager;
        this.logger = logger;
    }

    @Override
    public void onMousePressed(double x, double y) {
        Shape selectedShape = shapeManager.getSelector().getSelectedShape();
        if (selectedShape != null && selectedShape.contains(x, y)) {
            lastX = x;
            lastY = y;
            isDragging = true;
            logger.log("Started moving shape at (" + x + ", " + y + ")");
        }
    }

    @Override
    public void onMouseDragged(double x, double y) {
        Shape selectedShape = shapeManager.getSelector().getSelectedShape();
        if (selectedShape != null && isDragging) {
            double dx = x - lastX;
            double dy = y - lastY;
            selectedShape.move(dx, dy);
            logger.log("Moved shape by (" + dx + ", " + dy + ")");
            lastX = x;
            lastY = y;
        }
    }

    @Override
    public void onMouseReleased(double x, double y) {
        isDragging = false;
    }
}
