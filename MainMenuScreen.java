package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

    public MainMenuScreen(TheLostKeyGame game) {
        this.game = game;

        // --- KOREKSI KRITIS DI SINI ---
        // MASALAH: Baris ini menyebabkan crash karena aset ui/custom_skin.json tidak dimuat:
        // this.skin = game.getAssetManager().get("ui/custom_skin.json", Skin.class);

        // SOLUSI: Ambil Fallback Skin (Skin yang dibuat di kode) dari TheLostKeyGame
        this.skin = game.getSkin();
        // -----------------------------

        // Menggunakan FitViewport untuk memastikan menu terlihat baik di berbagai resolusi
        this.stage = new Stage(new FitViewport(1280, 720), game.getBatch());

        buildMenuTable();
    }

    /** * Metode untuk menyusun tata letak menu utama menggunakan Scene2D Table.
     */
    private void buildMenuTable() {
        // Buat Table sebagai wadah utama yang mengisi seluruh Stage
        // Catatan: Karena menggunakan Fallback Skin, semua elemen (tombol, label) akan terlihat polos.
        Table table = new Table(skin);
        table.setFillParent(true);
        // table.setDebug(true);

        // 1. Judul Game
        // Asumsi: Style "title" ada di Fallback Skin (walaupun hanya menggunakan default font)
        Label title = new Label("THE LOST KEY", skin, "default");
        table.add(title).padBottom(50).row();

        // 2. Tombol "Mulai Petualangan Baru"
        TextButton newGameButton = new TextButton("START NEW ADVENTURE", skin);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Aksi PENTING: Pindah ke GameScreen
                game.setScreen(new GameScreen(game));
            }
        });
        table.add(newGameButton).width(300).height(60).pad(10).row();

        // 3. Tombol "Lanjutkan"
        TextButton continueButton = new TextButton("LOAD CHECKPOINT", skin);
        table.add(continueButton).width(300).height(60).pad(10).row();

        // 4. Tombol "Keluar"
        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitButton).width(300).height(60).pad(10).row();

        // Tambahkan table ke Stage
        stage.addActor(table);
    }

    // --- Siklus Hidup LibGDX ---

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
    }
}
