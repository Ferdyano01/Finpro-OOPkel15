package com.Ferdyano.frontend;

import com.Ferdyano.frontend.Screen.LoadingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music; // <--- IMPORT PENTING INI
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * TheLostKeyGame: Class utama yang mengatur alur pergantian Screen
 * dan menyimpan referensi global seperti SpriteBatch, AssetManager, dan Skin.
 */
public class TheLostKeyGame extends Game {

    private SpriteBatch batch;
    private AssetManager assetManager;
    private Skin gameSkin;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        // Langsung arahkan ke LoadingScreen untuk memuat aset
        this.setScreen(new LoadingScreen(this));
    }

    /**
     * DAFTAR ASET: Panggil method ini di LoadingScreen!
     */
    public void loadGameAssets() {
        // --- PLAYER & ANIMASI ---
        assetManager.load("run_down.png", Texture.class);
        assetManager.load("run_up.png", Texture.class);
        assetManager.load("run_left.png", Texture.class);
        assetManager.load("run_right.png", Texture.class);
        assetManager.load("idle.png", Texture.class);

        // --- BACKGROUNDS ---
        assetManager.load("background.png", Texture.class);
        assetManager.load("background2.png", Texture.class);
        assetManager.load("background3.png", Texture.class);

        // --- ITEMS & OBJEK ---
        assetManager.load("fruit.png", Texture.class);
        assetManager.load("water.png", Texture.class);
        assetManager.load("key.png", Texture.class);
        assetManager.load("rock.png", Texture.class);
        assetManager.load("stone_monster.png", Texture.class);
        assetManager.load("SleepDog.png", Texture.class);

        // --- PORTAL ---
        assetManager.load("portal_frame.png", Texture.class);
        assetManager.load("portal_effect.png", Texture.class);

        // --- AUDIO / MUSIK ---
        // Memuat file music.wav sebagai tipe Music
        assetManager.load("music.wav", Music.class);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Skin getSkin() {
        if (gameSkin == null) {
            gameSkin = createFallbackSkin();
        }
        return gameSkin;
    }

    public void setSkin(Skin skin) {
        if (this.gameSkin != null && this.gameSkin != skin) {
            this.gameSkin.dispose();
        }
        this.gameSkin = skin;
    }

    private Skin createFallbackSkin() {
        Skin skin = new Skin();

        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);
        skin.add("default", font);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whiteTexture = new Texture(pixmap);
        skin.add("white", whiteTexture);
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.font = font;
        skin.add("default", textButtonStyle);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = skin.newDrawable("white", Color.GRAY);
        progressBarStyle.background.setMinHeight(15);
        progressBarStyle.knobBefore = skin.newDrawable("white", Color.WHITE);
        progressBarStyle.knobBefore.setMinHeight(15);
        skin.add("default-horizontal", progressBarStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.background = skin.newDrawable("white", new Color(0, 0, 0, 0.8f));
        skin.add("default", windowStyle);

        return skin;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (assetManager != null) assetManager.dispose();
        if (gameSkin != null) gameSkin.dispose();
        super.dispose();
    }
}
