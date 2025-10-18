package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;

public class ExpandPaddle extends PowerUp<Paddle> {
    private static final double EXPAND_SCALE = 1.5;
    private double baseWidth;

    public ExpandPaddle(double x, double y) {
        super(x, y, 20, 20, "EXPAND_PADDLE");
    }


    @Override
    public void applyEffect(Paddle paddle) {
        if (!isActive) {
            baseWidth = paddle.getWidth();
            paddle.setWidth(baseWidth * EXPAND_SCALE);
            isActive = true;
        }
    }

    @Override
    public void removeEffect(Paddle paddle) {
        if (isActive) {
            paddle.setWidth(baseWidth);
            isActive = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {

    }
}