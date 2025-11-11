package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
    private void handleAgain(javafx.event.ActionEvent event) {
        try {
            GameManager gameManager = GameManager.getInstance();
            gameManager.resetGameKeepLevel();
            int levelToPlay = gameManager.getLevel();

            if (navigationFacade != null) {
                navigationFacade.restartCurrentLevel(levelToPlay);
            }
        } catch (Exception e) {

        }
    }

    @FXML
    private void handleQuit(javafx.event.ActionEvent event) {
        GameManager.getInstance().resetGame();

        if (navigationFacade != null) {
            navigationFacade.navigateToMenu();
        }
    }
}
