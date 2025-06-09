package com.drawingapp.drawingapp.domain.commands;

import java.util.Objects;

public class CommandResult {
    private final boolean success;
    private final String message;

    public CommandResult(boolean success, String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static CommandResult success(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        return new CommandResult(true, message);
    }

    public static CommandResult failure(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        return new CommandResult(false, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandResult that = (CommandResult) o;
        return success == that.success && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message);
    }
} 