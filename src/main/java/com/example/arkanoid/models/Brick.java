package com.example.arkanoid.models;

public class Brick extends GameObject {
    public static final double BRICK_WIDTH = 960/11;
    public static final double BRICK_HEIGHT = 16*1.5;
    public int hitPoints;
    public int type;
    private final String crackedImagePath;

    public Brick(double x, double y, int hitPoints, int type, String imagePath, String crackedImagePath) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT, imagePath);
        this.hitPoints = hitPoints;
        this.type = type;
        this.crackedImagePath = crackedImagePath;
    }
    public void hit() {
        if(this.type != 4) {
            this.hitPoints--;
            if((this.hitPoints == 1 && this.type == 2) || this.hitPoints == 2 && this.type == 3) {
                setImage(crackedImagePath);
            }
        }
    }


    public boolean isDestroyed() {
        return hitPoints <= 0;
    }
}