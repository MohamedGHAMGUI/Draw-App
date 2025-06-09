package com.drawingapp.drawingapp.domain.algorithms;

import com.drawingapp.drawingapp.domain.graph.*;

import java.util.*;

public class DijkstraAlgorithm implements ShortestPathAlgorithm {
    @Override
    public List<Node> findShortestPath(Graph graph, Node source, Node target) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(
            Comparator.comparingDouble(distances::get)
        );

        // Initialize distances
        for (Node node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);
        queue.add(source);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current == target) {
                break;
            }

            for (Edge edge : graph.getEdges()) {
                if (edge.getSource() == current) {
                    Node neighbor = edge.getTarget();
                    double newDistance = distances.get(current) + calculateDistance(current, neighbor);

                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        previousNodes.put(neighbor, current);
                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return reconstructPath(source, target, previousNodes);
    }

    private double calculateDistance(Node source, Node target) {
        double dx = target.getX() - source.getX();
        double dy = target.getY() - source.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private List<Node> reconstructPath(Node source, Node target, Map<Node, Node> previousNodes) {
        List<Node> path = new ArrayList<>();
        Node current = target;

        while (current != null) {
            path.add(0, current);
            current = previousNodes.get(current);
        }

        return path.get(0) == source ? path : new ArrayList<>();
    }
} 