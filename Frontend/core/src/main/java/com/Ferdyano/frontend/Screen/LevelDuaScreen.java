package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.Ferdyano.frontend.core.Player;
import com.Ferdyano.frontend.core.Item;
import com.Ferdyano.frontend.core.Portal; // Import Class Portal
import com.Ferdyano.frontend.ui.SurvivalHud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Input.Keys;

public class LevelDuaScreen implements Screen {
    private final TheLostKeyGame game;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final Stage uiStage;
    private final Player player;
    private final Texture background;

    // --- PERUBAHAN: GANTI TEXTURE MANUAL JADI OBJEK PORTAL ---
    private final Portal exitPortal;

    private Array<Item> itemList;
    private int collectedFruit = 0;
    private int collectedWater = 0;

    private boolean isCheckpointActive = false;
    private boolean isPortalSpawned = false;
    private boolean dialogAlreadyShown = false;

    private final float PLAYER_SCALE = 3.5f;
    private final float WORLD_WIDTH = 1280f;
    private final float WORLD_HEIGHT = 720f;
    private final float BOTTOM_OFFSET = 10f;
    private final float TOP_OFFSET = 70f;

    public LevelDuaScreen(TheLostKeyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.uiStage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT), game.getBatch());

        if (game.getAssetManager().isLoaded("background2.png")) {
            this.background = game.getAssetManager().get("background2.png", Texture.class);
        } else {
            this.background = game.getAssetManager().get("background.png", Texture.class);
        }

        // --- INISIALISASI PORTAL ---
        // Lokasi Portal Level 2: (100, 120)
        this.exitPortal = new Portal(
            game.getAssetManager().get("portal_frame.png", Texture.class),
            game.getAssetManager().get("portal_effect.png", Texture.class),
            100f, 120f
        );

        this.player = new Player(
            game.getAssetManager().get("run_down.png", Texture.class),
            game.getAssetManager().get("run_up.png", Texture.class),
            game.getAssetManager().get("run_left.png", Texture.class),
            game.getAssetManager().get("run_right.png", Texture.class),
            game.getAssetManager().get("idle.png", Texture.class)
        );

        player.setPosition(100f, 300f);
        player.setBoundsSize(100, 100);

        itemList = new Array<>();
        spawnResources();

        HealthManager.getInstance().resetStatus();
        this.uiStage.addActor(new SurvivalHud(game.getSkin(), game));
    }

    private void spawnResources() {
        Texture fTex = game.getAssetManager().get("fruit.png", Texture.class);
        Texture wTex = game.getAssetManager().get("water.png", Texture.class);
        for (int i = 0; i < 5; i++) {
            itemList.add(new Item("Buah", "FRUIT", "Segar", MathUtils.random(150, 1100), MathUtils.random(100, 500), fTex));
            itemList.add(new Item("Air", "WATER", "Murni", MathUtils.random(150, 1100), MathUtils.random(100, 500), wTex));
        }
    }

    private void checkCollisions() {
        Rectangle pBounds = player.getBounds();
        float interactionSize = 60f;
        float offsetX = (pBounds.width - interactionSize) / 2;
        float offsetY = (pBounds.height - interactionSize) / 2;

        Rectangle interactionBox = new Rectangle(pBounds.x + offsetX, pBounds.y + offsetY, interactionSize, interactionSize);

        for (int i = itemList.size - 1; i >= 0; i--) {
            Item item = itemList.get(i);
            if (interactionBox.overlaps(item.getBounds())) {
                if (item.getType().equals("FRUIT")) {
                    HealthManager.getInstance().consumeFood(20);
                    collectedFruit++;
                } else if (item.getType().equals("WATER")) {
                    HealthManager.getInstance().consumeWater(20);
                    collectedWater++;
                }
                itemList.removeIndex(i);
            }
        }

        if (itemList.size == 0 && !dialogAlreadyShown && !isPortalSpawned) {
            showCheckpointDialog();
        }

        // Cek tabrakan dengan portal menggunakan posisi dari objek Portal
        // Kita asumsikan ukuran hitbox portal 60x60
        Rectangle portalHitbox = new Rectangle(exitPortal.getPosition().x, exitPortal.getPosition().y, 60, 60);

        if (isPortalSpawned && interactionBox.overlaps(portalHitbox)) {
            HealthManager.getInstance().clearObservers();
            game.setScreen(new LevelTigaScreen(game));
        }
    }

    private void showCheckpointDialog() {
        isCheckpointActive = true;
        dialogAlreadyShown = true;
        player.setVelocity(0, 0);

        final Label label = new Label("APAKAH SELURUH ITEM TELAH TERKUMPUL?", game.getSkin());
        label.setPosition(WORLD_WIDTH / 2 - label.getWidth()/2, 400);

        final TextButton yesBtn = new TextButton("YA", game.getSkin());
        yesBtn.setPosition(520, 320);
        yesBtn.setSize(100, 50);

        final TextButton noBtn = new TextButton("TIDAK", game.getSkin());
        noBtn.setPosition(660, 320);
        noBtn.setSize(100, 50);

        yesBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPortalSpawned = true;
                isCheckpointActive = false;
                label.remove(); yesBtn.remove(); noBtn.remove();
            }
        });

        noBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isCheckpointActive = false;
                dialogAlreadyShown = false;
                label.remove(); yesBtn.remove(); noBtn.remove();
            }
        });

        uiStage.addActor(label);
        uiStage.addActor(yesBtn);
        uiStage.addActor(noBtn);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isCheckpointActive) {
            updateLogic(delta);
        }

        // Update animasi portal
        if (isPortalSpawned) {
            exitPortal.update(delta);
        }

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // --- RENDER PORTAL DENGAN EFEK BERDENYUT ---
        if (isPortalSpawned) {
            // Gambar Frame (Diam)
            game.getBatch().draw(exitPortal.getFrameTexture(), exitPortal.getPosition().x, exitPortal.getPosition().y, 60, 60);

            // Gambar Efek (Berdenyut)
            float scale = exitPortal.getCurrentScale();
            // Base size efek kita atur 80f biar agak bersinar keluar
            float effectSize = 80f * scale;

            // Logika Center: (Posisi + Setengah Lebar Frame) - (Setengah Ukuran Efek)
            float centerX = exitPortal.getPosition().x + 30f - (effectSize / 2);
            float centerY = exitPortal.getPosition().y + 30f - (effectSize / 2);

            game.getBatch().draw(exitPortal.getEffectRegion(), centerX, centerY, effectSize, effectSize);
        }

        for (Item it : itemList) {
            game.getBatch().draw(it.getTexture(), it.getX(), it.getY(), 50, 50);
        }

        TextureRegion currentFrame = player.getCurrentFrame();
        float drawWidth = currentFrame.getRegionWidth() * PLAYER_SCALE;
        float drawHeight = currentFrame.getRegionHeight() * PLAYER_SCALE;
        game.getBatch().draw(currentFrame, player.getPositionX(), player.getPositionY(), drawWidth, drawHeight);

        game.getBatch().end();

        uiStage.act(delta);
        uiStage.draw();
    }

    private void updateLogic(float delta) {
        HealthManager.getInstance().update(delta);
        float speed = 350f;
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Keys.W)) { dy = speed; dx = 0; }
        else if (Gdx.input.isKeyPressed(Keys.S)) { dy = -speed; dx = 0; }
        else if (Gdx.input.isKeyPressed(Keys.A)) { dx = -speed; dy = 0; }
        else if (Gdx.input.isKeyPressed(Keys.D)) { dx = speed; dy = 0; }

        player.setVelocity(dx, dy);
        player.update(delta);

        if (player.getPositionX() < -20) player.setPosition(-20, player.getPositionY());
        else if (player.getPositionX() > WORLD_WIDTH - 160) player.setPosition(WORLD_WIDTH - 160, player.getPositionY());

        if (player.getPositionY() < BOTTOM_OFFSET) player.setPosition(player.getPositionX(), BOTTOM_OFFSET);
        else if (player.getPositionY() > WORLD_HEIGHT - (player.getCurrentFrame().getRegionHeight() * PLAYER_SCALE) + TOP_OFFSET) {
            player.setPosition(player.getPositionX(), WORLD_HEIGHT - (player.getCurrentFrame().getRegionHeight() * PLAYER_SCALE) + TOP_OFFSET);
        }

        checkCollisions();
        camera.update();
    }

    @Override public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(multiplexer);
    }
    @Override public void resize(int width, int height) {
        viewport.update(width, height);
        uiStage.getViewport().update(width, height, true);
    }
    @Override public void dispose() { uiStage.dispose(); }
    @Override public void hide() { Gdx.input.setInputProcessor(null); }
    @Override public void pause() {} @Override public void resume() {}
}
