package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.GameObject;
import com.example.arkanoid.models.Paddle;
import com.example.arkanoid.views.PowerView;
import javafx.scene.canvas.GraphicsContext;

public abstract class Power<T> extends GameObject {
    protected String type;
    protected boolean isActive = false;
    protected double speed = 3.5;
    protected long activeTime = 4000;
    protected long duration;
    protected PowerView view;
    private static final long BLINK_WARNING_TIME = 1000;
    private static final long BLINK_INTERVAL = 200;

    protected long activationTime;
    protected boolean isBlinking;
    protected PowerStrategy<T> strategy;

    public Power(double x, double y, double width, double height, String type, long duration) {
        super(x, y, width, height, null);
        this.type = type;
        this.duration = duration;
        this.view = new PowerView(type);
    }

    public void applyEffect(T object) {
        if (strategy != null) {
            strategy.apply(object);
        } else {
            applyDefaultEffect(object);
        }
    }

    public void removeEffect(T object) {
        if (strategy != null) {
            strategy.remove(object);
        } else {
            removeDefaultEffect(object);
        }
    }

    protected abstract void applyDefaultEffect(T object);
    protected abstract void removeDefaultEffect(T object);

    public void setStrategy(PowerStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public boolean intersects(Paddle paddle) {
        return getX() < paddle.getX() + paddle.getWidth() &&
                getX() + getWidth() > paddle.getX() &&
                getY() < paddle.getY() + paddle.getHeight() &&
                getY() + getHeight() > paddle.getY();
    }

    public void updateBlinkEffect(T object) {
        if (!isActive) return;

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - activationTime;
        long remainingTime = duration - elapsedTime;

        if (remainingTime <= BLINK_WARNING_TIME && remainingTime > 0) {
            isBlinking = true;
            long blinkCycle = (currentTime / BLINK_INTERVAL) % 2;
            if (object instanceof Ball) {
                ((Ball) object).setBlinking(blinkCycle == 0);
            } else  if (object instanceof Paddle) {
                ((Paddle) object).setBlinking(blinkCycle == 0);
            }
        } else {
            if (isBlinking) {
                if (object instanceof Ball) {
                    ((Ball) object).setBlinking(false);
                } else   if (object instanceof Paddle) {
                    ((Paddle) object).setBlinking(false);
                }
                isBlinking = false;
            }
        }
    }
    public void activate() {
        isActive = true;
        activeTime = duration;
    }

    public void update() {
        setY(getY() + speed);
        view.updateAnimation();
    }

    @Override
    public void render(GraphicsContext gc) {
        view.render(gc, getX(), getY(), getWidth(), getHeight());
    }

    // Getters
    public long getDuration() { return duration; }
    public String getType() { return type; }
    public boolean isActive() { return isActive; }
    public PowerView getView() { return view; }
    public long getActiveTime() { return activeTime; }
}