package com.example.arkanoid.models;

import com.example.arkanoid.utils.Particle;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ball extends MovableObject {
    public double dx, dy;
    private double prevX, prevY;
    private double speed;
    public static final double BALL_WIDTH = 14;
    public static final double BALL_HEIGHT = 14;
    private int collisionCooldown = 0;

    private List<Particle> particles;
    private boolean fireEffectEnabled = true;
    private int particleSpawnRate = 2; // Số particle spawn mỗi frame

    public Ball(double x, double y, double speed) {
        super(x, y, BALL_WIDTH, BALL_HEIGHT, "/images/ball/ball.png");
        this.dx = 0;
        this.dy = 0;
        this.speed = speed;
        this.particles = new ArrayList<>();
    }

    public double getPrevX() {
        return prevX;
    }

    public void setPrevX(double prevX) {
        this.prevX = prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setPrevY(double prevY) {
        this.prevY = prevY;
    }

    public void setSpeed(double s) {
        this.speed = s;
    }

    public void launchByAngle(double angleRad) {
        dx = Math.cos(angleRad) * speed;
        dy = Math.sin(angleRad) * speed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    @Override
    public void update() {
        prevX = x;
        prevY = y;
        x += dx;
        y += dy;

        // THÊM: Update particles
        updateParticles();
    }

    private void updateParticles() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update();

            if (!particle.isAlive()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        renderParticles(gc);
        if (getImage() != null) {
            gc.drawImage(getImage(), getX(), getY(), getWidth(), getHeight());
        }
    }

    private void renderParticles(GraphicsContext gc) {
        for (Particle particle : particles) {
            particle.render(gc);
        }
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }
}