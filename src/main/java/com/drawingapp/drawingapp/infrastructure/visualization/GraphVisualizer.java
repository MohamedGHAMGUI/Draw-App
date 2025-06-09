package com.drawingapp.drawingapp.infrastructure.visualization;

import com.drawingapp.drawingapp.domain.graph.*;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.List;

public class GraphVisualizer {
    private final Canvas canvas;
    private final ILogger logger;
    private static final double NODE_RADIUS = 10;
    private static final double EDGE_WIDTH = 2;
    private static final Color NODE_COLOR = Color.BLUE;
    private static final Color EDGE_COLOR = Color.BLACK;
    private static final Color PATH_COLOR = Color.RED;
    private static final Color TEXT_COLOR = Color.BLACK;

    public GraphVisualizer(Canvas canvas, ILogger logger) {
        this.canvas = canvas;
        this.logger = logger;
    }

    public void drawGraph(Graph graph) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw edges
        for (Edge edge : graph.getEdges()) {
            drawEdge(gc, edge);
        }

        // Draw nodes
        for (Node node : graph.getNodes()) {
            drawNode(gc, node);
        }

        logger.log("Graph drawn on canvas");
    }

    public void drawPath(List<Node> path) {
        if (path == null || path.isEmpty()) {
            return;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(PATH_COLOR);
        gc.setLineWidth(EDGE_WIDTH * 2);

        for (int i = 0; i < path.size() - 1; i++) {
            Node source = path.get(i);
            Node target = path.get(i + 1);
            gc.strokeLine(source.getX(), source.getY(), target.getX(), target.getY());
        }

        logger.log("Path drawn on canvas");
    }

    private void drawNode(GraphicsContext gc, Node node) {
        gc.setFill(node.getColor() != null ? node.getColor() : NODE_COLOR);
        gc.fillOval(node.getX() - NODE_RADIUS, node.getY() - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        
        gc.setStroke(Color.BLACK);
        gc.strokeOval(node.getX() - NODE_RADIUS, node.getY() - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        if (node.getLabel() != null && !node.getLabel().isEmpty()) {
            gc.setFill(TEXT_COLOR);
            gc.setFont(Font.font(12));
            gc.fillText(node.getLabel(), node.getX() - NODE_RADIUS, node.getY() - NODE_RADIUS - 5);
        }
    }

    private void drawEdge(GraphicsContext gc, Edge edge) {
        gc.setStroke(edge.getColor() != null ? edge.getColor() : EDGE_COLOR);
        gc.setLineWidth(EDGE_WIDTH);
        gc.strokeLine(edge.getSource().getX(), edge.getSource().getY(), edge.getTarget().getX(), edge.getTarget().getY());

        if (edge.getLabel() != null && !edge.getLabel().isEmpty()) {
            double midX = (edge.getSource().getX() + edge.getTarget().getX()) / 2;
            double midY = (edge.getSource().getY() + edge.getTarget().getY()) / 2;
            gc.setFill(TEXT_COLOR);
            gc.setFont(Font.font(12));
            gc.fillText(edge.getLabel(), midX, midY);
        }
    }
} 