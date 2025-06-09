package com.drawingapp.drawingapp.infrastructure.serialization;

import com.drawingapp.drawingapp.domain.graph.*;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class GraphSerializer {
    private static final Logger LOGGER = Logger.getLogger(GraphSerializer.class.getName());
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    private final ILogger logger;

    public GraphSerializer(ILogger logger) {
        this.logger = logger;
    }
    
    public String serializeNodes(List<Node> nodes) {
        return gson.toJson(nodes.stream()
            .filter(node -> node != null)
            .map(this::nodeToDto)
            .toArray());
    }
    
    public String serializeEdges(List<Edge> edges) {
        return gson.toJson(edges.stream()
            .filter(edge -> edge != null)
            .map(this::edgeToDto)
            .toArray());
    }
    
    public List<Node> deserializeNodes(String json) {
        try {
            NodeDto[] dtos = gson.fromJson(json, NodeDto[].class);
            if (dtos == null) {
                LOGGER.warning("Deserialized JSON resulted in null array for nodes");
                return List.of();
            }
            return Arrays.stream(dtos)
                .filter(dto -> dto != null)
                .map(this::dtoToNode)
                .filter(node -> node != null)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deserializing nodes", e);
            return List.of();
        }
    }
    
    public List<Edge> deserializeEdges(List<Node> nodes, String json) {
        try {
            EdgeDto[] dtos = gson.fromJson(json, EdgeDto[].class);
            if (dtos == null) {
                LOGGER.warning("Deserialized JSON resulted in null array for edges");
                return List.of();
            }
            return Arrays.stream(dtos)
                .filter(dto -> dto != null)
                .map(dto -> dtoToEdge(dto, nodes))
                .filter(edge -> edge != null)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deserializing edges", e);
            return List.of();
        }
    }
    
    private NodeDto nodeToDto(Node node) {
        if (node == null) return null;
        try {
            NodeDto dto = new NodeDto();
            dto.label = node.getLabel();
            dto.x = node.getX();
            dto.y = node.getY();
            dto.color = node.getColor().toString();
            return dto;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting node to DTO", e);
            return null;
        }
    }
    
    private Node dtoToNode(NodeDto dto) {
        if (dto == null) {
            LOGGER.warning("Invalid node DTO: null");
            return null;
        }
        try {
            Node node = new Node(dto.x, dto.y, dto.label);
            node.setColor(Color.valueOf(dto.color));
            return node;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting DTO to node", e);
            return null;
        }
    }
    
    private EdgeDto edgeToDto(Edge edge) {
        if (edge == null) return null;
        try {
            EdgeDto dto = new EdgeDto();
            dto.sourceLabel = edge.getSource().getLabel();
            dto.targetLabel = edge.getTarget().getLabel();
            dto.label = edge.getLabel();
            dto.color = edge.getColor().toString();
            return dto;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting edge to DTO", e);
            return null;
        }
    }
    
    private Edge dtoToEdge(EdgeDto dto, List<Node> nodes) {
        if (dto == null) {
            LOGGER.warning("Invalid edge DTO: null");
            return null;
        }
        try {
            Node source = findNodeByLabel(nodes, dto.sourceLabel);
            Node target = findNodeByLabel(nodes, dto.targetLabel);
            if (source != null && target != null) {
                Edge edge = new Edge(source, target, dto.label);
                edge.setColor(Color.valueOf(dto.color));
                return edge;
            } else {
                LOGGER.warning("Could not find source or target node for edge");
                return null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting DTO to edge", e);
            return null;
        }
    }
    
    private Node findNodeByLabel(List<Node> nodes, String label) {
        return nodes.stream()
            .filter(node -> node != null && node.getLabel().equals(label))
            .findFirst()
            .orElse(null);
    }
    
    public static class NodeDto {
        public String label;
        public double x;
        public double y;
        public String color;
    }
    
    public static class EdgeDto {
        public String sourceLabel;
        public String targetLabel;
        public String label;
        public String color;
    }
} 