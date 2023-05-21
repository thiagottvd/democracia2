module com.example.democracia_desktop {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.example.democracia_desktop to javafx.fxml;
    exports com.example.democracia_desktop;
    exports com.example.democracia_desktop.controllers;
    exports com.example.democracia_desktop.models;
    opens com.example.democracia_desktop.controllers to javafx.fxml;
}