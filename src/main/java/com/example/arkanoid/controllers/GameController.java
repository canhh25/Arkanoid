package com.example.arkanoid.controllers;

import com.example.arkanoid.main.Main;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.GameState;
import com.example.arkanoid.views.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameController {
    private boolean goLeft = false;
    private boolean goRight = false;
    private boolean isPaused = false;

    private final GameManager gameManager;
    private final GameView gameView;
    private final GraphicsContext gc;
    private AnimationTimer animationTimer;

    public GameController(GraphicsContext gc, int level) {
        this.gc = gc;
        this.gameManager = GameManager.getInstance();
        this.gameView = new GameView();
        gameManager.setupLevel(level);
        setupInputHandling(gc.getCanvas().getScene());
    }

    public void start() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameManager.gameState == GameState.WIN) {
                    animationTimer.stop();
                    handleWin();
                    return;
                }

                if (gameManager.isGameOver()) {
                    animationTimer.stop();
                    showGameOver();
                    return;
                }

                gameManager.update(goLeft, goRight);
                gameView.render(gc, gameManager);

                drawGameInfo();
            }
        };
        animationTimer.start();
    }

    private void handleWin() {
        Platform.runLater(() -> {
            try {
                int currentLevel = gameManager.getLevel();
                int nextLevel = currentLevel + 1;

                gameManager.unlockNextLevel();

                if (nextLevel <= 10) {
                    Stage stage = (Stage) gc.getCanvas().getScene().getWindow();

                    Canvas canvas = new Canvas(960, 640);
                    GraphicsContext newGc = canvas.getGraphicsContext2D();
                    StackPane root = new StackPane(canvas);
                    Scene scene = new Scene(root, 960, 640);

                    gameManager.setupLevel(nextLevel);
                    GameController gameController = new GameController(newGc, nextLevel);

                    stage.setTitle("Arkanoid - Level " + nextLevel);
                    stage.setScene(scene);

                    gameController.start();
                } else {
                    Stage stage = (Stage) gc.getCanvas().getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass()
                            .getResource("/com.example.arkanoid/main/MenuView.fxml"));
                    Parent root = loader.load();
                    stage.setScene(new Scene(root, 960, 640));
                    stage.setTitle("Arkanoid Menu");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private int loadMaxScore() {
        try (Scanner sc = new Scanner(new File("max_score.txt"))) {
            if (sc.hasNextInt()) {
                return sc.nextInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void saveMaxScore(int score) {
        try (PrintWriter out = new PrintWriter("max_score.txt")) {
            out.println(score);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showGameOver() {
        Platform.runLater(() -> {
            try {
                int currentScore = gameManager.getScore();
                int maxScore = Math.max(loadMaxScore(), currentScore);
                saveMaxScore(maxScore);

                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/com.example.arkanoid/main/GameOverView.fxml"));
                Parent root = loader.load();

                GameOverController controller = loader.getController();
                controller.setScores(currentScore, maxScore);

                Stage stage = (Stage) gc.getCanvas().getScene().getWindow();
                stage.setTitle("Game Over");
                stage.setScene(new Scene(root, 960, 640));
            } catch (Exception e) {
                e.printStackTrace();
                backToMenu();
            }
        });
    }

    private void backToMenu() {
        try {
            Stage stage = (Stage) gc.getCanvas().getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com.example.arkanoid/main/MenuView.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 960, 640));
            stage.setTitle("Arkanoid Menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupInputHandling(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (!isPaused) {
                    pauseGame();
                    showPauseWindow();
                }
                return;
            }
            if (isPaused) {
                return;
            }

            if (event.getCode() == KeyCode.LEFT) {
                goLeft = true;
            } else if (event.getCode() == KeyCode.RIGHT) {
                goRight = true;
            }else if (event.getCode() == KeyCode.SPACE) {

                if (gameManager.gameState == GameState.GAME_OVER ||
                        gameManager.gameState == GameState.WIN) {

                    gameManager.nextGame();
                    return;
                }

                if (gameManager.gameState == GameState.DEAD) {
                    gameManager.setupGame();
                    return;
                }

                gameManager.requestLaunch();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                goLeft = false;
            } else if (event.getCode() == KeyCode.RIGHT) {
                goRight = false;
            }
        });

    }
    private void pauseGame() {
        isPaused = true;
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    public void resumeGame() {
        isPaused = false;
        if (animationTimer != null) {
            animationTimer.start();
        }
    }

    private void showPauseWindow() {
        try {
            Stage pauseStage = new Stage();
            pauseStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com.example.arkanoid/main/PauseView.fxml"));
            Parent root = loader.load();

            LevelController levelController = loader.getController();
            levelController.setGameController(this);
            levelController.setCurrentLevel(gameManager.getLevel());

            Scene scene = new Scene(root);
            pauseStage.setScene(scene);
            pauseStage.setTitle("Paused");
            pauseStage.setResizable(false);

            pauseStage.setOnCloseRequest(e -> resumeGame());
            pauseStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void render() {
        drawGameInfo();
    }

    private void drawGameInfo() {
        double padding = 20;
        double y = 60;

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Montserrat", FontWeight.BOLD, 28));
        gc.setTextAlign(TextAlignment.LEFT);

        String scoreText = "Score: " + gameManager.getScore();
        gc.fillText(scoreText, padding, y);

        String timeText = "Time: " + gameManager.getFormattedTime();
        double timeX = Main.WIDTH * 0.35;
        gc.fillText(timeText, timeX, y);

        String levelText = "Level: " + gameManager.getLevel();
        double levelX = Main.WIDTH * 0.58;
        gc.fillText(levelText, levelX, y);

        drawHearts(Main.WIDTH - padding, y);
        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void drawHearts(double rightX, double y) {
        int lives = gameManager.getLives();
        double heartSize = 25;
        double spacing = 8;
        double heartWidth = heartSize;
        for (int i = 0; i < lives; i++) {
            double heartX = rightX - (i * (heartWidth + spacing)) - heartSize;
            drawHeart(heartX, y - heartSize + 5, heartSize);
        }
    }

    private void drawHeart(double x, double y, double size) {
        gc.setFill(Color.rgb(255, 50, 80));
        double centerX = x + size / 2;
        double centerY = y + size / 2;

        gc.beginPath();
        double topLeftX = centerX - size * 0.25;
        double topLeftY = centerY - size * 0.25;
        double topRightX = centerX + size * 0.25;
        double topRightY = centerY - size * 0.25;
        double bottomX = centerX;
        double bottomY = centerY + size * 0.4;

        gc.moveTo(centerX, centerY);
        gc.arc(topLeftX, topLeftY, size * 0.25, size * 0.25, 230, 180);
        gc.lineTo(bottomX, bottomY);

        gc.lineTo(centerX, centerY);
        gc.arc(topRightX, topRightY, size * 0.25, size * 0.25, 310, 180);
        gc.lineTo(bottomX, bottomY);

        gc.closePath();
        gc.fill();
        gc.setStroke(Color.rgb(200, 30, 60));
        gc.setLineWidth(2);
        gc.stroke();
    }

    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", mins, secs);
    }
}