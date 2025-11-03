package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Ball;
import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PowerUpManager {
    private static final List<PowerUp> fallingPowerUps = new ArrayList<>();
    private static final Map<String, ActivePowerUpEntry> activePowerUps = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public static GameManager gameManager;

    // Class để lưu PowerUp và task của nó
    private static class ActivePowerUpEntry {
        PowerUp powerUp;
        ScheduledFuture<?> scheduledTask;

        ActivePowerUpEntry(PowerUp powerUp, ScheduledFuture<?> task) {
            this.powerUp = powerUp;
            this.scheduledTask = task;
        }

        void cancel() {
            if (scheduledTask != null && !scheduledTask.isDone()) {
                scheduledTask.cancel(false);
            }
        }
    }

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
    }

    private static void handlePowerUpCollection(PowerUp powerUp, Paddle paddle, Ball ball) {
        String type = powerUp.getType();

        // Kiểm tra xem đã có PowerUp cùng loại đang hoạt động chưa
        if (activePowerUps.containsKey(type)) {
            ActivePowerUpEntry entry = activePowerUps.get(type);

            if (powerUp instanceof ExpandPaddle || powerUp instanceof FastBall) {
                // Cancel task cũ
                entry.cancel();

                // Extend thời gian trong PowerUp
                entry.powerUp.extendTime(3000);

                // Lấy thời gian còn lại từ activeTime của PowerUp
                long remainingTime = entry.powerUp.activeTime;

                // Schedule task mới với thời gian còn lại
                ScheduledFuture<?> newTask = scheduler.schedule(() -> {
                    removePowerUpEffect(entry.powerUp, paddle, ball);
                    activePowerUps.remove(type);
                    System.out.println("PowerUp hết hạn: " + type);
                }, remainingTime, TimeUnit.MILLISECONDS);

                entry.scheduledTask = newTask;

                System.out.println("Extend PowerUp: " + type + " - Remaining: " + remainingTime + "ms");
            } else if (powerUp instanceof ExtraLifePowerUp) {
                // Instant effect - apply ngay
                powerUp.applyEffect(gameManager);
            } else if (powerUp instanceof MultiBallPowerUp) {
                // Instant effect - apply ngay
                powerUp.applyEffect(gameManager);
            }
        } else {
            // Chưa có PowerUp này, apply mới
            applyNewPowerUp(powerUp, paddle, ball, type);
        }
    }

    private static void applyNewPowerUp(PowerUp powerUp, Paddle paddle, Ball ball, String type) {
        if (powerUp instanceof ExtraLifePowerUp) {
            powerUp.applyEffect(gameManager);
            System.out.println("Kích hoạt PowerUp (instant): " + type);

        } else if (powerUp instanceof MultiBallPowerUp) {
            powerUp.applyEffect(gameManager);
            System.out.println("Kích hoạt PowerUp: " + type);

        } else if (powerUp instanceof ExpandPaddle) {
            powerUp.applyEffect(paddle);

            long duration = powerUp.activeTime > 0 ? powerUp.activeTime : 5000; // Default 5 giây

            // Schedule việc remove effect sau duration
            ScheduledFuture<?> task = scheduler.schedule(() -> {
                powerUp.removeEffect(paddle);
                activePowerUps.remove(type);
                System.out.println("PowerUp hết hạn: " + type);
            }, duration, TimeUnit.MILLISECONDS);

            activePowerUps.put(type, new ActivePowerUpEntry(powerUp, task));
            System.out.println("Kích hoạt PowerUp: " + type + " - Duration: " + duration + "ms");

        } else if (powerUp instanceof FastBall) {
            powerUp.applyEffect(ball);

            // Lấy duration sau khi applyEffect (có thể được set trong applyEffect)
            long duration = powerUp.activeTime > 0 ? powerUp.activeTime : 5000; // Default 5 giây

            // Schedule việc remove effect sau duration
            ScheduledFuture<?> task = scheduler.schedule(() -> {
                powerUp.removeEffect(ball);
                activePowerUps.remove(type);
                System.out.println("PowerUp hết hạn: " + type);
            }, duration, TimeUnit.MILLISECONDS);

            activePowerUps.put(type, new ActivePowerUpEntry(powerUp, task));
            System.out.println("Kích hoạt PowerUp: " + type + " - Duration: " + duration + "ms");
        }
    }

    private static void removePowerUpEffect(PowerUp powerUp, Paddle paddle, Ball ball) {
        if (powerUp instanceof ExpandPaddle) {
            powerUp.removeEffect(paddle);
        } else if (powerUp instanceof FastBall) {
            powerUp.removeEffect(ball);
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
        // Cancel tất cả scheduled tasks
        for (ActivePowerUpEntry entry : activePowerUps.values()) {
            entry.cancel();
        }

        fallingPowerUps.clear();
        activePowerUps.clear();
    }

    public static void shutdown() {
        // Cancel tất cả tasks đang chạy
        for (ActivePowerUpEntry entry : activePowerUps.values()) {
            entry.cancel();
        }

        // Shutdown scheduler
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}