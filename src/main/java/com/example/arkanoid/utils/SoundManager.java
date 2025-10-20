package com.example.arkanoid.utils;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {
    private static final List<AudioClip> brickBreakPool = new ArrayList<>();
    private static final List<AudioClip> brickHitPool = new ArrayList<>();
    private static final List<AudioClip> paddleHitPool = new ArrayList<>();

    private static final int POOL_SIZE = 5;

    static {
        initializeSoundPool();
    }

    private static void initializeSoundPool() {
        preloadAllSounds();
    }

    private static void preloadAllSounds() {
        preloadSoundGroup(brickBreakPool, "/sounds/brick_break.wav");
        preloadSoundGroup(brickHitPool, "/sounds/brick_hit.wav");
        preloadSoundGroup(paddleHitPool, "/sounds/paddle_hit.wav");
    }

    private static void preloadSoundGroup(List<AudioClip> pool, String path) {
        for (int i = 0; i < POOL_SIZE; i++) {
            AudioClip clip = createPreloadedSound(path);
            if (clip != null) {
                pool.add(clip);
            }
        }
    }

    private static AudioClip createPreloadedSound(String path) {
        try {
            String resourcePath = SoundManager.class.getResource(path).toExternalForm();
            AudioClip clip = new AudioClip(resourcePath);


            clip.setVolume(0.001);
            clip.play();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            clip.stop();
            clip.setVolume(1.0);
            return clip;

        } catch (Exception e) {
            return null;
        }
    }

    public static void playBrickBreak() {
        playInstant(brickBreakPool);
    }

    public static void playBrickHit() {
        playInstant(brickHitPool);
    }

    public static void playPaddleHit() {
        playInstant(paddleHitPool);
    }

    private static void playInstant(List<AudioClip> pool) {
        for (AudioClip clip : pool) {
            if (clip != null) {
                clip.play();
                return;
            }
        }

        String path = getPathFromPool(pool);
        if (path != null) {
            AudioClip emergencyClip = createEmergencySound(path);
            if (emergencyClip != null) {
                emergencyClip.play();
                pool.add(emergencyClip);
            }
        }
    }

    private static String getPathFromPool(List<AudioClip> pool) {
        if (pool == brickBreakPool) return "/sounds/brick_break.wav";
        if (pool == brickHitPool) return "/sounds/brick_hit.wav";
        if (pool == paddleHitPool) return "/sounds/paddle_hit.wav";
        return null;
    }

    private static AudioClip createEmergencySound(String path) {
        try {
            String resourcePath = SoundManager.class.getResource(path).toExternalForm();
            return new AudioClip(resourcePath);
        } catch (Exception e) {
            return null;
        }
    }


    public static void testAllSounds() {
        System.out.println("🔊 Testing all sounds...");
        new Thread(() -> {
            try {
                Thread.sleep(100);
                playBrickHit();
                Thread.sleep(200);
                playPaddleHit();
                Thread.sleep(200);
                playBrickBreak();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}