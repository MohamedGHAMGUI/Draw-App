package com.drawingapp.drawingapp;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_factory.ShapeFactory;
import com.drawingapp.drawingapp.shapes_observer.ShapeObserver;
import com.drawingapp.drawingapp.shapes_state_observer.MoveState;
import com.drawingapp.drawingapp.shapes_state_observer.SelectState;
import com.drawingapp.drawingapp.shapes_state_observer.ShapeSelector;
import com.drawingapp.drawingapp.shapes_state_observer.State;
import com.drawingapp.drawingapp.shapes_graph.*;
import com.drawingapp.drawingapp.services.DrawingRepository;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import com.drawingapp.drawingapp.shapes_decorator.ResizableShape;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.logging.LoggerManager;
import com.drawingapp.drawingapp.services.GraphManager;
import java.util.List;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert;
import java.util.Optional;

public class HelloController implements ShapeObserver {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ComboBox<String> algorithmComboBox;

    private GraphicsContext gc;
    private String selectedShape = "";
    
    private ShapeManager shapeManager;
    private ModeManager modeManager;
    private State currentState;
    private SelectState selectState;
    private MoveState moveState;
    private Color currentColor = Color.BLACK;
    private double initialX, initialY;
    private Shape resizingShape;
    private GraphManager graphManager;
    private ShortestPathAlgorithm currentAlgorithm;
    private DrawingRepository drawingRepository;

    public void setShapeManager(ShapeManager shapeManager) {
        this.shapeManager = shapeManager;
    }

    public void setModeManager(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    public void setDrawingRepository(DrawingRepository drawingRepository) {
        this.drawingRepository = drawingRepository;
    }

    public void postInjectInit() {
        gc = canvas.getGraphicsContext2D();
        ShapeSelector.getInstance().getSubject().addObserver(this);
        selectState = new SelectState(shapeManager.getShapes(), shapeManager);
        moveState = new MoveState();
        currentState = selectState;
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        colorPicker.setValue(currentColor);
        graphManager = new GraphManager();
        currentAlgorithm = new DijkstraAlgorithm();
        
        // Initialize algorithm combo box
        algorithmComboBox.setItems(FXCollections.observableArrayList(
            "Dijkstra's Algorithm"
        ));
        algorithmComboBox.getSelectionModel().selectFirst();
    }

    private void handleMousePressed(MouseEvent event) {
        switch (modeManager.getCurrentMode()) {
            case RESIZE:
                Shape selected = shapeManager.getSelectedShape();
                if (selected != null) {
                    resizingShape = selected;
                    initialX = event.getX();
                    initialY = event.getY();
                    LoggerManager.getInstance().log("Started resizing shape at (" + initialX + ", " + initialY + ")");
                }
                break;
            case DRAW:
                if (selectedShape != null && !selectedShape.isEmpty()) {
                    Shape shape = ShapeFactory.createShape(selectedShape);
                    if (shape != null) {
                        shape.setColor(colorPicker.getValue());
                        shape = new ResizableShape(shape);
                        shapeManager.addShape(shape);
                        shape.draw(gc, event.getX(), event.getY());
                        LoggerManager.getInstance().log("Drew a " + selectedShape + " at (" + event.getX() + ", " + event.getY() + ")");
                    }
                }
                break;
            case NODE_DRAW:
                GraphNode node = new GraphNode(event.getX(), event.getY());
                graphManager.addNode(node);
                LoggerManager.getInstance().log("Added node at (" + event.getX() + ", " + event.getY() + ")");
                redrawCanvas();
                break;
            case EDGE_DRAW:
                GraphNode selectedNode = graphManager.getNodeAt(event.getX(), event.getY());
                if (selectedNode != null) {
                    if (graphManager.getSelectedNode() == null) {
                        graphManager.setSelectedNode(selectedNode);
                        LoggerManager.getInstance().log("Selected node for edge creation");
                    } else {
                        // Create edge with weight 1 by default
                        GraphEdge edge = new GraphEdge(
                            graphManager.getSelectedNode(),
                            selectedNode,
                            1.0
                        );
                        graphManager.addEdge(edge);
                        graphManager.clearSelection();
                        LoggerManager.getInstance().log("Created edge between nodes");
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
                redrawCanvas();
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        switch (modeManager.getCurrentMode()) {
            case RESIZE:
                if (resizingShape != null) {
                    double newWidth = Math.abs(event.getX() - initialX);
                    double newHeight = Math.abs(event.getY() - initialY);
                    resizingShape.resize(newWidth, newHeight);
                    LoggerManager.getInstance().log("Resized shape to width=" + newWidth + ", height=" + newHeight);
                    redrawCanvas();
                }
                break;
            case DRAW:
                // No drag logic for drawing
                break;
            default:
                currentState.onMouseDragged(event.getX(), event.getY());
                redrawCanvas();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
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

    private void redrawCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
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

    @Override
    public void onShapeSelected(String shapeType) {
        this.selectedShape = shapeType;
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        LoggerManager.getInstance().log("Selected shape type: " + shapeType);
        System.out.println("Selected shape: " + shapeType);
    }

    @FXML
    private void onSelectModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.SELECT);
        currentState = selectState;
    }

    @FXML
    private void onMoveModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.MOVE);
        Shape selected = shapeManager.getSelectedShape();
        moveState.setSelectedShape(selected);
        currentState = moveState;
    }

    @FXML
    private void onResizeModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.RESIZE);
    }

