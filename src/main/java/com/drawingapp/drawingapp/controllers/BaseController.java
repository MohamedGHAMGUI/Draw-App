package com.drawingapp.drawingapp.controllers;

import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.GraphManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_graph.GraphEdge;
import com.drawingapp.drawingapp.shapes_graph.GraphNode;
import javafx.scene.canvas.GraphicsContext;
import com.drawingapp.drawingapp.logging.LoggerManager;

public abstract class BaseController {
    protected final ShapeManager shapeManager;
    protected final ModeManager modeManager;
    protected final GraphManager graphManager;
    protected final DrawingRepository drawingRepository;
    protected GraphicsContext gc;
    
    public BaseController(ShapeManager shapeManager, ModeManager modeManager, 
                        GraphManager graphManager, DrawingRepository drawingRepository) {
        this.shapeManager = shapeManager;
        this.modeManager = modeManager;
        this.graphManager = graphManager;
        this.drawingRepository = drawingRepository;
    }
    
    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }
    
    protected void redrawCanvas() {
        if (gc == null) {
            LoggerManager.getInstance().log("Cannot redraw: GraphicsContext is null");
            return;
        }
        
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        // Draw edges first
        for (GraphEdge edge : graphManager.getEdges()) {
            edge.draw(gc);
        }
        
        // Draw nodes on top
        for (GraphNode node : graphManager.getNodes()) {
            node.draw(gc);
        }
        
        // Draw other shapes
        for (Shape shape : shapeManager.getShapes()) {
            shape.draw(gc);
        }
    }
} 