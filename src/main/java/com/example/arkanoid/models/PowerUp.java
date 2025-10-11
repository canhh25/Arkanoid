package com.example.arkanoid.models;

public abstract class PowerUp extends GameObject {
    protected String type;
    protected int duration;
    protected boolean isActive = false;

    public PowerUp(double x, double y, double width, double height, String type, int duration, String imagePath) {
        super(x, y, width, height,imagePath);
        this.type = type;
        this.duration = duration;
    }

    public String getType() {
        return type;
    }
    public int getDuration() {
        return duration;
    }
    public boolean isActive() {
        return isActive;
    }

    public abstract void applyEffect(Paddle paddle);

    public abstract void removeEffect(Paddle paddle);
}
