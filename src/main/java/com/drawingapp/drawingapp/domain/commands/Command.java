package com.drawingapp.drawingapp.domain.commands;

public interface Command {
    void execute();
    void undo();
    void redo();
} 