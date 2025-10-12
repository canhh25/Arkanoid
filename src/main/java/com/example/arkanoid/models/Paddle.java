package com.example.arkanoid.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    public static final double PADDLE_SPEED = 5;
    public static final double WIDTH = 100;
    public static final double HEIGHT = 30;

    private Image image;
    private int frame = 0;
    private int frameCount = 3; // số frame (bat0, bat1, bat2)
    private int frameDelay = 10;
    private int frameTimer = 0;

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private final double gameWidth;



    public Paddle(double x, double y, double gameWidth) {
        super(x, y, WIDTH, HEIGHT, "/images/paddle/bat0.png");
        this.gameWidth = gameWidth;
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

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }


}