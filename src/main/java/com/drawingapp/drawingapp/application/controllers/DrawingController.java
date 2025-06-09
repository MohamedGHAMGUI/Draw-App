package com.drawingapp.drawingapp.application.controllers;

import com.drawingapp.drawingapp.application.controllers.interfaces.IDrawingController;
import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.domain.shapes.ShapeFactory;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.application.services.ModeManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.commands.CommandFactory;
import com.drawingapp.drawingapp.domain.commands.CommandHistory;
import com.drawingapp.drawingapp.domain.commands.DeleteShapeCommand;
import com.drawingapp.drawingapp.domain.commands.Command;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawingController implements IDrawingController {
    private final ShapeManager shapeManager;
    private final ModeManager modeManager;
    private final ILogger logger;
    private final ShapeDrawingController shapeDrawingController;
    private final ShapeResizeController shapeResizeController;
    private final ShapeSelectionController shapeSelectionController;
    private final ShapeRotationController shapeRotationController;
    private final CommandHistory commandHistory;
    private GraphicsContext gc;
    private String selectedShape = "";
    private Color currentColor = Color.BLACK;

    public DrawingController(ShapeManager shapeManager, ModeManager modeManager, ILogger logger, 
                           CommandFactory commandFactory, CommandHistory commandHistory,
                           ShapeFactory shapeFactory) {
        this.shapeManager = shapeManager;
        this.modeManager = modeManager;
        this.logger = logger;
        this.commandHistory = commandHistory;
        this.shapeDrawingController = new ShapeDrawingController(shapeManager, modeManager, logger, commandFactory, commandHistory, shapeFactory);
        this.shapeResizeController = new ShapeResizeController(shapeManager, modeManager, logger, commandFactory, commandHistory);
        this.shapeSelectionController = new ShapeSelectionController(shapeManager, modeManager, logger, commandFactory, commandHistory);
        this.shapeRotationController = new ShapeRotationController(shapeManager, modeManager, logger, commandFactory, commandHistory);
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
        shapeDrawingController.setGraphicsContext(gc);
        shapeResizeController.setGraphicsContext(gc);
        shapeSelectionController.setGraphicsContext(gc);
        shapeRotationController.setGraphicsContext(gc);
    }

    public void handleMousePressed(double x, double y) {
        switch (modeManager.getCurrentMode()) {
            case SELECT:
                shapeSelectionController.handleMousePressed(x, y);
                break;
            case RESIZE:
                shapeResizeController.handleMousePressed(x, y);
                break;
            case DRAW:
                shapeDrawingController.handleMousePressed(x, y, selectedShape, currentColor);
                break;
            default:
                break;
        }
        redrawCanvas();
    }

    public void handleMouseDragged(double x, double y) {
        switch (modeManager.getCurrentMode()) {
            case RESIZE:
                shapeResizeController.handleMouseDragged(x, y);
                break;
            case MOVE:
                shapeSelectionController.handleMouseDragged(x, y);
                break;
            default:
                break;
        }
        redrawCanvas();
    }

    public void handleMouseReleased(double x, double y) {
        switch (modeManager.getCurrentMode()) {
            case RESIZE:
                shapeResizeController.handleMouseReleased(x, y);
                break;
            default:
                break;
        }
    }

    public void setSelectedShape(String shapeType) {
        this.selectedShape = shapeType;
        modeManager.setCurrentMode(ModeManager.Mode.DRAW);
        logger.log("Selected shape type: " + shapeType);
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
        Shape selected = shapeManager.getSelector().getSelectedShape();
        if (selected != null) {
            selected.setColor(color);
        }
    }

    public void redrawCanvas() {
        if (gc != null) {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            for (Shape shape : shapeManager.getShapes()) {
                shape.draw(gc);
            }
        }
    }

    public void rotateSelectedShape(double angle) {
        shapeRotationController.rotateSelectedShape(angle);
    }

    @Override
    public void deleteSelectedShape() {
        Shape selected = shapeManager.getSelector().getSelectedShape();
        if (selected != null) {
            Command deleteCommand = new DeleteShapeCommand(shapeManager, selected, logger);
            commandHistory.executeCommand(deleteCommand);
            shapeManager.getSelector().clearSelection();
        }
    }

    @Override
    public void undo() {
        if (commandHistory.canUndo()) {
            commandHistory.undo();
        }
    }

    @Override
    public void redo() {
        if (commandHistory.canRedo()) {
            commandHistory.redo();
        }
    }
} 