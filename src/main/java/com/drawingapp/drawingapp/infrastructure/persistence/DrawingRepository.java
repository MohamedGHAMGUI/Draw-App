package com.drawingapp.drawingapp.infrastructure.persistence;

import java.util.List;
import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.domain.graph.Node;
import com.drawingapp.drawingapp.domain.graph.Edge;

public interface DrawingRepository {
    void saveDrawing(String name, List<?> shapes, List<?> nodes, List<?> edges);
    DrawingData loadDrawing(String name);
    List<String> listSavedDrawings();

    record DrawingData(
        List<Shape> shapes,
        List<Node> nodes,
        List<Edge> edges
    ) {}
} 