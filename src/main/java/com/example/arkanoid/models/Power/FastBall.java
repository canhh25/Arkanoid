package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.Paddle;

public class FastBall extends PowerUp {
    private static final double SPEED_SCALE = 1.5;
    private double baseDx;
    private double baseDy;

    public FastBall(double x, double y) {
        super(x, y, 14, 14, "FastBall", 10000, "/images/ball/ball.png");
    }

    @Override
    public void applyEffect(Ball ball) {
        if (!isActive) {
            baseDx = ball.getDx();
            baseDy = ball.getDy();
            ball.setDx(baseDx * SPEED_SCALE);
            ball.setDy(baseDy * SPEED_SCALE);
            isActive = true;
        }
    }

    @Override
    public void removeEffect(Ball ball) {
        if (isActive) {
            ball.setDx(baseDx);
            ball.setDy(baseDy);
            isActive = false;
        }
    }

    @Override public void applyEffect(Paddle paddle) {}
    @Override public void removeEffect(Paddle paddle) {}
}
