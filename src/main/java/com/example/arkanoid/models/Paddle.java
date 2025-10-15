package com.example.arkanoid.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    public static final double PADDLE_SPEED = 5;
    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT= 30;

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private final double gameWidth;
    private Image image;
    private int frame = 0;
    private int frameCount = 3; // số frame (bat0, bat1, bat2)
    private int frameDelay = 10;
    private int frameTimer = 0;

    public Paddle(double x, double y, double gameWidth) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT, "/images/paddle/bat0.png");
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
}