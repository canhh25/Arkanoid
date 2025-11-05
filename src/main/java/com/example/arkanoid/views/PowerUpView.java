package com.example.arkanoid.views;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class PowerUpView {
    private Image[] frames;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final long FRAME_DELAY = 100; // 100ms between frames
    private String type;

    public PowerUpView(String type) {
        this.type = type;
        loadFrames();
    }

    private void loadFrames() {
        frames = new Image[8];
        try {
            // Load 8 frame animation
            for (int i = 0; i < 8; i++) {
                String imagePath = "/images/powerups/powerup_" + type +
                        "/powerup_" + type + "_" + (i + 1) + ".png";
                frames[i] = new Image(getClass().getResourceAsStream(imagePath));
            }
        } catch (Exception e) {
            System.out.println("Cannot load powerup images: " + type);
            frames = null;
        }
    }

    public void updateAnimation() {
        if (frames == null) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > FRAME_DELAY) {
            currentFrame = (currentFrame + 1) % frames.length;
            lastFrameTime = currentTime;
        }
    }

    public void render(GraphicsContext gc, double x, double y, double width, double height) {
        if (frames != null && frames[currentFrame] != null) {
            gc.drawImage(frames[currentFrame], x, y, width, height);
        }
    }

    public void resetAnimation() {
        currentFrame = 0;
        lastFrameTime = System.currentTimeMillis();
    }

    public boolean hasFrames() {
        return frames != null && frames.length > 0;
    }
}