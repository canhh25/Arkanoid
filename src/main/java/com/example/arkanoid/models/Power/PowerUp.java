package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.GameObject;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class PowerUp extends GameObject {
    protected String type;
    protected boolean isActive = false;
    protected double speed = 2.0;
    protected Image[] frames;
    protected int currentFrame = 0;
    protected long lastFrameTime = 0;
    protected static final long FRAME_DELAY = 100; // 100ms giữa các frame

    public PowerUp(double x, double y, double width, double height, String type) {
        super(x, y, width, height, null);
        this.type = type;
        loadFrames();
    }

    protected void loadFrames() {
        frames = new Image[8];
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

    protected abstract void renderFallback(GraphicsContext gc);
    public abstract void applyEffect(Paddle paddle);
    public abstract void removeEffect(Paddle paddle);

    public boolean intersects(Paddle paddle) {
        return getX() < paddle.getX() + paddle.getWidth() &&
                getX() + getWidth() > paddle.getX() &&
                getY() < paddle.getY() + paddle.getHeight() &&
                getY() + getHeight() > paddle.getY();
    }

    public String getType() { return type; }
    public boolean isActive() { return isActive; }
}