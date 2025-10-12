package com.example.arkanoid.models;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {
    private static final List<AudioClip> brickBreakPool = new ArrayList<>();
    private static final List<AudioClip> brickHitPool = new ArrayList<>();
    private static final List<AudioClip> paddleHitPool = new ArrayList<>();

    private static final int POOL_SIZE = 3; // Số lượng bản sao cho mỗi âm thanh

    static {
        initializeSoundPool();
    }

    private static void initializeSoundPool() {
        System.out.println("Initializing sound pool...");

        // Tạo nhiều instances cho mỗi âm thanh
        for (int i = 0; i < POOL_SIZE; i++) {
            brickBreakPool.add(createSound("/sounds/brick_break.wav"));
            brickHitPool.add(createSound("/sounds/brick_hit.wav"));
            paddleHitPool.add(createSound("/sounds/paddle_hit.wav"));
        }

        System.out.println("Sound pool ready! " + POOL_SIZE + " instances per sound");
    }

    private static AudioClip createSound(String path) {
        try {
            String resourcePath = SoundManager.class.getResource(path).toExternalForm();
            AudioClip clip = new AudioClip(resourcePath);

            // Preload by playing silently once
            clip.setVolume(0);
            clip.play();
            clip.stop();
            clip.setVolume(1.0);

            return clip;
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + path);
            return null;
        }
    }

    public static void playBrickBreak() {
        playFromPool(brickBreakPool, "brick_break");
    }

    public static void playBrickHit() {
        playFromPool(brickHitPool, "brick_hit");
    }

    public static void playPaddleHit() {
        playFromPool(paddleHitPool, "paddle_hit");
    }

    private static void playFromPool(List<AudioClip> pool, String soundName) {
        for (AudioClip clip : pool) {
            if (clip != null && !clip.isPlaying()) {
                clip.play();
                return;
            }
        }

        // Nếu tất cả đang play, chơi cái đầu tiên
        if (!pool.isEmpty() && pool.get(0) != null) {
            pool.get(0).play();
        }
    }
}