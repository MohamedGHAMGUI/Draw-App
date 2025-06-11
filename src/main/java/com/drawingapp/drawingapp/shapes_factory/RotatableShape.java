    package com.drawingapp.drawingapp.shapes_factory;

    public interface RotatableShape extends Shape {
        void rotate(double angle);
        double getRotation();
        void setRotation(double angle);
    } 