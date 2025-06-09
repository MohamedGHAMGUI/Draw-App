package com.drawingapp.drawingapp.shapes_graph;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class GraphNode implements Shape {
    private double x;
    private double y;
    private double width = NODE_RADIUS * 2;
    private double height = NODE_RADIUS * 2;
    private Color color = Color.BLACK;
    private boolean selected = false;
    private List<GraphEdge> edges;
    private static final double NODE_RADIUS = 10.0;
    private int id;

    public GraphNode(double x, double y) {
        this.x = x;
        this.y = y;
        this.edges = new ArrayList<>();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        gc.setStroke(selected ? Color.BLACK : Color.GRAY);
        gc.setLineWidth(selected ? 2 : 1);
        gc.strokeOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        this.x = x;
        this.y = y;
        draw(gc);
    }

    @Override
    public boolean contains(double x, double y) {
        double dx = this.x - x;
        double dy = this.y - y;
        return Math.sqrt(dx * dx + dy * dy) <= NODE_RADIUS;
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
        // Not used for nodes
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

    public void addEdge(GraphEdge edge) {
        edges.add(edge);
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
} 