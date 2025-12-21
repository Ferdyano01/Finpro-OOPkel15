package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.core.Player;
import com.Ferdyano.frontend.core.Item;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.Ferdyano.frontend.ui.SurvivalHud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LevelTigaScreen implements Screen {
    private final TheLostKeyGame game;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final Stage uiStage;

    private final Texture bgTexture, bossTexture, rockTexture, altarTexture, fruitTex, waterTex, keyTex;
    private final Texture portalFrame, portalEffect;
    private final Player player;

    private final Array<Rectangle> rocks = new Array<>();
    private float rockTimer = 0;
    private final Array<Rectangle> altars = new Array<>();
    private final float[] altarChargeTimers = {0, 0, 0};
    private final boolean[] keySpawned = {false, false, false};
    private final Array<Item> spawnedKeys = new Array<>();
    private final Array<Item> groundItems = new Array<>();

    private int keysCollected = 0;
    private boolean isGameOver = false;
    private boolean isWinMessageActive = false;
    private boolean isFinalPortalSpawned = false;
    private Rectangle finalPortalBounds;
    private Label winMessageLabel;

    private final float PLAYER_SCALE = 3.5f;
    private final float WORLD_WIDTH = 1280f;
    private final float WORLD_HEIGHT = 720f;
    private final float BOTTOM_OFFSET = 10f;
    private final float TOP_OFFSET = 55f;

    public LevelTigaScreen(TheLostKeyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.uiStage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT), game.getBatch());

        this.bgTexture = game.getAssetManager().get("background3.png", Texture.class);
        this.bossTexture = game.getAssetManager().get("stone_monster.png", Texture.class);
        this.rockTexture = game.getAssetManager().get("rock.png", Texture.class);
        this.altarTexture = game.getAssetManager().get("rock.png", Texture.class);
        this.fruitTex = game.getAssetManager().get("fruit.png", Texture.class);
        this.waterTex = game.getAssetManager().get("water.png", Texture.class);
        this.portalFrame = game.getAssetManager().get("portal_frame.png", Texture.class);
        this.portalEffect = game.getAssetManager().get("portal_effect.png", Texture.class);

        if (game.getAssetManager().contains("key.png")) {
            this.keyTex = game.getAssetManager().get("key.png", Texture.class);
        } else {
            this.keyTex = fruitTex;
        }

        this.player = new Player(
            game.getAssetManager().get("run_down.png", Texture.class),
            game.getAssetManager().get("run_up.png", Texture.class),
            game.getAssetManager().get("run_left.png", Texture.class),
            game.getAssetManager().get("run_right.png", Texture.class),
            game.getAssetManager().get("idle.png", Texture.class)
        );

        player.setPosition(100, 300);
        player.setBoundsSize(90, 90);

        altars.add(new Rectangle(400, 500, 100, 100));
        altars.add(new Rectangle(400, 100, 100, 100));
        altars.add(new Rectangle(850, 300, 100, 100));

        this.finalPortalBounds = new Rectangle(610, 530, 50, 60);

        groundItems.add(new Item("FRUIT", "FRUIT", "F1", 550, 400, fruitTex));
        groundItems.add(new Item("FRUIT", "FRUIT", "F2", 200, 550, fruitTex));
        groundItems.add(new Item("WATER", "WATER", "W1", 550, 250, waterTex));
        groundItems.add(new Item("WATER", "WATER", "W2", 800, 100, waterTex));

        this.uiStage.addActor(new SurvivalHud(game.getSkin(), game));
    }

    @Override
    public void render(float delta) {
        if (isGameOver) {
            Gdx.gl.glClearColor(0, 0, 0, 1); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            uiStage.act(delta); uiStage.draw();
            return;
        }

        if (isWinMessageActive && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            isWinMessageActive = false; isFinalPortalSpawned = true;
            if (winMessageLabel != null) winMessageLabel.remove();
        }

        if (!isWinMessageActive) updatePhysics(delta);

        HealthManager.getInstance().update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getBatch().draw(bgTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        for (int i = 0; i < altars.size; i++) {
            Rectangle a = altars.get(i);
            game.getBatch().draw(altarTexture, a.x, a.y, a.width, a.height);
            if (altarChargeTimers[i] > 0 && !keySpawned[i]) {
                game.getBatch().setColor(1, 0.8f, 0, 0.5f);
                game.getBatch().draw(keyTex, a.x + 25, a.y + 110, 50, 50);
                game.getBatch().setColor(Color.WHITE);
            }
        }

        for (Item it : groundItems) game.getBatch().draw(it.getTexture(), it.getX(), it.getY(), 50, 50);

        for (Item k : spawnedKeys) {
            game.getBatch().setColor(Color.GOLD);
            game.getBatch().draw(k.getTexture(), k.getX(), k.getY(), 60, 60);
            game.getBatch().setColor(Color.WHITE);
        }

        if (isFinalPortalSpawned) {
            game.getBatch().draw(portalEffect, finalPortalBounds.x, finalPortalBounds.y, finalPortalBounds.width, finalPortalBounds.height);
            game.getBatch().draw(portalFrame, finalPortalBounds.x - 10, finalPortalBounds.y - 10, finalPortalBounds.width + 20, finalPortalBounds.height + 20);
        }

        game.getBatch().draw(bossTexture, 1050, 200, 220, 280);
        for (Rectangle r : rocks) game.getBatch().draw(rockTexture, r.x, r.y, 45, 45);

        TextureRegion currentFrame = player.getCurrentFrame();
        float drawWidth = currentFrame.getRegionWidth() * PLAYER_SCALE;
        float drawHeight = currentFrame.getRegionHeight() * PLAYER_SCALE;
        game.getBatch().draw(currentFrame, player.getPositionX(), player.getPositionY(), drawWidth, drawHeight);

        game.getSkin().getFont("default-font").draw(game.getBatch(), "KUNCI TERKUMPUL: " + keysCollected + " / 3", 20, 550);

        game.getBatch().end();
        uiStage.act(delta); uiStage.draw();

        if (HealthManager.getInstance().getHealth() <= 0) handleEnd("GAME OVER! HP HABIS!");
    }

    private void updatePhysics(float delta) {
        float speed = 320f;
        float vx = 0, vy = 0;

        if (Gdx.input.isKeyPressed(Keys.W)) { vy = speed; vx = 0; }
        else if (Gdx.input.isKeyPressed(Keys.S)) { vy = -speed; vx = 0; }
        else if (Gdx.input.isKeyPressed(Keys.A)) { vx = -speed; vy = 0; }
        else if (Gdx.input.isKeyPressed(Keys.D)) { vx = speed; vy = 0; }

        player.setVelocity(vx, vy);
        player.update(delta);

        if (player.getPositionX() < -20) player.setPosition(-20, player.getPositionY());
        else if (player.getPositionX() > WORLD_WIDTH - 120) player.setPosition(WORLD_WIDTH - 120, player.getPositionY());

        if (player.getPositionY() < BOTTOM_OFFSET) player.setPosition(player.getPositionX(), BOTTOM_OFFSET);
        else if (player.getPositionY() > WORLD_HEIGHT - (player.getCurrentFrame().getRegionHeight() * PLAYER_SCALE) + TOP_OFFSET) {
            player.setPosition(player.getPositionX(), WORLD_HEIGHT - (player.getCurrentFrame().getRegionHeight() * PLAYER_SCALE) + TOP_OFFSET);
        }

        if (!isFinalPortalSpawned) {
            rockTimer += delta;
            if (rockTimer >= 1.5f) {
                rocks.add(new Rectangle(1050, player.getPositionY() + 40, 45, 45));
                rockTimer = 0;
            }
            for (int i = rocks.size - 1; i >= 0; i--) {
                Rectangle r = rocks.get(i);
                r.x -= 450 * delta;
                if (player.getBounds().overlaps(r)) {
                    rocks.removeIndex(i);
                    HealthManager.getInstance().changeHealth(-15);
                } else if (r.x < -50) rocks.removeIndex(i);
            }
        }

        for (int i = groundItems.size - 1; i >= 0; i--) {
            Item it = groundItems.get(i);
            if (player.getBounds().overlaps(new Rectangle(it.getX(), it.getY(), 50, 50))) {
                if (it.getType().equals("FRUIT")) HealthManager.getInstance().consumeFood(30);
                else HealthManager.getInstance().consumeWater(30);
                groundItems.removeIndex(i);
            }
        }

        for (int i = 0; i < altars.size; i++) {
            if (!keySpawned[i] && player.getBounds().overlaps(altars.get(i))) {
                altarChargeTimers[i] += delta;
                if (altarChargeTimers[i] >= 2.0f) {
                    spawnedKeys.add(new Item("KEY", "KEY", "K" + i, altars.get(i).x + 20, altars.get(i).y + 20, keyTex));
                    keySpawned[i] = true;
                }
            } else if (!keySpawned[i]) altarChargeTimers[i] = 0;
        }

        for (int i = spawnedKeys.size - 1; i >= 0; i--) {
            if (player.getBounds().overlaps(new Rectangle(spawnedKeys.get(i).getX(), spawnedKeys.get(i).getY(), 60, 60))) {
                spawnedKeys.removeIndex(i);
                keysCollected++;
                if (keysCollected == 3) showWinMessage();
            }
        }

        if (isFinalPortalSpawned && player.getBounds().overlaps(finalPortalBounds)) {
            handleEnd("SELAMAT! KAMU BERHASIL BEBAS DARI DUNIA THE LOST KEY!");
        }
    }

    private void showWinMessage() {
        isWinMessageActive = true;
        player.setVelocity(0, 0);
        winMessageLabel = new Label("SEMUA KUNCI DIDAPAT! TEKAN [ENTER] UNTUK MEMBUKA PORTAL KEBEBASAN!", game.getSkin());
        winMessageLabel.setColor(Color.YELLOW);
        winMessageLabel.setPosition(WORLD_WIDTH / 2 - winMessageLabel.getWidth()/2, 400);
        uiStage.addActor(winMessageLabel);
    }

    private void handleEnd(String msg) {
        if (isGameOver) return;
        isGameOver = true;
        Label.LabelStyle style = new Label.LabelStyle(game.getSkin().getFont("default-font"), Color.GOLD);
        Label statusLabel = new Label(msg, style);
        statusLabel.setFontScale(1.8f);
        statusLabel.setPosition(WORLD_WIDTH / 2 - (statusLabel.getWidth() * statusLabel.getFontScaleX()) / 2, 360);
        uiStage.addActor(statusLabel);
        HealthManager.getInstance().clearObservers();
        Timer.schedule(new Timer.Task() {
            @Override public void run() {
                HealthManager.getInstance().resetStatus();
                game.setScreen(new MainMenuScreen(game));
            }
        }, 5.0f);
    }

    @Override public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage); Gdx.input.setInputProcessor(multiplexer);
    }
    @Override public void resize(int w, int h) {
        viewport.update(w, h, true); uiStage.getViewport().update(w, h, true);
    }
    @Override public void hide() {} @Override public void pause() {} @Override public void resume() {} @Override public void dispose() { uiStage.dispose(); }
}
