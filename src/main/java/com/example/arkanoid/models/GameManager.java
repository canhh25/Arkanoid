package com.example.arkanoid.models;

import com.example.arkanoid.models.Power.PowerManager;
import com.example.arkanoid.utils.LevelLoader;
import com.example.arkanoid.utils.SoundManager;
import javafx.scene.canvas.GraphicsContext;

import java.io.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;

public class GameManager {
    private final int gameWidth = 960;
    private final int gameHeight = 640;

    private long startTime;
    private long elapsedTime;
    private boolean timerRunning;

    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<MovableObject> movables = new ArrayList<>();

    public int score;
    public int lives;
    public int level;
    private boolean brickBrokenThisFrame = false;
    private boolean brickHitThisFrame = false;
    private boolean paddleHitThisFrame = false;
    private boolean waitingLaunch = true;
    private static final Random RANDOM = new Random();
    public GameState gameState = GameState.PAUSED;

    private int unlockedLevel = 1;
    private int selectedLevel = 1;
    public static GameManager instance;

    private static final Preferences prefs = Preferences.userNodeForPackage(GameManager.class);
    private static final String PREF_UNLOCKED_LEVEL = "unlockedLevel";

    private GameManager() {
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.balls = new ArrayList<>();
        loadProgress();
        PowerManager.setGameManager(this);
        setupGame();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public int getUnlockedLevel() {
        return unlockedLevel;
    }

    public void unlockNextLevel() {
        if (unlockedLevel < 10) {
            unlockedLevel++;
            saveProgress();
        }
    }

    private void saveProgress() {
        prefs.putInt(PREF_UNLOCKED_LEVEL, unlockedLevel);
    }

    private void loadProgress() {
        unlockedLevel = prefs.getInt(PREF_UNLOCKED_LEVEL, 1);
    }

    public void resetProgress() {
        unlockedLevel = 1;
        saveProgress();
    }

    public void setSelectedLevel(int level) {
        this.selectedLevel = level;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }

    public void nextGame() {
        if (gameState == GameState.WIN) {
            this.level++;
        } else if (gameState == GameState.GAME_OVER) {
            this.score = 0;
            this.lives = 3;
            resetTimer();
        }
        setupGame();
        startTimer();
    }

    public void setupLevel(int level) {
        this.selectedLevel = level;
        this.level = level;
        setupGame();
    }

    public void setupGame() {
        if (gameState != GameState.DEAD) {
            bricks = LevelLoader.loadLevel(this.selectedLevel);
            this.level = this.selectedLevel;
        }
        gameState = GameState.RUNNING;
        resetSoundFlags();
        movables = new ArrayList<>();
        PowerManager.clearPowers(paddle, balls);
        paddle = new Paddle(gameWidth / 2.0 - 50, gameHeight - 50, gameWidth);

        balls = new ArrayList<>();
        Ball mainBall = new Ball(gameWidth / 2.0 - 10, gameHeight - 80);
        balls.add(mainBall);

        waitingLaunch = true;
        mainBall.setDx(0);
        mainBall.setDy(0);
        mainBall.setX(paddle.getX() + paddle.getWidth() / 2 - mainBall.getWidth() / 2);
        mainBall.setY(paddle.getY() - mainBall.getHeight());
        mainBall.setPrevX(mainBall.getX());
        mainBall.setPrevY(mainBall.getY());
        movables.add(paddle);
        movables.add(mainBall);
    }

    public void requestLaunch() {
        if (waitingLaunch && gameState == GameState.RUNNING && !balls.isEmpty()) {
            if (!timerRunning) {
                startTimer();
            }

            waitingLaunch = false;
            Ball mainBall = balls.get(0);
            double minAngleDeg = -120.0;
            double maxAngleDeg = -60.0;
            double randomAngleDeg = minAngleDeg + (maxAngleDeg - minAngleDeg) * RANDOM.nextDouble();
            double angle = Math.toRadians(randomAngleDeg);

            mainBall.launchByAngle(angle);
            mainBall.setPrevX(mainBall.getX());
            mainBall.setPrevY(mainBall.getY());
        }
    }

    public void update(boolean goLeft, boolean goRight) {
        if (timerRunning) {
            elapsedTime = System.currentTimeMillis() - startTime;
        }
        if (gameState == GameState.GAME_OVER) return;
        resetSoundFlags();
        paddle.setMovingLeft(goLeft);
        paddle.setMovingRight(goRight);
        paddle.update();

        if (waitingLaunch && !balls.isEmpty()) {
            Ball mainBall = balls.get(0);
            mainBall.setX(paddle.getX() + paddle.getWidth() / 2 - mainBall.getWidth() / 2);
            mainBall.setY(paddle.getY() - mainBall.getHeight());
            mainBall.setPrevX(mainBall.getX());
            mainBall.setPrevY(mainBall.getY());
            return;
        }

        for (Ball ball : balls) {
            ball.update();
        }

        PowerManager.updatePowers(paddle, balls.isEmpty() ? null : balls.get(0));
        checkCollisions();
        playSounds();
    }

    private boolean circleIntersectsRect(Ball ball, double rx, double ry, double rw, double rh) {
        double r = ball.getWidth() / 2.0;
        double cx = ball.getX() + r;
        double cy = ball.getY() + r;

        double closestX = Math.max(rx, Math.min(cx, rx + rw));
        double closestY = Math.max(ry, Math.min(cy, ry + rh));
        double dx = cx - closestX;
        double dy = cy - closestY;
        return (dx * dx + dy * dy) <= r * r;
    }

    private void forceResetSpeed(Ball ball) {
        double currentSpeed = Math.sqrt(ball.dx * ball.dx + ball.dy * ball.dy);
        if (currentSpeed < 0.01) return;

        double targetSpeed = ball.getSpeed();
        double angle = Math.atan2(ball.dy, ball.dx);

        ball.dx = targetSpeed * Math.cos(angle);
        ball.dy = targetSpeed * Math.sin(angle);
    }

    private void resolveWalls(Ball ball) {
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.reverseX();
            forceResetSpeed(ball);
        }
        if (ball.getX() + ball.getWidth() >= gameWidth) {
            ball.setX(gameWidth - ball.getWidth());
            ball.reverseX();
            forceResetSpeed(ball);
        }
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.reverseY();
            forceResetSpeed(ball);
        }
    }

    private void resolvePaddleCollision(Ball ball) {
        double pLeft = paddle.getX();
        double pTop = paddle.getY();
        double pRight = pLeft + paddle.getWidth();
        double pBottom = pTop + paddle.getHeight();

        if (!circleIntersectsRect(ball, pLeft, pTop, paddle.getWidth(), paddle.getHeight()))
            return;

        double prevBottom = ball.getPrevY() + ball.getHeight();
        boolean cameFromTop = (prevBottom <= pTop) && (ball.dy > 0);

        if (cameFromTop) {
            ball.setY(pTop - ball.getHeight());
            double r = ball.getWidth() / 2.0;
            double paddleCenter = pLeft + paddle.getWidth() / 2.0;
            double ballCenter = ball.getX() + r;
            double hit = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);
            hit = Math.max(-1, Math.min(1, hit));

            double MAX_DEFLECT = Math.toRadians(60);
            double angle = -Math.PI / 2 + hit * MAX_DEFLECT;

            double MIN_AWAY = Math.toRadians(10);
            double away = Math.abs(angle + Math.PI / 2);
            if (away < MIN_AWAY) {
                angle = (angle < -Math.PI / 2) ? -Math.PI / 2 - MIN_AWAY : -Math.PI / 2 + MIN_AWAY;
            }


            ball.dx = ball.getSpeed() * Math.cos(angle);
            ball.dy = ball.getSpeed() * Math.sin(angle);
            paddleHitThisFrame = true;
        } else {
            double overlapLeft = (ball.getX() + ball.getWidth()) - pLeft;
            double overlapRight = pRight - ball.getX();
            double overlapTop = (ball.getY() + ball.getHeight()) - pTop;
            double overlapBottom = pBottom - ball.getY();

            double minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

            if (minOverlap == overlapTop) {
                ball.setY(pTop - ball.getHeight());
                ball.reverseY();
            } else if (minOverlap == overlapBottom) {
                ball.setY(pBottom);
                ball.reverseY();
            } else if (minOverlap == overlapLeft) {
                ball.setX(pLeft - ball.getWidth());
                ball.reverseX();
            } else {
                ball.setX(pRight);
                ball.reverseX();
            }
            forceResetSpeed(ball);
        }
    }

    private boolean resolveBrickCollision(Ball ball, Brick brick) {
        if (!ball.getBounds().intersects(brick.getBounds())) {
            return false;
        }
        double prevX = ball.getPrevX();
        double prevY = ball.getPrevY();
        double nextX = ball.getX();
        double nextY = ball.getY();

        double bLeft = brick.getX();
        double bTop = brick.getY();
        double bRight = bLeft + brick.getWidth();
        double bBottom = bTop + brick.getHeight();

        boolean fromLeft = prevX + ball.getWidth() <= bLeft && nextX + ball.getWidth() > bLeft;
        boolean fromRight = prevX >= bRight && nextX < bRight;
        boolean fromTop = prevY + ball.getHeight() <= bTop && nextY + ball.getHeight() > bTop;
        boolean fromBottom = prevY >= bBottom && nextY < bBottom;

        if (fromTop) {
            ball.setY(bTop - ball.getHeight() - 0.1);
            ball.reverseY();
        } else if (fromBottom) {
            ball.setY(bBottom + 0.1);
            ball.reverseY();
        } else if (fromLeft) {
            ball.setX(bLeft - ball.getWidth() - 0.1);
            ball.reverseX();
        } else if (fromRight) {
            ball.setX(bRight + 0.1);
            ball.reverseX();
        } else {
            ball.reverseY();
        }

        forceResetSpeed(ball);

        brickHitThisFrame = true;
        if (brick.hitPoints == 1) {
            this.score += (brick.type * 10);
            brickBrokenThisFrame = true;
        }

        brick.hit();
        return true;
    }

    private void checkCollisions() {
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();

            resolveWalls(ball);
            resolvePaddleCollision(ball);

            for (Brick brick : bricks) {
                if (resolveBrickCollision(ball, brick)) {
                    break;
                }
            }

            if (ball.getY() > gameHeight) {
                ballIterator.remove();
                movables.remove(ball);
            }
        }

        bricks.removeIf(Brick::isDestroyed);

        if (balls.isEmpty()) {
            this.lives--;

            if (lives > 0) {
                gameState = GameState.DEAD;
                setupGame();
            } else {
                gameState = GameState.GAME_OVER;
                timerRunning = false;
                elapsedTime = 0;
            }
            return;
        }

        if (isEmptyBrick()) {
            gameState = GameState.WIN;
            timerRunning = false;
        }
    }

    public boolean isEmptyBrick() {
        for (Brick brick : bricks) {
            if (brick.type != 4) return false;
        }
        return true;
    }

    private void resetSoundFlags() {
        brickBrokenThisFrame = false;
        brickHitThisFrame = false;
        paddleHitThisFrame = false;
    }

    private void playSounds() {
        if (brickBrokenThisFrame) {
            SoundManager.playBrickBreak();
        }
        if (brickHitThisFrame) {
            SoundManager.playBrickHit();
        }
        if (paddleHitThisFrame) {
            SoundManager.playPaddleHit();
        }
    }

    public void renderPowerUps(GraphicsContext gc) {
        PowerManager.renderPowers(gc);
    }

    public void renderBalls(GraphicsContext gc) {
        for (Ball ball : balls) {
            ball.render(gc);
        }
    }

    public void addBall(double x, double y, double speed, double angle) {
        Ball ball = new Ball(x, y, speed);
        double angle_ball = Math.toRadians(angle);
        ball.launchByAngle(angle_ball);
        ball.setPrevX(ball.getX());
        ball.setPrevY(ball.getY());
        balls.add(ball);
        movables.add(ball);
    }

    public int getLives() {
        return lives;
    }

    public int getLevel() {
        return level;
    }

    public int getGameHeight() {
        return gameHeight;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return balls.isEmpty() ? null : balls.get(0);
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    public int getScore() {
        return this.score;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getFormattedTime() {
        long sec = elapsedTime / 1000;
        long minutes = sec / 60;
        long seconds = sec % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        timerRunning = true;
    }

    public void resetTimer() {
        elapsedTime = 0;
        timerRunning = false;
    }

    public void resetGame() {
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.selectedLevel = 1;
        this.waitingLaunch = true;
        this.timerRunning = false;
        this.elapsedTime = 0;

        PowerManager.clearPowers(paddle, balls);
        setupGame();
    }
}