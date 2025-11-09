package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameOverController {

    @FXML
    private void handleStart(javafx.event.ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            Canvas canvas = new Canvas(960, 640);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, 960, 640);

            GameManager.getInstance().resetGame();
            GameController controller = new GameController(gc, GameManager.getInstance().getLevel());
            stage.setScene(scene);
            controller.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEsc(javafx.event.ActionEvent event) {
        try {
            GameManager.getInstance().resetGame();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/com.example.arkanoid/main/MenuView.fxml"));
            stage.setScene(new Scene(loader.load(), 960, 640));
            stage.setTitle("Arkanoid Menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
