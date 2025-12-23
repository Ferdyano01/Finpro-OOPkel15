package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.Ferdyano.frontend.core.Player;
import com.Ferdyano.frontend.ui.SurvivalHud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.Ferdyano.frontend.core.Checkpoint;
import com.Ferdyano.frontend.core.Portal;

public class GameScreen implements Screen {
    private final TheLostKeyGame game;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final Stage uiStage;

    private final Player player;
    private final SurvivalHud survivalHud;

    private final Texture gameWorldTexture;
    private final Checkpoint mainCheckpoint;
    private final Portal exitPortal;

    private boolean isQuestionActive = false;
    private final Label questionLabel;
    private final TextButton yesButton, noButton;

    private final Array<Rectangle> obstacles = new Array<>();
    private final ShapeRenderer shapeRenderer;
    private final boolean DEBUG_MODE = false;

    private final float WORLD_WIDTH = 1300f;
    private final float WORLD_HEIGHT = 720f;
    private final float PLAYER_SCALE = 3.5f;

    // Hitbox settings
    private final float HITBOX_WIDTH = 60f;
    private final float HITBOX_HEIGHT = 40f;
    private final float HITBOX_OFFSET_X = 60f;

    public GameScreen(TheLostKeyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.uiStage = new Stage(viewport, game.getBatch());
        this.shapeRenderer = new ShapeRenderer();

        // Memuat background level 1
        this.gameWorldTexture = game.getAssetManager().get("background.png", Texture.class);

        // Inisialisasi Player dengan animasi
        this.player = new Player(
            game.getAssetManager().get("run_down.png", Texture.class),
            game.getAssetManager().get("run_up.png", Texture.class),
            game.getAssetManager().get("run_left.png", Texture.class),
            game.getAssetManager().get("run_right.png", Texture.class),
            game.getAssetManager().get("idle.png", Texture.class)
        );

        player.setPosition(600f, 250f);
        createObstacles();

        // Membuat objek Checkpoint (Anjing) dan Portal
        this.mainCheckpoint = new Checkpoint(game.getAssetManager().get("SleepDog.png", Texture.class), 600f, 350f);
        this.exitPortal = new Portal(
            game.getAssetManager().get("portal_frame.png", Texture.class),
            game.getAssetManager().get("portal_effect.png", Texture.class),
            900f, 500f
        );

        // Menambahkan HUD (Status Bar)
        this.survivalHud = new SurvivalHud(game.getSkin(), game);
        this.uiStage.addActor(survivalHud);

        // UI untuk pertanyaan Checkpoint
        this.questionLabel = new Label("APAKAH ANJING INI ANJING TIDUR?", game.getSkin());
        questionLabel.setPosition(WORLD_WIDTH / 2 - questionLabel.getWidth()/2, 400);
        questionLabel.setVisible(false);
        uiStage.addActor(questionLabel);

        this.yesButton = new TextButton("Ya", game.getSkin());
        this.noButton = new TextButton("Tidak", game.getSkin());
        yesButton.setSize(100, 50); noButton.setSize(100, 50);
        yesButton.setVisible(false); noButton.setVisible(false);

        // Logic tombol jawaban
        yesButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) { handleAnswer(true); }
        });
        noButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) { handleAnswer(false); }
        });

        uiStage.addActor(yesButton);
        uiStage.addActor(noButton);
    }

    // Membuat dinding pembatas agar pemain tidak keluar map
    private void createObstacles() {
        obstacles.add(new Rectangle(0, 690, 1300, 30)); // Atas
        obstacles.add(new Rectangle(0, 0, 1300, 20));   // Bawah
        obstacles.add(new Rectangle(1260, 0, 40, 720)); // Kanan
        obstacles.add(new Rectangle(20, 0, 20, 720));   // Kiri
    }

    // Menangani jawaban pemain saat berinteraksi dengan Anjing
    private void handleAnswer(boolean isYes) {
        if (isYes) mainCheckpoint.activate(); // Aktifkan portal jika 'Ya'
        isQuestionActive = false;
        questionLabel.setVisible(false);
        yesButton.setVisible(false);
        noButton.setVisible(false);
    }

    @Override
    public void show() {
        // Mengatur input processor agar tombol UI bisa diklik
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    // Update logika game setiap frame
    private void update(float delta) {
        HealthManager.getInstance().update(delta); // Update status lapar/haus

        float oldX = player.getPositionX();
        float oldY = player.getPositionY();
        float inputX = 0;
        float inputY = 0;
        float speed = 200f;

        // Kunci pergerakan jika sedang menjawab pertanyaan
        if (!isQuestionActive) {
            if (Gdx.input.isKeyPressed(Keys.W)) { inputY = 1; inputX = 0; }
            else if (Gdx.input.isKeyPressed(Keys.S)) { inputY = -1; inputX = 0; }
            else if (Gdx.input.isKeyPressed(Keys.A)) { inputX = -1; inputY = 0; }
            else if (Gdx.input.isKeyPressed(Keys.D)) { inputX = 1; inputY = 0; }
        }

        // Terapkan kecepatan ke pemain
        player.setVelocity(inputX * speed, inputY * speed);
        player.update(delta);
        player.setPosition(oldX, oldY); // Reset posisi untuk pengecekan tabrakan manual

        float moveAmount = speed * delta;

        // Cek tabrakan sumbu X
        if (inputX != 0) {
            float nextX = oldX + (inputX * moveAmount);
            if (isValidPosition(nextX, oldY)) player.setPosition(nextX, oldY);
        }
        // Cek tabrakan sumbu Y
        if (inputY != 0) {
            float currentX = player.getPositionX();
            float nextY = oldY + (inputY * moveAmount);
            if (isValidPosition(currentX, nextY)) player.setPosition(currentX, nextY);
        }

        // Update animasi portal jika sudah aktif
        if (mainCheckpoint.isActivated()) exitPortal.update(delta);
        camera.update();

        // Logika memunculkan pertanyaan jika dekat dengan Anjing (Checkpoint)
        if (!mainCheckpoint.isActivated() && !isQuestionActive) {
            if (Vector2.dst(player.getPositionX(), player.getPositionY(), 600, 350) < 100) {
                isQuestionActive = true;
                questionLabel.setVisible(true);
                yesButton.setVisible(true); yesButton.setPosition(500, 300);
                noButton.setVisible(true); noButton.setPosition(650, 300);
            }
        }

        // Logika Masuk Portal (Pindah ke Level 2)
        if (mainCheckpoint.isActivated()) {
            // Hitung jarak pemain ke tengah portal
            float distance = Vector2.dst(player.getPositionX(), player.getPositionY(), exitPortal.getPosition().x, exitPortal.getPosition().y);

            // DEBUG: Cetak jarak ke konsol (Cek tab "Run" di IntelliJ)
            // System.out.println("Jarak ke Portal: " + distance);

            // FIX: Jarak diperbesar dari 80 ke 120 agar lebih mudah masuk
            if (distance < 120) {
                System.out.println("MASUK KE PORTAL! Pindah ke Level 2...");
                game.setScreen(new LevelDuaScreen(game));
            }
        }
    }

    // Cek apakah posisi aman (tidak menabrak tembok)
    private boolean isValidPosition(float x, float y) {
        Rectangle futureHitbox = new Rectangle(x + HITBOX_OFFSET_X, y, HITBOX_WIDTH, HITBOX_HEIGHT);
        for (Rectangle obstacle : obstacles) {
            if (futureHitbox.overlaps(obstacle)) return false;
        }
        return true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Jalankan logika update sebelum menggambar
        update(delta);

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        // Gambar Background & Objek
        game.getBatch().draw(gameWorldTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        game.getBatch().draw(mainCheckpoint.getTextureRegion(), 600, 350, 192, 192);

        // Gambar Portal jika aktif
        if (mainCheckpoint.isActivated()) {
            float scale = exitPortal.getCurrentScale();
            float effectSize = 150f * scale;
            // Menggambar efek portal dengan posisi yang disesuaikan agar di tengah
            game.getBatch().draw(exitPortal.getEffectRegion(), 900 + (48-effectSize)/2, 500 + (48-effectSize)/2, effectSize, effectSize);
        }

        // Gambar Pemain
        TextureRegion currentFrame = player.getCurrentFrame();
        float drawWidth = currentFrame.getRegionWidth() * PLAYER_SCALE;
        float drawHeight = currentFrame.getRegionHeight() * PLAYER_SCALE;
        game.getBatch().draw(currentFrame, player.getPositionX(), player.getPositionY(), drawWidth, drawHeight);

        game.getBatch().end();

        // Debug Mode: Menampilkan garis merah hitbox tembok
        if (DEBUG_MODE) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            for (Rectangle r : obstacles) shapeRenderer.rect(r.x, r.y, r.width, r.height);
            shapeRenderer.end();
        }

        // Gambar UI (HUD & Tombol)
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height); }
    @Override public void dispose() { uiStage.dispose(); shapeRenderer.dispose(); }
    @Override public void pause() {} @Override public void resume() {} @Override public void hide() {}
}
