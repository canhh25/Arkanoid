package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MultiBallPowerUp extends PowerUp<GameManager> {

    public MultiBallPowerUp(double x, double y) {
        super(x, y, 30, 19, "multi_ball");
    }

    @Override
    public void applyEffect(GameManager gameManager) {
        gameManager.spawnExtraBalls();
    }

    @Override
    public void removeEffect(GameManager gameManager) {

    }

}