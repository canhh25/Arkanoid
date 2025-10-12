package com.example.arkanoid.models;

import com.example.arkanoid.utils.LevelLoader;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static final double BALL_SPEED = 3.5;
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
    private void normalizeBallSpeed(Ball ball) {
        double v = Math.sqrt(ball.dx * ball.dx + ball.dy * ball.dy);
        if (v == 0) return;
        ball.dx = ball.dx / v * BALL_SPEED;
        ball.dy = ball.dy / v * BALL_SPEED;
    }

    private void checkCollisions() {
        //ball va chạm với tường
        if (ball.getX() <= 0 || ball.getX() >= gameWidth - ball.getWidth()) {
            ball.dx *= -1;
            normalizeBallSpeed(ball);

        }
        if (ball.getY() <= 0) {
            ball.dy *= -1;
            normalizeBallSpeed(ball);
        }


        //ball va chạm với paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            // Biên của ball
            double ballLeft = ball.getX();
            double ballRight = ball.getX() + ball.getWidth();
            double ballTop = ball.getY();
            double ballBottom = ball.getY() + ball.getHeight();

            double paddleLeft = paddle.getX();
            double paddleRight = paddle.getX() + paddle.getWidth();
            double paddleTop = paddle.getY();
            double paddleBottom = paddle.getY() + paddle.getHeight();

            // Chỉ phản ứng nếu bóng đang rơi xuống, tránh bóng va chạm liên tục với paddle
            if (ball.dy > 0 && ballBottom >= paddleTop && ballTop < paddleTop) {
                // Đặt bóng lên trên mặt paddle, tránh mắc kẹt trong paddle
                ball.setY(paddleTop - ball.getHeight());
                ball.dy = -Math.abs(ball.dy);

                // Tính vị trí chạm để xác định hướng bật ngang
                double paddleCenter = paddleLeft + paddle.getWidth() / 2;
                double ballCenter = ballLeft + ball.getWidth() / 2;
                double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);
                hitPosition = Math.max(-1, Math.min(1, hitPosition));
                ball.dx = hitPosition * 3;
                normalizeBallSpeed(ball);
            }
        }

        //ball va chạm với brick
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                ball.dy *= -1;
                brick.hit();
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

    public void update(boolean goLeft, boolean goRight) {
        if (isGameOver) return;

        paddle.setMovingLeft(goLeft);
        paddle.setMovingRight(goRight);

        for (MovableObject obj : movables) {
            obj.update();
        }

        checkCollisions();
    }



    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}