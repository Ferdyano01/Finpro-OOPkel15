package com.Ferdyano.frontend.Screen;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CreditsScreen implements Screen {
    private final TheLostKeyGame game;
    private final Stage stage;
    private final Label creditsLabel;

    // Kecepatan scroll teks
    private final float SCROLL_SPEED = 50f;

    public CreditsScreen(TheLostKeyGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(1280, 720));

        // --- TEKS CREDITS (Ganti Nama Kelompok Anda di sini) ---
        String text = "THE LOST KEY\n\n\n" +
            "DEVELOPED BY:\n" +
            "Kelompok 15\n\n\n" +
            "Frontend:\n" +
            "Ferdyano\n" +
            "Syifa Aulia Azhim\n\n" +
            "Backend:\n" +
            "Qais Ismail\n\n" +
            "Haitsam\n\n" +
            "ASSETS BY:\n" +
            "Itch.io Creators\n\n\n" +
            "Thank you for playing!";

        Label.LabelStyle style = new Label.LabelStyle(game.getSkin().getFont("default-font"), Color.WHITE);
        creditsLabel = new Label(text, style);
        creditsLabel.setAlignment(Align.center);
        creditsLabel.setFontScale(1.5f);
        creditsLabel.setWrap(true);

        // Atur posisi awal di bawah layar
        creditsLabel.setWidth(1280);
        creditsLabel.setPosition(0, -600);

        stage.addActor(creditsLabel);
    }

    @Override
    public void render(float delta) {
        // Hapus layar dengan warna hitam
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update Stage
        stage.act(delta);
        stage.draw();

        // --- LOGIKA SCROLL ---
        creditsLabel.setY(creditsLabel.getY() + SCROLL_SPEED * delta);

        // Jika teks sudah lewat jauh ke atas, atau user klik layar -> Balik ke Menu
        if (creditsLabel.getY() > 1000 || Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
