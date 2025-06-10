package com.drawingapp.drawingapp.logging;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements LoggerStrategy {
    private String filename;

    public FileLogger(String filename) {
        this.filename = filename;
        System.out.println("Logging to: " + new java.io.File(filename).getAbsolutePath());
    }

    @Override
public void log(String message) {
    try (FileWriter fw = new FileWriter(filename, true)) {
        fw.write(message + System.lineSeparator());
    } catch (IOException e) {
        System.err.println("FileLogger failed to write to file: " + filename);
        e.printStackTrace();
    }
}

    
}
    
    

