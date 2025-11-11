package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LevelController {
    @FXML
    private Button btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5;
    @FXML
    private Button btnLevel6, btnLevel7, btnLevel8, btnLevel9, btnLevel10;
    @FXML
    private Button btnBack;

    private GameController gameController;
    private int currentLevel;
    private Button[] levelButtons;
    private GameFacade navigationFacade;

    public void setNavigationFacade(GameFacade facade) {
        this.navigationFacade = facade;
    }

    private GameFacade getNavigationFacade() {
        if (navigationFacade == null && btnBack != null) {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            navigationFacade = GameFacade.getInstance(stage);
        }
        return navigationFacade;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public void refreshLevelButtons() {
        setupLevelButtons();
    }

    @FXML
    private void initialize() {
        levelButtons = new Button[]{
                btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5,
                btnLevel6, btnLevel7, btnLevel8, btnLevel9, btnLevel10
        };
        setupLevelButtons();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        try {
            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            com.example.arkanoid.models.GameManager.getInstance().resetGame();
            getNavigationFacade().navigateToMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLevelButtons() {
        GameManager gameManager = GameManager.getInstance();
        int unlockedLevel = gameManager.getUnlockedLevel();

        for (int i = 0; i < levelButtons.length; i++) {
            if (levelButtons[i] == null) {
                continue;
            }

            int levelNumber = i + 1;
            if (levelNumber > unlockedLevel) {
                levelButtons[i].setDisable(true);
                levelButtons[i].setOpacity(0.3);
                levelButtons[i].setStyle(levelButtons[i].getStyle() + "-fx-cursor: default;");
            } else {
                levelButtons[i].setDisable(false);
                levelButtons[i].setOpacity(1.0);
                levelButtons[i].setStyle(levelButtons[i].getStyle() + "-fx-cursor: hand;");
            }
        }
    }

    private void startLevel(int level, ActionEvent event) {
        getNavigationFacade().navigateToGame(level);
    }

    @FXML
    private void handleLevel1(ActionEvent event) {
        startLevel(1, event);
    }

    @FXML
    private void handleLevel2(ActionEvent event) {
        startLevel(2, event);
    }

    @FXML
    private void handleLevel3(ActionEvent event) {
        startLevel(3, event);
    }

    @FXML
    private void handleLevel4(ActionEvent event) {
        startLevel(4, event);
    }

    @FXML
    private void handleLevel5(ActionEvent event) {
        startLevel(5, event);
    }

    @FXML
    private void handleLevel6(ActionEvent event) {
        startLevel(6, event);
    }

    @FXML
    private void handleLevel7(ActionEvent event) {
        startLevel(7, event);
    }

    @FXML
    private void handleLevel8(ActionEvent event) {
        startLevel(8, event);
    }

    @FXML
    private void handleLevel9(ActionEvent event) {
        startLevel(9, event);
    }

    @FXML
    private void handleLevel10(ActionEvent event) {
        startLevel(10, event);
    }

    @FXML
    private void handleRestart(ActionEvent event) {
        try {
            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            GameManager.getInstance().resetLives();
            GameManager.getInstance().resetScore();
            GameManager.getInstance().resetTimer();
            if (gameController != null) {
                gameController.restartLevel();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResume(ActionEvent event) {
        Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        pauseStage.close();

        if (gameController != null) {
            gameController.resumeGame();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        getNavigationFacade().navigateToMenu();
    }

    @FXML
    private void handleContinue(ActionEvent event) {
        GameManager.getInstance().resetLives();
        getNavigationFacade().navigateToNextLevel();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}
