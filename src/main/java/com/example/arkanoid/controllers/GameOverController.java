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
    private void handleAgain(javafx.event.ActionEvent event) {
        try {
            Stage gameOverStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            gameOverStage.close();
            Stage gameStage = (Stage) gameOverStage.getOwner();
            if (gameStage != null) {
                gameStage.close();
            }

            Stage newStage = new Stage();
            Canvas canvas = new Canvas(960, 640);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, 960, 640);

            GameManager gameManager = GameManager.getInstance();
            gameManager.resetGameKeepLevel();

            int levelToPlay = gameManager.getLevel();
            GameController controller = new GameController(gc, levelToPlay);
            newStage.setTitle("Arkanoid - Level " + levelToPlay);
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.show();
            controller.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleQuit(javafx.event.ActionEvent event) {
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
