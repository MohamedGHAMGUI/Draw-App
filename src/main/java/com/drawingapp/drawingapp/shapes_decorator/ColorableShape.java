package com.drawingapp.drawingapp.shapes_decorator;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ColorableShape implements Shape {
    protected Shape shape;
    protected Color color;

    public ColorableShape(Shape shape) {
        this.shape = shape;
        this.color = Color.BLACK;
    }

    @Override
    public void draw(GraphicsContext gc) {
        shape.setColor(color);
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
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void resize(double newWidth, double newHeight) {
        shape.resize(newWidth, newHeight);
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
        shape.setWidth(width);
    }

    @Override
    public void setHeight(double height) {
        shape.setHeight(height);
    }
}
