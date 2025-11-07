package com.arkanoid;
import com.example.arkanoid.models.Brick;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
public class BrickTest {

    @Test
    @DisplayName("Test khởi tạo")
    public void testInit() {
        Brick brick = new Brick(100, 100, 5, 5, "", "");
        assertTrue(brick.hitPoints == brick.type);
    }

    @Test
    @DisplayName("Test di chuyển")
    public void testMove() {}

    @Test
    @DisplayName("Test va chạm ball")
    public void testBrickHit() {
        Brick brick1 = new Brick(100, 100, 2, 2, "", "");
        Brick brick2 = new Brick(100, 100, 4, 4, "", "");

        brick1.hit();
        assertTrue(brick1.hitPoints == 1);

        brick2.hit();
        assertFalse(brick2.hitPoints != 4);
    }

    @Test
    @DisplayName("Test bị phá vỡ")
    public void testBrickDestroyed() {
        Brick tempBrick = new Brick(100, 50, 1, 1 , " ", " ");

        assertFalse(tempBrick.isDestroyed());
        tempBrick.hit();
        assertTrue(tempBrick.isDestroyed());
    }


}
