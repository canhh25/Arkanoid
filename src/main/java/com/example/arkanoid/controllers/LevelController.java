package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LevelController {
    @FXML private Button btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5;
    @FXML private Button btnLevel6, btnLevel7, btnLevel8, btnLevel9, btnLevel10;
    @FXML private Button btnBack;

    private GameController gameController;
    private int currentLevel;
    private Button[] levelButtons;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public void refreshLevelButtons() {
        setupLevelButtons();
        System.out.println("Refreshing level buttons. Unlocked level: " +
                GameManager.getInstance().getUnlockedLevel());
    }

    @FXML
    private void initialize() {
        levelButtons = new Button[]{
                btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5,
                btnLevel6, btnLevel7, btnLevel8, btnLevel9, btnLevel10
        };
        setupLevelButtons();
    }

    private void setupLevelButtons() {
        GameManager gameManager = GameManager.getInstance();
        int unlockedLevel = gameManager.getUnlockedLevel();

        for (int i = 0; i < levelButtons.length; i++) {
            if (levelButtons[i] == null) {
                System.err.println("Button " + (i+1) + " is null!");
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
        try {
            System.out.println("Starting level " + level + " from menu...");

            SoundManager.playGameStart();
            SoundManager.stopBackgroundMusic();

            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameManager gameManager = GameManager.getInstance();
            gameManager.setupLevel(level);

            GameController gameController = new GameController(gc, level);

            currentStage.setTitle("Arkanoid - Level " + level);
            currentStage.setScene(scene);
            currentStage.setResizable(false);

            gameController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLevelFromPause(int level, ActionEvent event) {
        try {
            System.out.println("Starting level " + level + " from pause...");

            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            Stage gameStage = (Stage) pauseStage.getOwner();

            if (gameStage == null) {
                System.err.println(" Không tìm thấy game stage!");
                return;
            }

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameManager gameManager = GameManager.getInstance();
            gameManager.resetGameKeepLevel();

            GameController newGameController = new GameController(gc, level);

            gameStage.setTitle("Arkanoid - Level " + level);
            gameStage.setScene(scene);
            gameStage.setResizable(false);

            newGameController.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLevelSelection(int level, ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        if (currentStage.getOwner() != null) {
            startLevelFromPause(level, event);
        } else {
            startLevel(level, event);
        }
    }

    @FXML
    private void handleLevel1(ActionEvent event) { handleLevelSelection(1, event); }

    @FXML
    private void handleLevel2(ActionEvent event) { handleLevelSelection(2, event); }

    @FXML
    private void handleLevel3(ActionEvent event) { handleLevelSelection(3, event); }

    @FXML
    private void handleLevel4(ActionEvent event) { handleLevelSelection(4, event); }

    @FXML
    private void handleLevel5(ActionEvent event) { handleLevelSelection(5, event); }

    @FXML
    private void handleLevel6(ActionEvent event) { handleLevelSelection(6, event); }

    @FXML
    private void handleLevel7(ActionEvent event) { handleLevelSelection(7, event); }

    @FXML
    private void handleLevel8(ActionEvent event) { handleLevelSelection(8, event); }

    @FXML
    private void handleLevel9(ActionEvent event) { handleLevelSelection(9, event); }

    @FXML
    private void handleLevel10(ActionEvent event) { handleLevelSelection(10, event); }

    @FXML
    private void handleRestart(ActionEvent event) {
        try {
            System.out.println(" Restarting level " + currentLevel + "...");

            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            Stage gameStage = (Stage) pauseStage.getOwner();

            if (gameStage == null) {
                System.err.println(" Không tìm thấy game stage!");
                return;
            }

            GameManager gameManager = GameManager.getInstance();
            gameManager.resetTimer();

            gameManager.gameState = com.example.arkanoid.models.GameState.RUNNING;
            gameManager.setupLevel(currentLevel);

            System.out.println(" GameManager reset: Lives=" + gameManager.getLives() +
                    ", Score=" + gameManager.getScore() +
                    ", State=" + gameManager.gameState);

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameController newGameController = new GameController(gc, currentLevel);

            gameStage.setTitle("Arkanoid - Level " + currentLevel);
            gameStage.setScene(scene);
            gameStage.setResizable(false);

            newGameController.start();

            System.out.println(" Game started successfully!");

        } catch (Exception e) {
            System.err.println(" Lỗi khi restart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResume(ActionEvent event) {
        try {
            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            if (gameController != null) {
                gameController.resumeGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        getNavigationFacade().navigateToMenu();
    }
}
