package com.drawingapp.drawingapp.shapes_adapter;

import javafx.scene.canvas.GraphicsContext;

public class SvgStar {
    public void renderStar(GraphicsContext gc, double centerX, double centerY, double radius, int points) {
        // Simple star drawing (not mathematically perfect, just for demo)
        double angle = Math.PI / points;
        double[] xPoints = new double[points * 2];
        double[] yPoints = new double[points * 2];
        for (int i = 0; i < points * 2; i++) {
            double r = (i % 2 == 0) ? radius : radius / 2.5;
            double a = i * angle;
            xPoints[i] = centerX + Math.cos(a) * r;
            yPoints[i] = centerY + Math.sin(a) * r;
        }
        gc.beginPath();
        gc.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            gc.lineTo(xPoints[i], yPoints[i]);
        }
        gc.closePath();
        gc.fill();   // Fill the star
        gc.stroke(); // Outline the star
    }
} 