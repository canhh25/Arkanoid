package com.example.arkanoid.controllers;

import com.example.arkanoid.utils.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    @FXML
    private Button btnStart, btnHelp, btnEsc, btnBack;

    @FXML
    private ImageView btnVolume;

    private boolean mute = false;
    private Image volumeSprite;

    public void openGame(Stage stage) {
        try {
            SoundManager.stopBackgroundMusic();
            SoundManager.playGameStart();

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameController gameController = new GameController(gc, 1);

            // DÙNG LẠI STAGE HIỆN TẠI thay vì tạo mới
            stage.setTitle("Arkanoid - Level 1");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            gameController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStart(ActionEvent event) {
        // PHÁT ÂM THANH START
        SoundManager.playGameStart();
        Stage stage = (Stage) btnStart.getScene().getWindow();
        openGame(stage);

    }

    @FXML
    private void handleHelp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.arkanoid/main/HelpView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Instructions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEsc(ActionEvent event) {
        SoundManager.stopBackgroundMusic();
        Stage stage = (Stage) btnEsc.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        if (btnVolume != null) {
            volumeSprite = new Image(getClass().getResource("/images/menu/volume.png").toExternalForm());
            btnVolume.setImage(volumeSprite);
            btnVolume.setViewport(new Rectangle2D(0, 0, 70, 70));
            SoundManager.playBackgroundMusic("/sounds/nen.mp3");
        }
    }

    @FXML
    private void toggleVolume() {
        mute = !mute;
        SoundManager.setMuted(mute);
        if (mute) {
            btnVolume.setViewport(new Rectangle2D(70, 0, 70, 70));
            // xu li am thanh
        } else {
            btnVolume.setViewport(new Rectangle2D(0, 0, 70, 70));
            // xu li am thanh
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    public void openGameOver(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com.example.arkanoid/main/GameOverView.fxml"));
            stage.setScene(new Scene(root, WIDTH, HEIGHT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void selectLevel(ActionEvent event) {
        try {
            Stage menuStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.arkanoid/main/LevelView.fxml"));
            Parent root = loader.load();

            // LẤY CONTROLLER ĐỂ CÓ THỂ REFRESH SAU
            LevelController levelController = loader.getController();

            // DÙNG LẠI STAGE HIỆN TẠI thay vì tạo Stage mới
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene levelScene = new Scene(root);

            currentStage.setTitle("Select Level");
            currentStage.setScene(levelScene);

            // REFRESH NÚT KHI CHUYỂN SCENE
            levelController.refreshLevelButtons();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
