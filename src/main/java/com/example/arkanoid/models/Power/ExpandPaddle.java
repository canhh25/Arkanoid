package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;

public class ExpandPaddle extends PowerUp<Paddle> {
    private static final double EXPAND_SCALE = 1.5;
    private double baseWidth;
    private double originalWidth;
    private static final double EXTENDED_TIME = 3000;
    private static final double EXPAND_MULTIPLIER = 1.5;


    public ExpandPaddle(double x, double y) {
        super(x, y, 38, 19, "EXPAND_PADDLE");
    }


    @Override
    public void applyEffect(Paddle paddle) {
        if (!isActive) {
            originalWidth = paddle.getWidth();
            double newWidth = originalWidth * EXPAND_MULTIPLIER;

            // Lưu vị trí trung tâm
            double centerX = paddle.getX() + paddle.getWidth() / 2;

            paddle.setWidth(newWidth);

            // Đặt lại vị trí để giữ nguyên trung tâm
            paddle.setX(centerX - newWidth / 2);

            activate();
            System.out.println("Paddle expanded: " + originalWidth + " -> " + newWidth);
        } else {
            extendTime(EXTENDED_TIME);
            System.out.println("Extended paddle time");
        }
    }

    @Override
    public void removeEffect(Paddle paddle) {
        if (isActive) {
            double centerX = paddle.getX() + paddle.getWidth() / 2;
            paddle.setWidth(originalWidth);
            paddle.setX(centerX - originalWidth / 2);
            System.out.println("Paddle returned to normal: " + originalWidth);
        }
    }

}
