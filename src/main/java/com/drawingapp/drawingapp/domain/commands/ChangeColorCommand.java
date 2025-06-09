package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import javafx.scene.paint.Color;

public class ChangeColorCommand implements Command {
    private final Shape shape;
    private final Color oldColor;
    private final Color newColor;
    private final ILogger logger;

    public ChangeColorCommand(Shape shape, Color oldColor, Color newColor, ILogger logger) {
        this.shape = shape;
        this.oldColor = oldColor;
        this.newColor = newColor;
        this.logger = logger;
    }

    @Override
    public void execute() {
        shape.setColor(newColor);
        logger.log("Changed shape color to " + newColor.toString());
    }

    @Override
    public void undo() {
        shape.setColor(oldColor);
        logger.log("Undo: Reverted shape color to " + oldColor.toString());
    }

    @Override
    public void redo() {
        shape.setColor(newColor);
        logger.log("Redo: Changed shape color to " + newColor.toString());
    }
} 