package com.drawingapp.drawingapp.services;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_graph.GraphNode;
import com.drawingapp.drawingapp.shapes_graph.GraphEdge;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDrawingRepository implements DrawingRepository {
    private final Connection connection;
    private final ShapeSerializer shapeSerializer;
    private final GraphSerializer graphSerializer;
    
    public JdbcDrawingRepository(Connection connection) throws SQLException {
        this.connection = connection;
        this.shapeSerializer = new ShapeSerializer();
        this.graphSerializer = new GraphSerializer();
        createTablesIfNotExists();
    }
    
    private void createTablesIfNotExists() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS drawings (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) UNIQUE," +
                "shapes_data TEXT," +
                "nodes_data TEXT," +
                "edges_data TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }
    }
    
    @Override
    public void saveDrawing(String name, List<Shape> shapes, List<GraphNode> nodes, List<GraphEdge> edges) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO drawings (name, shapes_data, nodes_data, edges_data) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE shapes_data = ?, nodes_data = ?, edges_data = ?")) {
            
            String shapesJson = shapeSerializer.serialize(shapes);
            String nodesJson = graphSerializer.serializeNodes(nodes);
            String edgesJson = graphSerializer.serializeEdges(edges);
            
            ps.setString(1, name);
            ps.setString(2, shapesJson);
            ps.setString(3, nodesJson);
            ps.setString(4, edgesJson);
            ps.setString(5, shapesJson);
            ps.setString(6, nodesJson);
            ps.setString(7, edgesJson);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save drawing", e);
        }
    }
    
    @Override
    public DrawingData loadDrawing(String name) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT shapes_data, nodes_data, edges_data FROM drawings WHERE name = ?")) {
            
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String shapesJson = rs.getString("shapes_data");
                    String nodesJson = rs.getString("nodes_data");
                    String edgesJson = rs.getString("edges_data");
                    
                    List<Shape> shapes = shapeSerializer.deserialize(shapesJson);
                    List<GraphNode> nodes = graphSerializer.deserializeNodes(nodesJson);
                    List<GraphEdge> edges = graphSerializer.deserializeEdges(nodes, edgesJson);
                    
                    return new DrawingData(shapes, nodes, edges);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load drawing", e);
        }
    }
    
    @Override
    public List<String> listSavedDrawings() {
        List<String> drawings = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM drawings ORDER BY created_at DESC")) {
            
            while (rs.next()) {
                drawings.add(rs.getString("name"));
            }
            return drawings;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list drawings", e);
        }
    }
    
    @Override
    public void deleteDrawing(String name) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM drawings WHERE name = ?")) {
            
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete drawing", e);
        }
    }
} 