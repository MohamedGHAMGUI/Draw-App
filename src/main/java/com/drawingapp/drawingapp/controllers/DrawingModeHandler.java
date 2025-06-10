package com.drawingapp.drawingapp.controllers;

import javafx.scene.input.MouseEvent;

public interface DrawingModeHandler {
    void handleMousePressed(MouseEvent event);
    void handleMouseDragged(MouseEvent event);
    void handleMouseReleased(MouseEvent event);
    void redrawCanvas();
} 