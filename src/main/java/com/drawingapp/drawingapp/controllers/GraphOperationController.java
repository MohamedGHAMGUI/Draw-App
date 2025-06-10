package com.drawingapp.drawingapp.controllers;

import com.drawingapp.drawingapp.logging.LoggerManager;
import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.GraphManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.ShapeManager;

public class GraphOperationController extends BaseController implements GraphOperationHandler {
    private DrawingModeHandler drawingModeHandler;
    
    public GraphOperationController(ShapeManager shapeManager, ModeManager modeManager, 
                                  GraphManager graphManager, DrawingRepository drawingRepository) {
        super(shapeManager, modeManager, graphManager, drawingRepository);
    }
    
    public void setDrawingModeHandler(DrawingModeHandler drawingModeHandler) {
        this.drawingModeHandler = drawingModeHandler;
    }

    @Override
    public void onAddNode() {
        if (drawingModeHandler instanceof DrawingModeController) {
            ((DrawingModeController) drawingModeHandler).setNodeDrawMode();
            LoggerManager.getInstance().log("Switched to node drawing mode");
        }
    }

    @Override
    public void onAddEdge() {
        if (drawingModeHandler instanceof DrawingModeController) {
            ((DrawingModeController) drawingModeHandler).setEdgeDrawMode();
            LoggerManager.getInstance().log("Switched to edge drawing mode");
        }
    }

    @Override
    public void onFindPath() {
        if (drawingModeHandler instanceof DrawingModeController) {
            ((DrawingModeController) drawingModeHandler).setPathFindMode();
            LoggerManager.getInstance().log("Switched to path finding mode");
        }
    }

    @Override
    public void onCreateExampleGraph() {
        graphManager.createExampleGraph();
        LoggerManager.getInstance().log("Created example graph");
        drawingModeHandler.redrawCanvas();
    }
} 