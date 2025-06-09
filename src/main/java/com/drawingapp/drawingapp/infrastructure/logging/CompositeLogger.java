package com.drawingapp.drawingapp.infrastructure.logging;

import java.util.Arrays;
import java.util.List;

public class CompositeLogger implements ILogger {
    private final List<ILogger> loggers;

    public CompositeLogger(ILogger... loggers) {
        this.loggers = Arrays.asList(loggers);
    }

    @Override
    public void log(String message) {
        for (ILogger logger : loggers) {
            logger.log(message);
        }
    }

    @Override
    public void error(String message) {
        for (ILogger logger : loggers) {
            logger.error(message);
        }
    }

    @Override
    public void warn(String message) {
        for (ILogger logger : loggers) {
            logger.warn(message);
        }
    }

    public void close() {
        for (ILogger logger : loggers) {
            if (logger instanceof DatabaseLogger) {
                ((DatabaseLogger) logger).close();
            }
        }
    }
} 