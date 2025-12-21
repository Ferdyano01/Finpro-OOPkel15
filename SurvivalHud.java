package com.Ferdyano.frontend.ui;

import com.syifa.frontend.TheLostKeyGame;
import com.syifa.frontend.Screen.MainMenuScreen;
import com.Ferdyano.frontend.pattern.HealthObserver;
import com.Ferdyano.frontend.pattern.SurvivalObserver;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * SurvivalHud: Menampilkan data HP, Lapar, dan Haus.
 * Mendaftarkan diri sebagai Observer ke HealthManager.
 */
public class SurvivalHud extends Table implements HealthObserver, SurvivalObserver {

    private final Label hpLabel;
    private final ProgressBar hpBar;
    private final ProgressBar hungerBar;
    private final ProgressBar thirstBar;

    // Konstruktor menerima Skin dan referensi Game untuk navigasi menu
    public SurvivalHud(Skin skin, final TheLostKeyGame game) {
        super(skin);

        // --- 1. INISIALISASI KOMPONEN DI DALAM KELAS ---
        HealthManager manager = HealthManager.getInstance();
        this.hpLabel = new Label("HP: " + manager.getHealth() + "/100", skin);
        // Asumsi style ProgressBar horizontal ada di skin
        this.hpBar = new ProgressBar(0, 100, 1, false, skin);
        this.hungerBar = new ProgressBar(0, 100, 1, false, skin);
        this.thirstBar = new ProgressBar(0, 100, 1, false, skin);

        // Set nilai awal
        this.hpBar.setValue(manager.getHealth());
        this.hungerBar.setValue(100);
        this.thirstBar.setValue(100);

        // Tombol Menu
        TextButton menuBtn = new TextButton("MENU", skin);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HealthManager.getInstance().clearObservers();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // --- 2. SETUP LAYOUT SCENE2D ---
        this.setFillParent(true);
        this.top().left();
        this.pad(20);

        // Baris HP
        add(new Label("HP", skin)).left();
        add(hpBar).width(150).height(20).padLeft(10);
        add(hpLabel).padLeft(10).row();

        // Baris Lapar
        add(new Label("LAPAR", skin)).left().padTop(5);
        add(hungerBar).width(150).height(10).padLeft(10).padTop(5).row();

        // Baris Haus
        add(new Label("HAUS", skin)).left().padTop(5);
        add(thirstBar).width(150).height(10).padLeft(10).padTop(5).row();

        // Baris Tombol Menu
        add(menuBtn).padTop(15).colspan(3).left();

        // --- 3. PENDAFTARAN OBSERVER ---
        manager.registerHealthObserver(this);
        manager.registerSurvivalObserver(this);
        System.out.println("SurvivalHud berhasil mendaftar ke HealthManager.");
    }

    // ... (Metode updateObserver tetap sama)
    @Override
    public void updateHealth(int currentHp, int maxHp) {
        hpBar.setValue(currentHp);
        hpLabel.setText("HP: " + currentHp + "/" + maxHp);
    }

    @Override
    public void updateSurvivalMeters(int currentHunger, int currentThirst) {
        hungerBar.setValue(currentHunger);
        thirstBar.setValue(currentThirst);
    }
}
