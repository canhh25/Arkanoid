package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.utils.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LevelController {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    @FXML private Button btnLevel1;
    @FXML private Button btnLevel2;
    @FXML private Button btnLevel3;
    @FXML private Button btnLevel4;
    @FXML private Button btnLevel5;
    @FXML private Button btnLevel6;
    @FXML private Button btnLevel7;
    @FXML private Button btnLevel8;
    @FXML private Button btnLevel9;
    @FXML private Button btnLevel10;
    @FXML private Button btnBack;

    private GameController gameController;
    private int currentLevel;


    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }
    // THÊM METHOD PUBLIC NÀY ĐỂ REFRESH TỪ BÊN NGOÀI
    public void refreshLevelButtons() {
        setupLevelButtons();
        System.out.println("Refreshing level buttons. Unlocked level: " +
                GameManager.getInstance().getUnlockedLevel());
    }

    private Button[] levelButtons;

    @FXML
    private void initialize() {
        // Khởi tạo mảng TRONG initialize(), SAU KHI các button đã được inject
        levelButtons = new Button[]{
                btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5,
                btnLevel6, btnLevel7, btnLevel8, btnLevel9, btnLevel10
        };

        setupLevelButtons();
    }

    private void setupLevelButtons() {
        GameManager gameManager = GameManager.getInstance();
        int unlockedLevel = gameManager.getUnlockedLevel();

        // Bỏ dòng khai báo mảng này đi vì đã khai báo ở trên
        // Button[] levelButtons = { ... }; // XÓA DÒNG NÀY

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
            System.out.println("Starting level " + level + "...");

            SoundManager.playGameStart();
            SoundManager.stopBackgroundMusic();

            // LẤY STAGE HIỆN TẠI thay vì tạo mới
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

    @FXML
    private void handleLevel1(ActionEvent event) { startLevel(1, event); }
    @FXML private void handleLevel2(ActionEvent event) { startLevel(2, event); }
    @FXML private void handleLevel3(ActionEvent event) { startLevel(3, event); }
    @FXML private void handleLevel4(ActionEvent event) { startLevel(4, event); }
    @FXML private void handleLevel5(ActionEvent event) { startLevel(5, event); }
    @FXML private void handleLevel6(ActionEvent event) { startLevel(6, event); }
    @FXML private void handleLevel7(ActionEvent event) { startLevel(7, event); }
    @FXML private void handleLevel8(ActionEvent event) { startLevel(8, event); }
    @FXML private void handleLevel9(ActionEvent event) { startLevel(9, event); }
    @FXML private void handleLevel10(ActionEvent event) { startLevel(10, event); }
    @FXML
    private void handleRestart(ActionEvent event) {
        try {
            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            Stage gameStage = (Stage) pauseStage.getOwner();
            if (gameStage != null) {
                gameStage.close();
            }

            Stage newStage = new Stage();
            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameManager gameManager = GameManager.getInstance();
            gameManager.setupLevel(currentLevel);

            GameController newGameController = new GameController(gc, currentLevel);

            newStage.setTitle("Level " + currentLevel);
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.show();

            newGameController.start();
        } catch (Exception e) {
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
        try {
            // Quay lại màn hình menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.arkanoid/main/MenuView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Arkanoid Menu");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}