package com.example.arkanoid.controllers;

import com.example.arkanoid.main.Main;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.GameState;
import com.example.arkanoid.views.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
    private GameFacade navigationFacade;

    public GameController(GraphicsContext gc, int level) {
        this.gc = gc;
        this.gameManager = GameManager.getInstance();
        this.gameView = new GameView();
        gameManager.setupLevel(level);
        setupInputHandling(gc.getCanvas().getScene());

        Stage stage = (Stage) gc.getCanvas().getScene().getWindow();
        this.navigationFacade = GameFacade.getInstance(stage);
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
                    navigationFacade.navigateToGame(nextLevel);
                } else {
                    navigationFacade.navigateToMenu();
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

                navigationFacade.navigateToGameOver(currentScore, maxScore);

            } catch (Exception e) {
                e.printStackTrace();
                navigationFacade.navigateToMenu();
            }
        });
    }

    private void setupInputHandling(javafx.scene.Scene scene) {
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

    public void restartLevel() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        isPaused = false;
        goLeft = false;
        goRight = false;

        int currentLevel = gameManager.getLevel();
        navigationFacade.restartCurrentLevel(currentLevel);
    }

    private void showPauseWindow() {
        Stage pauseStage = navigationFacade.showPauseDialog(this, gameManager.getLevel());
        if (pauseStage != null) {
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

        Font gameFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 18
        );

        gc.setFill(Color.WHITE);
        gc.setFont(gameFont);
        gc.setTextAlign(TextAlignment.LEFT);

        String scoreText = "Score: " + gameManager.getScore();
        gc.fillText(scoreText, padding, y);

        String timeText = "Time: " + gameManager.getFormattedTime();
        double timeX = Main.WIDTH * 0.25;
        gc.fillText(timeText, timeX, y);

        String levelText = "Level: " + gameManager.getLevel();
        double levelX = Main.WIDTH * 0.58;
        gc.fillText(levelText, levelX, y);

        drawHearts(Main.WIDTH - padding, y);
        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void drawHearts(double rightX, double y) {
        int lives = gameManager.getLives();
        double heartSize = 28;
        double spacing = 10;

        gc.setFont(Font.font("Arial Unicode MS", heartSize));
        gc.setFill(Color.rgb(255, 50, 80));

        for (int i = 0; i < lives; i++) {
            double heartX = rightX - (i * (heartSize + spacing)) - heartSize;
            gc.fillText("❤", heartX, y);
        }
    }
}