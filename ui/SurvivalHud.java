package com.Ferdyano.frontend.ui;

import com.Ferdyano.frontend.TheLostKeyGame;
import com.Ferdyano.frontend.Screen.MainMenuScreen;
import com.Ferdyano.frontend.pattern.HealthObserver;
import com.Ferdyano.frontend.pattern.SurvivalObserver;
import com.Ferdyano.frontend.Managers.HealthManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class SurvivalHud extends Table implements HealthObserver, SurvivalObserver {

    private final Label hpLabel;
    private final ProgressBar hpBar, hungerBar, thirstBar;

    public SurvivalHud(Skin skin, final TheLostKeyGame game) {
        super(skin);
        this.setFillParent(true);
        this.top().left().pad(20);

        // Inisialisasi Bar dengan warna manual agar PASTI terlihat
        this.hpBar = createColoredBar(skin, Color.RED);
        this.hungerBar = createColoredBar(skin, Color.ORANGE);
        this.thirstBar = createColoredBar(skin, Color.CYAN);

        HealthManager manager = HealthManager.getInstance();
        this.hpLabel = new Label("HP: " + manager.getHealth() + "/100", skin);
        TextButton menuBtn = new TextButton("MENU", skin);

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HealthManager.getInstance().clearObservers();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Susun UI
        add(new Label("HP", skin)).left();
        add(hpBar).width(200).padLeft(10);
        add(hpLabel).padLeft(10).row();

        add(new Label("LAPAR", skin)).left().padTop(5);
        add(hungerBar).width(200).padLeft(10).padTop(5).row();

        add(new Label("HAUS", skin)).left().padTop(5);
        add(thirstBar).width(200).padLeft(10).padTop(5).row();

        add(menuBtn).padTop(15).colspan(3).left();

        // Daftar ke Manager
        manager.registerHealthObserver(this);
        manager.registerSurvivalObserver(this);
    }

    // Helper untuk membuat bar berwarna
    private ProgressBar createColoredBar(Skin skin, Color color) {
        ProgressBar bar = new ProgressBar(0, 100, 1, false, skin);
        bar.setValue(100);
        bar.setColor(color); // Memaksa tint warna
        return bar;
    }

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
