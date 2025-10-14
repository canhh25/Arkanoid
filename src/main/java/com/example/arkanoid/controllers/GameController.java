package com.example.arkanoid.controllers;

import com.example.arkanoid.main.Main;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.views.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameController {
    private boolean goLeft = false;
    private boolean goRight = false;

    private final GameManager gameManager;
    private final GameView gameView;
    private final GraphicsContext gc;

    public GameController(GraphicsContext gc) {
        this.gc = gc;
        this.gameManager = new GameManager(960, 640);
        this.gameView = new GameView();
        setupInputHandling(gc.getCanvas().getScene());
    }
    public void start() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameManager.update(goLeft, goRight);
                gameView.render(gc, gameManager);
                render();
            }
        }.start();
    }
    private void render() {
        drawScore();
    }
    private void setupInputHandling(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                goLeft = true;
            } else if (event.getCode() == KeyCode.RIGHT) {
                goRight = true;
            } else if (event.getCode() == KeyCode.SPACE && gameManager.isGameOver()) {
                gameManager.setupGame();
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
    private void drawScore() {
        String scoreText = "Score: " + gameManager.getScore();
        double padding = 20;

        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        gc.setFill(Color.WHITE);

        gc.setTextAlign(TextAlignment.RIGHT);

        // Vẽ văn bản
        gc.fillText(scoreText, Main.SCREEN_WIDTH - padding, 30);
        gc.setTextAlign(TextAlignment.LEFT);
    }
}