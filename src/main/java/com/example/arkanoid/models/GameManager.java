package com.example.arkanoid.models;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final int gameWidth;
    private final int gameHeight;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<MovableObject> movables = new ArrayList<>();

    private boolean brickBrokenThisFrame = false;
    private boolean brickHitThisFrame = false;
    private boolean paddleHitThisFrame = false;

    private boolean isGameOver = false;

    public GameManager(int width, int height) {
        this.gameWidth = width;
        this.gameHeight = height;
        setupGame();
    }

    public void setupGame() {
        isGameOver = false;
        resetSoundFlags();
        bricks = new ArrayList<>();
        movables = new ArrayList<>();

        paddle = new Paddle(gameWidth / 2.0 - 50, gameHeight - 50, gameWidth);
        ball = new Ball(gameWidth / 2.0 - 10, gameHeight - 80);

        movables.add(paddle);
        movables.add(ball);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                double x = col * (Brick.BRICK_WIDTH + 10) + 35;
                double y = row * (Brick.BRICK_HEIGHT + 10) + 50;
                if (row < 2) {
                    bricks.add(new Brick(x, y,
                            2,
                            "/images/brick/brick_red.png",
                            "/images/brick/brick_red_cracked.png"));
                } else {
                    bricks.add(new Brick(x, y,
                            1,
                            "/images/brick/brick_blue.png",
                            null));
                }
            }
        }
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