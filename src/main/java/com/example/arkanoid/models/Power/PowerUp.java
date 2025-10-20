package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.GameObject;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class PowerUp<T> extends GameObject {
    protected String type;
    protected boolean isActive = false;
    protected double speed = 2.0;
    protected Image[] frames;
    protected int currentFrame = 0;
    protected long lastFrameTime = 0;
    protected static final long FRAME_DELAY = 100; // 100ms giữa các frame
    protected static final double DURATION = 100;
    public PowerUp(double x, double y, double width, double height, String type) {
        super(x, y, width, height, null);
        this.type = type;

    }

    public abstract void applyEffect(T object);
    public abstract void removeEffect(T object);

    public boolean intersects(Paddle paddle) {
        return getX() < paddle.getX() + paddle.getWidth() &&
                getX() + getWidth() > paddle.getX() &&
                getY() < paddle.getY() + paddle.getHeight() &&
                getY() + getHeight() > paddle.getY();
    }

    public String getType() { return type; }
    public boolean isActive() { return isActive; }
}
