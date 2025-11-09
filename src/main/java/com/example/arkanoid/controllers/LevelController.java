package com.example.arkanoid.controllers;

import com.example.arkanoid.models.GameManager;
import com.example.arkanoid.utils.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LevelController {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 640;

    @FXML private Button btnLevel1;
    @FXML private Button btnLevel2;
    @FXML private Button btnLevel3;
    @FXML private Button btnLevel4;
    @FXML private Button btnLevel5;
    @FXML private Button btnLevel6;
    @FXML private Button btnLevel7;
    @FXML private Button btnLevel8;
    @FXML private Button btnLevel9;
    @FXML private Button btnLevel10;
    @FXML private Button btnBack;

    private GameController gameController;
    private int currentLevel;
    private Button[] levelButtons;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    // THÊM METHOD PUBLIC NÀY ĐỂ REFRESH TỪ BÊN NGOÀI
    public void refreshLevelButtons() {
        setupLevelButtons();
        System.out.println("Refreshing level buttons. Unlocked level: " +
                GameManager.getInstance().getUnlockedLevel());
    }

    @FXML
    private void initialize() {
        // Khởi tạo mảng TRONG initialize(), SAU KHI các button đã được inject
        levelButtons = new Button[]{
                btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5,
                btnLevel6, btnLevel7, btnLevel8, btnLevel9, btnLevel10
        };

        setupLevelButtons();
    }

    private void setupLevelButtons() {
        GameManager gameManager = GameManager.getInstance();
        int unlockedLevel = gameManager.getUnlockedLevel();

        for (int i = 0; i < levelButtons.length; i++) {
            if (levelButtons[i] == null) {
                System.err.println("Button " + (i+1) + " is null!");
                continue;
            }

            int levelNumber = i + 1;
            if (levelNumber > unlockedLevel) {
                levelButtons[i].setDisable(true);
                levelButtons[i].setOpacity(0.3);
                levelButtons[i].setStyle(levelButtons[i].getStyle() + "-fx-cursor: default;");
            } else {
                levelButtons[i].setDisable(false);
                levelButtons[i].setOpacity(1.0);
                levelButtons[i].setStyle(levelButtons[i].getStyle() + "-fx-cursor: hand;");
            }
        }
    }

    //  Method cho việc chọn level từ MENU CHÍNH
    private void startLevel(int level, ActionEvent event) {
        try {
            System.out.println("Starting level " + level + " from menu...");

            SoundManager.playGameStart();
            SoundManager.stopBackgroundMusic();

            // LẤY STAGE HIỆN TẠI thay vì tạo mới
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            GameManager gameManager = GameManager.getInstance();
            gameManager.setupLevel(level);

            GameController gameController = new GameController(gc, level);

            currentStage.setTitle("Arkanoid - Level " + level);
            currentStage.setScene(scene);
            currentStage.setResizable(false);

            gameController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method mới cho việc chọn level từ PAUSE WINDOW
    private void startLevelFromPause(int level, ActionEvent event) {
        try {
            System.out.println("Starting level " + level + " from pause...");

            // 1. Đóng cửa sổ pause TRƯỚC
            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            // 2. Lấy game stage (cửa sổ chính)
            Stage gameStage = (Stage) pauseStage.getOwner();

            if (gameStage == null) {
                System.err.println(" Không tìm thấy game stage!");
                return;
            }

            // 3. Tạo canvas và scene mới TRÊN STAGE CŨ
            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            // 4. Setup level
            GameManager gameManager = GameManager.getInstance();
            gameManager.setupLevel(level);

            // 5. Tạo controller mới
            GameController newGameController = new GameController(gc, level);

            // 6. Set scene cho Stage CŨ (không tạo Stage mới!)
            gameStage.setTitle("Arkanoid - Level " + level);
            gameStage.setScene(scene);
            gameStage.setResizable(false);

            // 7. Start game
            newGameController.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  Method thông minh - tự động phát hiện đang ở menu hay pause
    private void handleLevelSelection(int level, ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        // Kiểm tra xem có owner không (pause window có owner là game stage)
        if (currentStage.getOwner() != null) {
            // Từ pause window
            startLevelFromPause(level, event);
        } else {
            // Từ menu chính
            startLevel(level, event);
        }
    }

    // ===== CÁC HANDLER CHO TỪNG LEVEL =====
    @FXML
    private void handleLevel1(ActionEvent event) { handleLevelSelection(1, event); }

    @FXML
    private void handleLevel2(ActionEvent event) { handleLevelSelection(2, event); }

    @FXML
    private void handleLevel3(ActionEvent event) { handleLevelSelection(3, event); }

    @FXML
    private void handleLevel4(ActionEvent event) { handleLevelSelection(4, event); }

    @FXML
    private void handleLevel5(ActionEvent event) { handleLevelSelection(5, event); }

    @FXML
    private void handleLevel6(ActionEvent event) { handleLevelSelection(6, event); }

    @FXML
    private void handleLevel7(ActionEvent event) { handleLevelSelection(7, event); }

    @FXML
    private void handleLevel8(ActionEvent event) { handleLevelSelection(8, event); }

    @FXML
    private void handleLevel9(ActionEvent event) { handleLevelSelection(9, event); }

    @FXML
    private void handleLevel10(ActionEvent event) { handleLevelSelection(10, event); }

    // ===== RESTART - ĐÃ FIX (RESET VỀ BAN ĐẦU) =====
    @FXML
    private void handleRestart(ActionEvent event) {
        try {
            System.out.println(" Restarting level " + currentLevel + "...");

            // 1. Đóng cửa sổ pause TRƯỚC
            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            // 2. Lấy game stage (cửa sổ chính)
            Stage gameStage = (Stage) pauseStage.getOwner();

            if (gameStage == null) {
                System.err.println(" Không tìm thấy game stage!");
                return;
            }

            // 3. RESET HOÀN TOÀN GameManager về trạng thái ban đầu của level
            GameManager gameManager = GameManager.getInstance();

            // Reset timer
            gameManager.resetTimer();

            // Reset trạng thái game về RUNNING
            gameManager.gameState = com.example.arkanoid.models.GameState.RUNNING;

            // Setup lại level từ đầu (load lại bricks, paddle, ball)
            gameManager.setupLevel(currentLevel);

            System.out.println(" GameManager reset: Lives=" + gameManager.getLives() +
                    ", Score=" + gameManager.getScore() +
                    ", State=" + gameManager.gameState);

            // 4. Tạo canvas và scene mới TRÊN STAGE CŨ
            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane(canvas);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            // 5. Tạo controller MỚI
            GameController newGameController = new GameController(gc, currentLevel);

            // 6. Set scene cho Stage CŨ
            gameStage.setTitle("Arkanoid - Level " + currentLevel);
            gameStage.setScene(scene);
            gameStage.setResizable(false);

            // 7. START game mới
            newGameController.start();

            System.out.println(" Game started successfully!");

        } catch (Exception e) {
            System.err.println(" Lỗi khi restart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== RESUME =====
    @FXML
    private void handleResume(ActionEvent event) {
        try {
            // Đóng cửa sổ pause
            Stage pauseStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            pauseStage.close();

            // Resume game
            if (gameController != null) {
                gameController.resumeGame();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== BACK TO MENU =====
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Quay lại màn hình menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.arkanoid/main/MenuView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Arkanoid Menu");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}