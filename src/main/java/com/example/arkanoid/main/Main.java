package com.example.arkanoid.main;

import com.example.arkanoid.controllers.GameController;
import com.example.arkanoid.models.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;
    GameManager gameManager = new GameManager(WIDTH, HEIGHT);


    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        GameController gameController = new GameController(gc);

        primaryStage.setTitle("Arkanoid");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameController.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}