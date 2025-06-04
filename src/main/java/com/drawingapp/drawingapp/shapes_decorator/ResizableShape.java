package com.drawingapp.drawingapp.shapes_decorator;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import javafx.scene.canvas.GraphicsContext;

public class ResizableShape extends ShapeDecorator {
    public ResizableShape(Shape decoratedShape) {
        super(decoratedShape);
    }

    public void resize(double newWidth, double newHeight) {
        decoratedShape.resize(newWidth, newHeight);
    }

    @Override
    public void draw(GraphicsContext gc) {
        decoratedShape.draw(gc);
    }
}
