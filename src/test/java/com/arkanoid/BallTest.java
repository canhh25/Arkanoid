package com.arkanoid;

import com.example.arkanoid.models.Ball;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    private Ball ball;

    @BeforeEach
    public void setup() {
        ball = new Ball(100, 200);
    }

    @Test
    @DisplayName("Test khởi tạo Ball với tọa độ")
    public void testBallInitWithCoordinates() {
        Ball testBall = new Ball(150, 250);
        assertEquals(150, testBall.getX(), "X phải = 150");
        assertEquals(250, testBall.getY(), "Y phải = 250");
        assertEquals(Ball.BALL_WIDTH, testBall.getWidth(), "Width phải = BALL_WIDTH");
        assertEquals(Ball.BALL_HEIGHT, testBall.getHeight(), "Height phải = BALL_HEIGHT");
    }

    @Test
    @DisplayName("Test khởi tạo Ball với speed tùy chỉnh")
    public void testBallInitWithCustomSpeed() {
        Ball testBall = new Ball(100, 200, 5.0);
        assertEquals(5.0, testBall.getSpeed(), "Speed phải = 5.0");
    }

    @Test
    @DisplayName("Test khởi tạo Ball với speed mặc định")
    public void testBallInitWithDefaultSpeed() {
        assertEquals(3.0, ball.getSpeed(), "Speed mặc định phải = 3.0");
    }

    @Test
    @DisplayName("Test dx dy ban đầu = 0")
    public void testInitialVelocityIsZero() {
        assertEquals(0, ball.getDx(), "dx ban đầu = 0");
        assertEquals(0, ball.getDy(), "dy ban đầu = 0");
    }

    @Test
    @DisplayName("Test BALL_WIDTH constant")
    public void testBallWidthConstant() {
        assertEquals(14, Ball.BALL_WIDTH, "BALL_WIDTH = 14");
    }

    @Test
    @DisplayName("Test BALL_HEIGHT constant")
    public void testBallHeightConstant() {
        assertEquals(14, Ball.BALL_HEIGHT, "BALL_HEIGHT = 14");
    }

    @Test
    @DisplayName("Test setSpeed thay đổi speed")
    public void testSetSpeed() {
        ball.setSpeed(7.5);
        assertEquals(7.5, ball.getSpeed(), "Speed phải = 7.5");
    }

    @Test
    @DisplayName("Test launchByAngle với góc 0 độ")
    public void testLaunchByAngle0Degrees() {
        ball.launchByAngle(0);
        assertEquals(3.0, ball.getDx(), 0.01, "dx phải = speed");
        assertEquals(0, ball.getDy(), 0.01, "dy phải = 0");
    }

    @Test
    @DisplayName("Test launchByAngle với góc 90 độ")
    public void testLaunchByAngle90Degrees() {
        ball.launchByAngle(Math.PI / 2);
        assertEquals(0, ball.getDx(), 0.01, "dx phải = 0");
        assertEquals(3.0, ball.getDy(), 0.01, "dy phải = speed");
    }

    @Test
    @DisplayName("Test launchByAngle với góc 180 độ")
    public void testLaunchByAngle180Degrees() {
        ball.launchByAngle(Math.PI);
        assertEquals(-3.0, ball.getDx(), 0.01, "dx phải = -speed");
        assertEquals(0, ball.getDy(), 0.01, "dy phải = 0");
    }

    @Test
    @DisplayName("Test launchByAngle với góc 270 độ")
    public void testLaunchByAngle270Degrees() {
        ball.launchByAngle(3 * Math.PI / 2);
        assertEquals(0, ball.getDx(), 0.01, "dx phải = 0");
        assertEquals(-3.0, ball.getDy(), 0.01, "dy phải = -speed");
    }

    @Test
    @DisplayName("Test launchByAngle với góc 45 độ")
    public void testLaunchByAngle45Degrees() {
        ball.launchByAngle(Math.PI / 4);
        double expected = 3.0 / Math.sqrt(2);
        assertEquals(expected, ball.getDx(), 0.01, "dx phải = speed/sqrt(2)");
        assertEquals(expected, ball.getDy(), 0.01, "dy phải = speed/sqrt(2)");
    }

    @Test
    @DisplayName("Test launchByAngle magnitude = speed")
    public void testLaunchByAngleMaintainsSpeed() {
        ball.launchByAngle(Math.PI / 3);
        double magnitude = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());
        assertEquals(3.0, magnitude, 0.01, "Magnitude phải = speed");
    }

    @Test
    @DisplayName("Test setDx thay đổi dx")
    public void testSetDx() {
        ball.setDx(4.5);
        assertEquals(4.5, ball.getDx(), "dx phải = 4.5");
    }

    @Test
    @DisplayName("Test setDy thay đổi dy")
    public void testSetDy() {
        ball.setDy(-2.5);
        assertEquals(-2.5, ball.getDy(), "dy phải = -2.5");
    }

    @Test
    @DisplayName("Test update lưu vị trí trước đó")
    public void testUpdateSavesPreviousPosition() {
        ball.setDx(2);
        ball.setDy(3);
        double initialX = ball.getX();
        double initialY = ball.getY();
        ball.update();
        assertEquals(initialX, ball.getPrevX(), "prevX phải = vị trí cũ");
        assertEquals(initialY, ball.getPrevY(), "prevY phải = vị trí cũ");
    }

    @Test
    @DisplayName("Test update di chuyển ball")
    public void testUpdateMovesPosition() {
        ball.setDx(2);
        ball.setDy(3);
        double initialX = ball.getX();
        double initialY = ball.getY();
        ball.update();
        assertEquals(initialX + 2, ball.getX(), "X phải tăng dx");
        assertEquals(initialY + 3, ball.getY(), "Y phải tăng dy");
    }

    @Test
    @DisplayName("Test update nhiều lần")
    public void testUpdateMultipleTimes() {
        ball.setDx(1);
        ball.setDy(1);
        double initialX = ball.getX();
        double initialY = ball.getY();
        for (int i = 0; i < 10; i++) {
            ball.update();
        }
        assertEquals(initialX + 10, ball.getX(), "X phải tăng 10");
        assertEquals(initialY + 10, ball.getY(), "Y phải tăng 10");
    }

    @Test
    @DisplayName("Test setPrevX thay đổi prevX")
    public void testSetPrevX() {
        ball.setPrevX(50);
        assertEquals(50, ball.getPrevX(), "prevX phải = 50");
    }

    @Test
    @DisplayName("Test setPrevY thay đổi prevY")
    public void testSetPrevY() {
        ball.setPrevY(75);
        assertEquals(75, ball.getPrevY(), "prevY phải = 75");
    }

    @Test
    @DisplayName("Test reverseX đảo chiều dx")
    public void testReverseX() {
        ball.setDx(3);
        ball.reverseX();
        assertEquals(-3, ball.getDx(), "dx phải đảo dấu");
    }

    @Test
    @DisplayName("Test reverseX với dx âm")
    public void testReverseXNegative() {
        ball.setDx(-2.5);
        ball.reverseX();
        assertEquals(2.5, ball.getDx(), "dx phải đảo dấu");
    }

    @Test
    @DisplayName("Test reverseX nhiều lần")
    public void testReverseXMultipleTimes() {
        ball.setDx(4);
        ball.reverseX();
        ball.reverseX();
        assertEquals(4, ball.getDx(), "dx phải về giá trị ban đầu");
    }

    @Test
    @DisplayName("Test reverseY đảo chiều dy")
    public void testReverseY() {
        ball.setDy(2);
        ball.reverseY();
        assertEquals(-2, ball.getDy(), "dy phải đảo dấu");
    }

    @Test
    @DisplayName("Test reverseY với dy âm")
    public void testReverseYNegative() {
        ball.setDy(-3.5);
        ball.reverseY();
        assertEquals(3.5, ball.getDy(), "dy phải đảo dấu");
    }

    @Test
    @DisplayName("Test reverseY nhiều lần")
    public void testReverseYMultipleTimes() {
        ball.setDy(5);
        ball.reverseY();
        ball.reverseY();
        assertEquals(5, ball.getDy(), "dy phải về giá trị ban đầu");
    }

    @Test
    @DisplayName("Test reverseX không ảnh hưởng dy")
    public void testReverseXDoesNotAffectDy() {
        ball.setDx(2);
        ball.setDy(3);
        ball.reverseX();
        assertEquals(3, ball.getDy(), "dy không đổi");
    }

    @Test
    @DisplayName("Test reverseY không ảnh hưởng dx")
    public void testReverseYDoesNotAffectDx() {
        ball.setDx(2);
        ball.setDy(3);
        ball.reverseY();
        assertEquals(2, ball.getDx(), "dx không đổi");
    }

    @Test
    @DisplayName("Test ball di chuyển theo đường chéo")
    public void testBallDiagonalMovement() {
        ball.setDx(3);
        ball.setDy(4);
        double initialX = ball.getX();
        double initialY = ball.getY();
        ball.update();
        assertEquals(initialX + 3, ball.getX(), "X phải tăng 3");
        assertEquals(initialY + 4, ball.getY(), "Y phải tăng 4");
    }

    @Test
    @DisplayName("Test ball đứng yên khi dx=dy=0")
    public void testBallStationary() {
        double initialX = ball.getX();
        double initialY = ball.getY();
        ball.update();
        assertEquals(initialX, ball.getX(), "X không đổi");
        assertEquals(initialY, ball.getY(), "Y không đổi");
    }

    @Test
    @DisplayName("Test launchByAngle với speed tùy chỉnh")
    public void testLaunchByAngleWithCustomSpeed() {
        Ball testBall = new Ball(100, 200, 10.0);
        testBall.launchByAngle(0);
        assertEquals(10.0, testBall.getDx(), 0.01, "dx phải = 10");
    }

    @Test
    @DisplayName("Test prevX prevY ban đầu = 0")
    public void testInitialPrevPosition() {
        Ball testBall = new Ball(100, 200);
        assertEquals(0, testBall.getPrevX(), "prevX ban đầu = 0");
        assertEquals(0, testBall.getPrevY(), "prevY ban đầu = 0");
    }
}