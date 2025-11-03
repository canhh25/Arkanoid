package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.example.arkanoid.models.GameManager.BALL_SPEED;

public class MultiBallPowerUp extends PowerUp<GameManager> {

    public MultiBallPowerUp(double x, double y) {
        super(x, y, 30, 19, "multi_ball");
    }

    @Override
    public void applyEffect(GameManager gameManager) {
        if (gameManager.getBalls().isEmpty()) return;
        System.out.println("Add ball");
        Ball originalBall = gameManager.getBall();

        gameManager.addBall(originalBall.getX(), originalBall.getY(), BALL_SPEED, -120);

        gameManager.addBall(originalBall.getX(), originalBall.getY(), BALL_SPEED, -60);

    }

    @Override
    public void removeEffect(GameManager gameManager) {

    }

}