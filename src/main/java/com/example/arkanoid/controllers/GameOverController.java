package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameOverController {
    @FXML
    private Label maxScoreLabel;
    @FXML
    private Label yourScoreLabel;

    private int currentScore;
    private int maxScore;

    public void setScores(int current, int max) {
        this.currentScore = current;
        this.maxScore = max;
        updateLabels();
    }

    private void updateLabels() {
        if (maxScoreLabel != null && yourScoreLabel != null) {
            maxScoreLabel.setText("" + maxScore);
            yourScoreLabel.setText("" + currentScore);
            Font gameFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 32);
            maxScoreLabel.setFont(gameFont);
            yourScoreLabel.setFont(gameFont);
            maxScoreLabel.setTextFill(Color.web("#FFD700")); // vàng pixel retro
            yourScoreLabel.setTextFill(Color.web("#FFD700"));
        }
    }
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
