package com.example.arkanoid.models;

import javafx.scene.canvas.GraphicsContext;

public class Ball extends MovableObject {
    public static final double BALL_SPEED = 7.0;
    public double dx, dy;
    private double prevX, prevY;
    private double speed;
    public static final double BALL_WIDTH = 14;
    public static final double BALL_HEIGHT = 14;

    private boolean isBlinking = false;

    public void setBlinking(boolean blinking) {
        this.isBlinking = blinking;
    }
    public Ball(double x, double y, double speed) {
        super(x, y, BALL_WIDTH, BALL_HEIGHT, "/images/ball/ball.png");
        this.dx = 0;
        this.dy = 0;
        this.speed = speed;
    }

    public Ball(double x, double y) {
        super(x, y, BALL_WIDTH, BALL_HEIGHT, "/images/ball/ball.png");
        this.dx = 0;
        this.dy = 0;
        this.speed = BALL_SPEED;
    }

    public double getPrevX() {
        return prevX;
    }

    public void setPrevX(double prevX) {
        this.prevX = prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setPrevY(double prevY) {
        this.prevY = prevY;
    }

    public void setSpeed(double s) {
        if (Math.abs(dx) < 0.01 && Math.abs(dy) < 0.01) {
            this.speed = s;
            return;
        }

        double currentSpeed = Math.sqrt(dx * dx + dy * dy);
        if (currentSpeed > 0.01) {
            double ratio = s / currentSpeed;
            this.dx *= ratio;
            this.dy *= ratio;
        }

        this.speed = s;
    }

    public void launchByAngle(double angleRad) {
        dx = Math.cos(angleRad) * speed;
        dy = Math.sin(angleRad) * speed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    @Override
    public void update() {
        prevX = x;
        prevY = y;
        x += dx;
        y += dy;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (getImage() != null) {
            gc.drawImage(getImage(), getX(), getY(), getWidth(), getHeight());
        }
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }
}