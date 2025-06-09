package com.drawingapp.drawingapp.application.services;

import com.drawingapp.drawingapp.infrastructure.logging.ILogger;

public class ModeManager {
    public enum Mode {
        DRAW, SELECT, MOVE, RESIZE,
        NODE_DRAW, EDGE_DRAW, PATH_FIND
    }

    private Mode currentMode = Mode.DRAW;
    private final ILogger logger;

    public ModeManager(ILogger logger) {
        this.logger = logger;
    }

    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
        logger.log("Mode set to: " + mode);
    }

    public Mode getCurrentMode() {
        return currentMode;
    }
} 