package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Paddle;

public class ExpandPaddle extends PowerUp<Paddle> {
    private double originalWidth;
    private static final double EXTENDED_TIME = 3000;
    private static final double EXPAND_MULTIPLIER = 2.0;

    public ExpandPaddle(double x, double y) {
        super(x, y, 30, 19, "expand");
        this.duration = 5000;
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
