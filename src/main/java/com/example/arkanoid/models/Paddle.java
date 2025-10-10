package com.example.arkanoid.models;

public class Paddle extends MovableObject {
    public static final double PADDLE_SPEED = 7.5;
    public static final double WIDTH = 100;
    public static final double HEIGHT = 30;

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private final double gameWidth;

    public Paddle(double x, double y, double gameWidth) {
        super(x, y, WIDTH, HEIGHT, "/images/paddle/bat.png");
        this.gameWidth = gameWidth;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    @Override
    public void update() {
        if (movingLeft && x > 0) {
            x -= PADDLE_SPEED;
        }
        if (movingRight && x < gameWidth - this.width) {
            x += PADDLE_SPEED;
        }
    }
}