package com.drawingapp.drawingapp.shapes_factory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RectangleShape implements RotatableShape {
    private double x;
    private double y;
    private double width;
    private double height;
    private Color color = Color.TRANSPARENT;
    private boolean selected = false;
    private double rotationAngle = 0.0;

    public RectangleShape() {
        this.x = 0;
        this.y = 0;
        this.width = 100;
        this.height = 50;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save(); // Save the current graphics context state
        
        // Translate to the center of the rectangle
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        gc.translate(centerX, centerY);
        
        // Rotate around the center
        gc.rotate(rotationAngle);
        
        // Translate back
        gc.translate(-centerX, -centerY);
        
        // Draw the rectangle
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
        
        // Add stroke if selected
        if (selected) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2.0);
            gc.strokeRect(x, y, width, height);
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
        // For rotated rectangle, we need to transform the point
        double centerX = this.x + width / 2;
        double centerY = this.y + height / 2;
        
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
        
        return rotatedX >= this.x && rotatedX <= this.x + width &&
               rotatedY >= this.y && rotatedY <= this.y + height;
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
}
