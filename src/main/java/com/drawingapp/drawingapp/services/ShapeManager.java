package com.drawingapp.drawingapp.services;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import java.util.ArrayList;
import java.util.List;

public class ShapeManager {
    private List<Shape> shapes;
    private Shape selectedShape;

    public ShapeManager() {
        this.shapes = new ArrayList<>();
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setSelectedShape(Shape shape) {
        if (selectedShape != null) {
            selectedShape.setSelected(false);
        }
        selectedShape = shape;
        if (selectedShape != null) {
            selectedShape.setSelected(true);
        }
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void clear() {
        shapes.clear();
        selectedShape = null;
    }
} 