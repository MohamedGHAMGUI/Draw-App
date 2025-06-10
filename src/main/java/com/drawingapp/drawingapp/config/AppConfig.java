package com.drawingapp.drawingapp.config;

import com.drawingapp.drawingapp.controllers.*;
import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.GraphManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.shapes_graph.DijkstraAlgorithm;
import com.drawingapp.drawingapp.shapes_graph.ShortestPathAlgorithm;
import com.drawingapp.drawingapp.commands.CommandManager;
import javafx.scene.canvas.GraphicsContext;

public class AppConfig {
    private final ShapeManager shapeManager;
    private final ModeManager modeManager;
    private final GraphManager graphManager;
    private final DrawingRepository drawingRepository;
    private final ShortestPathAlgorithm shortestPathAlgorithm;
    private final CommandManager commandManager;
    
    private DrawingModeController drawingModeController;
    private ShapeOperationController shapeOperationController;
    private GraphOperationController graphOperationController;
    private FileOperationController fileOperationController;
    
    public AppConfig(ShapeManager shapeManager, ModeManager modeManager, 
                     GraphManager graphManager, DrawingRepository drawingRepository) {
        this.shapeManager = shapeManager;
        this.modeManager = modeManager;
        this.graphManager = graphManager;
        this.drawingRepository = drawingRepository;
        this.shortestPathAlgorithm = new DijkstraAlgorithm();
        this.commandManager = new CommandManager();
        
        initializeControllers();
        wireControllers();
    }
    
    private void initializeControllers() {
        drawingModeController = new DrawingModeController(
            shapeManager, 
            modeManager, 
            graphManager, 
            drawingRepository,
            shortestPathAlgorithm
        );
        drawingModeController.setCommandManager(commandManager);
        
        shapeOperationController = new ShapeOperationController(
            shapeManager,
            modeManager,
            graphManager,
            drawingRepository
        );
        
        graphOperationController = new GraphOperationController(
            shapeManager,
            modeManager,
            graphManager,
            drawingRepository
        );
        
        fileOperationController = new FileOperationController(
            shapeManager,
            modeManager,
            graphManager,
            drawingRepository
        );
    }
    
    private void wireControllers() {
        shapeOperationController.setDrawingModeHandler(drawingModeController);
        graphOperationController.setDrawingModeHandler(drawingModeController);
        fileOperationController.setDrawingModeHandler(drawingModeController);
    }
    
    public void setGraphicsContext(GraphicsContext gc) {
        drawingModeController.setGraphicsContext(gc);
        shapeOperationController.setGraphicsContext(gc);
        graphOperationController.setGraphicsContext(gc);
        fileOperationController.setGraphicsContext(gc);
    }
    
    public DrawingModeHandler getDrawingModeHandler() {
        return drawingModeController;
    }
    
    public ShapeOperationHandler getShapeOperationHandler() {
        return shapeOperationController;
    }
    
    public GraphOperationHandler getGraphOperationHandler() {
        return graphOperationController;
    }
    
    public FileOperationHandler getFileOperationHandler() {
        return fileOperationController;
    }
    
    public CommandManager getCommandManager() {
        return commandManager;
    }
} 