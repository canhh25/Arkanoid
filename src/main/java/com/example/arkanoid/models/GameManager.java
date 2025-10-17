package com.example.arkanoid.models;

import com.example.arkanoid.utils.LevelLoader;
import com.example.arkanoid.utils.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static final double BALL_SPEED = 2;
    private final int gameWidth;
    private final int gameHeight;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<MovableObject> movables = new ArrayList<>();

    public int score;
    public int lives;
    public int level;
    private boolean brickBrokenThisFrame = false;
    private boolean brickHitThisFrame = false;
    private boolean paddleHitThisFrame = false;
    private boolean waitingLaunch = true;
    private GameState gameState = GameState.PAUSED;

    public GameManager(int width, int height) {
        this.gameWidth = width;
        this.gameHeight = height;
        this.score = 0;
        this.lives = 3;
        this.level = 5;
        setupGame();
    }

    public void setupGame() {
        gameState = GameState.RUNNING;
        resetSoundFlags();
        bricks = LevelLoader.loadLevel(this.level);
        movables = new ArrayList<>();

        paddle = new Paddle(gameWidth / 2.0 - 50, gameHeight - 50, gameWidth);
        ball = new Ball(gameWidth / 2.0 - 10, gameHeight - 80, BALL_SPEED);

        waitingLaunch = true;
        ball.setDx(0);
        ball.setDy(0);
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight());
        movables.add(paddle);
        movables.add(ball);
    }

    public void requestLaunch() {
        if (waitingLaunch && gameState == GameState.RUNNING) {
            waitingLaunch = false;
            // bắn lên trên, lệch phải 60 độ
            double angle = Math.toRadians(-60);
            ball.dx = BALL_SPEED * Math.cos(angle);
            ball.dy = BALL_SPEED * Math.sin(angle);
        }
    }

    public void update(boolean goLeft, boolean goRight) {
        if (gameState == GameState.GAME_OVER) return;
        resetSoundFlags();
        paddle.setMovingLeft(goLeft);
        paddle.setMovingRight(goRight);
        paddle.update();
        if (waitingLaunch) {
            // dán bóng theo paddle, không cho bóng chạy & không check va chạm
            ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(paddle.getY() - ball.getHeight());
            return;
        }
        ball.update();
        checkCollisions();
        playSounds();
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
            double ballTop = ball.getY();
            double ballBottom = ball.getY() + ball.getHeight();

            // Biên của paddle
            double paddleLeft = paddle.getX();
            double paddleRight = paddle.getX() + paddle.getWidth();
            double paddleTop = paddle.getY();
            double paddleBottom = paddle.getY() + paddle.getHeight();

            // Chỉ phản ứng nếu bóng đang rơi xuống, tránh bóng va chạm liên tục với paddle
            if (ball.dy > 0 && ballBottom >= paddleTop && ballTop < paddleTop) {
                // Đặt bóng lên trên mặt paddle, tránh mắc kẹt trong paddle
                ball.setY(paddleTop - ball.getHeight());

                // Tính vị trí chạm để xác định góc bật
                double paddleCenter = paddleLeft + paddle.getWidth() / 2.0;
                double ballCenter = ballLeft + ball.getWidth() / 2.0;
                double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0); // [-1..1]
                hitPosition = Math.max(-1, Math.min(1, hitPosition));

                double MAX_DEFLECT = Math.toRadians(60);
                double angle = -Math.PI / 2 + hitPosition * MAX_DEFLECT;

                double MIN_AWAY = Math.toRadians(10);
                double away = Math.abs(angle + Math.PI / 2);
                if (away < MIN_AWAY) {
                    angle = (angle < -Math.PI / 2) ? -Math.PI / 2 - MIN_AWAY : -Math.PI / 2 + MIN_AWAY;
                }

                ball.dx = BALL_SPEED * Math.cos(angle);
                ball.dy = BALL_SPEED * Math.sin(angle);
                paddleHitThisFrame = true;
            }
        }

        //ball va chạm với brick
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                ball.dy *= -1;
                normalizeBallSpeed(ball);
                brickHitThisFrame = true;
                if (brick.hitPoints == 1) {
                    this.score += (brick.type * 10);
                    brickBrokenThisFrame = true;
                }
                brick.hit();
                break;
            }
        }
        System.out.println(this.score);
        bricks.removeIf(Brick::isDestroyed);

        // Kiểm tra nếu hết brick(trừ brick không thể phá) thì qua màn.
        int countBrick = 0;
        for (Brick brick : bricks) {
            if (brick.type != 4) {
                countBrick++;
            }
        }
        if (countBrick == 0) {
            gameState = GameState.WIN;
        }
        if (ball.getY() > gameHeight) {
            this.lives--;
            if (this.lives == 0) {
                gameState = GameState.GAME_OVER;
            } else {
                setupGame();
            }

        }
        if (bricks.isEmpty()) {
            gameState = GameState.GAME_OVER;
        }
    }

    private void resetSoundFlags() {
        brickBrokenThisFrame = false;
        brickHitThisFrame = false;
        paddleHitThisFrame = false;
    }

    private void playSounds() {
        new Thread(() -> {
            if (brickBrokenThisFrame) {
                SoundManager.playBrickBreak();
            }
            if (brickHitThisFrame) {
                SoundManager.playBrickHit();
            }
            if (paddleHitThisFrame) {
                SoundManager.playPaddleHit();
            }
        }).start();
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
        return gameState == GameState.GAME_OVER;
    }

    public int getScore() {
        return this.score;
    }
}