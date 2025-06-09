package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.domain.graph.Node;
import com.drawingapp.drawingapp.application.services.GraphManager;
import com.drawingapp.drawingapp.infrastructure.logging.ILogger;

public class CreateNodeCommand implements Command {
    private final Node node;
    private final GraphManager graphManager;
    private final ILogger logger;

    public CreateNodeCommand(Node node, GraphManager graphManager, ILogger logger) {
        this.node = node;
        this.graphManager = graphManager;
        this.logger = logger;
    }

    @Override
    public void execute() {
        graphManager.addNode(node);
        logger.log("Created node: " + node.getLabel());
    }

    @Override
    public void undo() {
        graphManager.removeNode(node);
        logger.log("Undid node creation: " + node.getLabel());
    }

    @Override
    public void redo() {
        graphManager.addNode(node);
        logger.log("Redid node creation: " + node.getLabel());
    }
} 