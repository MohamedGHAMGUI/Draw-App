package com.drawingapp.drawingapp.commands;

import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.shapes_factory.Shape;

public class AddShapeCommand implements Command {
    private final ShapeManager shapeManager;
    private final Shape shape;

    public AddShapeCommand(ShapeManager shapeManager, Shape shape) {
        this.shapeManager = shapeManager;
        this.shape = shape;
    }

    @Override
    public void execute() {
        shapeManager.addShape(shape);
    }

    @Override
    public void undo() {
        shapeManager.removeShape(shape);
    }
} 