package com.drawingapp.drawingapp.shapes_adapter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SvgStar {
    private static final int DEFAULT_POINTS = 5;
    private static final double DEFAULT_RADIUS = 40;

    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        double radius = Math.min(width, height) / 2;
        
        double[] xPoints = new double[DEFAULT_POINTS * 2];
        double[] yPoints = new double[DEFAULT_POINTS * 2];
        
        for (int i = 0; i < DEFAULT_POINTS * 2; i++) {
            double angle = Math.PI * i / DEFAULT_POINTS;
            double r = (i % 2 == 0) ? radius : radius / 2;
            xPoints[i] = centerX + r * Math.cos(angle);
            yPoints[i] = centerY + r * Math.sin(angle);
        }
        
        gc.fillPolygon(xPoints, yPoints, DEFAULT_POINTS * 2);
        gc.strokePolygon(xPoints, yPoints, DEFAULT_POINTS * 2);
    }
} 