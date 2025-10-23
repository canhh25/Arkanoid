package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerUpManager {
    private static List<PowerUp> fallingPowerUps = new ArrayList<>(); // PowerUp đang rơi
    private static Map<String, PowerUp> activePowerUps = new HashMap<>(); // PowerUp đang hoạt động
    private static GameManager gameManager;

    public static void setGameManager(GameManager gm) {
        gameManager = gm;
    }

    public static void addPowerUp(PowerUp powerUp) {
        fallingPowerUps.add(powerUp);
    }

    public static void updatePowerUps(Paddle paddle, Ball ball) {
        if (gameManager == null || paddle == null) return;

        // Cập nhật PowerUp đang rơi
        for (int i = fallingPowerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = fallingPowerUps.get(i);
            powerUp.update();

            // Kiểm tra va chạm với paddle
            if (powerUp.intersects(paddle)) {
                handlePowerUpCollection(powerUp, paddle, ball);
                fallingPowerUps.remove(i);
                continue;
            }

            // Xóa nếu rơi ra ngoài màn hình
            if (powerUp.getY() > gameManager.getGameHeight()) {
                fallingPowerUps.remove(i);
            }
        }

        // Cập nhật PowerUp đang hoạt động
        List<String> expiredTypes = new ArrayList<>();
        for (Map.Entry<String, PowerUp> entry : activePowerUps.entrySet()) {
            PowerUp powerUp = entry.getValue();
            powerUp.update();

            if (powerUp.isExpired()) {
                // Gỡ hiệu ứng
                if (powerUp instanceof ExpandPaddle) {
                    powerUp.removeEffect(paddle);
                } else if (powerUp instanceof FastBall) {
                    powerUp.removeEffect(ball);
                }
                expiredTypes.add(entry.getKey());
            }
        }

        // Xóa các PowerUp hết hạn
        for (String type : expiredTypes) {
            activePowerUps.remove(type);
            System.out.println("PowerUp hết hạn: " + type);
        }
    }

    private static void handlePowerUpCollection(PowerUp powerUp, Paddle paddle, Ball ball) {
        String type = powerUp.getType();

        // Kiểm tra xem đã có PowerUp cùng loại đang hoạt động chưa
        if (activePowerUps.containsKey(type)) {
            PowerUp existingPowerUp = activePowerUps.get(type);

            // Nếu là loại có thời gian, extend thêm
            if (powerUp instanceof ExpandPaddle) {
                existingPowerUp.extendTime(3000);
                System.out.println("Extend PowerUp: " + type + " Times: " + existingPowerUp.activeTime);
            } else if (powerUp instanceof FastBall) {
                existingPowerUp.extendTime(3000);
                System.out.println("Extend PowerUp: " + type);
            }
            // ExtraLife thì apply luôn
            else if (powerUp instanceof ExtraLifePowerUp) {
              if(((ExtraLifePowerUp) powerUp).getCountLives() > 0) {
                  powerUp.applyEffect(gameManager);
              }
            }
        } else {
            // Chưa có, apply hiệu ứng mới
            if (powerUp instanceof ExtraLifePowerUp) {
                powerUp.applyEffect(gameManager);
            } else if (powerUp instanceof ExpandPaddle) {
                powerUp.applyEffect(paddle);
                activePowerUps.put(type, powerUp);
            } else if (powerUp instanceof FastBall) {
                powerUp.applyEffect(ball);
                activePowerUps.put(type, powerUp);
            }

            System.out.println("Kích hoạt PowerUp: " + type);
        }
    }

    public static void renderPowerUps(GraphicsContext gc) {
        // Render PowerUp đang rơi
        for (PowerUp powerUp : fallingPowerUps) {
            powerUp.render(gc);
        }

        // Có thể render UI hiển thị PowerUp đang active
    }

    public static void clearPowerUps() {
        fallingPowerUps.clear();
        activePowerUps.clear();
    }
}