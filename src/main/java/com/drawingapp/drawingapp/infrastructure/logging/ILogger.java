package com.drawingapp.drawingapp.infrastructure.logging;

public interface ILogger {
    void log(String message);
    void error(String message);
    void warn(String message);
} 