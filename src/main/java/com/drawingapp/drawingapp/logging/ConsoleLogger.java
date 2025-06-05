package com.drawingapp.drawingapp.logging;

public class ConsoleLogger implements LoggerStrategy {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
