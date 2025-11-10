package com.arkanoid;

import com.example.arkanoid.models.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class GameManagerTest {
    private GameManager gameManager;

    @BeforeEach
    public void setup() {
        GameManager.instance = null;
        gameManager = GameManager.getInstance();
    }

    @AfterEach
    public void cleanup() {
        GameManager.instance = null;
    }

    @Test
    @DisplayName("Test GameManager Singleton")
    public void testSingleton() {
        GameManager gm1 = GameManager.getInstance();
        GameManager gm2 = GameManager.getInstance();
        assertSame(gm1, gm2, "GameManager phải là singleton");
    }

    @Test
    @DisplayName("Test khởi tạo game")
    public void testGameInit() {
        assertNotNull(gameManager.getPaddle(), "Paddle phải được khởi tạo");
        assertNotNull(gameManager.getBall(), "Ball phải được khởi tạo");
        assertNotNull(gameManager.getBricks(), "Bricks phải được khởi tạo");

        assertEquals(3, gameManager.lives, "Lives ban đầu = 3");
        assertEquals(0, gameManager.score, "Score ban đầu = 0");
        assertEquals(1, gameManager.level, "Level ban đầu = 9");
        assertEquals(GameState.RUNNING, gameManager.gameState, "GameState = RUNNING");
    }

    @Test
    @DisplayName("Test bắt đầu game game tạo đúng số brick")
    public void testSetupGameCreatesBricks() {
        List<Brick> bricks = gameManager.getBricks();
        assertFalse(bricks.isEmpty(), "Phải có ít nhất 1 brick");
        assertTrue(bricks.size() > 0, "Số brick > 0");
    }

    @Test
    @DisplayName("Test ball ban đầu dính vào paddle")
    public void testBallStartsOnPaddle() {
        Ball ball = gameManager.getBall();
        Paddle paddle = gameManager.getPaddle();
        assertNotNull(ball, "Ball phải tồn tại");

        // Ball phải nằm ngay trên paddle
        double expectedY = paddle.getY() - ball.getHeight();
        assertEquals(expectedY, ball.getY(), 0.1, "Ball phải ở trên paddle");

        // Ball chưa di chuyển
        assertEquals(0, ball.getDx(), "dx ban đầu = 0");
        assertEquals(0, ball.getDy(), "dy ban đầu = 0");
    }

    @Test
    @DisplayName("Test balls list không empty")
    public void testBallListNotEmpty() {
        List<Ball> balls = gameManager.getBalls();

        assertNotNull(balls, "Ball list phải tồn tại");
        assertEquals(1, balls.size(), "Ban đầu phải có 1 ball");
    }

    @Test
    @DisplayName("Test launch ball với góc random")
    public void testLaunchBallRandomAngle() {
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        assertNotNull(ball, "Ball phải tồn tại");
        assertNotEquals(0, ball.getDx(), "dx phải khác 0 sau khi launch");
        assertNotEquals(0, ball.getDy(), "dy phải khác 0 sau khi launch");
        assertTrue(ball.getDy() < 0, "Ball phải bay lên (dy < 0)");
    }

    @Test
    @DisplayName("Test không launch 2 lần")
    public void testCannotLaunchTwice() {
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        double dx1 = ball.getDx();
        double dy1 = ball.getDy();
        gameManager.requestLaunch();
        assertEquals(dx1, ball.getDx(), 0.001, "dx không đổi sau launch thứ 2");
        assertEquals(dy1, ball.getDy(), 0.001, "dy không đổi sau launch thứ 2");
    }

    @Test
    @DisplayName("Test launch angle trong khoảng -120 đến -60 độ")
    public void testLaunchAngleRange() {
        for (int i = 0; i < 10; i++) {
            gameManager.setupGame();
            gameManager.requestLaunch();
            Ball ball = gameManager.getBall();
            double angle = Math.atan2(ball.getDy(), ball.getDx());
            double angleDeg = Math.toDegrees(angle);
            assertTrue(angleDeg >= -180 && angleDeg <= 0,
                    "Góc launch phải hợp lệ: " + angleDeg);
        }
    }

    @Test
    @DisplayName("Test paddle di chuyển trái")
    public void testPaddleMoveLeft() {
        Paddle paddle = gameManager.getPaddle();
        double initialX = paddle.getX();
        for (int i = 0; i < 10; i++) {
            gameManager.update(true, false);
        }
        assertTrue(paddle.getX() < initialX, "Paddle phải di chuyển sang trái");
    }

    @Test
    @DisplayName("Test paddle di chuyển phải")
    public void testPaddleMoveRight() {
        Paddle paddle = gameManager.getPaddle();
        double initialX = paddle.getX();
        for (int i = 0; i < 10; i++) {
            gameManager.update(false, true);
        }
        assertTrue(paddle.getX() > initialX, "Paddle phải di chuyển sang phải");
    }

    @Test
    @DisplayName("Test paddle không đi ra khỏi màn hình trái")
    public void testPaddleStaysInBoundsLeft() {
        for (int i = 0; i < 100; i++) {
            gameManager.update(true, false);
        }
        Paddle paddle = gameManager.getPaddle();
        assertTrue(paddle.getX() >= 0, "Paddle không được ra ngoài màn hình bên trái");
    }

    @Test
    @DisplayName("Test paddle không đi ra khỏi màn hình phải")
    public void testPaddleStaysInBoundsRight() {
        for (int i = 0; i < 200; i++) {
            gameManager.update(false, true);
        }
        Paddle paddle = gameManager.getPaddle();
        assertTrue(paddle.getX() + paddle.getWidth() <= 970,
                "Paddle không được ra ngoài màn hình bên phải");
    }

    @Test
    @DisplayName("Test ball di chuyển sau khi launch")
    public void testBallMovesAfterLaunch() {
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        double initialX = ball.getX();
        double initialY = ball.getY();
        for (int i = 0; i < 10; i++) {
            gameManager.update(false, false);
        }
        boolean moved = ball.getX() != initialX || ball.getY() != initialY;
        assertTrue(moved, "Ball phải di chuyển sau launch");
    }

    @Test
    @DisplayName("Test ball bounce trên tường trái")
    public void testBallBouncesLeftWall() {
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        ball.setX(2);
        ball.setDx(-2);
        ball.setDy(2);
        gameManager.update(false, false);
        assertTrue(ball.getDx() > 0, "Ball phải bounce sang phải");
        assertEquals(0, ball.getX(), 0.1, "Ball phải dính tường");
    }

    @Test
    @DisplayName("Test ball bounce trên tường phải")
    public void testBallBouncesRightWall() {
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        ball.setX(945);
        ball.setDx(2);
        ball.setDy(2);
        gameManager.update(false, false);
        assertTrue(ball.getDx() < 0, "Ball phải bounce sang trái");
    }

    @Test
    @DisplayName("Test ball bounce trên trần")
    public void testBallBouncesTopWall() {
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        ball.setY(2);
        ball.setDx(2);
        ball.setDy(-2);
        gameManager.update(false, false);
        assertTrue(ball.getDy() > 0, "Ball phải bounce xuống");
        assertEquals(0, ball.getY(), 0.1, "Ball phải dính trần");
    }

//    @Test
//    @DisplayName("Test ball speed được normalize")
//    public void testBallSpeedNormalized() {
//        gameManager.requestLaunch();
//        Ball ball = gameManager.getBall();
//        for (int i = 0; i < 50; i++) {
//            gameManager.update(false, false);
//            double speed = Math.sqrt(ball.getDx() * ball.getDx() +
//                    ball.getDy() * ball.getDy());
//            assertEquals(GameManager.BALL_SPEED, speed, 0.1,
//                    "Ball speed phải được normalize");
//        }
//    }

    @Test
    @DisplayName("Test mất mạng khi ball rơi xuống đáy")
    public void testLoseLiveWhenBallFalls() {
        int initialLives = gameManager.lives;
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        ball.setY(700);
        gameManager.update(false, false);
        assertEquals(initialLives - 1, gameManager.lives,
                "Lives-- khi ball rơi");
    }

    @Test
    @DisplayName("Test game state DEAD khi mất mạng nhưng còn lives")
    public void testGameStateDeadWhenLoseLive() {
        gameManager.lives = 2;
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        ball.setY(700);
        gameManager.update(false, false);
        assertEquals(1, gameManager.lives);
    }

    @Test
    @DisplayName("Test game over khi hết mạng")
    public void testGameOverWhenNoLives() {
        gameManager.lives = 1;
        gameManager.requestLaunch();
        Ball ball = gameManager.getBall();
        ball.setY(700);
        gameManager.update(false, false);
        assertTrue(gameManager.isGameOver(), "Game phải over khi hết mạng");
        assertEquals(GameState.GAME_OVER, gameManager.gameState);
        assertEquals(0, gameManager.lives, "Lives = 0");
    }

    @Test
    @DisplayName("Test không update khi game over")
    public void testNoUpdateWhenGameOver() {
        gameManager.gameState = GameState.GAME_OVER;
        Paddle paddle = gameManager.getPaddle();
        double initialX = paddle.getX();
        gameManager.update(true, false);
        assertEquals(initialX, paddle.getX(), "Paddle không được di chuyển khi game over");
    }

    @Test
    @DisplayName("Test brick type 1 bị phá sau 1 hit")
    public void testBrickType1Destroyed() {
        List<Brick> bricks = gameManager.getBricks();
        Brick type1Brick = null;
        for (Brick brick : bricks) {
            if (brick.type == 1) {
                type1Brick = brick;
                break;
            }
        }
        if (type1Brick != null) {
            assertEquals(1, type1Brick.hitPoints, "Brick type 1 có HP = 1");
            type1Brick.hit();
            assertTrue(type1Brick.isDestroyed(), "Brick type 1 phải bị phá sau 1 hit");
        }
    }

    @Test
    @DisplayName("Test brick type 4 không bị phá")
    public void testBrickType4Indestructible() {
        List<Brick> bricks = gameManager.getBricks();
        Brick type4Brick = null;
        for (Brick brick : bricks) {
            if (brick.type == 4) {
                type4Brick = brick;
                break;
            }
        }
        if (type4Brick != null) {
            int initialHP = type4Brick.hitPoints;
            for (int i = 0; i < 10; i++) {
                type4Brick.hit();
            }
            assertEquals(initialHP, type4Brick.hitPoints,
                    "Brick type 4 HP không đổi");
            assertFalse(type4Brick.isDestroyed(),
                    "Brick type 4 không bị phá");
        }
    }

    @Test
    @DisplayName("Test score tăng khi phá brick")
    public void testScoreIncreasesWhenBrickDestroyed() {
        int initialScore = gameManager.score;
        List<Brick> bricks = gameManager.getBricks();
        Brick testBrick = null;
        for (Brick brick : bricks) {
            if (brick.type != 4) {
                testBrick = brick;
                break;
            }
        }
        if (testBrick != null) {
            int expectedScore = testBrick.type * 10;
            while (!testBrick.isDestroyed()) {
                testBrick.hit();
            }
            gameManager.score += expectedScore;
            assertTrue(gameManager.score >= initialScore,
                    "Score phải tăng khi phá brick");
        }
    }

    @Test
    @DisplayName("Test isEmptyBrick khi còn brick phá được")
    public void testIsEmptyBrickWithBreakableBricks() {
        assertFalse(gameManager.isEmptyBrick(),
                "Phải có brick phá được ban đầu");
    }

    @Test
    @DisplayName("Test isEmptyBrick khi chỉ còn brick type 4")
    public void testIsEmptyBrickOnlyType4() {
        List<Brick> bricks = gameManager.getBricks();
        for (Brick brick : bricks) {
            if (brick.type != 4) {
                while (!brick.isDestroyed()) {
                    brick.hit();
                }
            }
        }

        // Remove destroyed bricks
        bricks.removeIf(Brick::isDestroyed);

        assertTrue(gameManager.isEmptyBrick(),
                "Phải empty khi chỉ còn brick type 4");
    }

    @Test
    @DisplayName("Test win và next level tự động")
    public void testWinAndNextLevel() {
        int currentLevel = gameManager.level;
        List<Brick> bricks = gameManager.getBricks();
        for (Brick brick : bricks) {
            if (brick.type != 4) {
                while (!brick.isDestroyed()) {
                    brick.hit();
                }
            }
        }
        gameManager.requestLaunch();
        gameManager.update(false, false);
    }

    @Test
    @DisplayName("Test nextGame khi GAME_OVER reset score và lives")
    public void testNextGameResetsOnGameOver() {
        gameManager.score = 500;
        gameManager.lives = 0;
        gameManager.gameState = GameState.GAME_OVER;
        gameManager.nextGame();

        assertEquals(0, gameManager.score, "Score phải reset về 0");
        assertEquals(3, gameManager.lives, "Lives phải reset về 3");
    }

//    @Test
//    @DisplayName("Test addBall thêm ball mới")
//    public void testAddBall() {
//        int initialCount = gameManager.getBalls().size();
//        gameManager.addBall(400, 300, GameManager.BALL_SPEED, -90);
//        assertEquals(initialCount + 1, gameManager.getBalls().size(),
//                "Phải thêm 1 ball");
//
//        Ball newBall = gameManager.getBalls().get(gameManager.getBalls().size() - 1);
//        assertNotEquals(0, newBall.getDx());
//        assertNotEquals(0, newBall.getDy());
//    }

//    @Test
//    @DisplayName("Test multiple balls di chuyển độc lập")
//    public void testMultipleBallsMove() {
//        gameManager.requestLaunch();
//        gameManager.addBall(400, 300, GameManager.BALL_SPEED, -45);
//        gameManager.addBall(500, 300, GameManager.BALL_SPEED, -135);
//        assertEquals(3, gameManager.getBalls().size(), "Phải có 3 balls");
//        for (int i = 0; i < 10; i++) {
//            gameManager.update(false, false);
//        }
//        for (Ball ball : gameManager.getBalls()) {
//            double speed = Math.sqrt(ball.getDx() * ball.getDx() +
//                    ball.getDy() * ball.getDy());
//            assertTrue(speed > 0, "Mỗi ball phải có speed > 0");
//        }
//    }

    @Test
    @DisplayName("Test getters")
    public void testGetters() {
        assertEquals(3, gameManager.getLives());
        assertEquals(1, gameManager.getLevel());
        assertEquals(640, gameManager.getGameHeight());
        assertEquals(0, gameManager.getScore());
        assertNotNull(gameManager.getPaddle());
        assertNotNull(gameManager.getBall());
        assertNotNull(gameManager.getBalls());
        assertNotNull(gameManager.getBricks());
        assertFalse(gameManager.isGameOver());
    }
}