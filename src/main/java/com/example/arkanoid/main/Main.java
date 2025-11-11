package com.example.arkanoid.main;

import com.example.arkanoid.controllers.GameFacade;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;

    @Override
    public void start(Stage primaryStage) {
        try {
            GameFacade navigationFacade = GameFacade.getInstance(primaryStage);

            primaryStage.setTitle("Arkanoid");
            primaryStage.setResizable(false);

            navigationFacade.navigateToMenu();
            primaryStage.show();

        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}