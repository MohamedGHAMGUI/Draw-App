package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.graph.Edge;
import com.drawingapp.drawingapp.application.services.GraphManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;

public class CreateEdgeCommand implements Command {
    private final Edge edge;
    private final GraphManager graphManager;
    private final ILogger logger;

    public CreateEdgeCommand(Edge edge, GraphManager graphManager, ILogger logger) {
        this.edge = edge;
        this.graphManager = graphManager;
        this.logger = logger;
    }

    @Override
    public void execute() {
        graphManager.addEdge(edge);
        logger.log("Created edge: " + edge.getLabel());
    }

    @Override
    public void undo() {
        graphManager.removeEdge(edge);
        logger.log("Undid edge creation: " + edge.getLabel());
    }

    @Override
    public void redo() {
        graphManager.addEdge(edge);
        logger.log("Redid edge creation: " + edge.getLabel());
    }
} 