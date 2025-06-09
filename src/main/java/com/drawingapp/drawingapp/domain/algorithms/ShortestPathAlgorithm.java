package com.drawingapp.drawingapp.domain.algorithms;

import com.drawingapp.drawingapp.domain.graph.Graph;
import com.drawingapp.drawingapp.domain.graph.Node;
import java.util.List;

public interface ShortestPathAlgorithm {
    List<Node> findShortestPath(Graph graph, Node source, Node target);
} 