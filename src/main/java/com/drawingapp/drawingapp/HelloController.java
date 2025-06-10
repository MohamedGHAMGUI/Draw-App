package com.drawingapp.drawingapp;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_factory.RotatableShape;
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
import javafx.scene.control.Slider;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import com.drawingapp.drawingapp.shapes_decorator.RotatableShapeDecorator;

public class HelloController implements ShapeObserver {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private Slider rotationSlider;

    @FXML
    private DoubleProperty rotationValue;

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

        // Add rotation slider listener
        rotationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            Shape selected = shapeManager.getSelectedShape();
            if (selected instanceof RotatableShape) {
                RotatableShape rotatableShape = (RotatableShape) selected;
                rotatableShape.setRotation(newVal.doubleValue());
                LoggerManager.getInstance().log("Rotated shape to " + newVal.doubleValue() + " degrees");
                redrawCanvas();
            }
        });
    }

    private void handleMousePressed(MouseEvent event) {
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
                        shape.setColor(colorPicker.getValue());
                        shape = new ResizableShape(shape);
                        // Add rotation capability for shapes that can be rotated
                        if (selectedShape.equals("rectangle") || selectedShape.equals("line") || selectedShape.equals("star")) {
                            shape = new RotatableShapeDecorator(shape);
                        }
                        shapeManager.addShape(shape);
                        shape.draw(gc, event.getX(), event.getY());
                        LoggerManager.getInstance().log(String.format("Created new %s at (%.2f, %.2f) with color %s", 
                            selectedShape, event.getX(), event.getY(), colorPicker.getValue().toString()));
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
                if (selectedShape instanceof RotatableShape) {
                    rotationSlider.setValue(((RotatableShape) selectedShape).getRotation());
                    rotationSlider.setDisable(false);
                } else {
                    rotationSlider.setValue(0);
                    rotationSlider.setDisable(true);
                }
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
            LoggerManager.getInstance().log(String.format("Changed color of shape at (%.2f, %.2f) to %s", 
                selected.getX(), selected.getY(), newColor.toString()));
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
            LoggerManager.getInstance().log(String.format("Saved drawing '%s' with %d shapes, %d nodes, and %d edges", 
                name, shapeManager.getShapes().size(), 
                graphManager.getNodes().size(), 
                graphManager.getEdges().size()));
        });
    }

    @FXML
    private void onLoadDrawingClicked() {
        List<String> drawings = drawingRepository.listSavedDrawings();
        if (drawings.isEmpty()) {
            showAlert("No drawings found");
            LoggerManager.getInstance().log("Attempted to load drawing but no saved drawings found");
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
                
                LoggerManager.getInstance().log(String.format("Loaded drawing '%s' with %d shapes, %d nodes, and %d edges", 
                    name, data.getShapes().size(), 
                    data.getNodes().size(), 
                    data.getEdges().size()));
                
                redrawCanvas();
            }
        });
    }

    @FXML
    private void onRotateLeftClicked() {
        Shape selected = shapeManager.getSelectedShape();
        if (selected instanceof RotatableShape) {
            double currentAngle = rotationSlider.getValue();
            double newAngle = (currentAngle - 90) % 360;
            if (newAngle < 0) newAngle += 360;
            rotationSlider.setValue(newAngle);
            LoggerManager.getInstance().log(String.format("Rotated shape at (%.2f, %.2f) left by 90 degrees to %.2f degrees", 
                selected.getX(), selected.getY(), newAngle));
        }
    }

    @FXML
    private void onRotateRightClicked() {
        Shape selected = shapeManager.getSelectedShape();
        if (selected instanceof RotatableShape) {
            double currentAngle = rotationSlider.getValue();
            double newAngle = (currentAngle + 90) % 360;
            rotationSlider.setValue(newAngle);
            LoggerManager.getInstance().log(String.format("Rotated shape at (%.2f, %.2f) right by 90 degrees to %.2f degrees", 
                selected.getX(), selected.getY(), newAngle));
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}