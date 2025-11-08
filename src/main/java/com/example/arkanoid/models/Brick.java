
package com.example.arkanoid.models;

import com.example.arkanoid.models.Power.*;
import java.util.Random;

public class Brick extends GameObject {
    public static final double BRICK_WIDTH = 960 / 11;
    public static final double BRICK_HEIGHT = 16 * 1.5;
    public int hitPoints;
    public int type;

    private final String crackedImagePath;
    private static final double POWERUP_CHANCE = 0.76;
    private static final Random random = new Random();

    private static final PowerFactory powerFactory = new PowerImpl();

    private static final String[] POWER_TYPES = {
            "expand_paddle",
            "fast_ball",
            "multi_ball",
            "extra_life"
    };

    public Brick(double x, double y, int hitPoints, int type, String imagePath, String crackedImagePath) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT, imagePath);
        this.hitPoints = hitPoints;
        this.type = type;
        this.crackedImagePath = crackedImagePath;
    }

    public void hit() {
        if (this.type != 4) {
            this.hitPoints--;

            if (this.hitPoints <= 0) {
                spawnPowerUp();
            }

            if ((this.hitPoints == 1 && this.type == 2) ||
                    (this.hitPoints == 2 && this.type == 3)) {
                setImage(crackedImagePath);
            }
        }
    }

    private void spawnPowerUp() {
        // Chỉ spawn từ brick thường (type 1,2,3)
        if (this.type == 4) {
            return;
        }

        if (random.nextDouble() >= POWERUP_CHANCE) {
            return;
        }

        double centerX = getX() + BRICK_WIDTH / 2 - 15;
        double centerY = getY() + BRICK_HEIGHT / 2;

        String powerType = POWER_TYPES[random.nextInt(POWER_TYPES.length)];
        Power<?> power = powerFactory.createPowerUp(powerType, centerX, centerY);
        PowerManager.addPower(power);
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    public int getType() {
        return type;
    }
}