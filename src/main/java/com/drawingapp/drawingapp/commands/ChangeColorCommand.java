package com.drawingapp.drawingapp.commands;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import javafx.scene.paint.Color;

public class ChangeColorCommand implements Command {
    private final Shape shape;
    private final Color oldColor;
    private final Color newColor;

    public ChangeColorCommand(Shape shape, Color oldColor, Color newColor) {
        this.shape = shape;
        this.oldColor = oldColor;
        this.newColor = newColor;
    }

    @Override
    public void execute() {
        shape.setColor(newColor);
    }

    @Override
    public void undo() {
        shape.setColor(oldColor);
    }
} 