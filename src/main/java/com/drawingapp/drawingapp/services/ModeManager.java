package com.drawingapp.drawingapp.services;

public class ModeManager {
    public enum Mode {
        DRAW, SELECT, MOVE, RESIZE,
        NODE_DRAW, EDGE_DRAW, PATH_FIND
    }

    private Mode currentMode = Mode.DRAW;

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
    }
} 