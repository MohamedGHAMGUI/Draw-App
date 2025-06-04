package com.drawingapp.drawingapp.shapes_observer;

import java.util.ArrayList;
import java.util.List;

public class ShapeSubject {
    private final List<ShapeObserver> observers = new ArrayList<>();

    public void addObserver(ShapeObserver observer) {
        observers.add(observer);
    }

    public void notifyShapeSelected(String shapeType) {
        for (ShapeObserver observer : observers) {
            observer.onShapeSelected(shapeType);
        }
    }
}
