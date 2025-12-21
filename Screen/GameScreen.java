package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.Ferdyano.frontend.core.Player;
import com.Ferdyano.frontend.core.Item;
import com.Ferdyano.frontend.core.Checkpoint;
import com.Ferdyano.frontend.core.Portal;
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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer; // <--- Import Timer
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

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

    private Array<Item> itemList;

    private boolean isQuestionActive = false;
    private final Label questionLabel;
    private final TextButton yesButton, noButton;

    private final Array<Rectangle> obstacles = new Array<>();
    private final ShapeRenderer shapeRenderer;
    private final boolean DEBUG_MODE = false;

    private final float WORLD_WIDTH = 1300f;
    private final float WORLD_HEIGHT = 720f;
    private final float PLAYER_SCALE = 3.5f;
    private final float HITBOX_OFFSET_X = 60f;

    // --- VARIABEL GAME OVER ---
    private boolean isGameOver = false;

    public GameScreen(TheLostKeyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.uiStage = new Stage(viewport, game.getBatch());
        this.shapeRenderer = new ShapeRenderer();

        this.gameWorldTexture = game.getAssetManager().get("background.png", Texture.class);

        this.player = new Player(
            game.getAssetManager().get("run_down.png", Texture.class),
            game.getAssetManager().get("run_up.png", Texture.class),
            game.getAssetManager().get("run_left.png", Texture.class),
            game.getAssetManager().get("run_right.png", Texture.class),
            game.getAssetManager().get("idle.png", Texture.class)
        );

        player.setPosition(600f, 250f);
        createObstacles();

        itemList = new Array<>();
        spawnResources();

        this.mainCheckpoint = new Checkpoint(game.getAssetManager().get("SleepDog.png", Texture.class), 600f, 350f);
        this.exitPortal = new Portal(
            game.getAssetManager().get("portal_frame.png", Texture.class),
            game.getAssetManager().get("portal_effect.png", Texture.class),
            900f, 500f
        );

        this.survivalHud = new SurvivalHud(game.getSkin(), game);
        this.uiStage.addActor(survivalHud);

        this.questionLabel = new Label("APAKAH ANJING INI ANJING TIDUR?", game.getSkin());
        questionLabel.setPosition(WORLD_WIDTH / 2 - questionLabel.getWidth()/2, 400);
        questionLabel.setVisible(false);
        uiStage.addActor(questionLabel);

        this.yesButton = new TextButton("Ya", game.getSkin());
        this.noButton = new TextButton("Tidak", game.getSkin());
        yesButton.setSize(100, 50); noButton.setSize(100, 50);
        yesButton.setVisible(false); noButton.setVisible(false);

        yesButton.addListener(new ClickListener() { @Override public void clicked(InputEvent event, float x, float y) { handleAnswer(true); } });
        noButton.addListener(new ClickListener() { @Override public void clicked(InputEvent event, float x, float y) { handleAnswer(false); } });

        uiStage.addActor(yesButton); uiStage.addActor(noButton);
    }

    private void spawnResources() {
        Texture fTex = game.getAssetManager().get("fruit.png", Texture.class);
        Texture wTex = game.getAssetManager().get("water.png", Texture.class);
        for (int i = 0; i < 4; i++) {
            float rx = MathUtils.random(50, 1200); float ry = MathUtils.random(50, 650);
            itemList.add(new Item("Buah", "FRUIT", "Segar", rx, ry, fTex));
            rx = MathUtils.random(50, 1200); ry = MathUtils.random(50, 650);
            itemList.add(new Item("Air", "WATER", "Murni", rx, ry, wTex));
        }
    }

    private void createObstacles() {
        obstacles.add(new Rectangle(0, 690, 1300, 30));
        obstacles.add(new Rectangle(0, 0, 1300, 20));
        obstacles.add(new Rectangle(1260, 0, 40, 720));
        obstacles.add(new Rectangle(20, 0, 20, 720));
    }

    private void handleAnswer(boolean isYes) {
        if (isYes) mainCheckpoint.activate();
        isQuestionActive = false;
        questionLabel.setVisible(false); yesButton.setVisible(false); noButton.setVisible(false);
    }

    @Override public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage); Gdx.input.setInputProcessor(multiplexer);
    }

    private void update(float delta) {
        // Jika game over, hentikan update logika
        if (isGameOver) return;

        HealthManager.getInstance().update(delta);

        // --- CEK KONDISI MATI (GAME OVER) ---
        if (HealthManager.getInstance().getHealth() <= 0) {
            handleEnd("GAME OVER! HP ANDA HABIS!");
            return;
        }
        if (HealthManager.getInstance().getHunger() <= 0) {
            handleEnd("GAME OVER! ANDA MATI KELAPARAN!");
            return;
        }
        if (HealthManager.getInstance().getThirst() <= 0) {
            handleEnd("GAME OVER! ANDA MATI KEHAUSAN!");
            return;
        }
        // ------------------------------------

        float oldX = player.getPositionX();
        float oldY = player.getPositionY();
        float inputX = 0, inputY = 0;
        float speed = 200f;

        if (!isQuestionActive) {
            if (Gdx.input.isKeyPressed(Keys.W)) { inputY = 1; inputX = 0; }
            else if (Gdx.input.isKeyPressed(Keys.S)) { inputY = -1; inputX = 0; }
            else if (Gdx.input.isKeyPressed(Keys.A)) { inputX = -1; inputY = 0; }
            else if (Gdx.input.isKeyPressed(Keys.D)) { inputX = 1; inputY = 0; }
        }

        player.setVelocity(inputX * speed, inputY * speed);
        player.update(delta);

        if (inputX != 0 || inputY != 0) {
            if (!isValidPosition(player.getPositionX(), player.getPositionY())) {
                player.setPosition(oldX, oldY);
            }
        }

        checkItemCollisions();

        if (mainCheckpoint.isActivated()) exitPortal.update(delta);
        camera.update();

        if (!mainCheckpoint.isActivated() && !isQuestionActive) {
            if (Vector2.dst(player.getPositionX(), player.getPositionY(), 600, 350) < 100) {
                isQuestionActive = true;
                questionLabel.setVisible(true); yesButton.setVisible(true); yesButton.setPosition(500, 300); noButton.setVisible(true); noButton.setPosition(650, 300);
            }
        }
        if (mainCheckpoint.isActivated()) {
            if (Vector2.dst(player.getPositionX(), player.getPositionY(), exitPortal.getPosition().x, exitPortal.getPosition().y) < 80) {
                game.setScreen(new LevelDuaScreen(game));
            }
        }
    }

    // --- METHOD GAME OVER ---
    private void handleEnd(String msg) {
        if (isGameOver) return;
        isGameOver = true;

        // Hentikan pemain
        player.setVelocity(0, 0);

        Label.LabelStyle style = new Label.LabelStyle(game.getSkin().getFont("default-font"), Color.RED);
        Label statusLabel = new Label(msg, style);
        statusLabel.setFontScale(2.0f); // Tulisannya besar

        // Taruh di tengah layar
        statusLabel.setPosition(WORLD_WIDTH / 2 - (statusLabel.getWidth() * 2) / 2, 400);
        uiStage.addActor(statusLabel);

        // Bersihkan observer
        HealthManager.getInstance().clearObservers();

        // Tunggu 3 detik, lalu balik ke Main Menu
        Timer.schedule(new Timer.Task() {
            @Override public void run() {
                HealthManager.getInstance().resetStatus(); // Reset HP/Lapar penuh lagi
                game.setScreen(new MainMenuScreen(game));
            }
        }, 3.0f);
    }

    private void checkItemCollisions() {
        for (int i = itemList.size - 1; i >= 0; i--) {
            Item item = itemList.get(i);
            if (player.getBounds().overlaps(new Rectangle(item.getX(), item.getY(), 50, 50))) {
                if (item.getType().equals("FRUIT")) HealthManager.getInstance().consumeFood(20);
                else if (item.getType().equals("WATER")) HealthManager.getInstance().consumeWater(20);
                itemList.removeIndex(i);
            }
        }
    }

    private boolean isValidPosition(float x, float y) {
        Rectangle futureHitbox = new Rectangle(x + 60f, y, 60, 40);
        for (Rectangle obstacle : obstacles) {
            if (futureHitbox.overlaps(obstacle)) return false;
        }
        return true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update logic (akan berhenti jika isGameOver = true)
        update(delta);

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getBatch().draw(gameWorldTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        for (Item item : itemList) {
            game.getBatch().draw(item.getTexture(), item.getX(), item.getY(), 50, 50);
        }

        game.getBatch().draw(mainCheckpoint.getTextureRegion(), 600, 350, 192, 192);

        if (mainCheckpoint.isActivated()) {
            float scale = exitPortal.getCurrentScale();
            float effectSize = 150f * scale;
            game.getBatch().draw(exitPortal.getEffectRegion(), 900 + (48-effectSize)/2, 500 + (48-effectSize)/2, effectSize, effectSize);
            game.getBatch().draw(exitPortal.getFrameTexture(), exitPortal.getPosition().x, exitPortal.getPosition().y, 60, 60);
        }

        TextureRegion currentFrame = player.getCurrentFrame();
        float drawWidth = currentFrame.getRegionWidth() * PLAYER_SCALE;
        float drawHeight = currentFrame.getRegionHeight() * PLAYER_SCALE;
        game.getBatch().draw(currentFrame, player.getPositionX(), player.getPositionY(), drawWidth, drawHeight);

        game.getBatch().end();

        if (DEBUG_MODE) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            for (Rectangle r : obstacles) shapeRenderer.rect(r.x, r.y, r.width, r.height);
            shapeRenderer.end();
        }

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height); }
    @Override public void dispose() { uiStage.dispose(); shapeRenderer.dispose(); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
