package com.example.arkanoid.main;

import com.example.arkanoid.controllers.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/arkanoid/views/MenuView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 960, 640);
        primaryStage.setTitle("Arkanoid Menu");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
