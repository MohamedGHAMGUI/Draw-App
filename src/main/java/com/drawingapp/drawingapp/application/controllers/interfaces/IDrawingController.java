package com.drawingapp.drawingapp.application.controllers.interfaces;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface IDrawingController {
    void setGraphicsContext(GraphicsContext gc);
    void handleMousePressed(double x, double y);
    void handleMouseDragged(double x, double y);
    void handleMouseReleased(double x, double y);
    void setSelectedShape(String shapeType);
    void setCurrentColor(Color color);
    void redrawCanvas();
    void rotateSelectedShape(double angle);
    void deleteSelectedShape();
    void undo();
    void redo();
} 