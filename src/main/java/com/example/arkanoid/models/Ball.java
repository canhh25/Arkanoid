package com.example.arkanoid.models;

public class Ball extends MovableObject {
    private static final double SPEED = 100;

    public Ball(double x, double y, double radius) {
        super(x, y, radius * 2, radius * 2);
    }

    @Override
    public void move() {
        super.move();
    }

    @Override
    public void update() {
        move();
        checkWallCollision(1000,1000);
    }

    public void checkWallCollision(double sceneWidth, double sceneHeight) {
        if (x <= 0) {
            x = 0;
            dx = -dx;
        } else if (x + width >= sceneWidth) {
            x = sceneWidth - width;
            dx = -dx;
        }
        if (y <= 0) {
            y = 0;
            dy = -dy;
        }
    }
}
