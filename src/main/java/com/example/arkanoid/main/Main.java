package com.example.arkanoid.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {

    public static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.arkanoid/main/MenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            primaryStage.setTitle("Arkanoid Menu");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
