package com.drawingapp.drawingapp.controllers;

import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.GraphManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.shapes_decorator.ResizableShape;
import com.drawingapp.drawingapp.shapes_decorator.RotatableShapeDecorator;
import com.drawingapp.drawingapp.shapes_factory.RotatableShape;
import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_factory.ShapeFactory;
import com.drawingapp.drawingapp.shapes_graph.GraphEdge;
import com.drawingapp.drawingapp.shapes_graph.GraphNode;
import com.drawingapp.drawingapp.shapes_graph.ShortestPathAlgorithm;
import com.drawingapp.drawingapp.shapes_state_observer.MoveState;
import com.drawingapp.drawingapp.shapes_state_observer.SelectState;
import com.drawingapp.drawingapp.shapes_state_observer.State;
import com.drawingapp.drawingapp.logging.LoggerManager;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import com.drawingapp.drawingapp.commands.CommandManager;
import com.drawingapp.drawingapp.commands.AddShapeCommand;

import java.util.List;

public class DrawingModeController extends BaseController implements DrawingModeHandler {
    private Shape resizingShape;
    private double initialX, initialY;
    private String selectedShape = "";
    private State currentState;
    private SelectState selectState;
    private MoveState moveState;
    private Color currentColor = Color.BLACK;
    private ShortestPathAlgorithm currentAlgorithm;
    private CommandManager commandManager;
    
    public DrawingModeController(ShapeManager shapeManager, ModeManager modeManager, 
                               GraphManager graphManager, DrawingRepository drawingRepository,
                               ShortestPathAlgorithm algorithm) {
        super(shapeManager, modeManager, graphManager, drawingRepository);
        this.currentAlgorithm = algorithm;
        selectState = new SelectState(shapeManager.getShapes(), shapeManager);
        moveState = new MoveState();
        currentState = selectState;
    }
    
    public void setSelectedShape(String selectedShape) {
        this.selectedShape = selectedShape;
    }
    
    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void handleMousePressed(MouseEvent event) {
        switch (modeManager.getCurrentMode()) {
            case RESIZE:
                Shape selected = shapeManager.getSelectedShape();
                if (selected != null) {
                    resizingShape = selected;
                    initialX = event.getX();
                    initialY = event.getY();
                    LoggerManager.getInstance().log(String.format("Started resizing shape at (%.2f, %.2f) with initial size: %.2f x %.2f", 
                        initialX, initialY, selected.getWidth(), selected.getHeight()));
                }
                break;
            case DRAW:
                if (selectedShape != null && !selectedShape.isEmpty()) {
                    Shape shape = ShapeFactory.createShape(selectedShape);
                    if (shape != null) {
                        shape.setColor(currentColor);
                        shape = new ResizableShape(shape);
                        // Add rotation capability for shapes that can be rotated
                        if (selectedShape.equals("rectangle") || selectedShape.equals("line") || selectedShape.equals("star")) {
                            shape = new RotatableShapeDecorator(shape);
                        }
                        // Use Command Pattern for adding shape
                        if (commandManager != null) {
                            commandManager.executeCommand(new AddShapeCommand(shapeManager, shape));
                        } else {
                            shapeManager.addShape(shape);
                        }
                        shape.draw(gc, event.getX(), event.getY());
                        LoggerManager.getInstance().log(String.format("Created new %s at (%.2f, %.2f) with color %s", 
                            selectedShape, event.getX(), event.getY(), currentColor.toString()));
                    }
                }
                break;
            case NODE_DRAW:
                GraphNode node = new GraphNode(event.getX(), event.getY());
                graphManager.addNode(node);
                LoggerManager.getInstance().log(String.format("Added graph node at (%.2f, %.2f)", event.getX(), event.getY()));
                redrawCanvas();
                break;
            case EDGE_DRAW:
                GraphNode selectedNode = graphManager.getNodeAt(event.getX(), event.getY());
                if (selectedNode != null) {
                    if (graphManager.getSelectedNode() == null) {
                        graphManager.setSelectedNode(selectedNode);
                        LoggerManager.getInstance().log(String.format("Selected node at (%.2f, %.2f) for edge creation", 
                            selectedNode.getX(), selectedNode.getY()));
                    } else {
                        GraphEdge edge = new GraphEdge(graphManager.getSelectedNode(), selectedNode, 1.0);
                        graphManager.addEdge(edge);
                        graphManager.clearSelection();
                        LoggerManager.getInstance().log(String.format("Created edge between nodes at (%.2f, %.2f) and (%.2f, %.2f)", 
                            edge.getSource().getX(), edge.getSource().getY(),
                            edge.getTarget().getX(), edge.getTarget().getY()));
                    }
                    redrawCanvas();
                }
                break;
            case PATH_FIND:
                GraphNode pathNode = graphManager.getNodeAt(event.getX(), event.getY());
                if (pathNode != null) {
                    if (graphManager.getStartNode() == null) {
                        graphManager.setStartNode(pathNode);
                        pathNode.setColor(Color.GREEN);
                        LoggerManager.getInstance().log("Set start node for path finding");
                    } else if (graphManager.getEndNode() == null) {
                        graphManager.setEndNode(pathNode);
                        pathNode.setColor(Color.RED);
                        
                        // Find and highlight shortest path
                        List<GraphNode> path = currentAlgorithm.findShortestPath(
                            graphManager.getStartNode(),
                            graphManager.getEndNode(),
                            graphManager
                        );
                        
                        // Highlight path
                        for (GraphNode nodeInPath : path) {
                            nodeInPath.setColor(Color.YELLOW);
                        }
                        
                        LoggerManager.getInstance().log("Found shortest path with " + path.size() + " nodes");
                    }
                    redrawCanvas();
                }
                break;
            default:
                currentState.onMousePressed(event.getX(), event.getY());
                Shape selectedShape = shapeManager.getSelectedShape();
                if (selectedShape != null) {
                    LoggerManager.getInstance().log(String.format("Selected shape at (%.2f, %.2f) with size %.2f x %.2f", 
                        selectedShape.getX(), selectedShape.getY(), 
                        selectedShape.getWidth(), selectedShape.getHeight()));
                }
                redrawCanvas();
        }
    }

