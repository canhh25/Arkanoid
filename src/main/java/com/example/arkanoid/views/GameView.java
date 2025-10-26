package com.example.arkanoid.views;

import com.example.arkanoid.main.Main;
import com.example.arkanoid.models.Brick;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.GameState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

public class GameView {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;
   private final Image background = new Image(
            getClass().getResource("/images/background/backTest.jpg").toExternalForm()
    );
    public void render(GraphicsContext gc, GameManager gameManager) {
        gc.drawImage(background, 0, 0, WIDTH, HEIGHT);

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

            if(gameManager.gameState == GameState.WIN ||  gameManager.gameState == GameState.GAME_OVER) {
                gameManager.resetGame();
            }

            gc.setFont(new Font("Arial", 20));
            gc.fillText("Press SPACE to Restart", WIDTH / 2.0, HEIGHT / 2.0 + 40);
        }
    }

}