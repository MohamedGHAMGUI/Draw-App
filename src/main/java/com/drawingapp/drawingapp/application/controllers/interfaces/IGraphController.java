package com.drawingapp.drawingapp.application.controllers.interfaces;

import javafx.scene.canvas.GraphicsContext;
import com.drawingapp.drawingapp.domain.algorithms.ShortestPathAlgorithm;

public interface IGraphController {
    void setGraphicsContext(GraphicsContext gc);
    void handleMousePressed(double x, double y);
    void redrawCanvas();
    void setAlgorithm(ShortestPathAlgorithm algorithm);
    void clearGraph();
} 