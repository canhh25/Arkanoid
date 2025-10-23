package com.example.arkanoid.models;

import com.example.arkanoid.models.Power.*;
import javafx.scene.canvas.GraphicsContext;

public class Brick extends GameObject {
    public static final double BRICK_WIDTH = 960/11;
    public static final double BRICK_HEIGHT = 16*1.5;
    public int hitPoints;
    // 4 type.
    // - 1, 2, 3 lần lượt hitPoints = type.
    // - Là loại không thể phá.

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

            if((this.hitPoints == 1 && this.type == 2)
                    || this.hitPoints == 2 && this.type == 3) {
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

            int randomPowerUp = (int) (Math.random() * 5);
            PowerUp powerUp = null;
            switch (randomPowerUp) {
                case 1:
                    powerUp = new ExtraLifePowerUp(centerX, centerY);
                    break;
                case 2:
                    powerUp = new FastBall(centerX, centerY);
                    break;
                case 3:
                    powerUp = new ExpandPaddle(centerX, centerY);
            }

            if(powerUp != null) {
                PowerUpManager.addPowerUp(powerUp);
            }
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