    @Override
    public void handleMouseDragged(MouseEvent event) {
        switch (modeManager.getCurrentMode()) {
            case RESIZE:
                if (resizingShape != null) {
                    double newWidth = Math.abs(event.getX() - initialX);
                    double newHeight = Math.abs(event.getY() - initialY);
                    resizingShape.resize(newWidth, newHeight);
                    LoggerManager.getInstance().log(String.format("Resized shape to %.2f x %.2f", newWidth, newHeight));
                    redrawCanvas();
                }
                break;
            case DRAW:
                // No drag logic for drawing
                break;
            default:
                currentState.onMouseDragged(event.getX(), event.getY());
                Shape selected = shapeManager.getSelectedShape();
                if (selected != null) {
                    LoggerManager.getInstance().log(String.format("Moved shape to (%.2f, %.2f)", 
                        selected.getX(), selected.getY()));
                }
                redrawCanvas();
        }
    }

    @Override
    public void handleMouseReleased(MouseEvent event) {
        switch (modeManager.getCurrentMode()) {
            case RESIZE:
                resizingShape = null;
                break;
            case DRAW:
                // No release logic for drawing
                break;
            default:
                currentState.onMouseReleased(event.getX(), event.getY());
                redrawCanvas();
        }
    }
    
    public void setSelectMode() {
        modeManager.setCurrentMode(ModeManager.Mode.SELECT);
        currentState = selectState;
    }
    
    public void setMoveMode() {
        modeManager.setCurrentMode(ModeManager.Mode.MOVE);
        Shape selected = shapeManager.getSelectedShape();
        moveState.setSelectedShape(selected);
        currentState = moveState;
    }
    
    public void setResizeMode() {
        modeManager.setCurrentMode(ModeManager.Mode.RESIZE);
    }
    
    public void setNodeDrawMode() {
        modeManager.setCurrentMode(ModeManager.Mode.NODE_DRAW);
    }
    
    public void setEdgeDrawMode() {
        modeManager.setCurrentMode(ModeManager.Mode.EDGE_DRAW);
    }
    
    public void setPathFindMode() {
        modeManager.setCurrentMode(ModeManager.Mode.PATH_FIND);
    }
    
    @Override
    public void redrawCanvas() {
        super.redrawCanvas();
    }
} 