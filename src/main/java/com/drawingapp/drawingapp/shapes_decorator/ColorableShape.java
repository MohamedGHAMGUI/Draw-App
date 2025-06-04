package com.drawingapp.drawingapp.shapes_decorator;

import com.drawingapp.drawingapp.shapes_factory.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ColorableShape extends ShapeDecorator {
    public ColorableShape(Shape shape) {
        super(shape);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.setStroke(isSelected() ? Color.BLACK : getColor());
        super.draw(gc);
    }

    @Override
    public void resize(double newWidth, double newHeight) {
        decoratedShape.resize(newWidth, newHeight);
    }
}
