package com.drawingapp.drawingapp.domain.shapes;

public interface RotatableShape extends Shape {
    void setRotationAngle(double angle);
    double getRotationAngle();
} 