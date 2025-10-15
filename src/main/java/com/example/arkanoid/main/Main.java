package com.example.arkanoid.main;

import com.example.arkanoid.controllers.GameController;
import com.example.arkanoid.models.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 640;
    GameManager gameManager = new GameManager(SCREEN_WIDTH, SCREEN_HEIGHT);

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        GameController gameController = new GameController(gc);

        Image background = new Image(getClass().getResource("/images/background/stars_big_2.png").toExternalForm());

        gc.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        primaryStage.setTitle("Arkanoid");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameController.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}