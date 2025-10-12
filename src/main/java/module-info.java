module com.example.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.jfr;

    opens com.example.arkanoid.main to javafx.fxml;
    opens com.example.arkanoid.controllers to javafx.fxml;

    exports com.example.arkanoid.main;
    exports com.example.arkanoid.controllers;
    exports com.example.arkanoid.models;
}