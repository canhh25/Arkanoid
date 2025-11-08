package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.GameManager;

public class MultiBallPower extends Power<GameManager> {

    public MultiBallPower(double x, double y) {
        super(x, y, 30, 19, "multi_ball");
    }

    @Override
    public void applyDefaultEffect(GameManager gameManager) {
        if (gameManager.getBalls().isEmpty()) return;
        System.out.println("Add ball");
        Ball originalBall = gameManager.getBall();

        gameManager.addBall(originalBall.getX(), originalBall.getY(), originalBall.getSpeed(), -120);

    }

    @Override
    public void removeDefaultEffect(GameManager gameManager) {

    }

}