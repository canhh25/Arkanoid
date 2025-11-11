package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;

public class FastBall extends Power<Ball> {
    private static final double SPEED_SCALE = 1.5;
    private static final long DURATION = 1000;
    public FastBall(double x, double y) {
        super(x, y, 30, 19, "fast_ball", DURATION);
        this.isBlinking = false;
    }


    @Override
    public void applyDefaultEffect(Ball ball) {
        if (!isActive) {
            ball.setSpeed(ball.getSpeed() * SPEED_SCALE);
            activate();
        }
    }

    @Override
    public void removeDefaultEffect(Ball ball) {
        if (isActive) {
            ball.setSpeed(ball.BALL_SPEED);
            isBlinking = false;
            ball.setBlinking(false);
            isActive = false;
        }
    }

}
