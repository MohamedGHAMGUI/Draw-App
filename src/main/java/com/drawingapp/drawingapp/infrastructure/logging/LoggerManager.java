package com.drawingapp.drawingapp.infrastructure.logging;

import java.util.ArrayList;
import java.util.List;

public class LoggerManager {
    private final List<ILogger> loggers;
    private static LoggerManager instance;

    private LoggerManager() {
        this.loggers = new ArrayList<>();
    }

    public static LoggerManager getInstance() {
        if (instance == null) {
            instance = new LoggerManager();
        }
        return instance;
    }

    public void addLogger(ILogger logger) {
        loggers.add(logger);
    }

    public void log(String message) {
        for (ILogger logger : loggers) {
            logger.log(message);
        }
    }
}
