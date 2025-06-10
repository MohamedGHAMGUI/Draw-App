package com.drawingapp.drawingapp.controllers;

import com.drawingapp.drawingapp.logging.LoggerManager;
import com.drawingapp.drawingapp.services.DrawingRepository;
import com.drawingapp.drawingapp.services.GraphManager;
import com.drawingapp.drawingapp.services.ModeManager;
import com.drawingapp.drawingapp.services.ShapeManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Optional;

public class FileOperationController extends BaseController implements FileOperationHandler {
    private DrawingModeHandler drawingModeHandler;
    
    public FileOperationController(ShapeManager shapeManager, ModeManager modeManager, 
                                 GraphManager graphManager, DrawingRepository drawingRepository) {
        super(shapeManager, modeManager, graphManager, drawingRepository);
    }
    
    public void setDrawingModeHandler(DrawingModeHandler drawingModeHandler) {
        this.drawingModeHandler = drawingModeHandler;
    }

    @Override
    public void onSaveDrawing() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Drawing");
        dialog.setHeaderText("Enter a name for your drawing");
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(name -> {
            drawingRepository.saveDrawing(
                name,
                shapeManager.getShapes(),
                graphManager.getNodes(),
                graphManager.getEdges()
            );
            LoggerManager.getInstance().log(String.format("Saved drawing '%s' with %d shapes, %d nodes, and %d edges", 
                name, shapeManager.getShapes().size(), 
                graphManager.getNodes().size(), 
                graphManager.getEdges().size()));
        });
    }

    @Override
    public void onLoadDrawing() {
        List<String> drawings = drawingRepository.listSavedDrawings();
        if (drawings.isEmpty()) {
            showAlert("No drawings found");
            LoggerManager.getInstance().log("Attempted to load drawing but no saved drawings found");
            return;
        }
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>(drawings.get(0), drawings);
        dialog.setTitle("Load Drawing");
        dialog.setHeaderText("Select a drawing to load");
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(name -> {
            DrawingRepository.DrawingData data = drawingRepository.loadDrawing(name);
            if (data != null) {
                shapeManager.clear();
                graphManager.clear();
                
                data.getShapes().forEach(shapeManager::addShape);
                data.getNodes().forEach(graphManager::addNode);
                data.getEdges().forEach(graphManager::addEdge);
                
                LoggerManager.getInstance().log(String.format("Loaded drawing '%s' with %d shapes, %d nodes, and %d edges", 
                    name, data.getShapes().size(), 
                    data.getNodes().size(), 
                    data.getEdges().size()));
                
                drawingModeHandler.redrawCanvas();
            }
        });
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 