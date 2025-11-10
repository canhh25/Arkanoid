package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.utils.SoundManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class GameFacade {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    private static GameFacade instance;
    private final Stage primaryStage;

    private GameFacade(Stage stage) {
        this.primaryStage = stage;
    }

    public static GameFacade getInstance(Stage stage) {
        if (instance == null) {
            instance = new GameFacade(stage);
        }
        return instance;
    }

    public void navigateToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.arkanoid/main/MenuView.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root, WIDTH, HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Arkanoid Menu");

            SoundManager.playBackgroundMusic("/sounds/nen.mp3");

        } catch (Exception e) {
            System.err.println("Error navigating to menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void navigateToGame(int level) {
        try {
            SoundManager.stopBackgroundMusic();
            SoundManager.playGameStart();

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameManager.getInstance().setupLevel(level);
            GameController gameController = new GameController(gc, level);

            primaryStage.setTitle("Arkanoid - Level " + level);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            gameController.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigateToGameOver(int currentScore, int maxScore) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.arkanoid/main/GameOverView.fxml")
            );
            Parent root = loader.load();

            GameOverController controller = loader.getController();
            controller.setScores(currentScore, maxScore);
            controller.setNavigationFacade(this);

            Scene scene = new Scene(root, WIDTH, HEIGHT);
            primaryStage.setTitle("Game Over");
            primaryStage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
            navigateToMenu();
        }
    }

    public void navigateToLevelSelect() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.arkanoid/main/LevelView.fxml")
            );
            Parent root = loader.load();

            LevelController levelController = loader.getController();
            levelController.setNavigationFacade(this);
            levelController.refreshLevelButtons();

            Scene scene = new Scene(root, WIDTH, HEIGHT);
            primaryStage.setTitle("Select Level");
            primaryStage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHelpDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.arkanoid/main/HelpView.fxml")
            );
            Parent root = loader.load();

            Stage helpStage = new Stage();
            helpStage.initModality(Modality.APPLICATION_MODAL);
            helpStage.initOwner(primaryStage);
            helpStage.setTitle("Instructions");
            helpStage.setScene(new Scene(root));
            helpStage.setResizable(false);

            Button btnBack = (Button) root.lookup("#btnBack");
            if (btnBack != null) {
                btnBack.setOnAction(e -> helpStage.close());
            }

            helpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Stage showPauseDialog(GameController gameController, int currentLevel) {
        try {
            Stage pauseStage = new Stage();
            pauseStage.initModality(Modality.APPLICATION_MODAL);
            pauseStage.initOwner(primaryStage);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.arkanoid/main/PauseView.fxml")
            );
            Parent root = loader.load();

            LevelController levelController = loader.getController();
            levelController.setGameController(gameController);
            levelController.setCurrentLevel(currentLevel);
            levelController.setNavigationFacade(this);

            Scene scene = new Scene(root);
            pauseStage.setScene(scene);
            pauseStage.setTitle("Paused");
            pauseStage.setResizable(false);

            pauseStage.setOnCloseRequest(e -> gameController.resumeGame());

            return pauseStage;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void restartCurrentLevel(int level) {
        try {
            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameManager gameManager = GameManager.getInstance();
            gameManager.setupLevel(level);

            GameController gameController = new GameController(gc, level);

            primaryStage.setTitle("Arkanoid - Level " + level);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            gameController.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitApplication() {
        SoundManager.stopBackgroundMusic();
        primaryStage.close();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
