package com.drawingapp.drawingapp.shapes_graph;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GraphEdge implements Shape {
    private GraphNode source;
    private GraphNode target;
    private double weight;
    private Color color = Color.BLACK;
    private boolean selected = false;
    private static final double PIXELS_PER_UNIT = 20.0; // 20 pixels = 1 unit
    private double x;
    private double y;
    private double width;
    private double height;
    private int id;

    public GraphEdge(GraphNode source, GraphNode target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        source.addEdge(this);
        target.addEdge(this);
        updateDimensions();
    }

    private void updateDimensions() {
        this.x = Math.min(source.getX(), target.getX());
        this.y = Math.min(source.getY(), target.getY());
        this.width = Math.abs(target.getX() - source.getX());
        this.height = Math.abs(target.getY() - source.getY());
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Draw the line
        gc.setStroke(color);
        gc.setLineWidth(selected ? 3 : 2);
        gc.strokeLine(source.getX(), source.getY(), target.getX(), target.getY());
        
        // Calculate the midpoint for the labels
        double midX = (source.getX() + target.getX()) / 2;
        double midY = (source.getY() + target.getY()) / 2;
        
        // Calculate actual distance between nodes in units
        double dx = target.getX() - source.getX();
        double dy = target.getY() - source.getY();
        double distanceInPixels = Math.sqrt(dx * dx + dy * dy);
        double distanceInUnits = distanceInPixels / PIXELS_PER_UNIT;
        
        // Draw white background for weight
        gc.setFill(Color.WHITE);
        gc.fillOval(midX - 15, midY - 20, 30, 30);
        
        // Draw weight text
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        String weightText = String.format("W:%.0f", weight);
        double textWidth = gc.getFont().getSize() * weightText.length() / 2;
        double textHeight = gc.getFont().getSize();
        gc.fillText(weightText, midX - textWidth/2, midY - 5);
        
        // Draw white background for distance
        gc.setFill(Color.WHITE);
        gc.fillOval(midX - 15, midY + 5, 30, 30);
        
        // Draw distance text
        gc.setFill(Color.BLUE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        String distanceText = String.format("D:%.1f u", distanceInUnits);
        textWidth = gc.getFont().getSize() * distanceText.length() / 2;
        gc.fillText(distanceText, midX - textWidth/2, midY + 20);
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        // Not used for edges
    }

    @Override
    public boolean contains(double x, double y) {
        // Check if point is near the line
        double x1 = source.getX();
        double y1 = source.getY();
        double x2 = target.getX();
        double y2 = target.getY();
        
        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        
        if (len_sq != 0) {
            param = dot / len_sq;
        }

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy) < 5;
    }

    @Override
    public void move(double dx, double dy) {
        // Edges move with their nodes
        updateDimensions();
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
        // Not used for edges
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
        // Not used for edges
    }

    @Override
    public void setY(double y) {
        // Not used for edges
    }

    @Override
    public void setWidth(double width) {
        // Not used for edges
    }

    @Override
    public void setHeight(double height) {
        // Not used for edges
    }

    public GraphNode getSource() {
        return source;
    }

    public GraphNode getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
} 