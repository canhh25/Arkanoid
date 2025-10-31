package com.example.arkanoid.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public interface Renderable {
    double getX();
    double getY();
    double getWidth();
    double getHeight();
    Image getImage();

    default void render(GraphicsContext gc) {
        Image img = getImage();
        if (img != null) {
            gc.drawImage(img, getX(), getY(), getWidth(), getHeight());
        }
    }
}
