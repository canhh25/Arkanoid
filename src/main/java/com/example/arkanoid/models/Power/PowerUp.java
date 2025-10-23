package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.GameObject;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class PowerUp<T> extends GameObject {
    protected String type;
    protected boolean isActive = false;
    protected double speed = 2.0;
    protected Image[] frames;
    protected int currentFrame = 0;
    protected long lastFrameTime = 0;
    protected static final long FRAME_DELAY = 100; // 100ms giữa các frame
    protected double activeTime = 0;
    protected double maxActiveTime = 10000;
    public PowerUp(double x, double y, double width, double height, String type) {
        super(x, y, width, height, null);
        this.type = type;
        update();
        loadFrames();
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
        activeTime = Math.max(0, activeTime - additionalTime);
    }

    public void activate() {
        isActive = true;
        activeTime = 0;
    }
    protected void renderFallback(GraphicsContext gc) {
        // Fallback nếu không load được ảnh animation
        gc.setFill(Color.RED);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());

        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 14));
        gc.fillText("+1", getX() + 12, getY() + 24);
    }

    public void update() {
        // Di chuyển xuống
        setY(getY() + speed);

        // Cập nhật animation
        updateAnimation();
    }

    protected void updateAnimation() {
        if (frames == null) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > FRAME_DELAY) {
            currentFrame = (currentFrame + 1) % frames.length;
            lastFrameTime = currentTime;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (frames != null && frames[currentFrame] != null) {
            gc.drawImage(frames[currentFrame], getX(), getY(), getWidth(), getHeight());
        } else {
            renderFallback(gc);
        }
    }
    protected void loadFrames() {
        frames = new javafx.scene.image.Image[8];
        try {
            // Load 8 frame animation
            for (int i = 0; i < 8; i++) {
                String imagePath = "/images/powerups/powerup_life/powerup_life_" + (i + 1) + ".png";
                frames[i] = new Image(getClass().getResourceAsStream(imagePath));
            }
        } catch (Exception e) {
            System.out.println("Không load được ảnh powerup: " + type);
            frames = null;
        }
    }
    public String getType() { return type; }
    public boolean isActive() { return isActive; }
}
