package com.drawingapp.drawingapp.shapes_state_observer;


import com.drawingapp.drawingapp.shapes_observer.ShapeSubject;

public class ShapeSelector {
    private static ShapeSelector instance;
    private final ShapeSubject subject = new ShapeSubject();

    private ShapeSelector() {}

    public static ShapeSelector getInstance() {
        if (instance == null) {
            instance = new ShapeSelector();
        }
        return instance;
    }

    public void selectShape(String shapeType) {
        subject.notifyShapeSelected(shapeType);
    }

    public ShapeSubject getSubject() {
        return subject;
    }
}
