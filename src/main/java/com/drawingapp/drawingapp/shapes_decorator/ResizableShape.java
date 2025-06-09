package com.drawingapp.drawingapp.shapes_decorator;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ResizableShape extends ShapeDecorator {
    private double width;
    private double height;
    protected double minWidth = 10;
    protected double minHeight = 10;

    public ResizableShape(Shape shape) {
        super(shape);
        this.width = shape.getWidth();
        this.height = shape.getHeight();
    }

    public Shape getBaseShape() {
        return decoratedShape;
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
    public void setX(double x) {
        decoratedShape.setX(x);
    }

    @Override
    public void setY(double y) {
        decoratedShape.setY(y);
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
    public void setWidth(double width) {
        this.width = width;
        decoratedShape.setWidth(Math.max(width, minWidth));
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
        decoratedShape.setHeight(Math.max(height, minHeight));
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void resize(double newWidth, double newHeight) {
        setWidth(newWidth);
        setHeight(newHeight);
    }
}
