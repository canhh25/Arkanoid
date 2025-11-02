package com.example.arkanoid.views;

import com.example.arkanoid.models.Brick;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.GameState;
import javafx.animation.KeyFrame;
import javafx.scene.canvas.GraphicsContext;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;
    private final List<Image> backgroundFrames = new ArrayList<>();
    private int currentFrameIndex = 0;
    private final double FPS = 60.0;
    private Image currentBackground;
    private static final Random RANDOM = new Random();
    private static final String[] BG_FOLDERS = {"Pixel-Art Background 1",
            "Pixel-Art Background 2", "Pixel-Art Background 3", "Pixel-Art Background 4"};
    private static final int[] BG_FRAME_COUNTS = {140, 152, 142, 66};
    private String selectedFolder;
    private int selectedFrameCount;

    public GameView() {
        selectRandomBackground();
        loadBackgroundFrames();
        setupAnimation();
    }

    private void selectRandomBackground() {
        int randomIndex = RANDOM.nextInt(BG_FOLDERS.length);
        selectedFolder = BG_FOLDERS[randomIndex];
        selectedFrameCount = BG_FRAME_COUNTS[randomIndex];
    }

    private void loadBackgroundFrames() {
        for (int i = 1; i <= selectedFrameCount; i++) {
            String fileName = String.format("/images/background/%s/frame%04d.png", selectedFolder, i);
            try {
                backgroundFrames.add(new Image(getClass().getResource(fileName).toExternalForm()));
            } catch (Exception e) {
                System.err.println("Lỗi tải frame: " + fileName + " (Folder đã chọn: " + selectedFolder + ")");
            }
        }
        if (!backgroundFrames.isEmpty()) {
            currentBackground = backgroundFrames.get(0);
        }
    }

    private void setupAnimation() {
        if (backgroundFrames.isEmpty()) return;

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration frameDuration = Duration.millis(1000 / FPS);
        KeyFrame keyFrame = new KeyFrame(frameDuration, event -> {
            currentFrameIndex = (currentFrameIndex + 1) % selectedFrameCount;
            currentBackground = backgroundFrames.get(currentFrameIndex);
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void render(GraphicsContext gc, GameManager gameManager) {
        if (currentBackground != null) {
            gc.drawImage(currentBackground, 0, 0, WIDTH, HEIGHT);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
        }
        if (!gameManager.isGameOver()) {
            gameManager.getPaddle().render(gc);
            gameManager.renderBalls(gc);
            for (Brick brick : gameManager.getBricks()) {
                brick.render(gc);
            }
            gameManager.renderPowerUps(gc);
        } else {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("Arial", 40));
            gc.setTextAlign(TextAlignment.CENTER);

            if (gameManager.isEmptyBrick()) {
                gameManager.gameState = GameState.WIN;
                gc.fillText("YOU WIN!", WIDTH / 2.0, HEIGHT / 2.0);
            } else {
                gc.fillText("GAME OVER", WIDTH / 2.0, HEIGHT / 2.0);
            }

            if (gameManager.gameState == GameState.WIN || gameManager.gameState == GameState.GAME_OVER) {
                gameManager.nextGame();
            }

            gc.setFont(new Font("Arial", 20));
            gc.fillText("Press SPACE to Restart", WIDTH / 2.0, HEIGHT / 2.0 + 40);
        }
    }
}