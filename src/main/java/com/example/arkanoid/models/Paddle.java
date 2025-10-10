package com.example.arkanoid.models;

public class Paddle extends MovableObject {
    private static final double PADDLE_SPEED = 400;

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
}
