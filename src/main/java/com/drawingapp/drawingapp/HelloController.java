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
import com.drawingapp.drawingapp.shapes_decorator.ColorableShape;
import com.drawingapp.drawingapp.shapes_decorator.ResizableShape;

import java.util.ArrayList;
import java.util.List;

public class HelloController implements ShapeObserver {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    private GraphicsContext gc;
    private String selectedShape = "";
    
    // Add these new fields
    private List<Shape> shapes = new ArrayList<>();
    private State currentState;
    private SelectState selectState;
    private MoveState moveState;
    private boolean isDrawingMode = true;
    private Color currentColor = Color.BLACK;
    private boolean isResizeMode = false;
    private double initialX, initialY;
    private Shape resizingShape;

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();

        // Subscribe to shape selection updates
        ShapeSelector.getInstance().getSubject().addObserver(this);

        // Initialize states
        selectState = new SelectState(shapes);
        moveState = new MoveState();
        currentState = selectState;

        // Set up canvas event handlers
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);

        colorPicker.setValue(currentColor);
    }

    private void handleMousePressed(MouseEvent event) {
        if (isResizeMode) {
            for (Shape shape : shapes) {
                if (shape.isSelected()) {
                    resizingShape = shape;
                    initialX = event.getX();
                    initialY = event.getY();
                    break;
                }
            }
        } else if (isDrawingMode) {
            if (selectedShape != null && !selectedShape.isEmpty()) {
                Shape shape = ShapeFactory.createShape(selectedShape);
                if (shape != null) {
                    // Set the color before adding to shapes list
                    shape.setColor(colorPicker.getValue());
                    shape = new ResizableShape(shape);
                    shapes.add(shape);
                    shape.draw(gc, event.getX(), event.getY());
                }
            }
        } else {
            currentState.onMousePressed(event.getX(), event.getY());
            redrawCanvas();
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isResizeMode && resizingShape != null) {
            double newWidth = Math.abs(event.getX() - initialX);
            double newHeight = Math.abs(event.getY() - initialY);
            resizingShape.resize(newWidth, newHeight);
            redrawCanvas();
        } else if (!isDrawingMode) {
            currentState.onMouseDragged(event.getX(), event.getY());
            redrawCanvas();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (isResizeMode) {
            resizingShape = null;
        } else if (!isDrawingMode) {
            currentState.onMouseReleased(event.getX(), event.getY());
            redrawCanvas();
        }
    }

    private void redrawCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
    }

    @Override
    public void onShapeSelected(String shapeType) {
        this.selectedShape = shapeType;
        isDrawingMode = true;
        System.out.println("Selected shape: " + shapeType);
    }

    // Add these new button handlers
    @FXML
    private void onSelectModeClicked() {
        isDrawingMode = false;
        isResizeMode = false;
        currentState = selectState;
    }

    @FXML
    private void onMoveModeClicked() {
        isDrawingMode = false;
        isResizeMode = false;
        // Find the selected shape
        Shape selected = null;
        for (Shape shape : shapes) {
            if (shape.isSelected()) {
                selected = shape;
                break;
            }
        }
        moveState.setSelectedShape(selected); // Pass the selected shape to MoveState
        currentState = moveState;
    }

    @FXML
    private void onResizeModeClicked() {
        isDrawingMode = false;
        isResizeMode = true;
    }

    // Button Handlers
    @FXML private void onRectangleClicked() {
        isDrawingMode = true;
        isResizeMode = false;
        ShapeSelector.getInstance().selectShape("rectangle");
    }

    @FXML private void onCircleClicked() {
        isDrawingMode = true;
        isResizeMode = false;
        ShapeSelector.getInstance().selectShape("circle");
    }

    @FXML private void onLineClicked() {
        isDrawingMode = true;
        isResizeMode = false;
        ShapeSelector.getInstance().selectShape("line");
    }

    @FXML
    private void onColorChanged() {
        Color newColor = colorPicker.getValue();
        for (Shape shape : shapes) {
            if (shape.isSelected()) {
                shape.setColor(newColor);
                redrawCanvas();
                break;
            }
        }
        currentColor = newColor;
    }
}
