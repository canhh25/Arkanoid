package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameOverController {
    @FXML private Label maxScoreLabel;
    @FXML private Label yourScoreLabel;

    private int currentScore;
    private int maxScore;
    private GameFacade navigationFacade;

    public void setNavigationFacade(GameFacade facade) {
        this.navigationFacade = facade;
    }

    public void setScores(int current, int max) {
        this.currentScore = current;
        this.maxScore = max;
        updateLabels();
    }

    private void updateLabels() {
        if (maxScoreLabel != null && yourScoreLabel != null) {
            maxScoreLabel.setText("" + maxScore);
            yourScoreLabel.setText("" + currentScore);

            Font gameFont = Font.loadFont(
                    getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 32
            );

            maxScoreLabel.setFont(gameFont);
            yourScoreLabel.setFont(gameFont);
            maxScoreLabel.setTextFill(Color.web("#FFD700"));
            yourScoreLabel.setTextFill(Color.web("#FFD700"));
        }
    }

    @FXML
    private void handleStart(javafx.event.ActionEvent event) {
        GameManager.getInstance().resetGame();

        if (navigationFacade != null) {
            navigationFacade.navigateToGame(1);
        }
    }

    @FXML
    private void handleEsc(javafx.event.ActionEvent event) {
        GameManager.getInstance().resetGame();

        if (navigationFacade != null) {
            navigationFacade.navigateToMenu();
        }
    }
}