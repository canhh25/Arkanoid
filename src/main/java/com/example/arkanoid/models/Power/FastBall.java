package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;

public class FastBall extends PowerUp<Ball> {
    private static final double SPEED_SCALE = 1.5;
    private static final long EXTEND_TIME = 3000;
    private double baseDx;
    private double baseDy;

    public FastBall(double x, double y) {
        super(x, y, 30, 19, "fast_ball");
        this.maxActiveTime = 10000;
    }


    @Override
    public void applyEffect(Ball ball) {
        if (!isActive) {
            ball.setSpeed(ball.getSpeed() * SPEED_SCALE);
        } else {
            extendTime(EXTEND_TIME);
        }
    }

    @Override
    public void removeEffect(Ball ball) {
        if (isActive) {
            ball.setSpeed(ball.getSpeed() / SPEED_SCALE);
            isActive = false;
        }
    }

}
