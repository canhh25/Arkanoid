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
        ball.setPrevX(ball.getX());
        ball.setPrevY(ball.getY());
        movables.add(paddle);
        movables.add(ball);
    }

    public void requestLaunch() {
        if (waitingLaunch && gameState == GameState.RUNNING) {
            waitingLaunch = false;
            double angle = Math.toRadians(-60);
            ball.dx = BALL_SPEED * Math.cos(angle);
            ball.dy = BALL_SPEED * Math.sin(angle);
            ball.setPrevX(ball.getX());
            ball.setPrevY(ball.getY());
        }
    }

    public void update(boolean goLeft, boolean goRight) {
        if (gameState == GameState.GAME_OVER) return;
        resetSoundFlags();
        paddle.setMovingLeft(goLeft);
        paddle.setMovingRight(goRight);
        paddle.update();
        if (waitingLaunch) {
            ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(paddle.getY() - ball.getHeight());
            ball.setPrevX(ball.getX());
            ball.setPrevY(ball.getY());
            return;
        }
        ball.update();
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

    private void normalizeBallSpeed(Ball ball) {
        double v = Math.sqrt(ball.dx * ball.dx + ball.dy * ball.dy);
        if (v == 0) return;
        ball.dx = ball.dx / v * BALL_SPEED;
        ball.dy = ball.dy / v * BALL_SPEED;
    }

    private void resolveWalls() {
        // Trái
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.reverseX();
            normalizeBallSpeed(ball);
        }
        // Phải
        if (ball.getX() + ball.getWidth() >= gameWidth) {
            ball.setX(gameWidth - ball.getWidth());
            ball.reverseX();
            normalizeBallSpeed(ball);
        }
        // Trần
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.reverseY();
            normalizeBallSpeed(ball);
        }
    }

    private void resolvePaddleCollision() {
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
            double hit = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0); // [-1..1]
            hit = Math.max(-1, Math.min(1, hit));

            double MAX_DEFLECT = Math.toRadians(60);
            double angle = -Math.PI / 2 + hit * MAX_DEFLECT;

            double MIN_AWAY = Math.toRadians(10);
            double away = Math.abs(angle + Math.PI / 2);
            if (away < MIN_AWAY) {
                angle = (angle < -Math.PI / 2) ? -Math.PI / 2 - MIN_AWAY : -Math.PI / 2 + MIN_AWAY;
            }

            ball.dx = BALL_SPEED * Math.cos(angle);
            ball.dy = BALL_SPEED * Math.sin(angle);
            paddleHitThisFrame = true;
            normalizeBallSpeed(ball);
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
            normalizeBallSpeed(ball);
        }
    }

    private boolean resolveBrickCollision(Brick brick) {
        var r = brick.getBounds();
        double bLeft = r.getMinX();
        double bTop = r.getMinY();
        double bRight = bLeft + r.getWidth();
        double bBottom = bTop + r.getHeight();

        if (!circleIntersectsRect(ball, bLeft, bTop, r.getWidth(), r.getHeight()))
            return false;

        boolean fromTop = (ball.getPrevY() + ball.getHeight()) <= bTop;
        boolean fromBottom = (ball.getPrevY()) >= bBottom;
        boolean fromLeft = (ball.getPrevX() + ball.getWidth()) <= bLeft;
        boolean fromRight = (ball.getPrevX()) >= bRight;

        if (fromTop) {
            ball.setY(bTop - ball.getHeight());
            ball.reverseY();
        } else if (fromBottom) {
            ball.setY(bBottom);
            ball.reverseY();
        } else if (fromLeft) {
            ball.setX(bLeft - ball.getWidth());
            ball.reverseX();
        } else if (fromRight) {
            ball.setX(bRight);
            ball.reverseX();
        } else {
            // fallback: chọn trục chồng lấp ít hơn
            double overlapX = Math.min((ball.getX() + ball.getWidth()) - bLeft, bRight - ball.getX());
            double overlapY = Math.min((ball.getY() + ball.getHeight()) - bTop, bBottom - ball.getY());
            if (overlapX < overlapY) {
                if ((ball.getX() + ball.getWidth() / 2.0) > (bLeft + r.getWidth() / 2.0)) {
                    ball.setX(bRight);
                } else {
                    ball.setX(bLeft - ball.getWidth());
                }
                ball.reverseX();
            } else {
                if ((ball.getY() + ball.getHeight() / 2.0) > (bTop + r.getHeight() / 2.0)) {
                    ball.setY(bBottom);
                } else {
                    ball.setY(bTop - ball.getHeight());
                }
                ball.reverseY();
            }
        }

        normalizeBallSpeed(ball);

        // điểm/âm thanh
        brickHitThisFrame = true;
        if (brick.hitPoints == 1) {
            this.score += (brick.type * 10);
            brickBrokenThisFrame = true;
        }
        brick.hit();

        return true;
    }

    private void checkCollisions() {
        // 1) Tường và trần
        resolveWalls();

        // 2) Paddle
        resolvePaddleCollision();

        //Brick: xử lý đúng 1 viên đầu tiên
        for (Brick brick : bricks) {
            if (resolveBrickCollision(brick)) {
                break;
            }
        }
        bricks.removeIf(Brick::isDestroyed);

        //  Rơi dưới đáy => mất mạng/reset
        if (ball.getY() > gameHeight) {
            this.lives--;
            if (this.lives == 0) {
                gameState = GameState.GAME_OVER;
            } else {
                setupGame(); // reset bóng dán vào paddle
            }
            return;
        }

        // 5) win nếu hết brick phá được
        int countBreakable = 0;
        for (Brick brick : bricks) {
            if (brick.type != 4) countBreakable++;
        }
        if (countBreakable == 0) {
            gameState = GameState.WIN;
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