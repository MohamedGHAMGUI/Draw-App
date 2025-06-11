package com.drawingapp.drawingapp.observer;

/**
 * Interface for observers that need to be notified of shape selection events.
 */
public interface Observer {
    void onShapeSelected(String shapeType);
} 