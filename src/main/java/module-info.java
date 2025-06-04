module com.drawingapp.drawingapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.drawingapp.drawingapp to javafx.fxml;
    exports com.drawingapp.drawingapp;
    exports com.drawingapp.drawingapp.shapes_state_observer;
    opens com.drawingapp.drawingapp.shapes_state_observer to javafx.fxml;
}