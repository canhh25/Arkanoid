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

public class PowerManager {
    private static final List<Power<?>> powers = new ArrayList<>();
    private static final Map<String, ActivePowerEntry> activePowers = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private static PowerFactory powerFactory = new PowerImpl();
    private static GameManager gameManager;

    private static class ActivePowerEntry {
        Power<?> power;
        ScheduledFuture<?> scheduledTask;
        Object target; // Lưu target để remove effect

        ActivePowerEntry(Power<?> power, ScheduledFuture<?> task, Object target) {
            this.power = power;
            this.scheduledTask = task;
            this.target = target;
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

    public static void setPowerFactory(PowerFactory factory) {
        powerFactory = factory;
    }

    public static void addPower(Power<?> power) {
        powers.add(power);
    }

    private static void updateBlinkEffects(Paddle paddle, Ball ball) {
        for (ActivePowerEntry entry : activePowers.values()) {
            Power<?> power = entry.power;

            if (power != null && power.isActive()) {
                if (power instanceof ExpandPaddle) {
                    ((ExpandPaddle) power).updateBlinkEffect(paddle);
                }
            }
        }
    }
    public static void updatePowers(Paddle paddle, Ball ball) {
        if (gameManager == null || paddle == null) return;

        updateBlinkEffects(paddle, ball);

        for (int i = powers.size() - 1; i >= 0; i--) {
            Power<?> power = powers.get(i);
            power.update();

            if (power.intersects(paddle)) {
                collectPowerUp(power, paddle, ball);
                powers.remove(i);
                continue;
            }

            if (power.getY() > gameManager.getGameHeight()) {
                powers.remove(i);
            }
        }
    }

    private static void collectPowerUp(Power<?> power, Paddle paddle, Ball ball) {
        String type = power.getType();
        PowerType powerType = PowerType.fromString(type);

        if (activePowers.containsKey(type)) {
            handleExistingPower(power, paddle, ball, type, powerType);
        } else {
            activateNewPower(power, paddle, ball, type, powerType);
        }
    }

    private static void handleExistingPower(Power<?> power, Paddle paddle, Ball ball,
                                            String type, PowerType powerType) {
        ActivePowerEntry entry = activePowers.get(type);

        switch (powerType) {
            case EXPAND:
            case SLOW:
            case FAST_BALL:
                extendTimedPower(entry, power, paddle, ball, type);
                break;
            case LIFE, MULTI_BALL:
                ((Power<GameManager>) power).applyEffect(gameManager);
                break;
        }
    }

    private static void extendTimedPower(ActivePowerEntry entry, Power<?> power,
                                         Paddle paddle, Ball ball, String type) {
        entry.cancel();

        long remainingTime = entry.power.getActiveTime();

        Object target = entry.target;
        ScheduledFuture<?> newTask = scheduler.schedule(() -> {
            ((Power<Object>) entry.power).removeEffect(target);
            activePowers.remove(type);
        }, remainingTime, TimeUnit.MILLISECONDS);

        entry.scheduledTask = newTask;
    }

    private static void activateNewPower(Power<?> power, Paddle paddle, Ball ball,
                                         String type, PowerType powerType) {
        switch (powerType) {
            case LIFE, MULTI_BALL:
                ((Power<GameManager>) power).applyEffect(gameManager);
                break;

            case EXPAND:
                scheduleTimedPower((Power<Paddle>) power, paddle, type);
                break;

            case FAST_BALL, SLOW:
                scheduleTimedPower((Power<Ball>) power, ball, type);
                break;
        }
    }

    private static <T> void scheduleTimedPower(Power<T> power, T target, String type) {
        power.applyEffect(target);
        long duration = power.getActiveTime() > 0 ? power.getActiveTime() : 0000;

        ScheduledFuture<?> task = scheduler.schedule(() -> {
            power.removeEffect(target);
            activePowers.remove(type);
            System.out.println("PowerUp expired: " + type);
        }, duration, TimeUnit.MILLISECONDS);

        activePowers.put(type, new ActivePowerEntry(power, task, target));
        System.out.println("Activated timed PowerUp: " + type + " - Duration: " + duration + "ms");
    }

    public static void renderPowers(GraphicsContext gc) {
        for (Power<?> power : powers) {
            power.render(gc);
        }
    }

    public static void clearPowers(Paddle paddle, List<Ball> balls) {
        for (ActivePowerEntry entry : activePowers.values()) {
            Power power = entry.power;

            if (power != null && power.isActive()) {
                power.removeDefaultEffect(paddle);

                if (power instanceof FastBall || power instanceof SlowBall) {
                    for (Ball ball : balls) {
                        power.removeEffect(ball);
                    }
                }
            }

            entry.cancel();
        }

        powers.clear();
        activePowers.clear();
    }


    private enum PowerType {
        EXPAND, FAST_BALL, MULTI_BALL, LIFE, SLOW, SHRINK;

        static PowerType fromString(String type) {
            return PowerType.valueOf(type.toUpperCase());
        }
    }
}