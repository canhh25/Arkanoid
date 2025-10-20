package com.example.arkanoid.controllers;

import com.example.arkanoid.main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button btnStart, btnHelp, btnEsc, btnSound, btnBack;

    @FXML
    private void handleStart(ActionEvent event) {

    }

    @FXML
    private void handleHelp(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/arkanoid/views/ViewMenu.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Instructions");
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    private void handleEsc(ActionEvent event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSound(ActionEvent event) {

    }

    @FXML
    private void handleBack(ActionEvent event) {

    }

}
