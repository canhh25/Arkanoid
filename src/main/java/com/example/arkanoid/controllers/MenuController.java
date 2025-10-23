package com.example.arkanoid.controllers;

import com.example.arkanoid.main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MenuController {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    public void openGame(Stage stage) {
        try {

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameController gameController = new GameController(gc);

            stage.setTitle("Arkanoid");
            stage.setScene(scene);
            stage.show();

            gameController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button btnStart, btnHelp, btnEsc, btnSound, btnBack;

    @FXML
    private void handleStart(ActionEvent event) {
        Stage stage = (Stage) btnStart.getScene().getWindow();
        openGame(stage);
    }


    @FXML
    private void handleHelp(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/arkanoid/views/HelpView.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Instructions");
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    private void handleEsc(ActionEvent event) {
        Stage stage = (Stage) btnEsc.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSound(ActionEvent event) {

    }

    @FXML
    private void handleBack(ActionEvent event) {
        Stage stage = (Stage) btnHelp.getScene().getWindow();
        openGame(stage);
    }

}
