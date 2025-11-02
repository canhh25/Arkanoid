package com.example.arkanoid.controllers;

import com.example.arkanoid.main.Main;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.views.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class GameController {
    private boolean goLeft = false;
    private boolean goRight = false;

    private final GameManager gameManager;
    private final GameView gameView;
    private final GraphicsContext gc;
    private AnimationTimer animationTimer;

    public GameController(GraphicsContext gc) {
        this.gc = gc;
        this.gameManager = GameManager.getInstance();
        this.gameView = new GameView();
        setupInputHandling(gc.getCanvas().getScene());
    }

    public void start() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameManager.isGameOver()) {
                    animationTimer.stop();
                    showGameOver();
                    return;
                }
                gameManager.update(goLeft, goRight);
                gameView.render(gc, gameManager);
                render();
            }
        };
        animationTimer.start();
    }

    private void showGameOver() {
        Platform.runLater(() -> {
            Stage stage = (Stage) gc.getCanvas().getScene().getWindow();
            MenuController menuController = new MenuController();
            menuController.openGameOver(stage);
        });
    }

    private void setupInputHandling(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                goLeft = true;
            } else if (event.getCode() == KeyCode.RIGHT) {
                goRight = true;
            } else if (event.getCode() == KeyCode.SPACE) {
                if (gameManager.isGameOver()) {
                    gameManager.setupGame();
                } else {
                    gameManager.requestLaunch();
                }
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
    private void render() {
        drawGameInfo();
    }

    private void drawGameInfo() {
        double padding = 20;
        double y = 35;

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Montserrat", FontWeight.BOLD, 28));

        // === SCORE - Căn trái ===
        gc.setTextAlign(TextAlignment.LEFT);
        String scoreText = "Score: " + gameManager.getScore();
        gc.fillText(scoreText, padding, y);

        // === TIME - Ở giữa (lệch trái một chút) ===
        String timeText = "Time: " + formatTime(14);
        double timeX = Main.WIDTH * 0.35; // 35% từ trái
        gc.fillText(timeText, timeX, y);

        // === LEVEL - Ở giữa (lệch phải một chút) ===
        String levelText = "Level: " + gameManager.getLevel();
        double levelX = Main.WIDTH * 0.58; // 58% từ trái
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
