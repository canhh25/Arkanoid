package com.example.arkanoid.models;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import javafx.scene.paint.Color;

public class Paddle extends MovableObject {
    public static final double PADDLE_SPEED = 5;
    public static double paddle_width = 100;
    public static double paddle_height = 30;
    public static final double PADDLE_WIDTH = 100;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private final double gameWidth;

    private int frame = 0;
    private int frameCount = 3;
    private int frameDelay = 10;
    private int frameTimer = 0;

    private boolean isBlinking = false;

    public void setBlinking(boolean blinking) {
        this.isBlinking = blinking;
    }

    public boolean isBlinking() {
        return isBlinking;
    }

    public Paddle(double x, double y, double gameWidth) {
        super(x, y, paddle_width, paddle_height, "/images/paddle/bat0.png");
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
        if (movingLeft) {
            this.x = Math.max(0, this.x - PADDLE_SPEED);
        }
        if (movingRight) {
            this.x = Math.min(gameWidth - this.width, this.x + PADDLE_SPEED);
        }
        frameTimer++;
        if (frameTimer >= frameDelay) {
            frameTimer = 0;
            frame = (frame + 1) % frameCount;
            setImage("/images/paddle/bat" + frame + ".png");
        }
    }

    public void draw(GraphicsContext gc) {
        render(gc);
    }

    public void render(GraphicsContext gc) {
        if (getImage() != null) {
            if (isBlinking) {
                gc.setGlobalAlpha(0.3);
                gc.drawImage(getImage(), getX(), getY(), getWidth(), getHeight());
                gc.setGlobalAlpha(1.0);
            } else {
                gc.drawImage(getImage(), getX(), getY(), getWidth(), getHeight());
            }
        } else {
            if (isBlinking) {
                gc.setFill(Color.YELLOW);
                gc.setGlobalAlpha(0.5);
                gc.fillRect(getX(), getY(), getWidth(), getHeight());
                gc.setGlobalAlpha(1.0);
            } else {
                gc.setFill(Color.BLUE);
                gc.fillRect(getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    public double getWidth() {
        return paddle_width;
    }

    public void setWidth(double newWidth) {
        double centerX = this.x + Paddle.paddle_width / 2;
        Paddle.paddle_width = newWidth;
        this.x = centerX - Paddle.paddle_width / 2;
    }

    public double getHeight() {
        return paddle_height;
    }

    public void setHeight(double paddle_height) {
        Paddle.paddle_height = paddle_height;
    }
}