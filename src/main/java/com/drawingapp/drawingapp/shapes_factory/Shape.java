package com.drawingapp.drawingapp.shapes_factory;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;



public interface Shape {
    void draw(GraphicsContext gc);
    void draw(GraphicsContext gc, double x, double y);
    boolean contains(double x, double y);
    void move(double dx, double dy);
    void setSelected(boolean selected);
    boolean isSelected();
    void setColor(Color color);
    Color getColor();
    void resize(double newWidth, double newHeight);
    double getX();
    double getY();
    double getWidth();
    double getHeight();
    void setX(double x);
    void setY(double y);
    void setWidth(double width);
    void setHeight(double height);
}