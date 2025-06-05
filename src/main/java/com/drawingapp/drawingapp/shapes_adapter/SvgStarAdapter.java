package com.drawingapp.drawingapp.shapes_adapter;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SvgStarAdapter implements Shape {
    private SvgStar adaptee;
    private double centerX, centerY, radius;
    private int points;
    private boolean selected;
    private Color fillColor = Color.GOLD;
    private Color strokeColor = Color.BLACK;

    public SvgStarAdapter(double centerX, double centerY, double radius, int points) {
        this.adaptee = new SvgStar();
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.points = points;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(fillColor);
        gc.setStroke(selected ? strokeColor : fillColor);
        adaptee.renderStar(gc, centerX, centerY, radius, points);
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        this.centerX = x;
        this.centerY = y;
        draw(gc);
    }

    @Override
    public boolean contains(double x, double y) {
        double dx = x - centerX;
        double dy = y - centerY;
        return (dx * dx + dy * dy) <= radius * radius;
    }

    @Override
    public void move(double dx, double dy) {
        centerX += dx;
        centerY += dy;
    }

    @Override
    public void setSelected(boolean selected) { this.selected = selected; }
    @Override
    public boolean isSelected() { return selected; }
    @Override
    public void setColor(Color color) { this.fillColor = color; }
    @Override
    public Color getColor() { return fillColor; }
    public void setStrokeColor(Color color) { this.strokeColor = color; }
    public Color getStrokeColor() { return strokeColor; }
    @Override
    public void resize(double newWidth, double newHeight) {
        this.radius = Math.min(newWidth, newHeight) / 2.0;
    }
} 