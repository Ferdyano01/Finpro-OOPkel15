package com.Ferdyano.frontend.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx; // Diperlukan untuk mengakses waktu (delta)

public class Portal {

    private final Vector2 position;
    private final float width;
    private final float height;

    private final TextureRegion frameRegion;
    private final TextureRegion effectRegion;

    // --- LOGIKA SCALING BARU ---
    private float time;
    private float currentScale;
    private static final float PULSE_SPEED = 2.0f;  // Kecepatan denyutan
    private static final float MIN_SCALE = 0.6f;    // Skala minimum efek (60% dari P_WIDTH)
    private static final float MAX_SCALE = 0.8f;    // Skala maksimum efek (80% dari P_WIDTH)
    // ----------------------------

    // KOREKSI 1: Gunakan 64x64 sebagai dimensi dasar portal
    private static final float P_WIDTH = 32f;
    private static final float P_HEIGHT = 32f;

    public Portal(Texture frameTexture, Texture effectTexture, float x, float y) {
        this.position = new Vector2(x, y);
        this.width = P_WIDTH;
        this.height = P_HEIGHT;

        // Inisialisasi variabel baru
        this.time = 0f;
        this.currentScale = MIN_SCALE;

        this.frameRegion = new TextureRegion(frameTexture, 0, 0, (int)P_WIDTH, (int)P_HEIGHT);
        this.effectRegion = new TextureRegion(effectTexture, 0, 0, (int)P_WIDTH, (int)P_HEIGHT);
    }

    public void update(float delta) {
        // KOREKSI 2: Logika Scaling (Berdenyut)
        time += delta * PULSE_SPEED;

        // Menggunakan fungsi sinus untuk mendapatkan nilai antara -1 dan 1
        float sinValue = (float)Math.sin(time);

        // Memetakan nilai sin ke rentang skala (MIN_SCALE hingga MAX_SCALE)
        float range = (MAX_SCALE - MIN_SCALE);
        currentScale = MIN_SCALE + (range * (sinValue + 1) / 2f);
    }

    // --- GETTERS KRITIS ---
    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    // --- GETTERS REGION & SCALE BARU ---
    public TextureRegion getFrameRegion() {
        return frameRegion;
    }

    public TextureRegion getEffectRegion() {
        return effectRegion;
    }

    // KOREKSI 3: Ganti getRotation() menjadi getCurrentScale()
    public float getCurrentScale() {
        return currentScale;
    }
}
