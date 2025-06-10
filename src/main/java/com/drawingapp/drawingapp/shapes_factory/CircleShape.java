package com.drawingapp.drawingapp.shapes_factory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleShape implements Shape {
    private double x;
    private double y;
    private double width;
    private double height;
    private Color color = Color.BLACK;
    private boolean selected = false;

    public CircleShape() {
        this.x = 0;
        this.y = 0;
        this.width = 50;
        this.height = 50;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save(); // Save the current graphics context state
        
        // Translate to the center of the circle
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        gc.translate(centerX, centerY);
        
        // Draw the circle
        gc.setFill(color);
        gc.fillOval(-width/2, -height/2, width, height);
        
        // Add stroke if selected
        if (selected) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2.0);
            gc.strokeOval(-width/2, -height/2, width, height);
        }
        
        gc.restore(); // Restore the graphics context state
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        this.x = x;
        this.y = y;
        draw(gc);
    }

    @Override
    public boolean contains(double x, double y) {
        double centerX = this.x + width / 2;
        double centerY = this.y + height / 2;
        double radius = Math.min(width, height) / 2;
        
        double dx = x - centerX;
        double dy = y - centerY;
        return Math.sqrt(dx * dx + dy * dy) <= radius;
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
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void resize(double newWidth, double newHeight) {
        this.width = newWidth;
        this.height = newHeight;
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
}