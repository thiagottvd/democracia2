module com.example.democracia_desktop {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;
            
    opens com.example.democracia_desktop to javafx.fxml;
    exports com.example.democracia_desktop;
}