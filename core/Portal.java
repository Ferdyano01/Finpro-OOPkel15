package com.Ferdyano.frontend.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Portal {
    private final Vector2 position;

    // Hanya menyimpan satu gambar (region) saja
    private final TextureRegion region;

    private float scaleTimer;

    // Constructor diubah: Hanya menerima SATU texture (gambar utama portal)
    public Portal(Texture texture, float x, float y) {
        this.position = new Vector2(x, y);
        this.region = new TextureRegion(texture);
    }

    public void update(float delta) {
        scaleTimer += delta;
    }

    // Gunakan method ini untuk mengambil gambar portal di Screen
    public TextureRegion getTexture() {
        return region;
    }

    // Fungsi denyut diperhalus
    public float getCurrentScale() {
        // Pengali 0.05f membuat denyutan kecil (halus)
        // Skala hanya berubah antara 0.95 sampai 1.05
        return 1.0f + (float)Math.sin(scaleTimer * 4f) * 0.05f;
    }

    public Vector2 getPosition() {
        return position;
    }
}
