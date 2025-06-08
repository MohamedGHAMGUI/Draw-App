package com.drawingapp.drawingapp.shapes_graph;

import com.drawingapp.drawingapp.services.GraphManager;
import java.util.List;

public interface ShortestPathAlgorithm {
    List<GraphNode> findShortestPath(GraphNode start, GraphNode end, GraphManager graphManager);
} 