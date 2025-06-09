package com.drawingapp.drawingapp.domain.commands;

import com.drawingapp.drawingapp.infrastructure.logging.ILogger;
import java.util.Stack;
import java.util.Objects;

public class CommandHistory {
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;
    private final ILogger logger;

    public CommandHistory(ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger cannot be null");
        }
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.logger = logger;
    }

    public void executeCommand(Command command) {
        if (command == null) {
            logger.log("Warning: Attempted to execute null command");
            return;
        }
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        logger.log("Executed command: " + command.getClass().getSimpleName());
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            logger.log("Undid command: " + command.getClass().getSimpleName());
        } else {
            logger.warn("Nothing to undo");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            logger.log("Redid command: " + command.getClass().getSimpleName());
        } else {
            logger.warn("Nothing to redo");
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
        logger.log("Command history cleared");
    }

    public int getUndoStackSize() {
        return undoStack.size();
    }

    public int getRedoStackSize() {
        return redoStack.size();
    }
} 