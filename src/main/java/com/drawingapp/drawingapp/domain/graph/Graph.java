package com.drawingapp.drawingapp.domain.graph;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Node> nodes;
    private final List<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        edges.removeIf(edge -> edge.getSource() == node || edge.getTarget() == node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public void draw(GraphicsContext gc) {
        // Draw edges first (so they appear behind nodes)
        for (Edge edge : edges) {
            edge.draw(gc);
        }
        
        // Draw nodes on top
        for (Node node : nodes) {
            node.draw(gc);
        }
    }

    public Node findNodeAt(double x, double y) {
        for (Node node : nodes) {
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public Edge findEdgeAt(double x, double y) {
        for (Edge edge : edges) {
            if (edge.contains(x, y)) {
                return edge;
            }
        }
        return null;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void clear() {
        nodes.clear();
        edges.clear();
    }
} 