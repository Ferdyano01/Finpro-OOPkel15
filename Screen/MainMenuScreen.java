package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music; // <--- IMPORT BARU
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * MainMenuScreen: Layar awal yang menangani navigasi menu utama.
 */
public class MainMenuScreen implements Screen {

    private final TheLostKeyGame game;
    private final Stage stage;
    private final Skin skin;

    // Variabel untuk musik
    private Music bgMusic;

    public MainMenuScreen(TheLostKeyGame game) {
        this.game = game;
        this.skin = game.getSkin();
        this.stage = new Stage(new FitViewport(1280, 720), game.getBatch());

        // --- LOGIKA MUSIK DI SINI ---
        // Cek apakah music.wav sudah dimuat oleh AssetManager di TheLostKeyGame
        if (game.getAssetManager().isLoaded("music.wav")) {
            // Ambil file musiknya
            bgMusic = game.getAssetManager().get("music.wav", Music.class);

            // Set agar musik mengulang terus (Looping)
            bgMusic.setLooping(true);

            // Set Volume (0.0f sampai 1.0f). Ubah jika terlalu keras/pelan.
            bgMusic.setVolume(0.5f);

            // Mainkan musik jika belum main
            if (!bgMusic.isPlaying()) {
                bgMusic.play();
            }
        }

        buildMenuTable();
    }

    private void buildMenuTable() {
        Table table = new Table(skin);
        table.setFillParent(true);

        // 1. Judul Game
        Label title = new Label("THE LOST KEY", skin, "default");
        table.add(title).padBottom(50).row();

        // 2. Tombol "Mulai Petualangan Baru"
        TextButton newGameButton = new TextButton("START NEW ADVENTURE", skin);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // OPSI: Jika ingin musik BERHENTI saat masuk game, hilangkan tanda komentar di bawah ini:
                // if (bgMusic != null) bgMusic.stop();

                // Pindah ke GameScreen
                game.setScreen(new GameScreen(game));
            }
        });
        table.add(newGameButton).width(300).height(60).pad(10).row();

        // 3. Tombol "Keluar"
        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitButton).width(300).height(60).pad(10).row();

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // Pastikan musik main lagi kalau kembali ke menu ini
        if (bgMusic != null && !bgMusic.isPlaying()) {
            bgMusic.play();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        // Musik tidak perlu didispose di sini karena dikelola oleh AssetManager global
    }
}
