package com.example.arkanoid.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particle {
    private double x, y;
    private double dx, dy;
    private double size;
    private double lifeTime;
    private double maxLifeTime;
    private Color color;
    private double alpha;

    public Particle(double x, double y, double dx, double dy, double size, double lifeTime, Color color) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.lifeTime = lifeTime;
        this.maxLifeTime = lifeTime;
        this.color = color;
        this.alpha = 1.0;
    }

    public void update() {
        x += dx;
        y += dy;

        // Giảm tốc độ dần
        dx *= 0.95;
        dy *= 0.95;

        // Giảm kích thước
        size *= 0.97;

        // Giảm thời gian sống
        lifeTime -= 16.67; // ~60 FPS

        // Giảm độ trong suốt
        alpha = lifeTime / maxLifeTime;
    }

    public void render(GraphicsContext gc) {
        gc.setGlobalAlpha(alpha);
        gc.setFill(color);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.setGlobalAlpha(1.0);
    }

    public boolean isAlive() {
        return lifeTime > 0 && size > 0.5;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}