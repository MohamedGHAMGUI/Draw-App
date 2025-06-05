package com.drawingapp.drawingapp.shapes_factory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.drawingapp.drawingapp.shapes_color.ColorStrategy;
import com.drawingapp.drawingapp.shapes_color.ShapeColorStrategy;

public class LineShape implements Shape {
    private double startX, startY, endX, endY;
    private boolean selected;
    private ColorStrategy colorStrategy = new ShapeColorStrategy();

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(selected ? Color.BLACK : getColor());
        gc.setLineWidth(selected ? 3 : 2);
        gc.strokeLine(startX, startY, endX, endY);
        if (selected) {
            // Draw endpoints to make selection more visible
            double radius = 4;
            gc.setFill(Color.BLACK);
            gc.fillOval(startX - radius, startY - radius, radius * 2, radius * 2);
            gc.fillOval(endX - radius, endY - radius, radius * 2, radius * 2);
        }
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        // Set start point to (x, y) and end point to (x+100, y+100) for initial creation
        this.startX = x;
        this.startY = y;
        this.endX = x + 100;
        this.endY = y + 100;
        draw(gc);
    }

    @Override
    public boolean contains(double x, double y) {
        // Simple hit detection: check if point (x, y) is near the line segment
        double threshold = 5.0; // pixels
        double dx = endX - startX;
        double dy = endY - startY;
        double lengthSquared = dx * dx + dy * dy;
        if (lengthSquared == 0) return false;
        double t = ((x - startX) * dx + (y - startY) * dy) / lengthSquared;
        t = Math.max(0, Math.min(1, t));
        double projX = startX + t * dx;
        double projY = startY + t * dy;
        double dist = Math.hypot(x - projX, y - projY);
        return dist <= threshold;
    }

    @Override
    public void move(double dx, double dy) {
        startX += dx; startY += dy;
        endX += dx; endY += dy;
    }

    @Override
    public void setSelected(boolean selected) { this.selected = selected; }
    @Override
    public boolean isSelected() { return selected; }

    @Override
    public void setColor(Color color) { colorStrategy.setColor(color); }
    @Override
    public Color getColor() { return colorStrategy.getColor(); }
    public void setColorStrategy(ColorStrategy strategy) { this.colorStrategy = strategy; }

    @Override
    public void resize(double newWidth, double newHeight) {
        // Keep startX/startY fixed, move endX/endY
        this.endX = this.startX + newWidth;
        this.endY = this.startY + newHeight;
    }
}