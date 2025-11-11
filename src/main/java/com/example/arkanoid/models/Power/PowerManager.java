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
        Object target;

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

    public static void addPower(Power<?> power) {
        powers.add(power);
    }

    private static void updateBlinkEffects(Paddle paddle, List<Ball> balls) {
        for (ActivePowerEntry entry : activePowers.values()) {
            Power<?> power = entry.power;

            if (power != null && power.isActive()) {
                if (power instanceof ExpandPaddle) {
                    ((ExpandPaddle) power).updateBlinkEffect(paddle);
                }
                if (power instanceof FastBall || power instanceof SlowBall) {
                    for (Ball ball : balls) {
                        ((Power<Ball>) power).updateBlinkEffect(ball);
                    }
                }
            }
        }
    }

    public static void updatePowers(Paddle paddle, List<Ball> balls) {
        if (gameManager == null || paddle == null) return;

        updateBlinkEffects(paddle, balls);

        for (int i = powers.size() - 1; i >= 0; i--) {
            Power<?> power = powers.get(i);
            power.update();

            if (power.intersects(paddle)) {
                collectPowerUp(power, paddle, balls);
                powers.remove(i);
                continue;
            }

            if (power.getY() > gameManager.getGameHeight()) {
                powers.remove(i);
            }
        }
    }

    private static void collectPowerUp(Power<?> power, Paddle paddle, List<Ball> balls) {
        String type = power.getType();
        PowerType powerType = PowerType.fromString(type);

        if (activePowers.containsKey(type)) {
            handleExistingPower(power, paddle, balls, type, powerType);
        } else {
            activateNewPower(power, paddle, balls, type, powerType);
        }
    }

    private static void handleExistingPower(Power<?> power, Paddle paddle, List<Ball> balls,
                                            String type, PowerType powerType) {
        ActivePowerEntry entry = activePowers.get(type);

        switch (powerType) {
            case EXPAND:
            case SLOW:
            case FAST_BALL:
                extendTimedPower(entry, power, paddle, balls, type);
                break;
            case LIFE, MULTI_BALL:
                ((Power<GameManager>) power).applyEffect(gameManager);
                break;
        }
    }

    private static void extendTimedPower(ActivePowerEntry entry, Power<?> power,
                                         Paddle paddle, List<Ball> balls, String type) {
        entry.cancel();

        long remainingTime = entry.power.getActiveTime();

        Object target = entry.target;
        ScheduledFuture<?> newTask = scheduler.schedule(() -> {

            if (target instanceof Paddle) {
                ((Power<Paddle>) entry.power).removeEffect((Paddle) target);
                ((Paddle) target).setBlinking(false);
            } else if (target instanceof List) {
                List<Ball> targetBalls = (List<Ball>) target;
                for (Ball ball : targetBalls) {
                    ((Power<Ball>) entry.power).removeEffect(ball);
                    ball.setBlinking(false);
                }
            }

            activePowers.remove(type);
        }, remainingTime, TimeUnit.MILLISECONDS);

        entry.scheduledTask = newTask;
    }

    private static void activateNewPower(Power<?> power, Paddle paddle, List<Ball> balls,
                                         String type, PowerType powerType) {
        switch (powerType) {
            case LIFE, MULTI_BALL:
                ((Power<GameManager>) power).applyEffect(gameManager);
                break;

            case EXPAND:
                scheduleTimedPower((Power<Paddle>) power, paddle, type);
                break;

            case FAST_BALL, SLOW:
                scheduleTimedPowerForBalls((Power<Ball>) power, balls, type);
                break;
        }
    }

    private static void scheduleTimedPowerForBalls(Power<Ball> power, List<Ball> balls, String type) {
        for (Ball ball : balls) {
            power.applyEffect(ball);
        }

        long duration = power.getActiveTime() > 0 ? power.getActiveTime() : 0;

        ScheduledFuture<?> task = scheduler.schedule(() -> {
            for (Ball ball : balls) {
                power.removeEffect(ball);
                ball.setBlinking(false);
            }

            activePowers.remove(type);
        }, duration, TimeUnit.MILLISECONDS);

        activePowers.put(type, new ActivePowerEntry(power, task, balls));
    }

    private static <T> void scheduleTimedPower(Power<T> power, T target, String type) {
        power.applyEffect(target);
        long duration = power.getActiveTime() > 0 ? power.getActiveTime() : 0000;

        ScheduledFuture<?> task = scheduler.schedule(() -> {
            power.removeEffect(target);
            activePowers.remove(type);
        }, duration, TimeUnit.MILLISECONDS);

        activePowers.put(type, new ActivePowerEntry(power, task, target));
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
                if (power instanceof ExpandPaddle || power instanceof ShrinkPaddle) {
                    power.removeDefaultEffect(paddle);
                }

                if (power instanceof FastBall || power instanceof SlowBall) {
                    for (Ball ball : balls) {
                        power.removeEffect(ball);
                    }
                }
            }
            paddle.setBlinking(false);
            for (Ball ball : balls) {
                ball.setBlinking(false);
            }
            entry.cancel();
        }

        powers.clear();
        activePowers.clear();
    }
    public static void applyActiveBallPowersToNewBall(Ball newBall) {
        for (Map.Entry<String, ActivePowerEntry> entry : activePowers.entrySet()) {
            Power<?> power = entry.getValue().power;

            if (power instanceof FastBall || power instanceof SlowBall) {
                ((Power<Ball>) power).applyEffect(newBall);
            }
        }
    }

    private enum PowerType {
        EXPAND, FAST_BALL, MULTI_BALL, LIFE, SLOW, SHRINK;

        static PowerType fromString(String type) {
            return PowerType.valueOf(type.toUpperCase());
        }
    }

    public static List<Power<?>> getPowers() {
        return powers;
    }
}