package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;

public class ExtraLifePowerUp extends PowerUp<Paddle> {
    public ExtraLifePowerUp(double x, double y) {
        super(x, y, 38, 19, "EXTRA_LIFE"); // Kích thước lớn hơn để nhìn rõ animation
        update();
        loadFrames();
    }

    @Override
    public void applyEffect(Paddle paddle) {
        this.isActive = true;
    }

    @Override
    public void removeEffect(Paddle paddle) {
        this.isActive = false;
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
}