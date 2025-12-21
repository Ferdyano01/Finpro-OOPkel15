package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;

public class LoadingScreen implements Screen {

    private final TheLostKeyGame game;
    private final ShapeRenderer shapeRenderer;
    private float progress;

    public LoadingScreen(TheLostKeyGame game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();

        // --- BAGIAN PENTING ---
        // Panggil method ini agar idle.png & aset baru terdaftar untuk dimuat
        game.loadGameAssets();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update progress loading aset
        if (game.getAssetManager().update()) {
            // Jika sudah selesai (return true), tunggu sebentar lalu pindah
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    // Pindah ke Menu Screen (atau GameScreen jika mau langsung test)
                    game.setScreen(new MainMenuScreen(game));
                }
            }, 0.5f);
        }

        // Dapatkan persentase loading (0.0 sampai 1.0)
        progress = game.getAssetManager().getProgress();

        // Gambar Loading Bar Sederhana
        shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Garis Putih (Background Bar)
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(340, 350, 600, 20);

        // Garis Biru (Isi Bar sesuai progress)
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(340, 350, 600 * progress, 20);

        shapeRenderer.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        shapeRenderer.dispose();
    }
}
