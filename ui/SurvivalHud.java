package com.Ferdyano.frontend.ui;

import com.Ferdyano.frontend.pattern.HealthObserver;
import com.Ferdyano.frontend.pattern.SurvivalObserver;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * SurvivalHud: Menampilkan data HP, Lapar, dan Haus.
 * Mendaftarkan diri sebagai Observer ke HealthManager.
 */
public class SurvivalHud extends Table implements HealthObserver, SurvivalObserver {

    private final Label hpLabel;
    private final ProgressBar hpBar;
    private final ProgressBar hungerBar;
    private final ProgressBar thirstBar;

    // KOREKSI UTAMA: Konstruktor HANYA menerima Skin
    public SurvivalHud(Skin skin) {

        // --- 1. INISIALISASI KOMPONEN DI DALAM KELAS ---
        this.hpLabel = new Label("HP: 100/100", skin);
        // Asumsi style ProgressBar horizontal ada di skin
        this.hpBar = new ProgressBar(0, 100, 1, false, skin);
        this.hungerBar = new ProgressBar(0, 100, 1, false, skin);
        this.thirstBar = new ProgressBar(0, 100, 1, false, skin);

        // --- 2. SETUP LAYOUT SCENE2D (Menggunakan Table) ---
        top().left(); // Posisikan HUD di kiri atas
        pad(10);

        // Baris HP
        add(hpLabel).left();
        add(hpBar).width(150).height(20).padLeft(10).row();

        // Baris Survival
        add(new Label("Lapar", skin)).left();
        add(hungerBar).width(150).height(10).padLeft(10).row();

        add(new Label("Haus", skin)).left();
        add(thirstBar).width(150).height(10).padLeft(10).row();


        // --- 3. Pendaftaran Observer ---
        HealthManager manager = HealthManager.getInstance();
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

    // ... Metode LibGDX lain (draw, act, dll)
}
