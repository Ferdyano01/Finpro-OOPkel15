package com.Ferdyano.frontend.core;

import com.Ferdyano.frontend.pattern.PlayerState;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Player: Entitas utama yang mewakili pemain.
 */
public class Player {

    // --- Konstanta Animasi ---
    private static final float FRAME_DURATION = 0.1f;
    private static final int FRAME_COUNT = 1;        // 8 frame per strip (sesuai aset Anda)

    // KOREKSI KRITIS: Kembali ke ukuran yang lebih kecil dan aman
    private final int FRAME_WIDTH = 60;
    private final int FRAME_HEIGHT = 60;

    // --- Fields Animasi ---
    private final Animation<TextureRegion> runDownAnimation;
    private final Animation<TextureRegion> runUpAnimation;
    private final Animation<TextureRegion> runLeftAnimation;
    private final Animation<TextureRegion> runRightAnimation;
    private TextureRegion idleFrame; // Frame statis untuk idle

    private PlayerState currentState;
    private float stateTime;
    private Vector2 position;
    private Vector2 velocity;
    private boolean isMoving = false;


    // Konstruktor harus menerima 4 sheets
    public Player(Texture runDownSheet, Texture runUpSheet, Texture runLeftSheet, Texture runRightSheet) {
        this.position = new Vector2(500, 300);
        this.velocity = new Vector2(0, 0);
        this.stateTime = 0f;

        // Gunakan frame pertama dari runDownSheet sebagai frame idle statis
        TextureRegion[][] tmpIdle = TextureRegion.split(runDownSheet, FRAME_WIDTH, FRAME_HEIGHT);
        this.idleFrame = tmpIdle[0][0];

        // INISIALISASI SEMUA ANIMASI PERGERAKAN
        this.runDownAnimation = createAnimation(runDownSheet, FRAME_WIDTH, FRAME_HEIGHT, FRAME_COUNT);
        this.runUpAnimation = createAnimation(runUpSheet, FRAME_WIDTH, FRAME_HEIGHT, FRAME_COUNT);
        this.runLeftAnimation = createAnimation(runLeftSheet, FRAME_WIDTH, FRAME_HEIGHT, FRAME_COUNT);
        this.runRightAnimation = createAnimation(runRightSheet, FRAME_WIDTH, FRAME_HEIGHT, FRAME_COUNT);

        System.out.println("Player created and animations initialized.");
    }

    /** * Metode Bantu: Membuat objek Animation dari Sprite Sheet. */
    private Animation<TextureRegion> createAnimation(Texture sheet, int frameWidth, int frameHeight, int totalFrames) {
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[totalFrames];

        // Memastikan kita hanya mengambil frame dari BARIS PERTAMA (index 0)
        for (int i = 0; i < totalFrames; i++) {
            frames[i] = tmp[0][i];
        }
        return new Animation<TextureRegion>(FRAME_DURATION, frames);
    }

    // --- LOGIKA UPDATE DAN ANIMASI ---

    public void update(float delta) {
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        stateTime += delta; // PENTING: StateTime harus diupdate untuk animasi

        if (currentState != null) {
            // Asumsi: currentState.update(this, delta) ada
        }
    }

    // KOREKSI KRITIS: Logika memilih animasi berdasarkan VELOCITY
    public TextureRegion getCurrentFrame() {
        if (isMoving) {
            // Prioritaskan Horizontal
            if (Math.abs(velocity.x) > 0) {
                if (velocity.x > 0) {
                    return runRightAnimation.getKeyFrame(stateTime, true);
                } else {
                    return runLeftAnimation.getKeyFrame(stateTime, true);
                }
            } else if (velocity.y != 0) {
                // Pergerakan Vertikal
                if (velocity.y > 0) {
                    return runUpAnimation.getKeyFrame(stateTime, true);
                } else {
                    return runDownAnimation.getKeyFrame(stateTime, true);
                }
            }
        }

        // Jika tidak bergerak, kembalikan frame idle statis
        return idleFrame;
    }


    // --- FUNGSI FISIKA & GETTERS (Diperlukan oleh GameScreen) ---
    public void setPosition(float x, float y) { this.position.set(x, y); }
    public void setVelocity(float dx, float dy) {
        this.velocity.set(dx, dy);
        this.isMoving = (dx != 0 || dy != 0);
    }
    public float getPositionX() { return position.x; }
    public float getPositionY() { return position.y; }
    public int getWidth() { return FRAME_WIDTH; } // Mengembalikan 16
    public int getHeight() { return FRAME_HEIGHT; } // Mengembalikan 16
    public float getMoveSpeed() { return 5.0f; }

    // ... (Metode setPlayerState, handleInput, dll. tetap sama) ...
}
