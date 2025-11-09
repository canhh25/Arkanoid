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

    @FXML
    public void initialize() {
        setupLevelButtons();
    }

    // THÊM METHOD PUBLIC NÀY ĐỂ REFRESH TỪ BÊN NGOÀI
    public void refreshLevelButtons() {
        setupLevelButtons();
        System.out.println("Refreshing level buttons. Unlocked level: " +
                GameManager.getInstance().getUnlockedLevel());
    }

    private void setupLevelButtons() {
        GameManager gameManager = GameManager.getInstance();
        int unlockedLevel = gameManager.getUnlockedLevel();

        Button[] levelButtons = {
                btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5,
                btnLevel6, btnLevel7, btnLevel8, btnLevel9, btnLevel10
        };

        for (int i = 0; i < levelButtons.length; i++) {
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

            Stage levelStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            levelStage.close();

            Stage mainStage = (Stage) levelStage.getOwner();
            if (mainStage == null) {
                mainStage = new Stage();
            }

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameManager gameManager = GameManager.getInstance();
            gameManager.setupLevel(level);

            GameController gameController = new GameController(gc, level);

            mainStage.setTitle("Arkanoid - Level " + level);
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.show();

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
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.arkanoid/main/MenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Arkanoid");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleContinue(ActionEvent event) {
        GameManager gameManager = GameManager.getInstance();
        int unlockedLevel = gameManager.getUnlockedLevel();
        startLevel(unlockedLevel, event);
    }
}
