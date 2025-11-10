package com.example.arkanoid.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HelpController {
    @FXML private Button btnBack;

    @FXML
    private void handleBack(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }
}
