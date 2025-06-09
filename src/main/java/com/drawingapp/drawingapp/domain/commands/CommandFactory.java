package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.shapes.Shape;
import com.drawingapp.drawingapp.domain.shapes.RotatableShape;
import com.drawingapp.drawingapp.application.services.ShapeManager;
import com.drawingapp.drawingapp.application.services.GraphManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.graph.Node;
import com.drawingapp.drawingapp.domain.graph.Edge;
import javafx.scene.paint.Color;
import java.util.Objects;

public class CommandFactory {
    private final ShapeManager shapeManager;
    private final GraphManager graphManager;
    private final ILogger logger;

    public CommandFactory(ShapeManager shapeManager, GraphManager graphManager, ILogger logger) {
        if (shapeManager == null) {
            throw new IllegalArgumentException("ShapeManager cannot be null");
        }
        if (graphManager == null) {
            throw new IllegalArgumentException("GraphManager cannot be null");
        }
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        this.shapeManager = shapeManager;
        this.graphManager = graphManager;
        this.logger = logger;
    }

    public Command createShapeCommand(Shape shape) {
        if (shape == null) {
            logger.log("Warning: Attempted to create command for null shape");
            return null;
        }
        return new CreateShapeCommand(shapeManager, shape, logger);
    }

    public Command deleteShapeCommand(Shape shape) {
        if (shape == null) {
            logger.log("Warning: Attempted to create delete command for null shape");
            return null;
        }
        return new DeleteShapeCommand(shapeManager, shape, logger);
    }

    public Command moveShapeCommand(Shape shape, double dx, double dy) {
        if (shape == null) {
            logger.log("Warning: Attempted to create move command for null shape");
            return null;
        }
        return new MoveShapeCommand(shape, dx, dy, logger);
    }

    public Command changeColorCommand(Shape shape, Color oldColor, Color newColor) {
        if (shape == null) {
            logger.log("Warning: Attempted to create color change command for null shape");
            return null;
        }
        if (oldColor == null || newColor == null) {
            logger.log("Warning: Invalid color values for color change command");
            return null;
        }
        return new ChangeColorCommand(shape, oldColor, newColor, logger);
    }

    public Command rotateShapeCommand(Shape shape, double angle) {
        if (shape == null) {
            logger.log("Warning: Attempted to create rotate command for null shape");
            return null;
        }
        if (!(shape instanceof RotatableShape)) {
            logger.log("Warning: Shape does not support rotation");
            return null;
        }
        RotatableShape rotatableShape = (RotatableShape) shape;
        double oldAngle = rotatableShape.getRotationAngle();
        return new RotateShapeCommand(rotatableShape, oldAngle, angle, logger);
    }

    public Command createRotateCommand(RotatableShape shape, double oldAngle, double newAngle) {
        if (shape == null) {
            logger.log("Warning: Attempted to create rotate command for null shape");
            return null;
        }
        return new RotateShapeCommand(shape, oldAngle, newAngle, logger);
    }

    public Command createResizeCommand(Shape shape, double oldWidth, double oldHeight, double newWidth, double newHeight) {
        if (shape == null) {
            logger.log("Warning: Attempted to create resize command for null shape");
            return null;
        }
        if (oldWidth <= 0 || oldHeight <= 0 || newWidth <= 0 || newHeight <= 0) {
            logger.log("Warning: Invalid dimensions for resize command");
            return null;
        }
        return new ResizeShapeCommand(shape, oldWidth, oldHeight, newWidth, newHeight, logger);
    }

    public Command createNodeCommand(Node node) {
        if (node == null) {
            logger.log("Warning: Attempted to create command for null node");
            return null;
        }
        return new CreateNodeCommand(node, graphManager, logger);
    }

    public Command createEdgeCommand(Edge edge) {
        if (edge == null) {
            logger.log("Warning: Attempted to create command for null edge");
            return null;
        }
        return new CreateEdgeCommand(edge, graphManager, logger);
    }
} 