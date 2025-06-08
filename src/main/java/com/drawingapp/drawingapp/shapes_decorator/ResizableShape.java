package com.drawingapp.drawingapp.shapes_decorator;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ResizableShape implements Shape {
    protected Shape shape;
    protected double minWidth = 10;
    protected double minHeight = 10;

    public ResizableShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void draw(GraphicsContext gc) {
        shape.draw(gc);
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        shape.draw(gc, x, y);
    }

    @Override
    public boolean contains(double x, double y) {
        return shape.contains(x, y);
    }

    @Override
    public void move(double dx, double dy) {
        shape.move(dx, dy);
    }

    @Override
    public void setSelected(boolean selected) {
        shape.setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return shape.isSelected();
    }

    @Override
    public void setColor(Color color) {
        shape.setColor(color);
    }

    @Override
    public Color getColor() {
        return shape.getColor();
    }

    @Override
    public void resize(double newWidth, double newHeight) {
        // Ensure minimum size
        double width = Math.max(newWidth, minWidth);
        double height = Math.max(newHeight, minHeight);
        shape.resize(width, height);
    }

    @Override
    public double getX() {
        return shape.getX();
    }

    @Override
    public double getY() {
        return shape.getY();
    }

    @Override
    public double getWidth() {
        return shape.getWidth();
    }

    @Override
    public double getHeight() {
        return shape.getHeight();
    }

    @Override
    public void setX(double x) {
        shape.setX(x);
    }

    @Override
    public void setY(double y) {
        shape.setY(y);
    }

    @Override
    public void setWidth(double width) {
        shape.setWidth(Math.max(width, minWidth));
    }

    @Override
    public void setHeight(double height) {
        shape.setHeight(Math.max(height, minHeight));
    }
}
