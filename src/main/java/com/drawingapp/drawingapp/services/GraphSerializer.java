package com.drawingapp.drawingapp.services;

import com.drawingapp.drawingapp.shapes_graph.GraphNode;
import com.drawingapp.drawingapp.shapes_graph.GraphEdge;
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
    
    public String serializeNodes(List<GraphNode> nodes) {
        return gson.toJson(nodes.stream()
            .filter(node -> node != null)
            .map(this::nodeToDto)
            .toArray());
    }
    
    public String serializeEdges(List<GraphEdge> edges) {
        return gson.toJson(edges.stream()
            .filter(edge -> edge != null)
            .map(this::edgeToDto)
            .toArray());
    }
    
    public List<GraphNode> deserializeNodes(String json) {
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
    
    public List<GraphEdge> deserializeEdges(List<GraphNode> nodes, String json) {
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
    
    private NodeDto nodeToDto(GraphNode node) {
        if (node == null) return null;
        try {
            NodeDto dto = new NodeDto();
            dto.id = node.getId();
            dto.x = node.getX();
            dto.y = node.getY();
            dto.color = node.getColor().toString();
            return dto;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting node to DTO", e);
            return null;
        }
    }
    
    private GraphNode dtoToNode(NodeDto dto) {
        if (dto == null) {
            LOGGER.warning("Invalid node DTO: null");
            return null;
        }
        try {
            GraphNode node = new GraphNode(dto.x, dto.y);
            node.setId(dto.id);
            node.setColor(Color.valueOf(dto.color));
            return node;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting DTO to node", e);
            return null;
        }
    }
    
    private EdgeDto edgeToDto(GraphEdge edge) {
        if (edge == null) return null;
        try {
            EdgeDto dto = new EdgeDto();
            dto.sourceId = edge.getSource().getId();
            dto.targetId = edge.getTarget().getId();
            dto.weight = edge.getWeight();
            dto.color = edge.getColor().toString();
            return dto;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting edge to DTO", e);
            return null;
        }
    }
    
    private GraphEdge dtoToEdge(EdgeDto dto, List<GraphNode> nodes) {
        if (dto == null) {
            LOGGER.warning("Invalid edge DTO: null");
            return null;
        }
        try {
            GraphNode source = findNodeById(nodes, dto.sourceId);
            GraphNode target = findNodeById(nodes, dto.targetId);
            if (source != null && target != null) {
                GraphEdge edge = new GraphEdge(source, target, dto.weight);
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
    
    private GraphNode findNodeById(List<GraphNode> nodes, int id) {
        return nodes.stream()
            .filter(node -> node != null && node.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    public static class NodeDto {
        public int id;
        public double x;
        public double y;
        public String color;
    }
    
    public static class EdgeDto {
        public int sourceId;
        public int targetId;
        public double weight;
        public String color;
    }
} 