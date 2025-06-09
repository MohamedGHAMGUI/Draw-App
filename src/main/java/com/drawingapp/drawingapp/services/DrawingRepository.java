package com.drawingapp.drawingapp.services;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_graph.GraphNode;
import com.drawingapp.drawingapp.shapes_graph.GraphEdge;
import java.util.List;

public interface DrawingRepository {
    void saveDrawing(String name, List<Shape> shapes, List<GraphNode> nodes, List<GraphEdge> edges);
    DrawingData loadDrawing(String name);
    List<String> listSavedDrawings();
    void deleteDrawing(String name);
    
    class DrawingData {
        private final List<Shape> shapes;
        private final List<GraphNode> nodes;
        private final List<GraphEdge> edges;
        
        public DrawingData(List<Shape> shapes, List<GraphNode> nodes, List<GraphEdge> edges) {
            this.shapes = shapes;
            this.nodes = nodes;
            this.edges = edges;
        }
        
        // Getters
        public List<Shape> getShapes() { return shapes; }
        public List<GraphNode> getNodes() { return nodes; }
        public List<GraphEdge> getEdges() { return edges; }
    }
} 