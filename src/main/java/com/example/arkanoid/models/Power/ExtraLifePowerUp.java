package com.example.arkanoid.models.Power;

import com.example.arkanoid.models.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class ExtraLifePowerUp extends PowerUp {
    public ExtraLifePowerUp(double x, double y) {
        super(x, y, 38, 19, "EXTRA_LIFE"); // Kích thước lớn hơn để nhìn rõ animation
    }

    @Override
    public void applyEffect(Paddle paddle) {
        this.isActive = true;
    }

    @Override
    public void removeEffect(Paddle paddle) {
        this.isActive = false;
    }

    @Override
    protected void renderFallback(GraphicsContext gc) {
        // Fallback nếu không load được ảnh animation
        gc.setFill(Color.RED);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());

        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 14));
        gc.fillText("+1", getX() + 12, getY() + 24);
    }


}