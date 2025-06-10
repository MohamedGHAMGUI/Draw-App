package com.drawingapp.drawingapp.commands;

import com.drawingapp.drawingapp.shapes_factory.Shape;

public class MoveShapeCommand implements Command {
    private final Shape shape;
    private final double oldX, oldY;
    private final double newX, newY;

    public MoveShapeCommand(Shape shape, double oldX, double oldY, double newX, double newY) {
        this.shape = shape;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public void execute() {
        shape.setX(newX);
        shape.setY(newY);
    }

    @Override
    public void undo() {
        shape.setX(oldX);
        shape.setY(oldY);
    }
} 