package com.example.arkanoid.models;

import java.awt.*;
import java.io.IOException;

import static jdk.jfr.internal.SecuritySupport.getResourceAsStream;

public class Ball extends MovableObject {
    public double dx, dy;
    public static final double WIDTH = 14;
    public static final double HEIGHT = 14;

    public Ball(double x, double y) {
        super(x, y, WIDTH, HEIGHT, "/images/ball/ball.png");

        // Vận tốc ban đầu
        dx = 2;
        dy = -2;
    }

    public void setSkin(String newSkin) throws IOException {
        this.image = new Image(getClass().getResourceAsStream(newSkin));
    }

    public void paddleCollision() {

    }

    @Override
    public void update() {
        x += dx;
        y += dy;
    }

    /**
     * Check ball's collision with paddle.
     *
     * @param paddle
     */
    public void paddleCollision(Paddle paddle) {
        // Biên của Ball
        double ballLeft = x;
        double ballRight = x + width;
        double ballTop = y;
        double ballBottom = y + height;

        // Biên của paddle
        double paddleLeft = paddle.getX();
        double paddleRight = paddle.getX() + paddle.getWidth();
        double paddleTop = paddle.getY();
        double paddleBottom = paddle.getY() + paddle.getHeight();

        boolean intersects = ballRight > paddleLeft
                && ballLeft < paddleRight
                && ballBottom > paddleTop
                && ballTop < paddleBottom;

        if (intersects) {
            //Bóng bật lên trên
            y = paddleTop - height;
            dy = -Math.abs(dy);

            //Góc bật của bóng
            double paddleCenter = paddleLeft + paddle.getWidth() / 2;
            double ballCenter = x + width / 2;
            double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);
            hitPosition = Math.max(-1, Math.min(1, hitPosition));
            dx = hitPosition * 3;
            //chưa xử lý bóng chạm 2 mép paddle
        }
    }
}