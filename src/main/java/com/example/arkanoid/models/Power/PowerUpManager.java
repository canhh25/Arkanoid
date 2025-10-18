package com.example.arkanoid.models;

import com.example.arkanoid.models.Power.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

public class PowerUpManager {
//    private static List<PowerUp> activePowerUps = new ArrayList<>();
    private static GameManager gameManager;
    private static PowerUpType typePower;
    public static void setGameManager(GameManager gm) {
        gameManager = gm;
    }

//    public static void addPowerUp(PowerUp powerUp) {
//        activePowerUps.add(powerUp);
//    }

    public static void updatePowerUps(Paddle paddle, Ball ball) {
        if (gameManager == null || paddle == null) return;

        for (int i = activePowerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = activePowerUps.get(i);

            if(powerUp instanceof ExtraLifePowerUp) {
                ((ExtraLifePowerUp) powerUp).update();
            }

            // Power chạm vào paddle
            if (powerUp.intersects(paddle)) {
                applyPowerUpEffect(powerUp, paddle, ball);
                activePowerUps.remove(i);
                continue;
            }

            if (powerUp.getY() > gameManager.getGameHeight()) {
                activePowerUps.remove(i);
            }
        }
    }

    private static void applyPowerUpEffect(PowerUp powerUp, Paddle paddle, Ball ball) {
        powerUp.applyEffect(ball);
        typePower = PowerUpType.EXTRA_LIFE;
        if (typePower.equals(PowerUpType.EXTRA_LIFE)) {
            gameManager.lives++;
            System.out.println("Nhận powerup: Thêm mạng! Số mạng: " + gameManager.lives);
        }
    }

//    public static void renderPowerUps(GraphicsContext gc) {
//        for (PowerUp powerUp : activePowerUps) {
//            powerUp.render(gc);
//        }
//    }

    public static void clearPowerUps() {
        activePowerUps.clear();
    }


}