package com.example.arkanoid.models;

abstract class MovableObject extends GameObject {
    protected double dx, dy;

    public MovableObject (double x, double y, double width, double height){
        super(x,y,width,height);
    }
    public void move() {
        x+=dx;
        y+=dy;
    }
    public void update() {
        move();
    }
}
