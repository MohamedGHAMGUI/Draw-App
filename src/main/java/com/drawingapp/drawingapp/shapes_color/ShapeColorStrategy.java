package com.drawingapp.drawingapp.shapes_color;
import javafx.scene.paint.Color;


public class ShapeColorStrategy implements ColorStrategy {
    private Color color;

    public ShapeColorStrategy() {
        this.color = Color.BLACK; // default color
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}