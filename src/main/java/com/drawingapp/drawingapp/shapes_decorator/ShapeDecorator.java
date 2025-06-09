package com.drawingapp.drawingapp.shapes_decorator;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class ShapeDecorator implements Shape {
    protected Shape decoratedShape;

    public ShapeDecorator(Shape shape) {
        this.decoratedShape = shape;
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
        decoratedShape.setColor(color);
    }

    @Override
    public Color getColor() {
        return decoratedShape.getColor();
    }

    @Override
    public void resize(double width, double height) {
        decoratedShape.resize(width, height);
    }

    @Override
    public double getX() {
        return decoratedShape.getX();
    }

    @Override
    public double getY() {
        return decoratedShape.getY();
    }

    @Override
    public double getWidth() {
        return decoratedShape.getWidth();
    }

    @Override
    public double getHeight() {
        return decoratedShape.getHeight();
    }

    @Override
    public void setX(double x) {
        decoratedShape.setX(x);
    }

    @Override
    public void setY(double y) {
        decoratedShape.setY(y);
    }

    @Override
    public void setWidth(double width) {
        decoratedShape.setWidth(width);
    }

    @Override
    public void setHeight(double height) {
        decoratedShape.setHeight(height);
    }
}
