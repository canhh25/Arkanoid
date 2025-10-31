package com.example.arkanoid.models;

import com.example.arkanoid.models.Power.ExtraLifePowerUp;
import com.example.arkanoid.models.Power.PowerUpManager;
import com.example.arkanoid.utils.LevelLoader;
import com.example.arkanoid.utils.SoundManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    private static final double BALL_SPEED = 5.5;
    private final int gameWidth = 960;
    private final int gameHeight = 640;

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
    public GameState gameState = GameState.PAUSED;

    public static GameManager instance;

    private GameManager() {
        this.score = 0;
        this.lives = 3;
        this.level = 5;
        this.balls = new ArrayList<>();
        PowerUpManager.setGameManager(this);
        setupGame();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void nextGame() {
        if (gameState == GameState.WIN) {
            this.level++;
        } else if (gameState == GameState.GAME_OVER) {
            this.score = 0;
            this.lives = 3;
        }
        setupGame();
    }

    public void setupGame() {
        if (gameState != GameState.DEAD) {
            bricks = LevelLoader.loadLevel(this.level);
        }
        gameState = GameState.RUNNING;
        resetSoundFlags();

        movables = new ArrayList<>();
        PowerUpManager.clearPowerUps();

        paddle = new Paddle(gameWidth / 2.0 - 50, gameHeight - 50, gameWidth);

        // Tạo bóng chính
        balls = new ArrayList<>();
        Ball mainBall = new Ball(gameWidth / 2.0 - 10, gameHeight - 80, BALL_SPEED);
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
            waitingLaunch = false;
            Ball mainBall = balls.get(0);
            double angle = Math.toRadians(-60);
            mainBall.dx = BALL_SPEED * Math.cos(angle);
            mainBall.dy = BALL_SPEED * Math.sin(angle);
            mainBall.setPrevX(mainBall.getX());
            mainBall.setPrevY(mainBall.getY());
        }
    }

    // THÊM: Phương thức spawn thêm 2 bóng
    public void spawnExtraBalls() {
        if (balls.isEmpty()) return;
        System.out.println("Add ball");
        Ball originalBall = balls.get(0);

        // Tạo bóng thứ 2 (bay về trái)
        Ball ball2 = new Ball(originalBall.getX(), originalBall.getY(), BALL_SPEED);
        double angle2 = Math.toRadians(-120); // 120 độ
        ball2.dx = BALL_SPEED * Math.cos(angle2);
        ball2.dy = BALL_SPEED * Math.sin(angle2);
        ball2.setPrevX(ball2.getX());
        ball2.setPrevY(ball2.getY());
        balls.add(ball2);
        movables.add(ball2);

        // Tạo bóng thứ 3 (bay về phải)
        Ball ball3 = new Ball(originalBall.getX(), originalBall.getY(), BALL_SPEED);
        double angle3 = Math.toRadians(-60); // 60 độ
        ball3.dx = BALL_SPEED * Math.cos(angle3);
        ball3.dy = BALL_SPEED * Math.sin(angle3);
        ball3.setPrevX(ball3.getX());
        ball3.setPrevY(ball3.getY());
        balls.add(ball3);
        movables.add(ball3);
    }

    public void update(boolean goLeft, boolean goRight) {
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

        // Update tất cả các bóng
        for (Ball ball : balls) {
            ball.update();
        }

        PowerUpManager.updatePowerUps(paddle, balls.isEmpty() ? null : balls.get(0));
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

    private void resolveWalls(Ball ball) {
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.reverseX();
            normalizeBallSpeed(ball);
        }
        if (ball.getX() + ball.getWidth() >= gameWidth) {
            ball.setX(gameWidth - ball.getWidth());
            ball.reverseX();
            normalizeBallSpeed(ball);
        }
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.reverseY();
            normalizeBallSpeed(ball);
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

    private boolean resolveBrickCollision(Ball ball, Brick brick) {
        if (!ball.getBounds().intersects(brick.getBounds())) {
            return false;
        }

        ball.dy *= -1;
        brickHitThisFrame = true;

        if (brick.hitPoints == 1) {
            this.score += (brick.type * 10);
            brickBrokenThisFrame = true;
        }

        brick.hit();
        return true;
    }

    private void checkCollisions() {
        // Duyệt qua tất cả các bóng
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();

            // 1) Tường và trần
            resolveWalls(ball);

            // 2) Paddle
            resolvePaddleCollision(ball);

            // 3) Brick
            for (Brick brick : bricks) {
                if (resolveBrickCollision(ball, brick)) {
                    break;
                }
            }

            // 4) Kiểm tra bóng rơi xuống đáy
            if (ball.getY() > gameHeight) {
                ballIterator.remove();
                movables.remove(ball);
            }
        }

        // Xóa brick bị phá
        bricks.removeIf(Brick::isDestroyed);

        // 5) Kiểm tra hết bóng => mất mạng
        if (balls.isEmpty()) {
            this.lives--;
            if (lives > 0) {
                gameState = GameState.DEAD;
                setupGame();
            } else {
                gameState = GameState.GAME_OVER;
            }
            return;
        }

        // 6) Win nếu hết brick
        if (isEmptyBrick()) {
            gameState = GameState.WIN;
            nextGame();
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
        PowerUpManager.renderPowerUps(gc);
    }

    // THÊM: Render tất cả các bóng
    public void renderBalls(GraphicsContext gc) {
        for (Ball ball : balls) {
            ball.render(gc);
        }
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

    // THAY ĐỔI: Trả về bóng đầu tiên (để tương thích code cũ)
    public Ball getBall() {
        return balls.isEmpty() ? null : balls.get(0);
    }

    // THÊM: Getter cho danh sách bóng
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
}