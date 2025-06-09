package com.drawingapp.drawingapp.domain.graph;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Edge {
    private Node source;
    private Node target;
    private String label;
    private Color color;
    private boolean selected;
    private static final double PIXELS_PER_UNIT = 20.0; // 20 pixels = 1 unit

    public Edge(Node source, Node target, String label) {
        this.source = source;
        this.target = target;
        this.label = label;
        this.color = Color.BLACK;
        this.selected = false;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setStroke(color);
        gc.setLineWidth(selected ? 2 : 1);
        
        double startX = source.getX();
        double startY = source.getY();
        double endX = target.getX();
        double endY = target.getY();
        
        // Draw line
        gc.strokeLine(startX, startY, endX, endY);
        
        // Draw arrow
        double angle = Math.atan2(endY - startY, endX - startX);
        double arrowLength = 10;
        double arrowAngle = Math.PI / 6;
        
        double x1 = endX - arrowLength * Math.cos(angle - arrowAngle);
        double y1 = endY - arrowLength * Math.sin(angle - arrowAngle);
        double x2 = endX - arrowLength * Math.cos(angle + arrowAngle);
        double y2 = endY - arrowLength * Math.sin(angle + arrowAngle);
        
        gc.strokeLine(endX, endY, x1, y1);
        gc.strokeLine(endX, endY, x2, y2);
        
        // Calculate midpoint for labels
        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;
        
        // Calculate distance between nodes
        double dx = endX - startX;
        double dy = endY - startY;
        double distanceInPixels = Math.sqrt(dx * dx + dy * dy);
        double distanceInUnits = distanceInPixels / PIXELS_PER_UNIT;
        
        // Draw white background for distance
        gc.setFill(Color.WHITE);
        gc.fillOval(midX - 15, midY - 10, 30, 20);
        
        // Draw distance text
        gc.setFill(Color.BLUE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        String distanceText = String.format("D:%.1f", distanceInUnits);
        double textWidth = gc.getFont().getSize() * distanceText.length() / 2;
        gc.fillText(distanceText, midX - textWidth/2, midY + 5);
        
        gc.restore();
    }

    public boolean contains(double x, double y) {
        double startX = source.getX();
        double startY = source.getY();
        double endX = target.getX();
        double endY = target.getY();
        
        // Calculate distance from point to line
        double lineLength = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        double distance = Math.abs((endY - startY) * x - (endX - startX) * y + endX * startY - endY * startX) / lineLength;
        return distance <= 5; // 5 pixels tolerance
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

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
} 