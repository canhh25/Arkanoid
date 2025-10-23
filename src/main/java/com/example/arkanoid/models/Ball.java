package com.example.arkanoid.models;

public class Ball extends MovableObject {
    public double dx, dy;
    private double prevX, prevY;
    private double speed;
    public static final double BALL_WIDTH = 14;
    public static final double BALL_HEIGHT = 14;

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

    public Ball(double x, double y, double speed) {
        super(x, y, BALL_WIDTH, BALL_HEIGHT, "/images/ball/ball.png");

        // Vận tốc ban đầu
        this.dx = 0;
        this.dy = 0;
        this.speed = speed;
    }

    public void setSpeed(double s) {
        this.speed = s;
        renormalize();
    }

    public void launchByAngle(double angleRad) {
        dx = Math.cos(angleRad) * speed;
        dy = Math.sin(angleRad) * speed;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public void render(GraphicsContext gc) {
        // Vẽ brick bằng ảnh từ GameObject - giữ nguyên chức năng hiện có
        if (getImage() != null) {
            gc.drawImage(getImage(), getX(), getY(), getWidth(), getHeight());
        }
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
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }

    public void renormalize() {
        double v = Math.hypot(dx, dy);
        if (v == 0) return;
        double k = speed / v;
        dx *= k;
        dy *= k;
    }
}
