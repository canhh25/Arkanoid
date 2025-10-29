package com.example.arkanoid.utils;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {
    private static final List<AudioClip> brickBreakPool = new ArrayList<>();
    private static final List<AudioClip> brickHitPool = new ArrayList<>();
    private static final List<AudioClip> paddleHitPool = new ArrayList<>();

    private static final int POOL_SIZE = 5;

    // Thêm MediaPlayer cho nhạc nền
    private static MediaPlayer backgroundMusicPlayer;
    private static boolean isMuted = false;

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
        if (!isMuted) playInstant(brickBreakPool);
    }

    public static void playBrickHit() {
        if (!isMuted) playInstant(brickHitPool);
    }

    public static void playPaddleHit() {
        if (!isMuted) playInstant(paddleHitPool);
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

    // ===== PHƯƠNG THỨC MỚI CHO NHẠC NỀN =====

    /**
     * Phát nhạc nền (loop)
     * @param musicPath Đường dẫn file nhạc (ví dụ: "/sounds/menu_music.mp3")
     */
    public static void playBackgroundMusic(String musicPath) {
        try {
            stopBackgroundMusic(); // Dừng nhạc cũ nếu có

            String resourcePath = SoundManager.class.getResource(musicPath).toExternalForm();
            Media media = new Media(resourcePath);
            backgroundMusicPlayer = new MediaPlayer(media);

            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô hạn
            backgroundMusicPlayer.setVolume(isMuted ? 0.0 : 0.3); // Volume 30%
            backgroundMusicPlayer.play();

        } catch (Exception e) {
            System.err.println("Không thể phát nhạc nền: " + musicPath);
            e.printStackTrace();
        }
    }

    /**
     * Dừng nhạc nền
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.dispose();
            backgroundMusicPlayer = null;
        }
    }

    /**
     * Tạm dừng nhạc nền
     */
    public static void pauseBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }

    /**
     * Tiếp tục phát nhạc nền
     */
    public static void resumeBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    /**
     * Bật/tắt tất cả âm thanh
     */
    public static void setMuted(boolean muted) {
        isMuted = muted;
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(muted ? 0.0 : 0.3);
        }
    }

    /**
     * Kiểm tra trạng thái mute
     */
    public static boolean isMuted() {
        return isMuted;
    }

    /**
     * Điều chỉnh âm lượng nhạc nền (0.0 - 1.0)
     */
    public static void setBackgroundVolume(double volume) {
        if (backgroundMusicPlayer != null && !isMuted) {
            backgroundMusicPlayer.setVolume(Math.max(0.0, Math.min(1.0, volume)));
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