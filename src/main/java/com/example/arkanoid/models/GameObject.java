package com.example.arkanoid.models;

abstract class GameObject {
    protected double x, y, width, height;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void checkCollision(GameObject other){

    }
}
