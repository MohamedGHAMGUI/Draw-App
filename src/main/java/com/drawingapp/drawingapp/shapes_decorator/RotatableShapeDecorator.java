package com.drawingapp.drawingapp.shapes_decorator;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_factory.RotatableShape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RotatableShapeDecorator extends ShapeDecorator implements RotatableShape {
    private double rotationAngle = 0.0;
    private Shape decoratedShape;

    public RotatableShapeDecorator(Shape shape) {
        super(shape);
        this.decoratedShape = shape;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        double centerX = decoratedShape.getX() + decoratedShape.getWidth() / 2;
        double centerY = decoratedShape.getY() + decoratedShape.getHeight() / 2;
        gc.translate(centerX, centerY);
        gc.rotate(rotationAngle);
        gc.translate(-centerX, -centerY);
        decoratedShape.draw(gc);
        
        gc.restore();
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        decoratedShape.draw(gc, x, y);
    }

    @Override
    public boolean contains(double x, double y) {
        // For rotated shape, we need to transform the point
        double centerX = decoratedShape.getX() + decoratedShape.getWidth() / 2;
        double centerY = decoratedShape.getY() + decoratedShape.getHeight() / 2;
        
        // Translate point to origin
        double translatedX = x - centerX;
        double translatedY = y - centerY;
        
        // Rotate point back
        double angle = -rotationAngle * Math.PI / 180;
        double rotatedX = translatedX * Math.cos(angle) - translatedY * Math.sin(angle);
        double rotatedY = translatedX * Math.sin(angle) + translatedY * Math.cos(angle);
        
        // Translate point back
        rotatedX += centerX;
        rotatedY += centerY;
        
        return decoratedShape.contains(rotatedX, rotatedY);
    }

    @Override
    public void rotate(double angle) {
        rotationAngle = (rotationAngle + angle) % 360;
    }

    @Override
    public double getRotation() {
        return rotationAngle;
    }

    @Override
    public void setRotation(double angle) {
        rotationAngle = angle % 360;
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

    @Override
    public void resize(double newWidth, double newHeight) {
        decoratedShape.resize(newWidth, newHeight);
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
    public Shape getDecoratedShape() {
        return decoratedShape;
    }
} 