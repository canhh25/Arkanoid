package com.example.arkanoid.models;

import java.util.List;

public class GameManager {
    public static GameManager instance;
    private Ball ball;
    private List<Brick> bricks;
    private int lives;
    private int score;
    private String gameState;

    private GameManager() {
        lives = 3;
        score = 0;
    }
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    public void startGame(){
    }

    public void updateGame(){}

    public void handleInput(){}

    public void checkCollisions(){}

    public void gameOver() {}
}
