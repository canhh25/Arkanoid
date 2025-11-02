package com.example.arkanoid.models;

import com.example.arkanoid.utils.Particle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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

    public double getPrevX() { return prevX; }
    public void setPrevX(double prevX) { this.prevX = prevX; }
    public double getPrevY() { return prevY; }
    public void setPrevY(double prevY) { this.prevY = prevY; }

    public void setSpeed(double s) {
        this.speed = s;
    }

    public void launchByAngle(double angleRad) {
        dx = Math.cos(angleRad) * speed;
        dy = Math.sin(angleRad) * speed;
    }

    public double getSpeed() { return speed; }
    public double getDx() { return dx; }
    public void setDx(double dx) { this.dx = dx; }
    public double getDy() { return dy; }
    public void setDy(double dy) { this.dy = dy; }

    // THÊM: Toggle fire effect
    public void setFireEffectEnabled(boolean enabled) {
        this.fireEffectEnabled = enabled;
    }

    public boolean isFireEffectEnabled() {
        return fireEffectEnabled;

    }

    @Override
    public void update() {
        prevX = x;
        prevY = y;
        x += dx;
        y += dy;

        // THÊM: Spawn particles nếu bóng đang di chuyển
        if (fireEffectEnabled && (Math.abs(dx) > 0.1 || Math.abs(dy) > 0.1)) {
            spawnFireParticles();
        }

        // THÊM: Update particles
        updateParticles();
    }

    // THÊM: Spawn fire particles
    private void spawnFireParticles() {
        double ballCenterX = x + BALL_WIDTH / 2;
        double ballCenterY = y + BALL_HEIGHT / 2;

        for (int i = 0; i < particleSpawnRate; i++) {
            // Tính vị trí spawn phía sau bóng
            double angle = Math.atan2(dy, dx);
            double spawnDistance = BALL_WIDTH / 2;
            double spawnX = ballCenterX - Math.cos(angle) * spawnDistance;
            double spawnY = ballCenterY - Math.sin(angle) * spawnDistance;

            // Random offset để tạo hiệu ứng tự nhiên
            spawnX += (Math.random() - 0.5) * 4;
            spawnY += (Math.random() - 0.5) * 4;

            // Vận tốc particle ngược với hướng bóng
            double particleDx = -dx * 0.2 + (Math.random() - 0.5) * 1;
            double particleDy = -dy * 0.2 + (Math.random() - 0.5) * 1;

            // Kích thước và thời gian sống random
            double size = 3 + Math.random() * 5;
            double lifeTime = 300 + Math.random() * 200; // 300-500ms

            // Màu lửa: Chuyển từ vàng -> cam -> đỏ
            Color color = getRandomFireColor();

            particles.add(new Particle(spawnX, spawnY, particleDx, particleDy, size, lifeTime, color));
        }
    }

    private Color getRandomFireColor() {
        double rand = Math.random();
        if (rand < 0.3) {
            return Color.rgb(255, 255, 100);
        } else if (rand < 0.6) {
            return Color.rgb(255, 150, 50);
        } else if (rand < 0.9) {
            return Color.rgb(255, 50, 50);
        } else {
            return Color.rgb(150, 20, 20);
        }
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

    public void reverseX() { dx = -dx; }
    public void reverseY() { dy = -dy; }

    public void clearParticles() {
        particles.clear();
    }
}