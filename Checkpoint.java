package com.Ferdyano.frontend.core;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Checkpoint {

    private final Vector2 position;
    private final TextureRegion textureRegion;
    private final float width;
    private final float height;
    private boolean isActivated;

    // Asumsi ukuran checkpoint 32x32 (Anda dapat menyesuaikannya)
    private static final float CP_WIDTH = 64f;
    private static final float CP_HEIGHT = 64f;

    public Checkpoint(Texture texture, float x, float y) {
        this.position = new Vector2(x, y);
        this.width = CP_WIDTH;
        this.height = CP_HEIGHT;
        this.isActivated = false;

        // Memotong frame pertama 32x32 dari texture sheet
        this.textureRegion = new TextureRegion(texture, 0, 0, (int)CP_WIDTH, (int)CP_HEIGHT);
    }

    public Vector2 getPosition() {
        return position;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void activate() {
        this.isActivated = true;
    }
}
