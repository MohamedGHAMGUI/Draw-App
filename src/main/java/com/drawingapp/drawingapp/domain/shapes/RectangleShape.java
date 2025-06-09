package com.drawingapp.drawingapp.domain.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RectangleShape implements RotatableShape {
    private double x, y, width, height;
    private Color color;
    private boolean selected;
    private double rotationAngle;

    public RectangleShape(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.selected = false;
        this.rotationAngle = 0;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(color);
        gc.setStroke(selected ? Color.BLUE : Color.TRANSPARENT);
        gc.setLineWidth(selected ? 2 : 1);
        
        // Apply rotation
        gc.translate(x + width/2, y + height/2);
        gc.rotate(rotationAngle);
        gc.translate(-(x + width/2), -(y + height/2));
        
        gc.fillRect(x, y, width, height);
        gc.strokeRect(x, y, width, height);
        gc.restore();
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        this.x = x;
        this.y = y;
        draw(gc);
    }

    @Override
    public boolean contains(double x, double y) {
        return x >= this.x && x <= this.x + width &&
               y >= this.y && y <= this.y + height;
    }

    @Override
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void resize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public void setRotationAngle(double angle) {
        this.rotationAngle = angle;
    }

    @Override
    public double getRotationAngle() {
        return rotationAngle;
    }
} 