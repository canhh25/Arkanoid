package com.example.arkanoid.controllers;

import com.example.arkanoid.utils.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MenuController {
    @FXML private Button btnStart, btnHelp, btnEsc, btnBack;
    @FXML private ImageView btnVolume;

    private boolean mute = false;
    private Image volumeSprite;
    private GameFacade navigationFacade;

    @FXML
    public void initialize() {
        volumeSprite = new Image(
                getClass().getResource("/images/menu/volume.png").toExternalForm()
        );
        btnVolume.setImage(volumeSprite);
        btnVolume.setViewport(new Rectangle2D(0, 0, 70, 70));

        SoundManager.playBackgroundMusic("/sounds/nen.mp3");
    }

    public void setNavigationFacade(GameFacade facade) {
        this.navigationFacade = facade;
    }

    private GameFacade getNavigationFacade() {
        if (navigationFacade == null && btnStart != null) {
            Stage stage = (Stage) btnStart.getScene().getWindow();
            navigationFacade = GameFacade.getInstance(stage);
        }
        return navigationFacade;
    }

    @FXML
    private void handleStart(ActionEvent event) {
        SoundManager.playGameStart();
        getNavigationFacade().navigateToGame(1);
    }

    @FXML
    private void handleHelp(ActionEvent event) {
        getNavigationFacade().showHelpDialog();
    }

    @FXML
    private void handleEsc(ActionEvent event) {
        getNavigationFacade().exitApplication();
    }

    @FXML
    private void toggleVolume() {
        mute = !mute;
        SoundManager.setMuted(mute);

        if (mute) {
            btnVolume.setViewport(new Rectangle2D(70, 0, 70, 70));
        } else {
            btnVolume.setViewport(new Rectangle2D(0, 0, 70, 70));
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void selectLevel(ActionEvent event) {
        getNavigationFacade().navigateToLevelSelect();
    }
}
