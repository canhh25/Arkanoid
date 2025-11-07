package com.arkanoid;

import com.example.arkanoid.models.Paddle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class PaddleTest {
    private Paddle paddle;
    private static final double GAME_WIDTH = 970;

    @BeforeEach
    public void setup() {
        Paddle.paddle_width = 100;
        Paddle.paddle_height = 30;
        paddle = new Paddle(400, 600, GAME_WIDTH);
    }

    @Test
    @DisplayName("Test khởi tạo paddle")
    public void testInit() {
        assertNotNull(paddle);
        assertEquals(400, paddle.getX());
        assertEquals(600, paddle.getY());
        assertEquals(100, paddle.getWidth());
        assertEquals(30, paddle.getHeight());
    }

    @Test
    @DisplayName("Test di chuyển sang trái")
    public void testMoveLeft() {
        paddle.setMovingLeft(true);
        paddle.update();
        assertEquals(397, paddle.getX()); // 400 - 3 = 397
    }

    @Test
    @DisplayName("Test di chuyển sang phải")
    public void testMoveRight() {
        paddle.setMovingRight(true);
        paddle.update();
        assertEquals(403, paddle.getX()); // 400 + 3 = 403
    }

    @Test
    @DisplayName("Test không di chuyển khi không bấm phím")
    public void testNoMove() {
        double x = paddle.getX();
        paddle.update();
        assertEquals(x, paddle.getX());
    }

    @Test
    @DisplayName("Test không vượt tường trái")
    public void testLeftWall() {
        paddle.setX(0);
        paddle.setMovingLeft(true);
        paddle.update();
        assertTrue(paddle.getX() >= 0);
    }

    @Test
    @DisplayName("Test không vượt tường phải")
    public void testRightWall() {
        paddle.setX(GAME_WIDTH - 100); // 970 - 100 = 870
        paddle.setMovingRight(true);
        paddle.update();
        assertTrue(paddle.getX() + paddle.getWidth() <= GAME_WIDTH);
    }

    @Test
    @DisplayName("Test di chuyển nhiều lần vẫn không vượt trái")
    public void testManyMovesLeft() {
        paddle.setMovingLeft(true);
        for (int i = 0; i < 200; i++) {
            paddle.update();
        }
        assertTrue(paddle.getX() >= 0);
    }

    @Test
    @DisplayName("Test di chuyển nhiều lần vẫn không vượt phải")
    public void testManyMovesRight() {
        paddle.setMovingRight(true);
        for (int i = 0; i < 200; i++) {
            paddle.update();
        }
        assertTrue(paddle.getX() + paddle.getWidth() <= GAME_WIDTH);
    }

    @Test
    @DisplayName("Test dừng di chuyển trái")
    public void testStopLeft() {
        paddle.setMovingLeft(true);
        paddle.update();
        paddle.setMovingLeft(false);
        double x = paddle.getX();
        paddle.update();
        assertEquals(x, paddle.getX());
    }

    @Test
    @DisplayName("Test dừng di chuyển phải")
    public void testStopRight() {
        paddle.setMovingRight(true);
        paddle.update();
        paddle.setMovingRight(false);
        double x = paddle.getX();
        paddle.update();
        assertEquals(x, paddle.getX());
    }

    @Test
    @DisplayName("Test tăng width (power-up)")
    public void testIncreaseWidth() {
        paddle.setWidth(150);
        assertEquals(150, paddle.getWidth());
    }

    @Test
    @DisplayName("Test giảm width")
    public void testDecreaseWidth() {
        paddle.setWidth(50);
        assertEquals(50, paddle.getWidth());
    }

    @Test
    @DisplayName("Test thay đổi width giữ nguyên tâm paddle")
    public void testWidthKeepsCenter() {
        paddle.setX(400);
        double center = paddle.getX() + paddle.getWidth() / 2; // 400 + 50 = 450
        paddle.setWidth(200);
        double newCenter = paddle.getX() + paddle.getWidth() / 2;
        assertEquals(center, newCenter, 0.01);
    }

    @Test
    @DisplayName("Test thay đổi height")
    public void testChangeHeight() {
        paddle.setHeight(50);

        assertEquals(50, paddle.getHeight());
    }

    @Test
    @DisplayName("Test paddle speed = 3")
    public void testSpeed() {
        assertEquals(3, Paddle.PADDLE_SPEED);
    }

    @Test
    @DisplayName("Test getters")
    public void testGetters() {
        assertEquals(400, paddle.getX());
        assertEquals(600, paddle.getY());
        assertEquals(100, paddle.getWidth());
        assertEquals(30, paddle.getHeight());
    }
}