package com.drawingapp.drawingapp.infrastructure.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.scene.paint.Color;
import java.io.IOException;
import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.domain.graph.Node;
import com.drawingapp.drawingapp.domain.graph.Edge;
import com.drawingapp.drawingapp.shapes_adapter.SvgStarAdapter;
import com.drawingapp.drawingapp.shapes_adapter.SvgStar;
import com.drawingapp.drawingapp.domain.shapes.CircleShape;
import com.drawingapp.drawingapp.domain.shapes.LineShape;
import com.drawingapp.drawingapp.domain.shapes.RectangleShape;
import com.drawingapp.drawingapp.domain.shapes.RotatableShape;
import java.util.stream.Collectors;

public class JdbcDrawingRepository implements DrawingRepository {
    private final String url;
    private final String user;
    private final String password;
    private final Gson gson;

    public JdbcDrawingRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.gson = new GsonBuilder()
            .registerTypeAdapter(Color.class, new ColorTypeAdapter())
            .registerTypeAdapter(SvgStarAdapter.class, new SvgStarAdapterTypeAdapter())
            .registerTypeAdapter(Shape.class, new ShapeTypeAdapter())
            .setPrettyPrinting()
            .create();
        createTablesIfNotExist();
    }

    private static class ShapeTypeAdapter extends TypeAdapter<Shape> {
        @Override
        public void write(JsonWriter out, Shape shape) throws IOException {
            if (shape == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name("type").value(shape.getClass().getSimpleName());
            out.name("x").value(shape.getX());
            out.name("y").value(shape.getY());
            out.name("width").value(shape.getWidth());
            out.name("height").value(shape.getHeight());
            
            // Write color as an object
            Color color = shape.getColor();
            out.name("color").beginObject()
                .name("red").value(color.getRed())
                .name("green").value(color.getGreen())
                .name("blue").value(color.getBlue())
                .name("opacity").value(color.getOpacity())
                .endObject();
                
            if (shape instanceof RotatableShape) {
                out.name("rotationAngle").value(((RotatableShape) shape).getRotationAngle());
            }
            out.endObject();
        }

        @Override
        public Shape read(JsonReader in) throws IOException {
            in.beginObject();
            String type = null;
            double x = 0, y = 0, width = 100, height = 100, rotationAngle = 0;
            Color color = Color.BLACK;
            
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "type": type = in.nextString(); break;
                    case "x": x = in.nextDouble(); break;
                    case "y": y = in.nextDouble(); break;
                    case "width": width = in.nextDouble(); break;
                    case "height": height = in.nextDouble(); break;
                    case "color": 
                        in.beginObject();
                        double red = 0, green = 0, blue = 0, opacity = 1;
                        while (in.hasNext()) {
                            String colorName = in.nextName();
                            switch (colorName) {
                                case "red": red = in.nextDouble(); break;
                                case "green": green = in.nextDouble(); break;
                                case "blue": blue = in.nextDouble(); break;
                                case "opacity": opacity = in.nextDouble(); break;
                                default: in.skipValue();
                            }
                        }
                        color = new Color(red, green, blue, opacity);
                        in.endObject();
                        break;
                    case "rotationAngle": rotationAngle = in.nextDouble(); break;
                    default: in.skipValue();
                }
            }
            in.endObject();
            
            Shape shape = null;
            if (type != null) {
                switch (type) {
                    case "CircleShape":
                        shape = new CircleShape(x, y, width, height, color);
                        break;
                    case "LineShape":
                        shape = new LineShape(x, y, width, height, color);
                        break;
                    case "RectangleShape":
                        shape = new RectangleShape(x, y, width, height, color);
                        break;
                    case "SvgStarAdapter":
                        shape = new SvgStarAdapter(new SvgStar());
                        shape.setX(x);
                        shape.setY(y);
                        shape.setWidth(width);
                        shape.setHeight(height);
                        shape.setColor(color);
                        break;
                }
                
                if (shape instanceof RotatableShape) {
                    ((RotatableShape) shape).setRotationAngle(rotationAngle);
                }
            }
            
            return shape;
        }
    }

    private static class SvgStarAdapterTypeAdapter extends TypeAdapter<SvgStarAdapter> {
        @Override
        public void write(JsonWriter out, SvgStarAdapter adapter) throws IOException {
            if (adapter == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name("x").value(adapter.getX());
            out.name("y").value(adapter.getY());
            out.name("width").value(adapter.getWidth());
            out.name("height").value(adapter.getHeight());
            out.name("color").value(adapter.getColor().toString());
            out.name("rotationAngle").value(adapter.getRotationAngle());
            out.endObject();
        }

        @Override
        public SvgStarAdapter read(JsonReader in) throws IOException {
            in.beginObject();
            double x = 0, y = 0, width = 100, height = 100, rotationAngle = 0;
            Color color = Color.BLACK;
            
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "x": x = in.nextDouble(); break;
                    case "y": y = in.nextDouble(); break;
                    case "width": width = in.nextDouble(); break;
                    case "height": height = in.nextDouble(); break;
                    case "color": color = Color.valueOf(in.nextString()); break;
                    case "rotationAngle": rotationAngle = in.nextDouble(); break;
                    default: in.skipValue();
                }
            }
            in.endObject();
            
            SvgStarAdapter adapter = new SvgStarAdapter(new SvgStar());
            adapter.setX(x);
            adapter.setY(y);
            adapter.setWidth(width);
            adapter.setHeight(height);
            adapter.setColor(color);
            adapter.setRotationAngle(rotationAngle);
            return adapter;
        }
    }

    private static class ColorTypeAdapter extends TypeAdapter<Color> {
        @Override
        public void write(JsonWriter out, Color color) throws IOException {
            if (color == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name("red").value(color.getRed());
            out.name("green").value(color.getGreen());
            out.name("blue").value(color.getBlue());
            out.name("opacity").value(color.getOpacity());
            out.endObject();
        }

        @Override
        public Color read(JsonReader in) throws IOException {
            in.beginObject();
            double red = 0, green = 0, blue = 0, opacity = 1;
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "red": red = in.nextDouble(); break;
                    case "green": green = in.nextDouble(); break;
                    case "blue": blue = in.nextDouble(); break;
                    case "opacity": opacity = in.nextDouble(); break;
                    default: in.skipValue();
                }
            }
            in.endObject();
            return new Color(red, green, blue, opacity);
        }
    }

    private void createTablesIfNotExist() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            try (Statement stmt = conn.createStatement()) {
                // Create table only if it doesn't exist
                stmt.execute("CREATE TABLE IF NOT EXISTS drawings (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "shapes_data TEXT," +
                    "nodes_data TEXT," +
                    "edges_data TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
                System.out.println("Database tables checked/created successfully");
            }
        } catch (SQLException e) {
            System.err.println("Failed to create tables: " + e.getMessage());
            throw new RuntimeException("Failed to create tables: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveDrawing(String name, List<?> shapes, List<?> nodes, List<?> edges) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO drawings (name, shapes_data, nodes_data, edges_data) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                
                // Convert shapes to JSON
                String shapesJson = gson.toJson(shapes);
                pstmt.setString(2, shapesJson);
                
                // Convert nodes to JSON
                String nodesJson = gson.toJson(nodes);
                pstmt.setString(3, nodesJson);
                
                // Convert edges to JSON
                String edgesJson = gson.toJson(edges);
                pstmt.setString(4, edgesJson);
                
                pstmt.executeUpdate();
                System.out.println("Drawing saved successfully: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Failed to save drawing: " + e.getMessage());
            throw new RuntimeException("Failed to save drawing: " + e.getMessage(), e);
        }
    }

    @Override
    public DrawingData loadDrawing(String name) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT shapes_data, nodes_data, edges_data FROM drawings WHERE name = ? ORDER BY created_at DESC LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String shapesJson = rs.getString("shapes_data");
                        System.out.println("Loading shapes data: " + shapesJson);
                        
                        List<Shape> shapes = new ArrayList<>();
                        List<ShapeDto> dtos = gson.fromJson(shapesJson, 
                            new com.google.gson.reflect.TypeToken<List<ShapeDto>>(){}.getType());
                            
                        if (dtos != null) {
                            for (ShapeDto dto : dtos) {
                                if (dto != null) {
                                    // Create a RectangleShape since that's what we saved
                                    Shape shape = new RectangleShape(
                                        dto.x, dto.y, dto.width, dto.height,
                                        new Color(dto.color.red, dto.color.green, dto.color.blue, dto.color.opacity)
                                    );
                                    if (shape instanceof RotatableShape) {
                                        ((RotatableShape) shape).setRotationAngle(dto.rotationAngle);
                                    }
                                    shapes.add(shape);
                                }
                            }
                        }
                        
                        List<Node> nodes = gson.fromJson(rs.getString("nodes_data"), 
                            new com.google.gson.reflect.TypeToken<List<Node>>(){}.getType());
                        List<Edge> edges = gson.fromJson(rs.getString("edges_data"), 
                            new com.google.gson.reflect.TypeToken<List<Edge>>(){}.getType());
                            
                        System.out.println("Drawing loaded successfully: " + name);
                        return new DrawingData(shapes, nodes, edges);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to load drawing: " + e.getMessage());
            throw new RuntimeException("Failed to load drawing: " + e.getMessage(), e);
        }
        System.out.println("No drawing found with name: " + name);
        return null;
    }

    public static class ShapeDto {
        public double x;
        public double y;
        public double width;
        public double height;
        public ColorDto color;
        public boolean selected;
        public double rotationAngle;
    }

    public static class ColorDto {
        public double red;
        public double green;
        public double blue;
        public double opacity;
    }

    @Override
    public List<String> listSavedDrawings() {
        List<String> drawings = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // First, let's check if we can connect and see the data
            String checkSql = "SELECT COUNT(*) FROM drawings";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(checkSql)) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Total drawings in database: " + count);
                }
            }

            // Now get the list of drawings - include id in the SELECT for proper ordering
            String sql = "SELECT DISTINCT id, name FROM drawings ORDER BY id DESC";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    drawings.add(name);
                    System.out.println("Found drawing: " + name);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw new RuntimeException("Failed to list drawings: " + e.getMessage(), e);
        }
        return drawings;
    }
} 