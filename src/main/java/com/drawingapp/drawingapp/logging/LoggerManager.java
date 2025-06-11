package com.drawingapp.drawingapp.logging;



    public class LoggerManager {
        private static LoggerManager instance;
        private LoggerStrategy strategy;

        private LoggerManager() {
            // Set ConsoleLogger as default strategy
            this.strategy = new ConsoleLogger();
        }

        public static LoggerManager getInstance() {
            if (instance == null) {
                instance = new LoggerManager();
            }
            return instance;
        }

        public void setStrategy(LoggerStrategy strategy) {
            this.strategy = strategy;
        }

        public void log(String message) {
            if (strategy != null) {
                strategy.log(message);
            }
        }
    }
