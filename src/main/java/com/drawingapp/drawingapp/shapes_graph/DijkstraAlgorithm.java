package com.drawingapp.drawingapp.shapes_graph;
import com.drawingapp.drawingapp.services.GraphManager;

import java.util.*;

public class DijkstraAlgorithm implements ShortestPathAlgorithm {
    @Override
    public List<GraphNode> findShortestPath(GraphNode start, GraphNode end, GraphManager graphManager) {
        Map<GraphNode, Double> distances = new HashMap<>();
        Map<GraphNode, GraphNode> previousNodes = new HashMap<>();
        PriorityQueue<GraphNode> queue = new PriorityQueue<>(
            Comparator.comparingDouble(distances::get)
        );

        // Initialize distances
        for (GraphNode node : graphManager.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();
            if (current == end) break;

            for (GraphEdge edge : graphManager.getAdjacencyList().get(current)) {
                GraphNode neighbor = edge.getTarget() == current ? edge.getSource() : edge.getTarget();
                double newDistance = distances.get(current) + edge.getWeight();

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previousNodes.put(neighbor, current);
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        // Reconstruct path
        List<GraphNode> path = new ArrayList<>();
        GraphNode current = end;
        while (current != null) {
            path.add(0, current);
            current = previousNodes.get(current);
        }

        return path.isEmpty() || path.get(0) != start ? new ArrayList<>() : path;
    }
} 