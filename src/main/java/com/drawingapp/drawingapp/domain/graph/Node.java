package com.drawingapp.drawingapp.domain.graph;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Node {
    private double x;
    private double y;
    private String label;
    private Color color;
    private boolean selected;

    public Node(double x, double y, String label) {
        this.x = x;
        this.y = y;
        this.label = label;
        this.color = Color.BLACK;
        this.selected = false;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(color);
        gc.setStroke(selected ? Color.BLUE : Color.BLACK);
        gc.setLineWidth(selected ? 2 : 1);
        
        double radius = 10;
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
        
        gc.restore();
    }

    public boolean contains(double x, double y) {
        double dx = x - this.x;
        double dy = y - this.y;
        return Math.sqrt(dx * dx + dy * dy) <= 20;
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
} 