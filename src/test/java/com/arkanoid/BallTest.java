package com.arkanoid;
import com.example.arkanoid.models.Ball;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
public class BallTest {
    private Ball ball;

    @BeforeEach
    public void setup() {
        ball = new Ball(100, 100, 10);
    }

    @Test
    @DisplayName("Test init")
    public void testBallInit() {
        assertEquals(100, ball.getX());
        assertEquals(100, ball.getY());
    }

    @Test
    @DisplayName("Test render")
    public void testRenderImage() {

    }

    @Test
    @DisplayName("Test move")
    public void testMove() {

    }

    @Test
    @DisplayName("Test reset")
    public void testReset() {}
}
