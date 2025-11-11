package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Paddle;

public class ExpandPaddle extends Power<Paddle> {
    private static final long DURATION = 3000;
    private static final double EXPAND_MULTIPLIER = 2.0;

    public ExpandPaddle(double x, double y) {
        super(x, y, 30, 19, "expand", DURATION);
        this.isBlinking = false;
    }

    @Override
    public void applyDefaultEffect(Paddle paddle) {
        if (!isActive) {
            double currWidth = paddle.getWidth();
            double newWidth = currWidth * EXPAND_MULTIPLIER;
            paddle.setWidth(newWidth);
            activate();
            activationTime = System.currentTimeMillis();
        }
    }

    @Override
    public void removeDefaultEffect(Paddle paddle) {
        if (isActive) {
            double newWidth = paddle.PADDLE_WIDTH;
            paddle.setWidth(newWidth);
            isBlinking = false;
        }
    }
}