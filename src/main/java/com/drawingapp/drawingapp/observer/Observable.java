package com.drawingapp.drawingapp.observer;

/**
 * Interface for subjects that can be observed for shape selection changes.
 */
public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String shapeType);
} 