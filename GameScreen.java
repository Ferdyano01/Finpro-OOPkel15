package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.Ferdyano.frontend.core.Player;
import com.Ferdyano.frontend.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.Ferdyano.frontend.core.Checkpoint;
import com.Ferdyano.frontend.core.Portal;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

// IMPORT BARU: Tujuan transisi level
import com.Ferdyano.frontend.Screen.LevelDuaScreen;
// Hapus import MainMenuScreen jika tidak diperlukan lagi
import com.Ferdyano.frontend.Screen.MainMenuScreen;


public class GameScreen implements Screen {

    private final TheLostKeyGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Player player;
    private final GameUI gameUI;

    // --- VARIABEL ASET ---
    private final Texture gameWorldTexture;
    private final Texture playerRunDownSheet;
    private final Texture playerRunUpSheet;
    private final Texture playerRunLeftSheet;
    private final Texture playerRunRightSheet;
    private final Texture checkpointTexture;

    private final Texture portalFrameTexture;
    private final Texture portalEffectTexture;

    // --- LOGIKA GAME ---
    private final Checkpoint mainCheckpoint;
    private final Portal exitPortal;
    private boolean isQuestionActive = false;
    private final Label questionLabel;
    private final TextButton yesButton;
    private final TextButton noButton;


    public GameScreen(TheLostKeyGame game) {
        this.game = game;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1280, 720);
        this.viewport = new FitViewport(1280, 720, camera);

        // 2. Inisialisasi Aset dari AssetManager
        this.gameWorldTexture = game.getAssetManager().get("background.png", Texture.class);
        this.playerRunDownSheet = game.getAssetManager().get("run_down.png", Texture.class);
        this.playerRunUpSheet = game.getAssetManager().get("run_up.png", Texture.class);
        this.playerRunLeftSheet = game.getAssetManager().get("run_left.png", Texture.class);
        this.playerRunRightSheet = game.getAssetManager().get("run_right.png", Texture.class);
        this.checkpointTexture = game.getAssetManager().get("SleepDog.png", Texture.class);

        this.portalFrameTexture = game.getAssetManager().get("portal_frame.png", Texture.class);
        this.portalEffectTexture = game.getAssetManager().get("portal_effect.png", Texture.class);


        // 3. Inisialisasi Entitas
        this.player = new Player(playerRunDownSheet, playerRunUpSheet, playerRunLeftSheet, playerRunRightSheet);
        player.setPosition(100f, 100f);

        this.mainCheckpoint = new Checkpoint(checkpointTexture, 600f, 350f);

        this.exitPortal = new Portal(portalFrameTexture, portalEffectTexture, 900f, 500f);

        // ... (Inisialisasi UI, Label, dan Tombol) ...
        Skin gameSkin = game.getSkin();
        this.gameUI = new GameUI(viewport, gameSkin);
        this.questionLabel = new Label("APAKAH ANJING INI ANJING TIDUR?", gameSkin, "default");
        questionLabel.setFontScale(1.5f);
        questionLabel.setPosition(1280 / 2 - questionLabel.getWidth() / 2, 720 / 2 + 50);
        questionLabel.setVisible(false);

        this.yesButton = new TextButton("Ya", gameSkin);
        this.noButton = new TextButton("Tidak", gameSkin);
        yesButton.setBounds(1280 / 2 - 170, 720 / 2 - 50, 150, 40);
        noButton.setBounds(1280 / 2 + 20, 720 / 2 - 50, 150, 40);

        yesButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) { handleAnswer(true); }
        });
        noButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) { handleAnswer(false); }
        });
    }

    private void handleAnswer(boolean isYes) {
        if (isYes) {
            mainCheckpoint.activate();
            Gdx.app.log("CHECKPOINT", "Jawaban Benar! Portal Sekarang Tersedia.");
        } else {
            Gdx.app.log("CHECKPOINT", "Jawaban Salah!");
        }

        isQuestionActive = false;
        questionLabel.setVisible(false);
        Gdx.input.setInputProcessor(gameUI.getStage());
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameUI.getStage());
        HealthManager.getInstance().changeHealth(0);

        playerRunDownSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        playerRunUpSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        playerRunLeftSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        playerRunRightSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        portalFrameTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        portalEffectTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    private void update(float delta) {
        float speed = 200f;
        float dx = 0;
        float dy = 0;

        if (!isQuestionActive) {
            if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) { dy = speed; }
            else if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) { dy = -speed; }
            if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) { dx = -speed; }
            else if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) { dx = speed; }
        }
        player.setVelocity(dx, dy);

        camera.update();
        player.update(delta);
        HealthManager.getInstance().update(delta);

        if (mainCheckpoint.isActivated()) {
            exitPortal.update(delta);
        }

        // LOGIKA COLLISION CHECKPOINT
        if (!mainCheckpoint.isActivated() && !isQuestionActive) {
            float playerCenterX = player.getPositionX() + player.getWidth() * 3 / 2f;
            float playerCenterY = player.getPositionY() + player.getHeight() * 3 / 2f;
            float cpCenterX = mainCheckpoint.getPosition().x + mainCheckpoint.getWidth() * 3 / 2f;
            float cpCenterY = mainCheckpoint.getPosition().y + mainCheckpoint.getHeight() * 3 / 2f;
            float distance = Vector2.dst(playerCenterX, playerCenterY, cpCenterX, cpCenterY);

            if (distance < 100) {
                isQuestionActive = true;
                questionLabel.setVisible(true);
                gameUI.getStage().addActor(yesButton);
                noButton.remove();
                gameUI.getStage().addActor(noButton);
            }
        }

        // LOGIKA COLLISION PORTAL
        if (mainCheckpoint.isActivated()) {
            float playerCenterX = player.getPositionX() + player.getWidth() * 3 / 2f;
            float playerCenterY = player.getPositionY() + player.getHeight() * 3 / 2f;
            float portalCenterX = exitPortal.getPosition().x + exitPortal.getWidth() * 3 / 2f;
            float portalCenterY = exitPortal.getPosition().y + exitPortal.getHeight() * 3 / 2f;
            float distanceToPortal = Vector2.dst(playerCenterX, playerCenterY, portalCenterX, portalCenterY);

            if (distanceToPortal < 100) {
                Gdx.app.log("PORTAL", "Memuat Level Baru...");
                // KOREKSI UTAMA: Ganti transisi ke LevelDuaScreen
                game.setScreen(new LevelDuaScreen(game));
            }
        }

        if (!isQuestionActive) {
            yesButton.remove();
            noButton.remove();
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getBatch().draw(gameWorldTexture, 0, 0, 1280, 720);

        // RENDER CHECKPOINT
        game.getBatch().draw(
            mainCheckpoint.getTextureRegion(),
            mainCheckpoint.getPosition().x,
            mainCheckpoint.getPosition().y,
            mainCheckpoint.getWidth() * 3,
            mainCheckpoint.getHeight() * 3
        );

        // KOREKSI 2: RENDER PORTAL (Hanya Efek Berdenyut)
        if (mainCheckpoint.isActivated()) {

            // --- VARIABEL DASAR PORTAL (Frame 64x64, Skala 3x) ---
            float totalWidth = exitPortal.getWidth() * 3;
            float totalHeight = exitPortal.getHeight() * 3;
            float baseX = exitPortal.getPosition().x;
            float baseY = exitPortal.getPosition().y;

            // --- VARIABEL EFEK BERDENYUT (Menggunakan getCurrentScale) ---
            float currentScale = exitPortal.getCurrentScale(); // Ambil skala dinamis dari Portal.java

            float effectW = totalWidth * currentScale;
            float effectH = totalHeight * currentScale;

            // Hitung posisi baru (x_effect, y_effect) agar efek yang lebih kecil tetap di pusat frame
            float effectX = baseX + (totalWidth - effectW) / 2f;
            float effectY = baseY + (totalHeight - effectH) / 2f;

            // 6b. Render HANYA EFEK BERDENYUT (Layer Atas)
            game.getBatch().draw(
                exitPortal.getEffectRegion(), // Gunakan Effect Region
                effectX, effectY, // Posisi agar berada di pusat tempat frame portal seharusnya
                effectW / 2f, effectH / 2f, // Origin harus di pusat Efek
                effectW, effectH,
                1, 1,
                0 // Rotasi 0 (Non-rotasi)
            );
        }


        // RENDERING PLAYER DENGAN SKALA 3X
        game.getBatch().setColor(Color.WHITE);
        game.getBatch().draw(
            player.getCurrentFrame(),
            player.getPositionX(),
            player.getPositionY(),
            player.getWidth() * 3 ,
            player.getHeight() * 3
        );

        game.getBatch().end();

        // RENDER UI DAN PERTANYAAN
        gameUI.updateAndDraw(delta);

        if (isQuestionActive) {
            game.getBatch().setProjectionMatrix(viewport.getCamera().combined);
            game.getBatch().begin();
            questionLabel.draw(game.getBatch(), 1f);
            game.getBatch().end();
        }
    }

    // --- METODE SCREEN WAJIB ---
    @Override public void resize(int width, int height) { viewport.update(width, height, true); gameUI.resize(width, height); }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { gameUI.dispose(); }
}
