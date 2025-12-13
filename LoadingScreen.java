package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.Ferdyano.frontend.Screen.MainMenuScreen;

// --- IMPORT UNTUK MEMBUAT SKIN KOSONG ---
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
// --- AKHIR IMPORT BARU ---

/**
 * LoadingScreen: Menampilkan progres pemuatan aset menggunakan AssetManager.
 */
public class LoadingScreen implements Screen {

    private final TheLostKeyGame game;
    private final AssetManager assetManager;
    private final Stage stage;
    private ProgressBar progressBar;
    private Label loadingLabel;

    private Skin fallbackSkin;

    public LoadingScreen(TheLostKeyGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();

        this.stage = new Stage(new FitViewport(1280, 720), game.getBatch());
        queueAssetsToLoad();
    }

    /** * Daftar semua aset yang perlu dimuat.
     */
    private void queueAssetsToLoad() {

        // A. Muat TILE MAPS & GAME TEXTURES
        assetManager.load("background.png", Texture.class);
        assetManager.load("background2.png", Texture.class);


        // B. Muat SPRITE SHEETS KARAKTER (Ditemukan di root assets/)
        assetManager.load("run_down.png", Texture.class);
        assetManager.load("run_up.png", Texture.class);

        // KOREKSI KRITIS: Tambahkan aset untuk Run Left dan Run Right
        assetManager.load("run_left.png", Texture.class);
        assetManager.load("run_right.png", Texture.class);

        assetManager.load("SleepDog.png", Texture.class);

        assetManager.load("portal_frame.png", Texture.class);
        assetManager.load("portal_effect.png", Texture.class);




        System.out.println("Semua aset telah ditambahkan ke antrian pemuatan.");
    }

    /** * Membuat Skin minimalis secara kode (Fallback Skin).
     */
    private Skin createFallbackSkin() {
        Skin skin = new Skin();

        // 2. Tambahkan Font Default
        skin.add("default-font", new BitmapFont(), BitmapFont.class);

        // 3. Tambahkan Drawable/Texture Placeholder
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whiteTexture = new Texture(pixmap);
        pixmap.dispose();

        skin.add("white", whiteTexture);
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(whiteTexture));

        // KOREKSI UTAMA: Tambahkan Drawable Placeholder untuk InventoryBar
        skin.add("inventory_frame", drawable);
        // -------------------------------------------------------------

        // 4. Tambahkan Style Dasar Label
        skin.add("default", new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE));

        // 5. Tambahkan ProgressBarStyle Dasar
        ProgressBarStyle style = new ProgressBarStyle();
        style.background = drawable;
        style.knob = drawable;
        style.knobBefore = drawable;
        skin.add("default-horizontal", style);

        // 6. TAMBAHKAN TEXTBUTTON STYLE
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");

        // Mengubah warna font menjadi PUTIH agar terlihat di atas balok gelap
        textButtonStyle.fontColor = Color.WHITE;

        textButtonStyle.up = drawable;
        textButtonStyle.down = drawable;
        skin.add("default", textButtonStyle, TextButton.TextButtonStyle.class);

        return skin;
    }


    private void setupUI(Skin skin) {
        Table table = new Table(skin);
        table.setFillParent(true);

        loadingLabel = new Label("LOADING...", skin, "default");
        table.add(loadingLabel).padBottom(20).row();

        progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin, "default-horizontal");
        table.add(progressBar).width(500).height(40);

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Menerapkan warna biru tua untuk background Loading Screen (Bagus untuk kontras)
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (assetManager.update()) {
            // Pemuatan Selesai:

            // 1. SET SKIN DULU sebelum memanggil konstruktor MainMenuScreen
            game.setSkin(fallbackSkin);

            // 2. Pindah ke Main Menu
            game.setScreen(new MainMenuScreen(game));

            // 3. Dispose texture yang digunakan
            if (fallbackSkin.has("white", Texture.class)) {
                fallbackSkin.get("white", Texture.class).dispose();
            }

        } else {
            float progress = assetManager.getProgress();

            // Cek dan buat UI hanya jika Fallback Skin belum dibuat
            if (fallbackSkin == null) {
                this.fallbackSkin = createFallbackSkin();
                setupUI(fallbackSkin);
            }

            if (progressBar != null) {
                progressBar.setValue(progress);
                loadingLabel.setText("LOADING... " + (int)(progress * 100) + "%");
            }
        }

        stage.act(delta);
        stage.draw();
    }

    // ... (Metode resize, pause, resume, hide, dispose) ...
    @Override public void resize(int i, int i1) { stage.getViewport().update(i, i1, true); }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() {
        stage.dispose();
    }
}
