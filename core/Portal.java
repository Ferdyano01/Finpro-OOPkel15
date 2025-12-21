package com.Ferdyano.frontend.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Portal {
    private final Vector2 position;
    private final TextureRegion frameRegion;

    // Hapus Animation, ganti dengan TextureRegion tunggal untuk efek
    private final TextureRegion effectRegion;

    private float scaleTimer;

    // Constructor diubah: effectTexture bukan lagi spritesheet, tapi gambar tunggal
    public Portal(Texture frameTexture, Texture effectTexture, float x, float y) {
        this.position = new Vector2(x, y);
        this.frameRegion = new TextureRegion(frameTexture);

        // Gunakan seluruh gambar effectTexture sebagai satu region statis
        this.effectRegion = new TextureRegion(effectTexture);
    }

    public void update(float delta) {
        // Timer ini digunakan untuk menghitung skala denyutan
        scaleTimer += delta;
    }

    public TextureRegion getEffectRegion() {
        // Cukup kembalikan region gambar statis tadi
        return effectRegion;
    }

    public TextureRegion getFrameTexture() {
        return frameRegion;
    }

    // Fungsi ini yang membuat efek "berdenyut" dengan mengubah nilai skala
    public float getCurrentScale() {
        // Skala berayun antara 0.9 (mengecil) dan 1.1 (membesar)
        return 1.0f + (float)Math.sin(scaleTimer * 5f) * 0.1f;
    }

    public Vector2 getPosition() {
        return position;
    }
}
