package com.example.arkanoid.models;

public class Brick extends MovableObject {
    private int hitPoints;
    private String type;

    public void takeHit(){}

    public boolean isDestroyed() {
        return this.hitPoints <= 0;
    }


}
