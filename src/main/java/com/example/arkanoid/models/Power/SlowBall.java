package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;

public class SlowBall extends Power<Ball> {
    private static final double SPEED_SCALE = 1.5;
    private static final long DURATION = 3000;

    public SlowBall(double x, double y) {
        super(x, y, 30, 19, "slow_ball", DURATION);
    }


    @Override
    public void applyDefaultEffect(Ball ball) {
        if (!isActive) {
            ball.setSpeed(ball.getSpeed() / SPEED_SCALE);
            activate();
        }
    }

    @Override
    public void removeDefaultEffect(Ball ball) {
        if (isActive) {
            ball.setSpeed(ball.getSpeed() * SPEED_SCALE);
            isActive = false;
        }
    }
}
