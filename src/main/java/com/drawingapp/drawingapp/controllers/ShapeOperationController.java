package com.drawingapp.drawingapp.controllers;

import com.drawingapp.drawingapp.logging.LoggerManager;
import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.GraphManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.shapes_factory.RotatableShape;
import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.observer.Observer;
import com.drawingapp.drawingapp.observer.ShapeSelector;

import javafx.scene.paint.Color;

public class ShapeOperationController extends BaseController implements ShapeOperationHandler, Observer {
    private String selectedShape = "";
    private DrawingModeHandler drawingModeHandler;
    private double rotationValue = 0;
    
    public ShapeOperationController(ShapeManager shapeManager, ModeManager modeManager, 
                                  GraphManager graphManager, DrawingRepository drawingRepository) {
        super(shapeManager, modeManager, graphManager, drawingRepository);
    }

    public void setDrawingModeHandler(DrawingModeHandler drawingModeHandler) {
        this.drawingModeHandler = drawingModeHandler;
    }
    
    public double getRotationValue() {
        return rotationValue;
    }
    
    public void setRotationValue(double value) {
        this.rotationValue = value;
    }

    @Override
    public void onShapeSelected(String shapeType) {
        this.selectedShape = shapeType;
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        if (drawingModeHandler instanceof DrawingModeController) {
            ((DrawingModeController) drawingModeHandler).setSelectedShape(shapeType);
        }
        LoggerManager.getInstance().log("Selected shape type: " + shapeType);
    }

    @Override
    public void onColorChanged(Color newColor) {
        Shape selected = shapeManager.getSelectedShape();
        if (selected != null) {
            selected.setColor(newColor);
            LoggerManager.getInstance().log(String.format("Changed color of shape at (%.2f, %.2f) to %s", 
                selected.getX(), selected.getY(), newColor.toString()));
            drawingModeHandler.redrawCanvas();
        }
        if (drawingModeHandler instanceof DrawingModeController) {
            ((DrawingModeController) drawingModeHandler).setCurrentColor(newColor);
        }
    }

    @Override
    public void onRotateLeft() {
        Shape selected = shapeManager.getSelectedShape();
        if (selected instanceof RotatableShape) {
            double currentAngle = rotationValue;
            double newAngle = (currentAngle - 90) % 360;
            if (newAngle < 0) newAngle += 360;
            rotationValue = newAngle;
            ((RotatableShape) selected).setRotation(newAngle);
            LoggerManager.getInstance().log(String.format("Rotated shape at (%.2f, %.2f) left by 90 degrees to %.2f degrees", 
                selected.getX(), selected.getY(), newAngle));
            drawingModeHandler.redrawCanvas();
        }
    }

    @Override
    public void onRotateRight() {
        Shape selected = shapeManager.getSelectedShape();
        if (selected instanceof RotatableShape) {
            double currentAngle = rotationValue;
            double newAngle = (currentAngle + 90) % 360;
            rotationValue = newAngle;
            ((RotatableShape) selected).setRotation(newAngle);
            LoggerManager.getInstance().log(String.format("Rotated shape at (%.2f, %.2f) right by 90 degrees to %.2f degrees", 
                selected.getX(), selected.getY(), newAngle));
            drawingModeHandler.redrawCanvas();
        }
    }

    @Override
    public void onRotationSliderChanged(double newValue) {
        Shape selected = shapeManager.getSelectedShape();
        if (selected instanceof RotatableShape) {
            ((RotatableShape) selected).setRotation(newValue);
            LoggerManager.getInstance().log("Rotated shape to " + newValue + " degrees");
            drawingModeHandler.redrawCanvas();
            this.rotationValue = newValue;
        }
    }
} 