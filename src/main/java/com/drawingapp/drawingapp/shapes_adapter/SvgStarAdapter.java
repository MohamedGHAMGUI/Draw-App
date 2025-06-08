package com.drawingapp.drawingapp.shapes_adapter;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SvgStarAdapter implements Shape {
    private SvgStar svgStar;
    private double x;
    private double y;
    private double width;
    private double height;
    private Color color = Color.BLACK;
    private boolean selected = false;

    public SvgStarAdapter(SvgStar svgStar) {
        this.svgStar = svgStar;
        this.x = 0;
        this.y = 0;
        this.width = 100;
        this.height = 100;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        svgStar.draw(gc, x, y, width, height);
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