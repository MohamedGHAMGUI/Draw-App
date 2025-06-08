package com.drawingapp.drawingapp.shapes_factory;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_factory.CircleShape;
import com.drawingapp.drawingapp.shapes_factory.LineShape;
import com.drawingapp.drawingapp.shapes_factory.RectangleShape;
import com.drawingapp.drawingapp.shapes_adapter.SvgStar;
import com.drawingapp.drawingapp.shapes_adapter.SvgStarAdapter;

public class ShapeFactory {
    public static Shape createShape(String type) {
        switch (type.toLowerCase()) {
            case "rectangle":
                return new RectangleShape();
            case "circle":
                return new CircleShape();
            case "line":
                return new LineShape();
            case "star":
                return new SvgStarAdapter(new SvgStar());
            default:
                return null;
        }
    }
}