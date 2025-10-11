package com.example.arkanoid.models;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameObject {
    protected Image image;
    protected double x, y;
    protected double width, height;

    public GameObject(double x, double y, double width, double height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            this.image = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
            this.width = 0;
            this.height = 0;
        }
    }

    protected void setImage(String imagePath) {
        try {
            this.image = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}