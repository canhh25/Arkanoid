package com.example.arkanoid.main;

import com.example.arkanoid.controllers.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {

    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/arkanoid/views/MenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            primaryStage.setTitle("Arkanoid Menu");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    public static void main(String[] args) {
        launch(args);
    }
}
