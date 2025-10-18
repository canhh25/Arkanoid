package com.example.arkanoid.models;

import com.example.arkanoid.models.Power.ExtraLifePowerUp;
import javafx.scene.canvas.GraphicsContext;

public class Brick extends GameObject {
    public static final double BRICK_WIDTH = 48;
    public static final double BRICK_HEIGHT = 16;
    public int hitPoints;
    public int type;
    private final String crackedImagePath;
    private static final double POWERUP_CHANCE = 1.0; // 15% cơ hội

    public Brick(double x, double y, int hitPoints, int type, String imagePath, String crackedImagePath) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT, imagePath);
        this.hitPoints = hitPoints;
        this.type = type;
        this.crackedImagePath = crackedImagePath;
    }

    public void hit() {
        if(this.type != 4) {
            this.hitPoints--;

            // THÊM: Spawn powerup khi brick bị phá
            if (this.hitPoints <= 0) {
                spawnPowerUp();
            }

            if((this.hitPoints == 1 && this.type == 2) || this.hitPoints == 2 && this.type == 3) {
                setImage(crackedImagePath);
            }
        }
    }

    // THÊM PHƯƠNG THỨC SPAWN POWERUP
    private void spawnPowerUp() {
        // Chỉ spawn từ brick thường (type 1,2,3), không spawn từ brick đặc biệt (type 4)
        if (this.type != 4 && Math.random() < POWERUP_CHANCE) {
            double centerX = getX() + BRICK_WIDTH / 2 - 20;
            double centerY = getY() + BRICK_HEIGHT / 2;

            ExtraLifePowerUp powerUp = new ExtraLifePowerUp(centerX, centerY);
            PowerUpManager.addPowerUp(powerUp);
        }
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }
    @Override
    public void render(GraphicsContext gc) {
        // Vẽ brick bằng ảnh từ GameObject - giữ nguyên chức năng hiện có
        if (getImage() != null) {
            gc.drawImage(getImage(), getX(), getY(), getWidth(), getHeight());
        }
    }
}