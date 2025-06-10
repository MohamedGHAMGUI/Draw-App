package com.drawingapp.drawingapp.commands;

import com.drawingapp.drawingapp.shapes_factory.Shape;

public class ResizeShapeCommand implements Command {
    private final Shape shape;
    private final double oldWidth, oldHeight;
    private final double newWidth, newHeight;

    public ResizeShapeCommand(Shape shape, double oldWidth, double oldHeight, double newWidth, double newHeight) {
        this.shape = shape;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public void execute() {
        shape.setWidth(newWidth);
        shape.setHeight(newHeight);
    }

    @Override
    public void undo() {
        shape.setWidth(oldWidth);
        shape.setHeight(oldHeight);
    }
} 