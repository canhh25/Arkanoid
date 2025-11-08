package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.GameObject;
import com.example.arkanoid.models.Paddle;
import com.example.arkanoid.views.PowerUpView;
import javafx.scene.canvas.GraphicsContext;

public abstract class PowerUp<T> extends GameObject {
    protected String type;
    protected boolean isActive = false;
    protected double speed = 1.5;
    protected long activeTime = 4000;
    protected double maxActiveTime = 10000;
    protected long duration = 3000;

    protected PowerUpView view;

    public PowerUp(double x, double y, double width, double height, String type) {
        super(x, y, width, height, null);
        this.type = type;
        this.view = new PowerUpView(type);
    }

    public abstract void applyEffect(T object);

    public abstract void removeEffect(T object);

    public boolean intersects(Paddle paddle) {
        return getX() < paddle.getX() + paddle.getWidth() &&
                getX() + getWidth() > paddle.getX() &&
                getY() < paddle.getY() + paddle.getHeight() &&
                getY() + getHeight() > paddle.getY();
    }

    public boolean isExpired() {
        return isActive && activeTime >= maxActiveTime;
    }

    public void extendTime(double additionalTime) {
        activeTime = (long) Math.max(0, activeTime - additionalTime);
    }

    public void activate() {
        isActive = true;
        activeTime = 0;
    }

    public void update() {
        setY(getY() + speed);

        // Update animation
        view.updateAnimation();
    }

    @Override
    public void render(GraphicsContext gc) {
        view.render(gc, getX(), getY(), getWidth(), getHeight());
    }

    public long getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public PowerUpView getView() {
        return view;
    }
}