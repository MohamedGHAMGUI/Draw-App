package com.drawingapp.drawingapp.controllers;

import javafx.scene.paint.Color;

public interface ShapeOperationHandler {
    void onShapeSelected(String shapeType);
    void onColorChanged(Color newColor);
    void onRotateLeft();
    void onRotateRight();
    void onRotationSliderChanged(double newValue);
} 