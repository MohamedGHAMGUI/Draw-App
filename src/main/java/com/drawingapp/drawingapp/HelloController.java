package com.drawingapp.drawingapp;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_observer.ShapeObserver;
import com.drawingapp.drawingapp.shapes_state_observer.MoveState;
import com.drawingapp.drawingapp.shapes_state_observer.SelectState;
import com.drawingapp.drawingapp.shapes_state_observer.ShapeSelector;
import com.drawingapp.drawingapp.shapes_state_observer.State;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;
import com.drawingapp.drawingapp.shapes_decorator.ResizableShape;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.logging.LoggerManager;

public class HelloController implements ShapeObserver {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

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

    public void setShapeManager(ShapeManager shapeManager) {
        this.shapeManager = shapeManager;
    }

    public void setModeManager(ModeManager modeManager) {
        this.modeManager = modeManager;
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
    private void onEllipseClicked() {
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        ShapeSelector.getInstance().selectShape("ellipse");
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
}
