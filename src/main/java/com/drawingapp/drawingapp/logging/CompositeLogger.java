package com.drawingapp.drawingapp.logging;

import java.util.ArrayList;
import java.util.List;

public class CompositeLogger implements LoggerStrategy {
    private List<LoggerStrategy> loggers;

    public CompositeLogger() {
        this.loggers = new ArrayList<>();
    }

    public void addLogger(LoggerStrategy logger) {
        loggers.add(logger);
    }

    @Override
    public void log(String message) {
        for (LoggerStrategy logger : loggers) {
            logger.log(message);
        }
    }
} 