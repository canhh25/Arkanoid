package com.example.arkanoid.models;

public abstract class MovableObject extends GameObject {

    public MovableObject(double x, double y, double width, double height, String imagePath) {
        super(x, y, width, height, imagePath);
    }

    public abstract void update();
}