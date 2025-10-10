package com.example.arkanoid.models;

public class Brick extends GameObject {
    public static final double BRICK_WIDTH = 96;
    public static final double BRICK_HEIGHT = 32;
    public int hitPoints;
    private final String crackedImagePath;
    public Brick(double x, double y, int hitPoints, String imagePath, String crackedImagePath) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT, imagePath);
        this.hitPoints = hitPoints;
        this.crackedImagePath = crackedImagePath;
    }
    public void hit() {
        this.hitPoints--;
        if (this.hitPoints == 2 && crackedImagePath != null) {
            setImage(crackedImagePath);
        }
    }


    public boolean isDestroyed() {
        return hitPoints <= 0;
    }
}