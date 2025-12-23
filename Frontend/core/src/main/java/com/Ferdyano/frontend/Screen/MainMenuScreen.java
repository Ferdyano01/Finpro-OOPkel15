package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.network.BackendFacade;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.Ferdyano.frontend.Managers.InventoryManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class MainMenuScreen implements Screen {
    private final TheLostKeyGame game;
    private Stage stage;
    private Skin skin;
    private TextField usernameInput;

    public MainMenuScreen(TheLostKeyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        createBasicSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = skin.getFont("default-font");
        titleStyle.fontColor = Color.WHITE;
        Label titleLabel = new Label("THE LOST KEY", titleStyle);
        titleLabel.setFontScale(2.0f);

        // Input Field
        TextField.TextFieldStyle inputStyle = new TextField.TextFieldStyle();
        inputStyle.font = skin.getFont("default-font");
        inputStyle.fontColor = Color.WHITE;

        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(Color.DARK_GRAY);
        p.fill();
        inputStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(p)));
        inputStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(p)));

        usernameInput = new TextField("", inputStyle);
        usernameInput.setMessageText("Enter Name...");
        usernameInput.setAlignment(Align.center);

        TextButton startButton = new TextButton("START ADVENTURE", skin);
        TextButton exitButton = new TextButton("EXIT", skin);

        table.add(titleLabel).padBottom(50).row();
        table.add(usernameInput).width(300).height(40).padBottom(20).row();
        table.add(startButton).width(300).height(50).padBottom(20).row();
        table.add(exitButton).width(300).height(50).row();

        // Logic
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String nameText = usernameInput.getText();
                if (nameText.trim().isEmpty()) return;

                System.out.println("Connecting as: " + nameText);

                BackendFacade.getInstance().registerOrLogin(nameText, new BackendFacade.NetworkCallback() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            // Parse the raw JSON string
                            JsonValue root = new JsonReader().parse(response);

                            JsonValue healthObj = root.get("health");
                            JsonValue inventoryObj = root.get("inventory");

                            // Extract data
                            int hp = healthObj != null ? healthObj.getInt("currentHp", 100) : 100;
                            int hunger = healthObj != null ? healthObj.getInt("hungerLevel", 100) : 100;
                            int thirst = healthObj != null ? healthObj.getInt("thirstLevel", 100) : 100;
                            int food = inventoryObj != null ? inventoryObj.getInt("foodCount", 0) : 0;

                            // Update Managers
                            HealthManager.getInstance().setValues(hp, hunger, thirst);
                            InventoryManager.getInstance().setFoodCount(food);

                            System.out.println("Data Loaded! HP: " + hp);

                            // Switch Screen
                            Gdx.app.postRunnable(() -> {
                                game.setScreen(new com.Ferdyano.frontend.Screen.GameScreen(game));
                            });

                        } catch (Exception e) {
                            System.out.println("Error parsing data: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        System.out.println("Connection Failed: " + error);
                    }
                });
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    private void createBasicSkin() {
        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fill();
        skin.add("gray", new Texture(pixmap));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("gray", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("gray", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("gray", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default-font");
        skin.add("default", textButtonStyle);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}
