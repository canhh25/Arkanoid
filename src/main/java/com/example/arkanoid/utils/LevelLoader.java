package com.example.arkanoid.utils;

import com.example.arkanoid.models.Brick;

import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private static final List<int[][]> LEVELS = new ArrayList<>();

    private static final int Y_OFFSET = 80;

    static {
        int[][] level1 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 1, 2, 2, 1, 1, 2, 2, 2, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0}
        };
        LEVELS.add(level1);

        int[][] level2 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0},
                {0, 1, 2, 3, 3, 3, 3, 3, 2, 1, 0},
                {0, 1, 2, 2, 3, 3, 3, 2, 2, 1, 0},
                {0, 0, 1, 1, 2, 2, 2, 1, 1, 0, 0}
        };
        LEVELS.add(level2);

        int[][] level3 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 3, 3, 3, 2, 2, 0, 0},
                {0, 2, 3, 3, 3, 4, 3, 3, 3, 2, 0},
                {0, 2, 2, 3, 3, 4, 3, 3, 2, 2, 0},
                {0, 0, 2, 2, 3, 3, 3, 2, 2, 0, 0}
        };
        LEVELS.add(level3);

        int[][] level4 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 2, 0, 3, 0, 2, 0, 1, 0},
                {0, 2, 2, 3, 2, 4, 2, 3, 2, 2, 0},
                {0, 1, 0, 3, 0, 4, 0, 3, 0, 1, 0},
                {0, 2, 0, 3, 0, 4, 0, 3, 0, 2, 0}
        };
        LEVELS.add(level4);

        int[][] level5 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 2, 3, 4, 3, 2, 1, 0, 0},
                {0, 1, 2, 3, 4, 4, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 4, 4, 3, 2, 1, 0},
                {0, 0, 1, 2, 3, 4, 3, 2, 1, 0, 0}
        };
        LEVELS.add(level5);

        int[][] level6 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 2, 1, 1, 1, 2, 3, 0, 0},
                {0, 1, 2, 3, 3, 3, 3, 3, 2, 1, 0},
                {0, 1, 2, 3, 3, 3, 3, 3, 2, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {4, 4, 4, 4, 1, 0, 1, 4, 4, 4, 4}
        };
        LEVELS.add(level6);

        int[][] level7 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0},
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0},
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1}
        };
        LEVELS.add(level7);

        int[][] level8 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 3, 0, 4, 0, 3, 0, 2, 0},
                {0, 3, 4, 3, 4, 4, 4, 3, 4, 3, 0},
                {0, 2, 3, 4, 4, 4, 4, 4, 3, 2, 0},
                {0, 1, 2, 3, 4, 4, 4, 3, 2, 1, 0},
                {0, 2, 3, 4, 4, 4, 4, 4, 3, 2, 0}
        };
        LEVELS.add(level8);

        int[][] level9 = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 3, 1, 1, 1, 1, 1, 3, 2, 0},
                {0, 3, 4, 4, 2, 2, 2, 4, 4, 3, 0},
                {0, 3, 4, 4, 3, 3, 3, 4, 4, 3, 0},
                {0, 3, 4, 4, 4, 4, 4, 4, 4, 3, 0},
                {0, 2, 3, 4, 4, 4, 4, 4, 3, 2, 0}
        };
        LEVELS.add(level9);

        int[][] level10 = {
                {3, 2, 1, 3, 2, 0, 0, 0, 0, 0, 0},
                {3, 2, 1, 3, 2, 1, 0, 0, 0, 0, 0},
                {3, 2, 1, 3, 2, 1, 3, 0, 0, 0, 0},
                {3, 2, 1, 3, 2, 1, 3, 0, 0, 0, 0},
                {3, 2, 1, 3, 2, 1, 3, 2, 0, 0, 0},
                {3, 2, 1, 3, 2, 1, 3, 2, 1, 0, 0},
                {3, 2, 1, 3, 2, 1, 3, 2, 1, 3, 0},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1}
        };
        LEVELS.add(level10);
    }

    public static List<Brick> loadLevel(int level) {
        if (level < 1 || level > LEVELS.size()) {
            throw new IllegalArgumentException("Level out of range: " + level);
        }
        List<Brick> bricks = new ArrayList<>();
        int[][] grid = LEVELS.get(level - 1);
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int cell = grid[row][col];
                if (cell <= 0) continue;
                double x = col * Brick.BRICK_WIDTH;

                double y = row * Brick.BRICK_HEIGHT + Y_OFFSET;

                String imagePath;
                String crackedImagePath;
                switch (cell) {
                    case 1 -> {
                        imagePath = "/images/brick/brick_green.png";
                        crackedImagePath = "/images/brick/brick_green_cracked.png";
                    }
                    case 2 -> {
                        imagePath = "/images/brick/brick_blue.png";
                        crackedImagePath = "/images/brick/brick_blue_cracked.png";
                    }
                    case 3 -> {
                        imagePath = "/images/brick/brick_yellow.png";
                        crackedImagePath = "/images/brick/brick_yellow_cracked.png";
                    }
                    case 4 -> {
                        imagePath = "/images/brick/brick_red.png";
                        crackedImagePath = "/images/brick/brick_red_cracked.png";
                    }
                    default -> {
                        imagePath = "/images/brick/brick_green.png";
                        crackedImagePath = "/images/brick/brick_green_cracked.png";
                    }
                }

                bricks.add(new Brick(x, y, cell, cell, imagePath, crackedImagePath));
            }
        }
        return bricks;
    }
}
