package com.example.arkanoid.models;

import com.example.arkanoid.utils.LevelLoader;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final int gameWidth;
    private final int gameHeight;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<MovableObject> movables = new ArrayList<>();

    public int score;
    public int lives;
    public int level;
    private boolean isGameOver = false;

    public GameManager(int width, int height) {
        this.gameWidth = width;
        this.gameHeight = height;
        this.score = 0;
        this.lives = 3;
        this.level = 6;
        setupGame();
    }

    public void setupGame() {
        isGameOver = false;

        bricks = LevelLoader.loadLevel(this.level);
        movables = new ArrayList<>();

        paddle = new Paddle(gameWidth / 2.0 - 50, gameHeight - 50, gameWidth);
        ball = new Ball(gameWidth / 2.0 - 10, gameHeight - 80);

        movables.add(paddle);
        movables.add(ball);

    }

    public void update(boolean goLeft, boolean goRight) {
        if (isGameOver) return;

        resetSoundFlags();

        paddle.setMovingLeft(goLeft);
        paddle.setMovingRight(goRight);

        for (MovableObject obj : movables) {
            obj.update();
        }

        checkCollisions();
        playSounds();
    }

    private void checkCollisions() {
        if (ball.getX() <= 0 || ball.getX() >= gameWidth - ball.getWidth()) ball.dx *= -1;
        if (ball.getY() <= 0) ball.dy *= -1;
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.dy *= -1;
            ball.setY(paddle.getY() - ball.getHeight());
            paddleHitThisFrame = true;
        }

        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                ball.dy *= -1;
                brick.hit();
                brickHitThisFrame=true;
                if (brick.isDestroyed()) {
                    brickBrokenThisFrame = true;
                }
                break;
            }
        }
        bricks.removeIf(Brick::isDestroyed);

        if (ball.getY() > gameHeight) {
            isGameOver = true;
        }
        if (bricks.isEmpty()) {
            isGameOver = true;
        }
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

    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public List<Brick> getBricks() { return bricks; }
    public boolean isGameOver() { return isGameOver; }
}