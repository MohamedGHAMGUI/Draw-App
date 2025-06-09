package com.drawingapp.drawingapp.infrastructure.logging;

public class ConsoleLogger implements ILogger {
    @Override
    public void log(String message) {
        System.out.println("[INFO] " + message);
    }

    @Override
    public void error(String message) {
        System.err.println("[ERROR] " + message);
    }

    @Override
    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }
}
