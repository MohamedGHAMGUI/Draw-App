package com.drawingapp.drawingapp;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_factory.CircleShape;
import com.drawingapp.drawingapp.shapes_factory.LineShape;
import com.drawingapp.drawingapp.shapes_factory.RectangleShape;
import com.drawingapp.drawingapp.shapes_adapter.SvgStarAdapter;

public class ShapeFactory {
    public static Shape createShape(String shapeType) {
        Shape shape = null;
        switch (shapeType.toLowerCase()) {
            case "rectangle":
                shape = new RectangleShape();
                break;
            case "circle":
                shape = new CircleShape();
                break;
            case "line":
                shape = new LineShape();
                break;
           
            case "star":
                shape = new SvgStarAdapter(200, 200, 50, 5); // default values
                break;
        }
        return shape;
    }
}