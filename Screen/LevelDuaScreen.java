// File: com.Ferdyano.frontend.Screen.LevelDuaScreen.java (Ganti seluruh isinya dengan ini)

package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.core.Player; // Import Player
import com.Ferdyano.frontend.ui.GameUI; // Import GameUI untuk input dan HUD
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input.Keys; // Import Keys untuk input
import com.badlogic.gdx.graphics.Color; // Import Color

public class LevelDuaScreen implements Screen {

    private final TheLostKeyGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    // --- VARIABEL LEVEL 2 ---
    private final Texture levelTwoBgTexture;
    private final Player player; // Deklarasi objek Player

    // --- VARIABEL ASET PLAYER (Dimuat dari AssetManager) ---
    private final Texture playerRunDownSheet;
    private final Texture playerRunUpSheet;
    private final Texture playerRunLeftSheet;
    private final Texture playerRunRightSheet;


    public LevelDuaScreen(TheLostKeyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1280, 720); // Set ukuran kamera
        this.viewport = new FitViewport(1280, 720, camera);

        // 1. Muat Aset Player (Ambil dari AssetManager yang sudah dimuat di LoadingScreen)
        this.playerRunDownSheet = game.getAssetManager().get("run_down.png", Texture.class);
        this.playerRunUpSheet = game.getAssetManager().get("run_up.png", Texture.class);
        this.playerRunLeftSheet = game.getAssetManager().get("run_left.png", Texture.class);
        this.playerRunRightSheet = game.getAssetManager().get("run_right.png", Texture.class);

        // 2. Inisialisasi Player
        this.player = new Player(
            playerRunDownSheet,
            playerRunUpSheet,
            playerRunLeftSheet,
            playerRunRightSheet
        );
        player.setPosition(100f, 100f); // Posisi awal di Level 2

        // 3. Muat Background Level 2
        this.levelTwoBgTexture = game.getAssetManager().get("background2.png", Texture.class);
    }

    @Override
    public void show() {
        Gdx.app.log("LEVEL", "Memasuki Level Dua.");
        // Set input processor agar pemain bisa bergerak
        Gdx.input.setInputProcessor(null);
    }

    // --- Metode Update Baru ---
    private void update(float delta) {
        // Logika Input Pemain (sama seperti GameScreen.java)
        float speed = 200f;
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
            dy = speed;
        } else if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
            dy = -speed;
        }

        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
            dx = -speed;
        } else if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            dx = speed;
        }

        player.setVelocity(dx, dy);

        camera.update();
        player.update(delta);
    }


    @Override
    public void render(float delta) {

        update(delta); // Panggil update

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        // 1. Render Background Level Dua
        game.getBatch().draw(levelTwoBgTexture, 0, 0, 1280, 720);

        // 2. Render Player (Skala 3x, sama seperti Level 1)
        game.getBatch().setColor(Color.WHITE);
        game.getBatch().draw(
            player.getCurrentFrame(),
            player.getPositionX(),
            player.getPositionY(),
            player.getWidth() * 3,
            player.getHeight() * 3
        );

        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() {
        // Player tidak perlu didispose karena asetnya dimuat oleh AssetManager
    }
}
