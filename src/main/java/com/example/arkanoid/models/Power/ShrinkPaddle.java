package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Paddle;

public class ShrinkPaddle extends Power<Paddle> {
    private static final double EXTENDED_TIME = 3000;
    private static final double EXPAND_MULTIPLIER = 2.0;

    public ShrinkPaddle(double x, double y) {
        super(x, y, 30, 19, "shrink");
        this.duration = 5000;
    }

    @Override
    public void applyDefaultEffect(Paddle paddle) {
        if (!isActive) {
            double currWidth = paddle.getWidth();
            double newWidth = currWidth / EXPAND_MULTIPLIER;

            paddle.setWidth(newWidth);

            activate();
        } else {
            extendTime(EXTENDED_TIME);
            System.out.println("Extended paddle time");
        }
    }

    @Override
    public void removeDefaultEffect(Paddle paddle) {
        if (isActive) {
            double currWidth = paddle.getWidth();
            double newWidth = currWidth * EXPAND_MULTIPLIER;
            paddle.setWidth(newWidth);
        }
    }
}
