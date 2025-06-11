package com.drawingapp.drawingapp.services;

import com.drawingapp.drawingapp.shapes_graph.GraphNode;
import com.drawingapp.drawingapp.shapes_graph.GraphEdge;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GraphManager {
    private List<GraphNode> nodes;
    private List<GraphEdge> edges;
    private GraphNode selectedNode;
    private GraphNode startNode;
    private GraphNode endNode;

    public GraphManager() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public void addNode(GraphNode node) {
        nodes.add(node);
    }

    public void addEdge(GraphEdge edge) {
        edges.add(edge);
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public GraphNode getNodeAt(double x, double y) {
        for (GraphNode node : nodes) {
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public void setSelectedNode(GraphNode node) {
        this.selectedNode = node;
    }

    public GraphNode getSelectedNode() {
        return selectedNode;
    }

    public void clearSelection() {
        this.selectedNode = null;
    }

    public void setStartNode(GraphNode node) {
        this.startNode = node;
    }

    public GraphNode getStartNode() {
        return startNode;
    }

    public void setEndNode(GraphNode node) {
        this.endNode = node;
    }

    public GraphNode getEndNode() {
        return endNode;
    }

    public void clear() {
        nodes.clear();
        edges.clear();
        selectedNode = null;
        startNode = null;
        endNode = null;
    }

    public GraphNode getNodeById(int id) {
        if (id >= 0 && id < nodes.size()) {
            return nodes.get(id);
        }
        return null;
    }

    public void createExampleGraph() {
        clear();

        GraphNode nodeA = new GraphNode(100, 100);
        GraphNode nodeB = new GraphNode(300, 100);
        GraphNode nodeC = new GraphNode(100, 300);
        GraphNode nodeD = new GraphNode(300, 300);
        

        addNode(nodeA);
        addNode(nodeB);
        addNode(nodeC);
        addNode(nodeD);

        GraphEdge edgeAB = new GraphEdge(nodeA, nodeB, 5.0);
        GraphEdge edgeAC = new GraphEdge(nodeA, nodeC, 2.0);
        GraphEdge edgeBD = new GraphEdge(nodeB, nodeD, 1.0);
        GraphEdge edgeCD = new GraphEdge(nodeC, nodeD, 3.0);

        addEdge(edgeAB);
        addEdge(edgeAC);
        addEdge(edgeBD);
        addEdge(edgeCD);
    }

    public Map<GraphNode, List<GraphEdge>> getAdjacencyList() {
        Map<GraphNode, List<GraphEdge>> adjacencyList = new HashMap<>();
        for (GraphNode node : nodes) {
            adjacencyList.put(node, new ArrayList<>());
        }
        for (GraphEdge edge : edges) {
            adjacencyList.get(edge.getSource()).add(edge);
            adjacencyList.get(edge.getTarget()).add(edge);
        }
        return adjacencyList;
    }
} 
