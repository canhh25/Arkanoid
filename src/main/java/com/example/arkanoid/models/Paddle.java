package com.example.arkanoid.models;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Paddle extends MovableObject {
    public static final double PADDLE_SPEED = 3;
    public static double paddle_width = 100;
    public static double paddle_height= 30;

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private final double gameWidth;
    private int frame = 0;
    private int frameCount = 3; // số frame (bat0, bat1, bat2)
    private int frameDelay = 10;
    private int frameTimer = 0;

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
        if (movingLeft && x > 0) {
            x -= PADDLE_SPEED;
        }
        if (movingRight && x < gameWidth - this.paddle_width) {
            x += PADDLE_SPEED;
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
            gc.drawImage(getImage(), getX(), getY(), getWidth(), getHeight());
        } else {
            gc.setFill(javafx.scene.paint.Color.BLUE);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
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
