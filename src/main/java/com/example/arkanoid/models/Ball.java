package com.example.arkanoid.models;


public class Ball extends MovableObject {
    public double dx, dy;
    public static final double BALL_WIDTH = 14;
    public static final double BALL_HEIGHT = 14;

    public Ball(double x, double y) {
        super(x, y, BALL_WIDTH, BALL_HEIGHT, "/images/ball/ball.png");

        // Vận tốc ban đầu
        dx = 2;
        dy = -2;
    }

    @Override
    public void update() {
        x += dx;
        y += dy;
    }
}