package com.example.arkanoid.utils;

import com.example.arkanoid.models.Brick;

import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private static final List<int[][]> LEVELS = new ArrayList<>();

    static {
        int[][] level1 = {
                {0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 1, 2, 2, 1, 1, 2, 2, 1, 0},
                {1, 2, 2, 1, 3, 3, 1, 2, 2, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 1, 2, 2, 2, 2, 1, 0, 0},
                {0, 0, 0, 1, 3, 3, 1, 0, 0, 0}
        };
        LEVELS.add(level1);

        int[][] level2 = {
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 0, 2, 0, 2, 0, 2, 0, 3, 3},
                {3, 2, 2, 2, 2, 2, 2, 2, 2, 3},
                {3, 0, 2, 0, 0, 0, 0, 2, 0, 3},
                {3, 2, 2, 2, 1, 1, 2, 2, 2, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
        };
        LEVELS.add(level2);

        int[][] level3 = {
                {0, 0, 1, 1, 2, 2, 1, 1, 0, 0},
                {0, 1, 1, 2, 2, 2, 2, 1, 1, 0},
                {1, 1, 1, 1, 2, 2, 1, 1, 1, 1},
                {1, 2, 2, 1, 1, 1, 1, 2, 2, 1},
                {0, 1, 2, 2, 1, 1, 2, 2, 1, 0},
                {0, 0, 1, 2, 2, 2, 2, 1, 0, 0}
        };
        LEVELS.add(level3);

        int[][] level4 = {
                {1, 0, 2, 1, 3, 3, 1, 2, 0, 1},
                {1, 0, 2, 1, 3, 3, 1, 2, 0, 1},
                {2, 1, 2, 1, 1, 1, 1, 2, 1, 2},
                {2, 1, 2, 1, 1, 1, 1, 2, 1, 2},
                {1, 0, 2, 1, 3, 3, 1, 2, 0, 1},
                {1, 0, 2, 1, 3, 3, 1, 2, 0, 1}
        };
        LEVELS.add(level4);

// Level 5 – Viền 3, trong caro 1–2
        int[][] level5 = {
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
                {3, 1, 2, 1, 2, 1, 2, 1, 2, 3},
                {3, 2, 1, 2, 1, 2, 1, 2, 1, 3},
                {3, 1, 2, 1, 2, 1, 2, 1, 2, 3},
                {3, 2, 1, 2, 1, 2, 1, 2, 1, 3},
                {3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
        };
        LEVELS.add(level5);

// Level 6 – Kim cương 2, cột 3 chắn bóng
        int[][] level6 = {
                {0, 0, 0, 2, 2, 2, 2, 0, 0, 0},
                {0, 0, 2, 2, 1, 1, 2, 2, 0, 0},
                {0, 2, 2, 1, 3, 3, 1, 2, 2, 0},
                {2, 2, 1, 3, 3, 3, 3, 1, 2, 2},
                {0, 2, 2, 1, 3, 3, 1, 2, 2, 0},
                {0, 0, 2, 2, 1, 1, 2, 2, 0, 0}
        };
        LEVELS.add(level6);
    }

    public static List<Brick> loadLevel(int level) {
        List<Brick> bricks = new ArrayList<>();
        int[][] levelAgain = LEVELS.get(level - 1);
        for (int row = 0; row < levelAgain.length; row++) {
            for (int col = 0; col < levelAgain[row].length; col++) {
                double x = col * (Brick.BRICK_WIDTH);
                double y = row * (Brick.BRICK_HEIGHT);
                String color = "";
                int randomColor = (int) (Math.random() * 1000);
                if(randomColor % 2 == 0) {
                    color = "red";
                } else if(randomColor % 3 == 0) {
                    color = "green";
                } else {
                    color = "blue";
                }

                String imagePath = "/images/brick/brick_" + color + ".png";
                String crackedImagePath = "/images/brick/brick_" + color + "_cracked.png";

                if(levelAgain[row][col] == 1) {
                    bricks.add(new Brick(x, y,
                            levelAgain[row][col],
                            1,
                            imagePath,
                            crackedImagePath));
                } else if(levelAgain[row][col] == 2) {
                    bricks.add(new Brick(x, y,
                            levelAgain[row][col],
                            2,
                            imagePath,
                            crackedImagePath));
                }
            }
        }
        return bricks;
    }
}