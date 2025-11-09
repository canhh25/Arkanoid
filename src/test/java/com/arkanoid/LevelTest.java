package com.arkanoid;

import com.example.arkanoid.models.Brick;
import com.example.arkanoid.utils.LevelLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class LevelTest {

    @Test
    @DisplayName("Test load level hợp lệ")
    public void testLoadValidLevel() {
        List<Brick> bricks = LevelLoader.loadLevel(1);
        assertNotNull(bricks);
        assertFalse(bricks.isEmpty());
    }

    @Test
    @DisplayName("Test load tất cả 10 level")
    public void testLoadAllLevels() {
        for (int i = 1; i <= 10; i++) {
            List<Brick> bricks = LevelLoader.loadLevel(i);
            assertNotNull(bricks, "Level " + i + " không được null");
            assertTrue(bricks.size() > 0, "Level " + i + " phải có brick");
        }
    }

    @Test
    @DisplayName("Test level không hợp lệ (quá nhỏ)")
    public void testLoadInvalidLevelTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> {
            LevelLoader.loadLevel(0);
        });
    }

    @Test
    @DisplayName("Test level không hợp lệ (quá lớn)")
    public void testLoadInvalidLevelTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            LevelLoader.loadLevel(11);
        });
    }

    @Test
    @DisplayName("Test vị trí brick có Y offset")
    public void testBrickPositionWithOffset() {
        List<Brick> bricks = LevelLoader.loadLevel(10);

        // Tìm brick đầu tiên (ở row 0)
        Brick firstBrick = bricks.stream()
                .filter(b -> b.getY() < 100)
                .findFirst()
                .orElse(null);

        assertNotNull(firstBrick);
        // Brick phải có Y >= 80 (Y_OFFSET)
        assertTrue(firstBrick.getY() >= 80, "Brick phải có Y offset >= 80");
    }

    @Test
    @DisplayName("Test số lượng brick trong level")
    public void testBrickCount() {
        List<Brick> level1 = LevelLoader.loadLevel(1);
        List<Brick> level10 = LevelLoader.loadLevel(10);

        assertTrue(level1.size() > 0, "Level 1 phải có brick");
        assertTrue(level10.size() > 0, "Level 10 phải có brick");

        // Level 10 thường có nhiều brick hơn level 1
        System.out.println("Level 1 có " + level1.size() + " brick");
        System.out.println("Level 10 có " + level10.size() + " brick");
    }

    @Test
    @DisplayName("Test level khác nhau có cấu trúc khác nhau")
    public void testDifferentLevelsHaveDifferentStructure() {
        List<Brick> level1 = LevelLoader.loadLevel(1);
        List<Brick> level2 = LevelLoader.loadLevel(2);

        // Số lượng brick có thể khác nhau
        // hoặc vị trí brick khác nhau
        boolean isDifferent = level1.size() != level2.size();

        if (!isDifferent) {
            // Kiểm tra vị trí brick
            for (int i = 0; i < Math.min(level1.size(), level2.size()); i++) {
                if (level1.get(i).getX() != level2.get(i).getX() ||
                        level1.get(i).getY() != level2.get(i).getY()) {
                    isDifferent = true;
                    break;
                }
            }
        }

        assertTrue(isDifferent, "Level 1 và Level 2 phải có cấu trúc khác nhau");
    }

    @Test
    @DisplayName("Test level trả về danh sách brick")
    public void testLevelReturnsList() {
        List<Brick> bricks = LevelLoader.loadLevel(5);

        assertNotNull(bricks, "Phải trả về list");
        assertTrue(bricks instanceof List, "Phải là List");
    }

    @Test
    @DisplayName("Test load cùng level nhiều lần")
    public void testLoadSameLevelMultipleTimes() {
        List<Brick> firstLoad = LevelLoader.loadLevel(3);
        List<Brick> secondLoad = LevelLoader.loadLevel(3);

        assertEquals(firstLoad.size(), secondLoad.size(),
                "Load cùng level phải có số brick giống nhau");
    }
}