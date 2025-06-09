package com.drawingapp.drawingapp.application.services;

import com.drawingapp.drawingapp.shapes_graph.GraphNode;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.algorithms.ShortestPathAlgorithm;
import com.drawingapp.drawingapp.domain.algorithms.DijkstraAlgorithm;
import com.drawingapp.drawingapp.domain.graph.Graph;
import com.drawingapp.drawingapp.domain.graph.Node;

import java.util.List;

public class PathFinder {
    private final GraphManager graphManager;
    private final ILogger logger;
    private ShortestPathAlgorithm algorithm;
    private Node startNode;
    private Node endNode;

    public PathFinder(GraphManager graphManager, ILogger logger) {
        this.graphManager = graphManager;
        this.logger = logger;
        this.algorithm = new DijkstraAlgorithm();
    }

    public void setAlgorithm(ShortestPathAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setStartNode(Node node) {
        this.startNode = node;
        if (node != null) {
            node.setColor(javafx.scene.paint.Color.GREEN);
            logger.log("Set start node for path finding");
        }
    }

    public void setEndNode(Node node) {
        this.endNode = node;
        if (node != null) {
            node.setColor(javafx.scene.paint.Color.RED);
            findPath();
        }
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    private void findPath() {
        if (startNode != null && endNode != null) {
            Graph graph = graphManager.getGraph();
            Node source = graph.getNodes().stream().filter(n -> n.getLabel().equals(startNode.getLabel())).findFirst().orElse(null);
            Node target = graph.getNodes().stream().filter(n -> n.getLabel().equals(endNode.getLabel())).findFirst().orElse(null);
            if (source != null && target != null) {
                List<Node> path = algorithm.findShortestPath(graph, source, target);
                for (Node nodeInPath : path) {
                    nodeInPath.setColor(javafx.scene.paint.Color.YELLOW);
                }
                logger.log("Found shortest path with " + path.size() + " nodes");
            }
        }
    }

    public void clear() {
        startNode = null;
        endNode = null;
    }
} 