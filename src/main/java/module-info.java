module com.example.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.desktop;
    requires java.prefs;
//    requires com.example.arkanoid;

    opens com.example.arkanoid.main to javafx.fxml;
    opens com.example.arkanoid.controllers to javafx.fxml;

    exports com.example.arkanoid.main;
    exports com.example.arkanoid.controllers;
    exports com.example.arkanoid.models;
    exports com.example.arkanoid.utils;
    exports com.example.arkanoid.models.Power;
}