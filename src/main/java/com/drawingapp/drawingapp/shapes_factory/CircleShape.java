package com.drawingapp.drawingapp.shapes_factory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleShape implements Shape {
    private double centerX, centerY;
    private double radius = 40; // half of 80 (original size)
    private boolean selected;
    private Color color = Color.RED; // default color

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(selected ? Color.BLACK : color);
        gc.setLineWidth(selected ? 2 : 1);
        gc.fillOval(centerX, centerY, radius * 2, radius * 2);
        if (selected) {
            gc.strokeOval(centerX, centerY, radius * 2, radius * 2);
        }
    }

    @Override
    public boolean contains(double x, double y) {
        // Check if point (x,y) is inside the circle using distance formula
        double dx = x - (centerX + radius);
        double dy = y - (centerY + radius);
        return (dx * dx + dy * dy) <= radius * radius;
    }

    @Override
    public void move(double dx, double dy) {
        centerX += dx;
        centerY += dy;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    // Add this method to handle initial drawing
    public void draw(GraphicsContext gc, double x, double y) {
        this.centerX = x;
        this.centerY = y;
        draw(gc);
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
        // For a circle, use the smaller of width/height as the new diameter
        double newRadius = Math.min(newWidth, newHeight) / 2.0;
        if (newRadius > 0) {
            this.radius = newRadius;
        }
    }
}