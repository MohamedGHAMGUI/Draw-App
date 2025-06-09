package com.drawingapp.drawingapp;

import com.drawingapp.drawingapp.application.controllers.interfaces.IDrawingController;
import com.drawingapp.drawingapp.application.controllers.interfaces.IGraphController;
import com.drawingapp.drawingapp.application.controllers.interfaces.IFileController;
import com.drawingapp.drawingapp.application.controllers.DrawingController;
import com.drawingapp.drawingapp.application.controllers.GraphController;
import com.drawingapp.drawingapp.application.controllers.FileController;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.application.services.ModeManager;
import com.drawingapp.drawingapp.infrastructure.persistence.DrawingRepository;
import com.drawingapp.drawingapp.application.services.GraphManager;
import com.drawingapp.drawingapp.application.services.ShapeSelector;
import com.drawingapp.drawingapp.domain.graph.Graph;
import com.drawingapp.drawingapp.domain.algorithms.ShortestPathAlgorithm;
import com.drawingapp.drawingapp.domain.algorithms.DijkstraAlgorithm;
import com.drawingapp.drawingapp.domain.commands.Command;
import com.drawingapp.drawingapp.domain.commands.DeleteShapeCommand;
import com.drawingapp.drawingapp.domain.commands.ChangeColorCommand;
import com.drawingapp.drawingapp.domain.commands.CommandHistory;
import com.drawingapp.drawingapp.domain.commands.CommandFactory;
import com.drawingapp.drawingapp.domain.shapes.ShapeFactory;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.infrastructure.logging.ConsoleLogger;
import com.drawingapp.drawingapp.shapes_state_observer.SelectState;
import com.drawingapp.drawingapp.shapes_state_observer.MoveState;
import com.drawingapp.drawingapp.shapes_state_observer.State;
import com.drawingapp.drawingapp.shapes_observer.ShapeObserver;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import com.drawingapp.drawingapp.domain.shapes.Shape;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;
import javafx.scene.control.Slider;
import com.drawingapp.drawingapp.application.services.NodeSelector;
import com.drawingapp.drawingapp.infrastructure.logging.FileLogger;
import com.drawingapp.drawingapp.infrastructure.logging.DatabaseLogger;
import com.drawingapp.drawingapp.infrastructure.logging.CompositeLogger;
import javafx.application.Application;

public class HelloController implements ShapeObserver {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private Slider rotationSlider;

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
    private IDrawingController drawingController;
    private IGraphController graphController;
    private IFileController fileController;
    private ILogger logger;
    private CommandHistory commandHistory;
    private ShapeSelector shapeSelector;
    private Stage stage;

    public HelloController() {
        String dbUrl = "jdbc:mysql://localhost:3306/drawingapp_logs?useSSL=false&serverTimezone=UTC";
        String dbUser = "root";
        String dbPass = "";
        String logFile = "drawing_app.log";
        
        // Create a temporary logger until the real one is set
        ILogger fileLogger = new FileLogger(logFile);
        ILogger dbLogger = new DatabaseLogger(dbUrl, dbUser, dbPass, logFile);
        ILogger consoleLogger = new ConsoleLogger();
        this.logger = new CompositeLogger(fileLogger, dbLogger, consoleLogger);
        
        this.commandHistory = new CommandHistory(logger);
        this.shapeSelector = new ShapeSelector(new ArrayList<>(), logger);
        this.modeManager = new ModeManager(logger);
    }

    public void setShapeManager(ShapeManager shapeManager) {
        this.shapeManager = shapeManager;
    }

