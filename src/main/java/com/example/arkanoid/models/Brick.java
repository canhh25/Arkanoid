package com.example.arkanoid.models;

public class Brick extends GameObject {
    private int hitPoints;
    private String type;

    public Brick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    public void takeHit() {
        hitPoints--;
    }

    public boolean isDestroyed() {
        return this.hitPoints <= 0;
    }

    public String getType() {
        return type;
    }

}
