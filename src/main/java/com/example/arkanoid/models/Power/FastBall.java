package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import javafx.scene.canvas.GraphicsContext;

public class FastBall extends PowerUp<Ball> {
    private static final double SPEED_SCALE = 1.5;
    private double baseDx;
    private double baseDy;
    public static final double DURATION = 100;
    public FastBall(double x, double y) {
        super(x, y, 14, 14, "FAST_BALL");
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

    @Override
    public void render(GraphicsContext gc) {

    }
}
