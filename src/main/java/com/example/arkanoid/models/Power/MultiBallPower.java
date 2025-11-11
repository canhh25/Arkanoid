package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.GameManager;

public class MultiBallPower extends Power<GameManager> {
    public MultiBallPower(double x, double y) {
        super(x, y, 30, 19, "multi_ball", 0);
    }

    @Override
    public void applyDefaultEffect(GameManager gameManager) {
        if (gameManager.getBalls().isEmpty()) return;
        Ball originalBall = gameManager.getBall();

        gameManager.addBall(originalBall.getX(), originalBall.getY(), originalBall.getSpeed(), -60);

    }

    @Override
    public void removeDefaultEffect(GameManager gameManager) {

    }

}