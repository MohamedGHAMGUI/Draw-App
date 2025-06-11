package com.drawingapp.drawingapp.shapes_decorator;

import com.drawingapp.drawingapp.shapes_color.ColorStrategy;
import com.drawingapp.drawingapp.shapes_color.ShapeColorStrategy;
import com.drawingapp.drawingapp.shapes_factory.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class ShapeDecorator implements Shape , ColorStrategy {
    protected Shape decoratedShape;
    protected ColorStrategy colorStrategy;

    public ShapeDecorator(Shape shape) {
        this.decoratedShape = shape;
        this.colorStrategy = new ShapeColorStrategy();
    }

    @Override
    public void draw(GraphicsContext gc) {
        decoratedShape.draw(gc);
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        decoratedShape.draw(gc, x, y);
    }

    @Override
    public boolean contains(double x, double y) {
        return decoratedShape.contains(x, y);
    }

    @Override
    public void move(double dx, double dy) {
        decoratedShape.move(dx, dy);
    }

    @Override
    public void setSelected(boolean selected) {
        decoratedShape.setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return decoratedShape.isSelected();
    }

    @Override
    public void setColor(Color color) {
        colorStrategy.setColor(color);
        decoratedShape.setColor(color);
    }

    @Override
    public Color getColor() {
        return colorStrategy.getColor();
    }

    public Shape getDecoratedShape() {
        return decoratedShape;
    }
}
