module com.example.teamproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires opencv;

    opens com.example.teamproject to javafx.fxml;
    exports com.example.teamproject;
    exports com.example.teamproject.controller;
    opens com.example.teamproject.controller to javafx.fxml;
}