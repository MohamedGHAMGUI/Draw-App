package com.drawingapp.drawingapp.shapes_state_observer;

public interface State {
    void onMousePressed(double x, double y);
    void onMouseDragged(double x, double y);
    void onMouseReleased(double x, double y);
}
