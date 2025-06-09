package com.drawingapp.drawingapp.domain.shapes;

import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.shapes_adapter.SvgStarAdapter;
import com.drawingapp.drawingapp.shapes_adapter.SvgStar;
import javafx.scene.paint.Color;

public class ShapeFactory {
    private final ILogger logger;

    public ShapeFactory(ILogger logger) {
        this.logger = logger;
    }

    public Shape createShape(String type, double x, double y, double width, double height, Color color) {
        Shape shape = switch (type.toLowerCase()) {
            case "rectangle" -> new RectangleShape(x, y, width, height, color);
            case "circle" -> new CircleShape(x, y, width, height, color);
            case "line" -> new LineShape(x, y, width, height, color);
            case "star" -> {
                SvgStarAdapter star = new SvgStarAdapter(new SvgStar());
                star.setX(x);
                star.setY(y);
                star.setWidth(width);
                star.setHeight(height);
                star.setColor(color);
                yield star;
            }
            default -> throw new IllegalArgumentException("Unknown shape type: " + type);
        };
        logger.log("Created " + type + " shape at (" + x + ", " + y + ")");
        return shape;
    }
} 