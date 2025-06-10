package com.drawingapp.drawingapp.commands;

import com.drawingapp.drawingapp.services.ShapeManager;
import com.drawingapp.drawingapp.shapes_factory.Shape;

public class DeleteShapeCommand implements Command {
    private final ShapeManager shapeManager;
    private final Shape shape;

    public DeleteShapeCommand(ShapeManager shapeManager, Shape shape) {
        this.shapeManager = shapeManager;
        this.shape = shape;
    }

    @Override
    public void execute() {
        shapeManager.removeShape(shape);
    }

    @Override
    public void undo() {
        shapeManager.addShape(shape);
    }
} 