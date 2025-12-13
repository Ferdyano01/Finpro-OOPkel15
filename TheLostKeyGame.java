package com.Ferdyano.frontend;

import com.Ferdyano.frontend.Screen.LoadingScreen; // Import LoadingScreen
import com.badlogic.gdx.Game; // PENTING: Mengimplementasikan Game untuk manajemen Screen
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** * TheLostKeyGame: Kelas utama game yang mengimplementasikan LibGDX Game.
 * Ini adalah Entry Point yang akan dipanggil oleh Desktop Launcher.
 */
public class TheLostKeyGame extends Game {

    private SpriteBatch batch;
    private AssetManager assetManager;
    private Skin gameSkin;

    // --- Lifecycle Methods ---

    @Override
    public void create() {
        // 1. Inisialisasi Global Resources
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        // 2. Mulai Game
        // Mengatur layar pertama ke LoadingScreen
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        // PENTING: memanggil super.render() yang akan memanggil render() pada Screen aktif
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetManager.dispose();
        if (gameSkin != null) {
            gameSkin.dispose();
        }
    }

    // --- Getters untuk diakses oleh kelas Screen lain ---

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Skin getSkin() {
        return gameSkin;
    }

    public void setSkin(Skin skin) {
        this.gameSkin = skin;
    }
}
