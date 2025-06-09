package com.drawingapp.drawingapp.ui.controllers;

import com.drawingapp.drawingapp.application.services.ModeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.application.controllers.interfaces.IDrawingController;
import com.drawingapp.drawingapp.application.controllers.interfaces.IGraphController;
import com.drawingapp.drawingapp.application.controllers.interfaces.IFileController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;

public class MainViewController {
    @FXML private Canvas canvas;
    @FXML private ColorPicker colorPicker;
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private Slider rotationSlider;

    private final IDrawingController drawingController;
    private final IGraphController graphController;
    private final IFileController fileController;
    private final ModeManager modeManager;
    private final ILogger logger;
    private GraphicsContext gc;

    public MainViewController(IDrawingController drawingController, 
                            IGraphController graphController,
                            IFileController fileController,
                            ModeManager modeManager,
                            ILogger logger) {
        this.drawingController = drawingController;
        this.graphController = graphController;
        this.fileController = fileController;
        this.modeManager = modeManager;
        this.logger = logger;
    }

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        drawingController.setGraphicsContext(gc);
        graphController.setGraphicsContext(gc);
        
        setupEventHandlers();
        setupControls();
    }

    private void setupEventHandlers() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        
        rotationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            drawingController.rotateSelectedShape(newVal.doubleValue());
        });
    }

    private void setupControls() {
        colorPicker.setValue(Color.BLACK);
        algorithmComboBox.setItems(FXCollections.observableArrayList("Dijkstra's Algorithm"));
        algorithmComboBox.getSelectionModel().selectFirst();
    }

    private void handleMousePressed(MouseEvent event) {
        drawingController.handleMousePressed(event.getX(), event.getY());
        graphController.handleMousePressed(event.getX(), event.getY());
        redrawCanvas();
    }

    private void handleMouseDragged(MouseEvent event) {
        drawingController.handleMouseDragged(event.getX(), event.getY());
        redrawCanvas();
    }

    private void handleMouseReleased(MouseEvent event) {
        drawingController.handleMouseReleased(event.getX(), event.getY());
        redrawCanvas();
    }

    private void redrawCanvas() {
        drawingController.redrawCanvas();
        graphController.redrawCanvas();
    }

    @FXML private void onSelectModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.SELECT);
        redrawCanvas();
    }

    @FXML private void onMoveModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.MOVE);
        redrawCanvas();
    }

    @FXML private void onResizeModeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.RESIZE);
        redrawCanvas();
    }

    @FXML private void onRectangleClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        drawingController.setSelectedShape("rectangle");
    }

    @FXML private void onCircleClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        drawingController.setSelectedShape("circle");
    }

    @FXML private void onLineClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        drawingController.setSelectedShape("line");
    }

    @FXML private void onStarClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        drawingController.setSelectedShape("star");
    }

    @FXML private void onColorChanged() {
        drawingController.setCurrentColor(colorPicker.getValue());
    }

    @FXML private void onAddNodeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.NODE_DRAW);
    }

    @FXML private void onAddEdgeClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.EDGE_DRAW);
    }

    @FXML private void onFindPathClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.PATH_FIND);
    }

    @FXML private void onSaveDrawingClicked() {
        fileController.saveDrawing();
    }

    @FXML private void onLoadDrawingClicked() {
        fileController.loadDrawing();
        redrawCanvas();
    }

    @FXML private void onDeleteClicked() {
        drawingController.deleteSelectedShape();
        redrawCanvas();
    }

    @FXML private void onUndoClicked() {
        drawingController.undo();
        redrawCanvas();
    }

    @FXML private void onRedoClicked() {
        drawingController.redo();
        redrawCanvas();
    }

    @FXML private void onRotateLeftClicked() {
        double currentAngle = rotationSlider.getValue();
        rotationSlider.setValue((currentAngle - 90) % 360);
    }

    @FXML private void onRotateRightClicked() {
        double currentAngle = rotationSlider.getValue();
        rotationSlider.setValue((currentAngle + 90) % 360);
    }
} 