    public void setModeManager(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    public void setDrawingRepository(DrawingRepository drawingRepository) {
        this.drawingRepository = drawingRepository;
        this.shapeManager = new ShapeManager(logger);
        this.shapeSelector = new ShapeSelector(shapeManager.getShapes(), logger);
        NodeSelector nodeSelector = new NodeSelector(new ArrayList<>(), logger);
        this.graphManager = new GraphManager(new Graph(), new DijkstraAlgorithm(), logger, nodeSelector);
        CommandFactory commandFactory = new CommandFactory(shapeManager, graphManager, logger);
        ShapeFactory shapeFactory = new ShapeFactory(logger);
        this.drawingController = new DrawingController(shapeManager, modeManager, logger, commandFactory, commandHistory, shapeFactory);
        this.graphController = new GraphController(graphManager, modeManager, logger);
        this.fileController = new FileController(drawingRepository, shapeManager, graphManager, logger, commandFactory, commandHistory);
        logger.log("All controllers initialized successfully");
    }

    public void setControllers(IDrawingController drawingController, 
                             IGraphController graphController, 
                             IFileController fileController) {
        this.drawingController = drawingController;
        this.graphController = graphController;
        this.fileController = fileController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        if (fileController != null) {
            ((FileController)fileController).setStage(stage);
        }
    }

    public void postInjectInit() {
        gc = canvas.getGraphicsContext2D();
        shapeSelector.getSubject().addObserver(this);
        selectState = new SelectState(shapeManager.getShapes(), shapeManager, logger);
        moveState = new MoveState(shapeManager, logger);
        currentState = selectState;
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        colorPicker.setValue(currentColor);
        currentAlgorithm = new DijkstraAlgorithm();
        
        // Initialize algorithm combo box
        algorithmComboBox.setItems(FXCollections.observableArrayList(
            "Dijkstra's Algorithm"
        ));
        algorithmComboBox.getSelectionModel().selectFirst();
        
        // Initialize controllers with graphics context
        drawingController.setGraphicsContext(gc);
        graphController.setGraphicsContext(gc);
        
        // Initialize rotation slider
        rotationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (drawingController != null) {
                drawingController.rotateSelectedShape(newVal.doubleValue());
            }
        });
    }

    private void handleMousePressed(MouseEvent event) {
        logger.log("Mouse pressed at (" + event.getX() + ", " + event.getY() + ")");
        drawingController.handleMousePressed(event.getX(), event.getY());
        graphController.handleMousePressed(event.getX(), event.getY());
        redrawCanvas();
    }

    private void handleMouseDragged(MouseEvent event) {
        drawingController.handleMouseDragged(event.getX(), event.getY());
        redrawCanvas();
    }

    private void handleMouseReleased(MouseEvent event) {
        logger.log("Mouse released at (" + event.getX() + ", " + event.getY() + ")");
        drawingController.handleMouseReleased(event.getX(), event.getY());
        redrawCanvas();
    }

    private void redrawCanvas() {
        drawingController.redrawCanvas();
        graphController.redrawCanvas();
    }

    @Override
    public void onShapeSelected(String shapeType) {
        drawingController.setSelectedShape(shapeType);
    }

    @FXML
    private void onSelectModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.SELECT);
        currentState = selectState;
        logger.log("Switched to SELECT mode");
        redrawCanvas();
    }

    @FXML
    private void onMoveModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.MOVE);
        currentState = moveState;
        logger.log("Switched to MOVE mode");
        redrawCanvas();
    }

    @FXML
    private void onResizeModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.RESIZE);
        logger.log("Switched to RESIZE mode");
        redrawCanvas();
    }

    @FXML private void onRectangleClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        shapeSelector.selectShape("rectangle");
        logger.log("Selected rectangle shape");
    }

    @FXML private void onCircleClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        shapeSelector.selectShape("circle");
        logger.log("Selected circle shape");
    }

    @FXML private void onLineClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        shapeSelector.selectShape("line");
        logger.log("Selected line shape");
    }

    @FXML
    private void onStarClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        shapeSelector.selectShape("star");
        logger.log("Selected star shape");
    }

    @FXML
    private void onColorChanged() {
        Shape selected = shapeManager.getSelector().getSelectedShape();
        if (selected != null) {
            Color oldColor = selected.getColor();
            Color newColor = colorPicker.getValue();
            Command colorCommand = new ChangeColorCommand(selected, oldColor, newColor, logger);
            commandHistory.executeCommand(colorCommand);
            logger.log("Changed color of shape to " + newColor);
            redrawCanvas();
        }
    }

    @FXML
    private void onAddNodeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.NODE_DRAW);
        logger.log("Switched to NODE_DRAW mode");
    }

    @FXML
    private void onAddEdgeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.EDGE_DRAW);
        logger.log("Switched to EDGE_DRAW mode");
    }

    @FXML
    private void onFindPathClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.PATH_FIND);
        logger.log("Switched to PATH_FIND mode");
    }

    @FXML
    private void onSaveDrawingClicked() {
        logger.log("Attempting to save drawing");
        fileController.saveDrawing();
    }

    @FXML
    private void onLoadDrawingClicked() {
        logger.log("Attempting to load drawing");
        fileController.loadDrawing();
        redrawCanvas();
    }

    @FXML
    private void onDeleteClicked() {
        Shape selected = shapeManager.getSelector().getSelectedShape();
        if (selected != null) {
            Command deleteCommand = new DeleteShapeCommand(shapeManager, selected, logger);
            commandHistory.executeCommand(deleteCommand);
            shapeManager.getSelector().clearSelection();
            logger.log("Deleted selected shape");
            redrawCanvas();
        }
    }

    @FXML
    private void onUndoClicked() {
        if (commandHistory.canUndo()) {
            commandHistory.undo();
            logger.log("Undid last action");
            redrawCanvas();
        }
    }

    @FXML
    private void onRedoClicked() {
        if (commandHistory.canRedo()) {
            commandHistory.redo();
            logger.log("Redid last action");
            redrawCanvas();
        }
    }

    @FXML
    private void onRotateLeftClicked() {
        double currentAngle = rotationSlider.getValue();
        rotationSlider.setValue((currentAngle - 90) % 360);
        logger.log("Rotated shape left by 90 degrees");
    }

    @FXML
    private void onRotateRightClicked() {
        double currentAngle = rotationSlider.getValue();
        rotationSlider.setValue((currentAngle + 90) % 360);
        logger.log("Rotated shape right by 90 degrees");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
        this.commandHistory = new CommandHistory(logger);
        this.shapeSelector = new ShapeSelector(new ArrayList<>(), logger);
        this.modeManager = new ModeManager(logger);
    }
}