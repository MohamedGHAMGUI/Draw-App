package com.drawingapp.drawingapp.services;

import com.drawingapp.drawingapp.shapes_factory.Shape;
import com.drawingapp.drawingapp.shapes_factory.ShapeFactory;
import com.drawingapp.drawingapp.shapes_decorator.ResizableShape;
import com.drawingapp.drawingapp.shapes_decorator.RotatableShapeDecorator;
import com.drawingapp.drawingapp.shapes_adapter.SvgStarAdapter;
import com.drawingapp.drawingapp.shapes_adapter.SvgStar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ShapeSerializer {
    private static final Logger LOGGER = Logger.getLogger(ShapeSerializer.class.getName());
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    
    public String serialize(List<Shape> shapes) {
        return gson.toJson(shapes.stream()
            .filter(shape -> shape != null)
            .map(this::shapeToDto)
            .toArray());
    }
    
    public List<Shape> deserialize(String json) {
        try {
            ShapeDto[] dtos = gson.fromJson(json, ShapeDto[].class);
            if (dtos == null) {
                LOGGER.warning("Deserialized JSON resulted in null array");
                return List.of();
            }
            return Arrays.stream(dtos)
                .filter(dto -> dto != null && dto.type != null)
                .map(this::dtoToShape)
                .filter(shape -> shape != null)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deserializing shapes", e);
            return List.of();
        }
    }
    
    private ShapeDto shapeToDto(Shape shape) {
        if (shape == null) return null;
        try {
            ShapeDto dto = new ShapeDto();

            // Unwrap decorators to get the base shape
            Shape base = shape;
            boolean isRotatable = false;
            double rotation = 0;
            boolean isResizable = false;

            // Unwrap decorators
            while (base instanceof com.drawingapp.drawingapp.shapes_decorator.RotatableShapeDecorator ||
                   base instanceof com.drawingapp.drawingapp.shapes_decorator.ResizableShape) {
                if (base instanceof com.drawingapp.drawingapp.shapes_decorator.RotatableShapeDecorator) {
                    isRotatable = true;
                    rotation = ((com.drawingapp.drawingapp.shapes_decorator.RotatableShapeDecorator) base).getRotation();
                    base = ((com.drawingapp.drawingapp.shapes_decorator.RotatableShapeDecorator) base).getDecoratedShape();
                } else if (base instanceof com.drawingapp.drawingapp.shapes_decorator.ResizableShape) {
                    isResizable = true;
                    base = ((com.drawingapp.drawingapp.shapes_decorator.ResizableShape) base).getDecoratedShape();
                }
            }

            String className = base.getClass().getSimpleName();
            if (className.equals("SvgStarAdapter")) {
                dto.type = "star";
                dto.isStar = true;
            } else {
                dto.type = className.replace("Shape", "").toLowerCase();
            }

            dto.isRotatable = isRotatable;
            dto.rotation = rotation;
            dto.isResizable = isResizable;

            dto.x = shape.getX();
            dto.y = shape.getY();
            dto.width = shape.getWidth();
            dto.height = shape.getHeight();
            dto.color = shape.getColor().toString();
            return dto;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting shape to DTO", e);
            return null;
        }
    }
    
    private Shape dtoToShape(ShapeDto dto) {
        if (dto == null || dto.type == null) {
            LOGGER.warning("Invalid DTO: " + (dto == null ? "null" : "type is null"));
            return null;
        }
        try {
            // Create the base shape
            Shape shape = null;
            String type = dto.type.toLowerCase();
            
            // Handle special cases
            if (type.equals("svgstaradapter")) {
                type = "star";
            }
            
            if (dto.isStar || type.equals("star")) {
                shape = new SvgStarAdapter(new SvgStar());
            } else {
                shape = ShapeFactory.createShape(type);
            }
            
            if (shape != null) {
                shape.setX(dto.x);
                shape.setY(dto.y);
                shape.setWidth(dto.width);
                shape.setHeight(dto.height);
                shape.setColor(Color.valueOf(dto.color));
                
                // Apply decorators if needed
                if (dto.isResizable) {
                    shape = new ResizableShape(shape);
                }
                if (dto.isRotatable) {
                    shape = new RotatableShapeDecorator(shape);
                    ((RotatableShapeDecorator) shape).setRotation(dto.rotation);
                }
                
                return shape;
            } else {
                LOGGER.warning("Failed to create shape of type: " + type);
                return null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error converting DTO to shape", e);
            return null;
        }
    }
    
    public static class ShapeDto {
        public String type;
        public double x;
        public double y;
        public double width;
        public double height;
        public String color;
        public boolean isResizable;
        public boolean isStar;
        public boolean isRotatable;
        public double rotation;
    }
} 