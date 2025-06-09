package com.drawingapp.drawingapp.application.services;

import com.drawingapp.drawingapp.domain.graph.Node;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import java.util.List;

public class NodeSelector {
    private Node selectedNode;
    private final ILogger logger;

    public NodeSelector(List<Node> nodes, ILogger logger) {
        this.logger = logger;
    }

    public void selectNodeAt(double x, double y, List<Node> nodes) {
        for (Node node : nodes) {
            if (node.contains(x, y)) {
                setSelectedNode(node);
                logger.log("Selected node at (" + x + ", " + y + ")");
                return;
            }
        }
        clearSelection();
    }

    public void setSelectedNode(Node node) {
        if (selectedNode != null) {
            selectedNode.setSelected(false);
        }
        selectedNode = node;
        if (selectedNode != null) {
            selectedNode.setSelected(true);
        }
    }

    public Node getSelectedNode() {
        return selectedNode;
    }

    public void clearSelection() {
        setSelectedNode(null);
    }
} 