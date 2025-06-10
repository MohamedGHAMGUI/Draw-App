package com.drawingapp.drawingapp;

import com.drawingapp.drawingapp.config.AppConfig;
import com.drawingapp.drawingapp.controllers.DrawingModeHandler;
import com.drawingapp.drawingapp.controllers.FileOperationHandler;
import com.drawingapp.drawingapp.controllers.GraphOperationHandler;
import com.drawingapp.drawingapp.controllers.ShapeOperationHandler;
import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.GraphManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.shapes_observer.ShapeObserver;
import com.drawingapp.drawingapp.shapes_state_observer.ShapeSelector;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.collections.FXCollections;

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
    
    private DrawingModeHandler drawingModeHandler;
    private ShapeOperationHandler shapeOperationHandler;
    private GraphOperationHandler graphOperationHandler;
    private FileOperationHandler fileOperationHandler;
    
    private ShapeManager shapeManager;
    private ModeManager modeManager;
    private GraphManager graphManager;
    private DrawingRepository drawingRepository;
    private AppConfig appConfig;

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
        
        // Initialize graph manager if not injected
        if (graphManager == null) {
            graphManager = new GraphManager();
        }
        
        // Initialize app config and controllers
        appConfig = new AppConfig(shapeManager, modeManager, graphManager, drawingRepository);
        appConfig.setGraphicsContext(gc);
        
        drawingModeHandler = appConfig.getDrawingModeHandler();
        shapeOperationHandler = appConfig.getShapeOperationHandler();
        graphOperationHandler = appConfig.getGraphOperationHandler();
        fileOperationHandler = appConfig.getFileOperationHandler();
        
        // Set up event handlers
        canvas.setOnMousePressed(drawingModeHandler::handleMousePressed);
        canvas.setOnMouseDragged(drawingModeHandler::handleMouseDragged);
        canvas.setOnMouseReleased(drawingModeHandler::handleMouseReleased);
        
        // Set up color picker
        colorPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            shapeOperationHandler.onColorChanged(newVal);
        });
        
        // Initialize algorithm combo box
        algorithmComboBox.setItems(FXCollections.observableArrayList(
            "Dijkstra's Algorithm"
        ));
        algorithmComboBox.getSelectionModel().selectFirst();

        // Add rotation slider listener
        rotationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            shapeOperationHandler.onRotationSliderChanged(newVal.doubleValue());
        });
    }

    @Override
    public void onShapeSelected(String shapeType) {
        shapeOperationHandler.onShapeSelected(shapeType);
    }

    @FXML
    private void onSelectModeClicked() {
        if (drawingModeHandler instanceof com.drawingapp.drawingapp.controllers.DrawingModeController) {
            ((com.drawingapp.drawingapp.controllers.DrawingModeController) drawingModeHandler).setSelectMode();
        }
    }

    @FXML
    private void onMoveModeClicked() {
        if (drawingModeHandler instanceof com.drawingapp.drawingapp.controllers.DrawingModeController) {
            ((com.drawingapp.drawingapp.controllers.DrawingModeController) drawingModeHandler).setMoveMode();
        }
    }

    @FXML
    private void onResizeModeClicked() {
        if (drawingModeHandler instanceof com.drawingapp.drawingapp.controllers.DrawingModeController) {
            ((com.drawingapp.drawingapp.controllers.DrawingModeController) drawingModeHandler).setResizeMode();
        }
    }

    @FXML private void onRectangleClicked() {
        ShapeSelector.getInstance().selectShape("rectangle");
    }

    @FXML private void onCircleClicked() {
        ShapeSelector.getInstance().selectShape("circle");
    }

    @FXML private void onLineClicked() {
        ShapeSelector.getInstance().selectShape("line");
    }

    @FXML
    private void onStarClicked() {
        ShapeSelector.getInstance().selectShape("star");
    }

    @FXML
    private void onColorChanged() {
        shapeOperationHandler.onColorChanged(colorPicker.getValue());
    }

    @FXML
    private void onAddNodeClicked() {
        graphOperationHandler.onAddNode();
    }

    @FXML
    private void onAddEdgeClicked() {
        graphOperationHandler.onAddEdge();
    }

    @FXML
    private void onFindPathClicked() {
        graphOperationHandler.onFindPath();
    }

    @FXML
    private void onCreateExampleGraphClicked() {
        graphOperationHandler.onCreateExampleGraph();
    }

    @FXML
    private void onSaveDrawingClicked() {
        fileOperationHandler.onSaveDrawing();
    }

    @FXML
    private void onLoadDrawingClicked() {
        fileOperationHandler.onLoadDrawing();
    }

    @FXML
    private void onRotateLeftClicked() {
        shapeOperationHandler.onRotateLeft();
    }

    @FXML
    private void onRotateRightClicked() {
        shapeOperationHandler.onRotateRight();
    }
}