    @FXML private void onRectangleClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        ShapeSelector.getInstance().selectShape("rectangle");
    }

    @FXML private void onCircleClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        ShapeSelector.getInstance().selectShape("circle");
    }

    @FXML private void onLineClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        ShapeSelector.getInstance().selectShape("line");
    }

    @FXML
    private void onStarClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        ShapeSelector.getInstance().selectShape("star");
    }

    @FXML
    private void onColorChanged() {
        Color newColor = colorPicker.getValue();
        Shape selected = shapeManager.getSelectedShape();
        if (selected != null) {
            selected.setColor(newColor);
            LoggerManager.getInstance().log("Changed color of selected shape to " + newColor.toString());
            redrawCanvas();
        }
        currentColor = newColor;
    }

    @FXML
    private void onAddNodeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.NODE_DRAW);
    }

    @FXML
    private void onAddEdgeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.EDGE_DRAW);
    }

    @FXML
    private void onFindPathClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.PATH_FIND);
    }

    @FXML
    private void onCreateExampleGraphClicked() {
        graphManager.createExampleGraph();
    }

    @FXML
    private void onSaveDrawingClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Drawing");
        dialog.setHeaderText("Enter a name for your drawing");
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(name -> {
            drawingRepository.saveDrawing(
                name,
                shapeManager.getShapes(),
                graphManager.getNodes(),
                graphManager.getEdges()
            );
            LoggerManager.getInstance().log("Saved drawing: " + name);
        });
    }

    @FXML
    private void onLoadDrawingClicked() {
        List<String> drawings = drawingRepository.listSavedDrawings();
        if (drawings.isEmpty()) {
            showAlert("No drawings found");
            return;
        }
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>(drawings.get(0), drawings);
        dialog.setTitle("Load Drawing");
        dialog.setHeaderText("Select a drawing to load");
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(name -> {
            DrawingRepository.DrawingData data = drawingRepository.loadDrawing(name);
            if (data != null) {
                shapeManager.clear();
                graphManager.clear();
                
                data.getShapes().forEach(shapeManager::addShape);
                data.getNodes().forEach(graphManager::addNode);
                data.getEdges().forEach(graphManager::addEdge);
                
                redrawCanvas();
                LoggerManager.getInstance().log("Loaded drawing: " + name);
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}