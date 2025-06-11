package com.drawingapp.drawingapp.observer;

import java.util.ArrayList;
import java.util.List;


public class ShapeSelector implements Observable {
    private static ShapeSelector instance;
    private final List<Observer> observers = new ArrayList<>();
    private String currentShape;

    private ShapeSelector() {}

    public static synchronized ShapeSelector getInstance() {
        if (instance == null) {
            instance = new ShapeSelector();
        }
        return instance;
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String shapeType) {
        new ArrayList<>(observers).forEach(observer -> 
            observer.onShapeSelected(shapeType)
        );
    }

    public void selectShape(String shapeType) {
        if (shapeType != null && !shapeType.equals(currentShape)) {
            this.currentShape = shapeType;
            notifyObservers(shapeType);
        }
    }

    public String getCurrentShape() {
        return currentShape;
    }
} 