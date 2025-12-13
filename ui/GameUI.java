package com.Ferdyano.frontend.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.Ferdyano.frontend.Managers.GameManager; // Untuk cek GameState
import com.Ferdyano.frontend.core.CelestialInstrument; // Contoh objek untuk puzzle

/**
 * GameUI: Kelas utama yang mengelola Stage dan semua komponen UI.
 * DIKOREKSI: Disederhanakan untuk melewati crash di InventoryBar.
 */
public class GameUI {

    private final Stage stage;
    private final Skin skin;

    // Komponen yang menyebabkan crash (DIHAPUS SEMENTARA):
    // private InventoryBar inventoryBar;
    // private PuzzleInterface puzzleInterface;

    // Komponen yang mungkin OK:
    private SurvivalHud survivalHud;

    // Asumsi kita memiliki satu objek Celestial Instrument untuk testing
    private final CelestialInstrument testInstrument = new CelestialInstrument("Obelisk A", 0);

    public GameUI(Viewport viewport, Skin skin) {
        this.skin = skin;
        this.stage = new Stage(viewport);

        // Load Komponen UI Inti
        initPermanentUI();
    }

    /** * Inisialisasi komponen UI yang selalu ada di layar.
     */
    private void initPermanentUI() {
        // Survival HUD (HP, Lapar, Haus)
        // Kita harap SurvivalHud tidak memanggil drawable kustom.
        this.survivalHud = new SurvivalHud(skin);
        survivalHud.setFillParent(true);
        stage.addActor(survivalHud);

        // KOREKSI KRITIS: HAPUS INVENTORY BAR UNTUK MELEWATI CRASH
        // HAPUS BARIS INI: this.inventoryBar = new InventoryBar(skin);
        // HAPUS BARIS INI: stage.addActor(inventoryBar);
    }

    /** * Metode yang dipanggil setiap frame dari GameScreen.render().
     */
    public void updateAndDraw(float delta) {
        // Logika checkGameState dihapus karena tidak ada UI overlay lagi
        // checkGameState(); // DIHAPUS

        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    /** * Logika untuk menampilkan atau menyembunyikan overlay.
     * DIHAPUS SEMENTARA AGAR TIDAK ADA DEPENDENCY KE PuzzleInterface
     */
    /*
    private void checkGameState() {
        GameManager.GameState currentState = GameManager.getInstance().getCurrentGameState();

        if (currentState == GameManager.GameState.SOLVING_PUZZLE) {
            // ... (Logika Puzzle) ...
        } else {
            // ...
        }
    }
    */

    public void dispose() {
        stage.dispose();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
