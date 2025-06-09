package com.drawingapp.drawingapp.application.controllers;

import com.drawingapp.drawingapp.application.controllers.interfaces.IGraphController;
import com.drawingapp.drawingapp.application.services.ModeManager;
import com.drawingapp.drawingapp.application.services.GraphManager;
import com.drawingapp.drawingapp.application.services.PathFinder;
import com.drawingapp.drawingapp.domain.graph.Node;
import com.drawingapp.drawingapp.domain.graph.Edge;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import com.drawingapp.drawingapp.domain.algorithms.ShortestPathAlgorithm;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class GraphController implements IGraphController {
    private final GraphManager graphManager;
    private final ModeManager modeManager;
    private final PathFinder pathFinder;
    private final ILogger logger;
    private GraphicsContext gc;

    public GraphController(GraphManager graphManager, ModeManager modeManager, ILogger logger) {
        this.graphManager = graphManager;
        this.modeManager = modeManager;
        this.logger = logger;
        this.pathFinder = new PathFinder(graphManager, logger);
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public void handleMousePressed(double x, double y) {
        switch (modeManager.getCurrentMode()) {
            case NODE_DRAW:
                Node node = new Node(x, y, "Node " + graphManager.getNodes().size());
                graphManager.addNode(node);
                logger.log("Added node at (" + x + ", " + y + ")");
                break;
            case EDGE_DRAW:
                Node selectedNode = graphManager.getNodeAt(x, y);
                if (selectedNode != null) {
                    if (graphManager.getNodeSelector().getSelectedNode() == null) {
                        graphManager.getNodeSelector().setSelectedNode(selectedNode);
                        logger.log("Selected node for edge creation");
                    } else {
                        Edge edge = new Edge(
                            graphManager.getNodeSelector().getSelectedNode(),
                            selectedNode,
                            "Edge " + graphManager.getEdges().size()
                        );
                        graphManager.addEdge(edge);
                        graphManager.getNodeSelector().clearSelection();
                        logger.log("Created edge between nodes");
                    }
                }
                break;
            case PATH_FIND:
                Node pathNode = graphManager.getNodeAt(x, y);
                if (pathNode != null) {
                    if (pathFinder.getStartNode() == null) {
                        pathFinder.setStartNode(pathNode);
                    } else if (pathFinder.getEndNode() == null) {
                        pathFinder.setEndNode(pathNode);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void redrawCanvas() {
        if (gc != null) {
            // Draw edges first
            for (Edge edge : graphManager.getEdges()) {
                edge.draw(gc);
            }
            
            // Draw nodes on top
            for (Node node : graphManager.getNodes()) {
                node.draw(gc);
            }
        }
    }

    public void setAlgorithm(ShortestPathAlgorithm algorithm) {
        pathFinder.setAlgorithm(algorithm);
    }

    public void clearGraph() {
        graphManager.clear();
        pathFinder.clear();
    }
} 