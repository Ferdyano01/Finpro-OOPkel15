package com.Ferdyano.frontend.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.Ferdyano.frontend.TheLostKeyGame; // TAMBAHKAN IMPORT INI
import com.Ferdyano.frontend.core.CelestialInstrument;

/**
 * GameUI: Kelas utama yang mengelola Stage dan semua komponen UI.
 */
public class GameUI {

    private final Stage stage;
    private final Skin skin;
    private final TheLostKeyGame game; // TAMBAHKAN FIELD INI

    private SurvivalHud survivalHud;

    private final CelestialInstrument testInstrument = new CelestialInstrument("Obelisk A", 0);

    // PERBAIKAN: Tambahkan TheLostKeyGame ke parameter konstruktor
    public GameUI(TheLostKeyGame game, Viewport viewport, Skin skin) {
        this.game = game; // Simpan referensi game
        this.skin = skin;
        this.stage = new Stage(viewport);

        initPermanentUI();
    }

    private void initPermanentUI() {
        // PERBAIKAN: Masukkan skin DAN game (2 argumen) sesuai class SurvivalHud yang baru
        this.survivalHud = new SurvivalHud(skin, game);

        survivalHud.setFillParent(true);
        stage.addActor(survivalHud);
    }

    public void updateAndDraw(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
