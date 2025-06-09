package com.drawingapp.drawingapp.application.services;

import com.drawingapp.drawingapp.domain.graph.*;
import com.drawingapp.drawingapp.domain.algorithms.DijkstraAlgorithm;
import com.drawingapp.drawingapp.domain.algorithms.ShortestPathAlgorithm;

import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import java.util.List;

public class GraphManager {
    public enum Mode {
        NODE_DRAW,
        EDGE_DRAW,
        PATH_FIND
    }

    private final Graph graph;
    private Node selectedNode;
    private Edge selectedEdge;
    private final ILogger logger;
    private final ShortestPathAlgorithm pathAlgorithm;
    private Mode currentMode = Mode.NODE_DRAW;
    private final NodeSelector nodeSelector;

    public GraphManager(Graph graph, ShortestPathAlgorithm pathAlgorithm, ILogger logger, NodeSelector nodeSelector) {
        this.graph = graph;
        this.pathAlgorithm = pathAlgorithm;
        this.logger = logger;
        this.nodeSelector = nodeSelector;
    }

    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
        logger.log("Graph mode set to: " + mode);
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void addNode(Node node) {
        graph.addNode(node);
        logger.log("Added node at (" + node.getX() + ", " + node.getY() + ")");
    }

    public void removeNode(Node node) {
        graph.removeNode(node);
        if (selectedNode == node) {
            selectedNode = null;
        }
        logger.log("Removed node at (" + node.getX() + ", " + node.getY() + ")");
    }

    public void addEdge(Edge edge) {
        graph.addEdge(edge);
        logger.log("Added edge from node " + edge.getSource().getLabel() + " to node " + edge.getTarget().getLabel());
    }

    public void removeEdge(Edge edge) {
        graph.removeEdge(edge);
        if (selectedEdge == edge) {
            selectedEdge = null;
        }
        logger.log("Removed edge from node " + edge.getSource().getLabel() + " to node " + edge.getTarget().getLabel());
    }

    public void selectNodeAt(double x, double y) {
        Node node = graph.findNodeAt(x, y);
        if (node != null) {
            if (selectedNode != null) {
                selectedNode.setSelected(false);
            }
            selectedNode = node;
            selectedNode.setSelected(true);
            logger.log("Selected node: " + node.getLabel());
        } else {
            clearNodeSelection();
        }
    }

    public void selectEdgeAt(double x, double y) {
        Edge edge = graph.findEdgeAt(x, y);
        if (edge != null) {
            if (selectedEdge != null) {
                selectedEdge.setSelected(false);
            }
            selectedEdge = edge;
            selectedEdge.setSelected(true);
            logger.log("Selected edge: " + edge.getLabel());
        } else {
            clearEdgeSelection();
        }
    }

    public void clearNodeSelection() {
        if (selectedNode != null) {
            selectedNode.setSelected(false);
            selectedNode = null;
            logger.log("Cleared node selection");
        }
    }

    public void clearEdgeSelection() {
        if (selectedEdge != null) {
            selectedEdge.setSelected(false);
            selectedEdge = null;
            logger.log("Cleared edge selection");
        }
    }

    public void clearSelection() {
        clearNodeSelection();
        clearEdgeSelection();
    }

    public Node getSelectedNode() {
        return selectedNode;
    }

    public Edge getSelectedEdge() {
        return selectedEdge;
    }

    public Graph getGraph() {
        return graph;
    }

    public void clear() {
        graph.clear();
        clearSelection();
        logger.log("Cleared graph");
    }

    public List<Node> findShortestPath(Node source, Node target) {
        List<Node> path = pathAlgorithm.findShortestPath(graph, source, target);
        logger.log("Found shortest path from node " + source.getLabel() + " to node " + target.getLabel());
        return path;
    }

    public Node getNodeAt(double x, double y) {
        for (Node node : graph.getNodes()) {
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public NodeSelector getNodeSelector() {
        return nodeSelector;
    }

    public List<Edge> getEdges() {
        return graph.getEdges();
    }

    public List<Node> getNodes() {
        return graph.getNodes();
    }
} 