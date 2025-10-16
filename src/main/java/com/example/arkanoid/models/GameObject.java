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
        if (imagePath != null) {
            try {
                this.image = new Image(getClass().getResourceAsStream(imagePath));
            } catch (Exception e) {
                System.out.println("Cannot load image: " + imagePath);
                // KHÔNG set width, height = 0 vì sẽ ảnh hưởng đến collision detection
            }
        }
    }

    public void setImage(String imagePath) {
        try {
            this.image = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.out.println("Cannot load image: " + imagePath);
        }
    }

    // XÓA PHƯƠNG THỨC RENDER(GraphicsContext gc) Ở ĐÂY
    // Chỉ giữ phương thức abstract

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Image getImage() { return image; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    // CHỈ GIỮ LẠI PHƯƠNG THỨC ABSTRACT CHO JavaFX
    public abstract void render(GraphicsContext gc);
}