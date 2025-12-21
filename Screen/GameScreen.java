package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.Ferdyano.frontend.Managers.InventoryManager;
import com.Ferdyano.frontend.network.BackendFacade;
import com.Ferdyano.frontend.core.Player;
import com.Ferdyano.frontend.ui.GameUI;
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
    private final ShapeRenderer shapeRenderer;

    private final Player player;
    private final GameUI gameUI;

    private final Texture levelOneBgTexture;
    private final Texture groundTexture;
    private final Array<Rectangle> obstacles;

    private final Checkpoint mainCheckpoint;
    private final Portal exitPortal;

    private final Stage uiStage;
    private Label questionLabel;
    private TextButton yesButton;
    private TextButton noButton;
    private boolean isQuestionActive = false;

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final float PLAYER_SCALE = 3.0f;
    private static final boolean DEBUG_MODE = false;

    public GameScreen(TheLostKeyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.shapeRenderer = new ShapeRenderer();

        this.levelOneBgTexture = game.getAssetManager().get("level1_bg.png", Texture.class);
        this.groundTexture = game.getAssetManager().get("ground.png", Texture.class);

        Texture runDown = game.getAssetManager().get("player_run_down.png", Texture.class);
        Texture runUp = game.getAssetManager().get("player_run_up.png", Texture.class);
        Texture runLeft = game.getAssetManager().get("player_run_left.png", Texture.class);
        Texture runRight = game.getAssetManager().get("player_run_right.png", Texture.class);
        Texture idle = game.getAssetManager().get("player_idle.png", Texture.class);

        this.player = new Player(runDown, runUp, runLeft, runRight, idle);

        this.gameUI = new GameUI(game, viewport, game.getSkin());

        this.obstacles = new Array<>();
        obstacles.add(new Rectangle(200, 200, 100, 100));
        obstacles.add(new Rectangle(500, 400, 150, 50));
        obstacles.add(new Rectangle(800, 100, 80, 300));

        Texture checkpointTex = game.getAssetManager().get("checkpoint.png", Texture.class);
        this.mainCheckpoint = new Checkpoint(checkpointTex, 600, 350);

        Texture portalFrame = game.getAssetManager().get("portal_frame.png", Texture.class);
        Texture portalEffect = game.getAssetManager().get("portal_effect.png", Texture.class);
        this.exitPortal = new Portal(portalFrame, portalEffect, 900, 500);

        this.uiStage = new Stage(viewport);
        setupQuestionUI();
    }

    private void setupQuestionUI() {
        Label.LabelStyle style = new Label.LabelStyle(game.getSkin().getFont("default-font"), Color.WHITE);
        questionLabel = new Label("Aktifkan Checkpoint? (Y/N)", style);
        questionLabel.setPosition(WORLD_WIDTH / 2 - 100, WORLD_HEIGHT / 2 + 50);
        questionLabel.setVisible(false);

        yesButton = new TextButton("YES", game.getSkin());
        yesButton.setPosition(WORLD_WIDTH / 2 - 120, WORLD_HEIGHT / 2 - 20);
        yesButton.setVisible(false);
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleAnswer(true);
            }
        });

        noButton = new TextButton("NO", game.getSkin());
        noButton.setPosition(WORLD_WIDTH / 2 + 20, WORLD_HEIGHT / 2 - 20);
        noButton.setVisible(false);
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleAnswer(false);
            }
        });

        uiStage.addActor(questionLabel);
        uiStage.addActor(yesButton);
        uiStage.addActor(noButton);
    }

    private void handleAnswer(boolean isYes) {
        if (isYes) {
            mainCheckpoint.activate();

            // Save Game to Backend
            System.out.println("Checkpoint Activated! Saving Game...");

            // Collect Data from Managers
            int currentHp = HealthManager.getInstance().getHealth();
            int currentHunger = HealthManager.getInstance().getHunger();
            int currentThirst = HealthManager.getInstance().getThirst();
            int foodCount = InventoryManager.getInstance().getFoodCount();

            // Send to Backend
            BackendFacade.getInstance().saveGame(
                    "CHECKPOINT_1",
                    currentHp,
                    currentHunger,
                    currentThirst,
                    false,
                    foodCount,
                    new BackendFacade.NetworkCallback() {
                        @Override
                        public void onSuccess(String response) {
                            System.out.println("GAME SAVED SUCCESSFULLY!");
                        }

                        @Override
                        public void onFailure(String error) {
                            System.out.println("SAVE FAILED: " + error);
                        }
                    }
            );
        }

        isQuestionActive = false;
        questionLabel.setVisible(false);
        yesButton.setVisible(false);
        noButton.setVisible(false);
    }

    private void checkCollisions() {
        Rectangle pBounds = player.getBounds();
        for (Rectangle obstacle : obstacles) {
            if (pBounds.overlaps(obstacle)) {
                if (player.getPositionX() < obstacle.x) player.setPosition(obstacle.x - pBounds.width, player.getPositionY());
                else if (player.getPositionX() > obstacle.x) player.setPosition(obstacle.x + obstacle.width, player.getPositionY());
            }
        }
    }

    public void update(float delta) {
        float speed = 200 * delta;
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) { dy = speed; }
        else if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) { dy = -speed; }
        else if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) { dx = -speed; }
        else if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) { dx = speed; }

        player.setVelocity(dx, dy);
        player.update(delta);

        checkCollisions();
        HealthManager.getInstance().update(delta);

        Vector2 playerPos = new Vector2(player.getPositionX(), player.getPositionY());
        if (playerPos.dst(mainCheckpoint.getPosition()) < 50 && !mainCheckpoint.isActivated() && !isQuestionActive) {
            isQuestionActive = true;
            questionLabel.setVisible(true);
            yesButton.setVisible(true);
            noButton.setVisible(true);
        }

        if (mainCheckpoint.isActivated()) {
            if (playerPos.dst(exitPortal.getPosition()) < 50) {
                game.setScreen(new LevelDuaScreen(game));
            }
        }

        exitPortal.update(delta);
        camera.position.set(player.getPositionX() + player.getWidth() / 2, player.getPositionY() + player.getHeight() / 2, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getBatch().draw(levelOneBgTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        game.getBatch().draw(groundTexture, 0, 0, WORLD_WIDTH, 100);

        game.getBatch().setColor(Color.GRAY);
        for (Rectangle r : obstacles) {
            game.getBatch().draw(groundTexture, r.x, r.y, r.width, r.height);
        }
        game.getBatch().setColor(Color.WHITE);

        game.getBatch().draw(mainCheckpoint.getTextureRegion(), mainCheckpoint.getPosition().x, mainCheckpoint.getPosition().y, 192, 192);

        if (mainCheckpoint.isActivated()) {
            float frameW = exitPortal.getFrameTexture().getRegionWidth();
            float frameH = exitPortal.getFrameTexture().getRegionHeight();
            game.getBatch().draw(exitPortal.getFrameTexture(), 900, 500, frameW, frameH);

            float scale = exitPortal.getCurrentScale();
            float effectW = exitPortal.getEffectRegion().getRegionWidth();
            float effectH = exitPortal.getEffectRegion().getRegionHeight();

            float effectCenterX = 900 + frameW / 2f;
            float effectCenterY = 500 + frameH / 2f;
            float effectX = effectCenterX - (effectW / 2f);
            float effectY = effectCenterY - (effectH / 2f);

            game.getBatch().draw(
                    exitPortal.getEffectRegion(),
                    effectX, effectY,
                    effectW / 2f, effectH / 2f,
                    effectW, effectH,
                    scale, scale,
                    0
            );
        }

        game.getBatch().setColor(Color.WHITE);
        game.getBatch().draw(
                player.getCurrentFrame(),
                player.getPositionX(),
                player.getPositionY(),
                player.getWidth() * PLAYER_SCALE,
                player.getHeight() * PLAYER_SCALE
        );

        game.getBatch().end();

        gameUI.updateAndDraw(delta);

        if (isQuestionActive) {
            uiStage.act(delta);
            uiStage.draw();
        }
    }

    @Override public void resize(int width, int height) { viewport.update(width, height); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        uiStage.dispose();
    }
}