package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;

public class ExtraLifePowerUp extends PowerUp<GameManager> {
    public int countLives = 2;
    public ExtraLifePowerUp(double x, double y) {
        super(x, y, 30, 19, "life");
    }

    @Override
    public void applyEffect(GameManager gameManager) {
        if(!isActive()) {
            gameManager.lives++;
        }
    }

    @Override
    public void removeEffect(GameManager gameManager) {
        if(this.getCountLives() > 0) {
            this.isActive = false;
            this.setCountLives(this.countLives - 1);
        }
    }

    public int getCountLives() {
        return countLives;
    }

    public void setCountLives(int countLives) {
        this.countLives = countLives;
    }
}