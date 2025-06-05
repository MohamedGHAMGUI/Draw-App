package com.drawingapp.drawingapp.shapes_factory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.drawingapp.drawingapp.shapes_color.ColorStrategy;
import com.drawingapp.drawingapp.shapes_color.ShapeColorStrategy;

public class RectangleShape implements Shape {
    private double x, y;
    private double width = 100;
    private double height = 70;
    private boolean selected;
    private ColorStrategy colorStrategy = new ShapeColorStrategy();

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.setStroke(selected ? Color.BLACK : getColor());
        gc.setLineWidth(selected ? 2 : 1);
        gc.fillRect(x, y, width, height);
        if (selected) {
            gc.strokeRect(x, y, width, height);
        }
    }

    @Override
    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width &&
               mouseY >= y && mouseY <= y + height;
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

    // Add this method to handle initial drawing
    public void draw(GraphicsContext gc, double x, double y) {
        this.x = x;
        this.y = y;
        draw(gc);
    }

    @Override
    public void setColor(Color color) { colorStrategy.setColor(color); }

    @Override
    public Color getColor() { return colorStrategy.getColor(); }

    public void setColorStrategy(ColorStrategy strategy) { this.colorStrategy = strategy; }

    @Override
    public void resize(double newWidth, double newHeight) {
        if (newWidth > 0) this.width = newWidth;
        if (newHeight > 0) this.height = newHeight;
    }
}
